package controllers;

import db.dao.AccountDAO;
import db.dao.UserInfoDAO;
import db.entity.Account;
import db.entity.UserInfo;
import request.commands.ConfirmCommands;
import request.Request;
import request.controller.BaseRequestController;
import request.tdo.AccountDTO;
import request.tdo.UserInfoDTO;
import request.tdo.constructors.AccountTDOConstructor;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.MessageDigest;
import java.time.LocalDateTime;
import java.util.Base64;

public class AuthenticationController extends BaseRequestController {
    private AccountDAO accountDao = new AccountDAO();
    private UserInfoDAO userInfoDao = new UserInfoDAO();
    private AccountTDOConstructor tdoConstructor = new AccountTDOConstructor();

    public AuthenticationController(BaseRequestController contr) {
        super(contr);
    }

    public AccountDTO login() throws IOException {
        // Отправляем начальное подтверждение клиенту
        sendResponse(ConfirmCommands.class, ConfirmCommands.SUCCESSFULLY);

        Request<AccountDTO> accountRequest;
        try {
            // Читаем данные авторизации от клиента
            accountRequest = (Request<AccountDTO>) inputObjectStream.readObject();
        } catch (Exception e) {
            System.err.println("Ошибка чтения данных авторизации: " + e.getMessage());
            e.printStackTrace();
            //sendResponse(ConfirmCommands.class, ConfirmCommands.FAILED);
            return null;
        }

        AccountDTO accountDTO = accountRequest.getCommand();
        if (accountDTO == null) {
            System.err.println("Данные авторизации отсутствуют.");
            return null;
        }

        String username = accountDTO.getUsername();
        String passwordHash = accountDTO.getPasswordHash();

        // Проверяем учетные данные
        boolean isAuthenticated = accountDao.checkLogin(username, passwordHash);
        if (isAuthenticated) {
            Account account = accountDao.findByUsername(username);
            if (account != null) {
                AccountDTO responseAccountDTO = tdoConstructor.accountToDTO(account);

                System.out.println("Пользователь " + username + " успешно авторизован.");

                // Отправляем успешное подтверждение и объект пользователя
                //sendResponse(ConfirmCommands.class, ConfirmCommands.SUCCESSFULLY);
                sendResponse(AccountDTO.class, responseAccountDTO);

                return responseAccountDTO;
            }
        }

        System.err.println("Не удалось авторизовать пользователя: " + username);

        // Если авторизация не удалась
        sendResponse(ConfirmCommands.class, ConfirmCommands.FAILED);
        return null;
    }


    public void register() throws IOException {
        sendResponse(ConfirmCommands.class, ConfirmCommands.SUCCESSFULLY);
        Request<AccountDTO> accountRequest;
        Request<UserInfoDTO> userInfoRequest;

        try {
            accountRequest = (Request<AccountDTO>) inputObjectStream.readObject();
            userInfoRequest = (Request<UserInfoDTO>) inputObjectStream.readObject();
        } catch (Exception e) {
            e.printStackTrace();
            sendResponse(ConfirmCommands.class, ConfirmCommands.FAILED);
            return;
        }

        AccountDTO accountDTO = accountRequest.getCommand();
        UserInfoDTO userInfoDTO = userInfoRequest.getCommand();

        Account existingAccount = accountDao.findByUsername(accountDTO.getUsername());
        if (existingAccount != null) {
            sendResponse(ConfirmCommands.class, ConfirmCommands.FAILED);
            return;
        }

        Account newAccount = tdoConstructor.dtoToAccount(accountDTO);
        newAccount.setCreatedAt(LocalDateTime.now());
        boolean isSaved = accountDao.save(newAccount);

        if (isSaved) {
            sendResponse(ConfirmCommands.class, ConfirmCommands.SUCCESSFULLY);

            UserInfo newUserInfo = tdoConstructor.dtoToUserInfo(userInfoDTO, newAccount);
            userInfoDao.save(newUserInfo);

            AccountDTO createdAccountDTO = tdoConstructor.accountToDTO(newAccount);
            UserInfoDTO createdUserInfoDTO = tdoConstructor.userInfoToDTO(newUserInfo);

            Request<AccountDTO> responseAccountRequest = new Request<>(AccountDTO.class);
            responseAccountRequest.setCommand(createdAccountDTO);

            Request<UserInfoDTO> responseUserInfoRequest = new Request<>(UserInfoDTO.class);
            responseUserInfoRequest.setCommand(createdUserInfoDTO);

            sendResponse(Request.class, responseAccountRequest);
            sendResponse(Request.class, responseUserInfoRequest);
        } else {
            sendResponse(ConfirmCommands.class, ConfirmCommands.FAILED);
        }
    }

    public static String encrypt(String passwordString) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-512");
            digest.reset();
            byte[] encryptedPassword = digest.digest(passwordString.getBytes("UTF-8"));
            return Base64.getEncoder().encodeToString(encryptedPassword);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public UserInfoDTO getUserInfo(AccountDTO accountDTO) {
        int accountId = accountDTO.getAccountId();
        UserInfo userInfo = userInfoDao.findByAccountId(accountId);
        if (userInfo == null) {
            throw new IllegalStateException("No UserInfo found for accountId: " + accountId);
        }

        return tdoConstructor.userInfoToDTO(userInfo);
    }
}
