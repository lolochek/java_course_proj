package controllers;

import request.commands.AdminCommands;
import request.commands.ConfirmCommands;
import request.commands.UserCommands;
import request.controller.BaseRequestController;
import request.tdo.AccountDTO;
import request.tdo.UserInfoDTO;

public class UserController {
public static ConfirmCommands changePersonalData(AccountDTO accountDTO, UserInfoDTO userInfoDTO)
    {
        BaseRequestController.sendRequest(AdminCommands.class, AdminCommands.CHANGE_PERSONAL_DATA);
        ConfirmCommands confirm;
        try {
            confirm = BaseRequestController.getCommand(ConfirmCommands.class);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        switch (confirm) {
            case SUCCESSFULLY: {
                try {
                    BaseRequestController.getObjectOutputStream().writeObject(accountDTO);
                    BaseRequestController.getObjectOutputStream().writeObject(userInfoDTO);

                    ConfirmCommands response = (ConfirmCommands) BaseRequestController.getObjectInputStream().readObject();
                    return response;

                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }
            default: break;
        }
        return null;
    }

    public static ConfirmCommands deleteAccount(int accountID) {
    ConfirmCommands confirm = null;
    return confirm;
    }
}
