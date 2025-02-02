package controllers;

import request.commands.ConfirmCommands;
import request.commands.StartMenuCommands;
import request.controller.BaseRequestController;
import request.tdo.AccountDTO;
import request.Request;

import java.security.MessageDigest;
import java.util.Base64;

public class AuthorisationController {

    public static AccountDTO authorisation(String login, String password) {
        BaseRequestController.sendRequest(StartMenuCommands.class, StartMenuCommands.AUTHORISATION);

        try {
            // Получение подтверждения команды
            ConfirmCommands confirm = BaseRequestController.getCommand(ConfirmCommands.class);
            if (confirm != ConfirmCommands.SUCCESSFULLY) {
                System.err.println("Авторизация отклонена сервером.");
                return null;
            }

            // Создание объекта для авторизации
            AccountDTO accountDTO = new AccountDTO();
            accountDTO.setUsername(login);
            accountDTO.setPasswordHash(encrypt(password));

            Request<AccountDTO> accountRequest = new Request<>(AccountDTO.class);
            accountRequest.setCommand(accountDTO);

            // Отправка данных на сервер
            BaseRequestController.getObjectOutputStream().writeObject(accountRequest);

            // Получение ответа от сервера
            Object serverResponse = BaseRequestController.getObjectInputStream().readObject();
            if (serverResponse instanceof Request) {

                Request<AccountDTO> response = (Request<AccountDTO>) serverResponse;

                // Извлечение AccountDTO из ответа
                AccountDTO accountResponse = response.getCommand();
                System.out.println("Авторизация успешна для пользователя: " + accountResponse.getUsername());
                return accountResponse; // Возврат AccountDTO
            } else {
                System.err.println("Неожиданный ответ от сервера: " + serverResponse.getClass().getName());
                return null; // Возврат null при неправильном формате ответа
            }
        } catch (Exception e) {
            System.err.println("Ошибка при авторизации: " + e.getMessage());
            e.printStackTrace();
            return null; // Возврат null в случае ошибки
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
}
