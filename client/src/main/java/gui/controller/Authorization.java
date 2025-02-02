package gui.controller;

import controllers.AuthorisationController;
import gui.GUI;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import request.commands.AdminCommands;
import request.commands.ConfirmCommands;
import request.commands.ManagerCommands;
import request.commands.UserCommands;
import request.controller.BaseRequestController;
import request.tdo.AccountDTO;
import request.tdo.UserInfoDTO;

import java.io.IOException;

public class Authorization {
    @FXML
    private TextField textFieldLogin;
    @FXML
    private PasswordField passwordFieldPassword;
    @FXML
    private Label labelError;

    @FXML
    public void onClickRegistration(ActionEvent actionEvent) {
        try {
            GUI.setRoot("registration");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void onClickLogin(ActionEvent actionEvent) throws IOException, ClassNotFoundException {
        String login = textFieldLogin.getText().strip();
        String password = passwordFieldPassword.getText().strip();

        if (login.isEmpty() || password.isEmpty()) {
            labelError.setText("Заполните оба поля!");
            labelError.setVisible(true);
            return;
        }

        AccountDTO accountInfo = AuthorisationController.authorisation(login, password);
        System.out.println(accountInfo);
        if (accountInfo != null) {
            switch (accountInfo.getAccessLevel()) {
                //user
                case 1: {
                    BaseRequestController.sendRequest(UserCommands.class, UserCommands.GET_USER_INFO);
                    ConfirmCommands confirm = BaseRequestController.getCommand(ConfirmCommands.class);
                    if (confirm == ConfirmCommands.SUCCESSFULLY) {
                        System.out.println("Пока всё гуд.");
                        UserInfoDTO userInfo = BaseRequestController.getCommand(UserInfoDTO.class);
                        System.out.println(userInfo);
                        UserInfo.setAccountInfo(accountInfo, userInfo);
                        GUI.setRoot("user_info");
                        break;
                    }
                    else {
                        System.out.println("Возникла ошибка.");
                    }
                }
                //manager
                case 2: {
                    BaseRequestController.sendRequest(AdminCommands.class, AdminCommands.GET_USER_INFO);
                    ConfirmCommands confirm = BaseRequestController.getCommand(ConfirmCommands.class);
                    if (confirm == ConfirmCommands.SUCCESSFULLY) {
                        System.out.println("Пока всё гуд.");
                        UserInfoDTO userInfo = BaseRequestController.getCommand(UserInfoDTO.class);
                        System.out.println(userInfo);
                        ManagerInfo.setAccountInfo(accountInfo, userInfo);
                        GUI.setRoot("manager_info");
                        break;
                    }
                    else {
                        System.out.println("Возникла ошибка.");
                    }
                }
                //admin
                case 3: {
                    BaseRequestController.sendRequest(AdminCommands.class, AdminCommands.GET_USER_INFO);
                    ConfirmCommands confirm = BaseRequestController.getCommand(ConfirmCommands.class);
                    if (confirm == ConfirmCommands.SUCCESSFULLY) {
                        System.out.println("Пока всё гуд.");
                        UserInfoDTO userInfo = BaseRequestController.getCommand(UserInfoDTO.class);
                        System.out.println(userInfo);
                        AdminInfo.setAccountInfo(accountInfo, userInfo);
                        GUI.setRoot("admin_info");
                        break;
                    }
                    else {
                        System.out.println("Возникла ошибка.");
                    }
                }
                //loh
                case 0: {
                    labelError.setText("Аккаунт заблокирован. Пшёл отсюда");
                    labelError.setVisible(true);
                }
            }

        } else {
            labelError.setText("Неверный логин или пароль.");
            labelError.setVisible(true);
        }
    }
}

