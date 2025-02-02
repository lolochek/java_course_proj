package controllers;

import request.commands.ConfirmCommands;
import request.commands.StartMenuCommands;
import request.controller.BaseRequestController;
import request.tdo.AccountDTO;
import request.tdo.UserInfoDTO;
import request.Request;

public class RegistrationController {

    public static ConfirmCommands registration(String login, String password, String firstname, String secondname,
                                               String email, String contactInfo) {
        BaseRequestController.sendRequest(StartMenuCommands.class, StartMenuCommands.USER_REGISTRATION);

        ConfirmCommands confirm = null;
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
                    accountDTO.setAccessLevel(1);  // Default access level

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
}
