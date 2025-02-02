package controllers;

import db.dao.GenericDao;
import db.entity.Account;
import request.Request;
import request.commands.ConfirmCommands;
import request.controller.BaseRequestController;
import request.tdo.AccountDTO;
import request.tdo.UserInfoDTO;
import request.tdo.constructors.AccountTDOConstructor;

import java.util.ArrayList;

public class AllUsersController extends BaseRequestController {

    public AllUsersController(BaseRequestController contr) {
        super(contr);
    }

    public void sendAllUsers() {
        sendResponse(ConfirmCommands.class, ConfirmCommands.SUCCESSFULLY);
        try {
            var accountDao = new GenericDao<>(Account.class);
            var accountList = accountDao.getAll();
            var accountTDOConstructor = new AccountTDOConstructor();
            var userTDOList = new ArrayList<AccountDTO>();
            for (Account acc : accountList) {
                userTDOList.add(accountTDOConstructor.accountToDTO(acc));
            }
            userTDOList.removeIf(accountDTO -> accountDTO.getAccessLevel() == 3);

            System.out.println(userTDOList);
            outputObjectStream.writeObject(userTDOList);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void changeAccess() {
        sendResponse(ConfirmCommands.class, ConfirmCommands.SUCCESSFULLY);
        try {
            AccountDTO account = (AccountDTO) inputObjectStream.readObject();
            int newAccessLevel = (int) inputObjectStream.readObject();
            AccountDTO newAccount = account;
            newAccount.setAccessLevel(newAccessLevel);
            var accountTDOConstructor = new AccountTDOConstructor();
            Account account1 = accountTDOConstructor.dtoToAccount(account);
            account1.updateInformation(newAccount);
            var userdao = new GenericDao<>(Account.class);
            if(userdao.update(account1))
            {
                System.out.println(account);
            }
            else
                System.out.println("Error");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}

