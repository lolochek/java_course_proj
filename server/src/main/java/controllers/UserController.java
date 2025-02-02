package controllers;

import db.dao.GenericDao;
import db.entity.Account;
import db.entity.UserInfo;
import request.commands.ConfirmCommands;
import request.controller.BaseRequestController;
import request.tdo.AccountDTO;
import request.tdo.UserInfoDTO;
import request.tdo.constructors.AccountTDOConstructor;

public class UserController extends BaseRequestController {

    public UserController(BaseRequestController contr) {
        super(contr);
    }

    public void sendUserInfo(AccountDTO account, UserInfoDTO userInfo) {
        sendResponse(ConfirmCommands.class, ConfirmCommands.SUCCESSFULLY);
        try {
            System.out.println(account);
            System.out.println(userInfo);
            sendResponse(UserInfoDTO.class, userInfo);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void changePersonalData(Account account, UserInfo userInfo) {
        sendResponse(ConfirmCommands.class, ConfirmCommands.SUCCESSFULLY);
        try {
            AccountDTO newAccount = (AccountDTO)inputObjectStream.readObject();
            UserInfoDTO newUserInfo = (UserInfoDTO)inputObjectStream.readObject();

            account.updateInformation(newAccount);
            userInfo.updateInformation(newUserInfo, account);

            var accountdao = new GenericDao<>(Account.class);
            var userdao = new GenericDao<>(UserInfo.class);

            AccountTDOConstructor accountTDOConstructor = new AccountTDOConstructor();
            if(accountdao.update(account) && userdao.update(userInfo))
            {
                outputObjectStream.writeObject(ConfirmCommands.SUCCESSFULLY);

                outputObjectStream.writeObject(accountTDOConstructor.accountToDTO(account));
                //System.out.println(accountTDOConstructor.accountToDTO(account));
                outputObjectStream.writeObject(accountTDOConstructor.userInfoToDTO(userInfo));
                //System.out.println(accountTDOConstructor.userInfoToDTO(userInfo));
            }
            else
                outputObjectStream.writeObject(ConfirmCommands.FAILED);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
