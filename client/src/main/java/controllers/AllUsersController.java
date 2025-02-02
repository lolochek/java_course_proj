package controllers;

import request.Request;
import request.commands.AdminCommands;
import request.commands.ConfirmCommands;
import request.commands.StartMenuCommands;
import request.controller.BaseRequestController;
import request.tdo.AccountDTO;
import request.tdo.UserInfoDTO;

import java.util.List;
import java.util.Objects;

public class AllUsersController {

    public static List<AccountDTO> getAccountList() {
        BaseRequestController.sendRequest(AdminCommands.class, AdminCommands.GET_USER_LIST);
        ConfirmCommands confirm;
        try {
            confirm = BaseRequestController.getCommand(ConfirmCommands.class);
        } catch (Exception e) {
            return null;
        }
        if (Objects.requireNonNull(confirm) == ConfirmCommands.SUCCESSFULLY) {
            try {
                var usersList = (List<AccountDTO>) BaseRequestController.getObjectInputStream().readObject();
                return usersList;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
        return null;
    }

    public static ConfirmCommands add_manager(String login, String password, String firstname, String secondname,
                                               String email, String contactInfo) {
        BaseRequestController.sendRequest(AdminCommands.class, AdminCommands.ADD_MANAGER);

        ConfirmCommands confirm;
        try {
            confirm = BaseRequestController.getCommand(ConfirmCommands.class);
        } catch (Exception e) {
            return ConfirmCommands.FAILED;
        }
        switch (confirm) {
            case SUCCESSFULLY: {
                try {
                    AccountDTO accountDTO = new AccountDTO();
                    accountDTO.setUsername(login);
                    accountDTO.setPasswordHash(AuthorisationController.encrypt(password));
                    accountDTO.setAccessLevel(2);

                    UserInfoDTO userInfoDTO = new UserInfoDTO();
                    userInfoDTO.setFirstName(firstname);
                    userInfoDTO.setLastName(secondname);
                    userInfoDTO.setEmail(email);
                    userInfoDTO.setContactInfo(contactInfo);

                    Request<AccountDTO> accountRequest = new Request<>(AccountDTO.class);
                    accountRequest.setCommand(accountDTO);

                    Request<UserInfoDTO> userInfoRequest = new Request<>(UserInfoDTO.class);
                    userInfoRequest.setCommand(userInfoDTO);

                    BaseRequestController.getObjectOutputStream().writeObject(accountRequest);
                    BaseRequestController.getObjectOutputStream().writeObject(userInfoRequest);
                    return BaseRequestController.getCommand(ConfirmCommands.class);
                } catch (Exception e) {
                    return ConfirmCommands.FAILED;
                }
            }
            default:
                break;
        }
        return confirm;
    }

    public static void changeAccess(AccountDTO account, int level)
    {
        BaseRequestController.sendRequest(AdminCommands.class, AdminCommands.CHANGE_ACCESS_LEVEL);
        ConfirmCommands confirm;
        try {
            confirm = BaseRequestController.getCommand(ConfirmCommands.class);
        } catch (Exception e) {
            return;
        }
        switch (confirm) {
            case SUCCESSFULLY: {
                try {
                    BaseRequestController.getObjectOutputStream().writeObject(account);
                    BaseRequestController.getObjectOutputStream().writeObject(level);

                } catch (Exception e) {
                    e.printStackTrace();
                    return;
                }
            }
            default: break;
        }
    }
}
