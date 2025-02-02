package gui.controller;

import gui.ConfirmWindow;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.event.ActionEvent;
import gui.GUI;
import request.commands.AdminCommands;
import request.commands.UserCommands;
import request.controller.BaseRequestController;
import request.tdo.AccountDTO;
import request.tdo.UserInfoDTO;

import java.io.IOException;

public class UserInfo {

    @FXML
    private Label labelLogin;
    @FXML
    private Label labelName;
    @FXML
    private Label labelSurname;
    @FXML
    private Label labelEmail;
    @FXML
    private Label labelPhone;
    @FXML
    private Label labelCreatedAt;

    private static AccountDTO accountInfo;
    private static UserInfoDTO userInfo;

    public static void setAccountInfo(AccountDTO accountDTO, UserInfoDTO userInfoDTO) {
        UserInfo.accountInfo = accountDTO;
        UserInfo.userInfo = userInfoDTO;
    }

    @FXML
    public void initialize() {
        labelLogin.setText("Логин: " + accountInfo.getUsername());
        labelName.setText("Имя: " + userInfo.getFirstName());
        labelSurname.setText("Фамилия: " + userInfo.getLastName());
        labelEmail.setText("Почта: " + userInfo.getEmail());
        labelPhone.setText("Номер телефона: " + userInfo.getContactInfo());
        labelCreatedAt.setText("Аккаунт создан: " + accountInfo.getCreatedAt());
    }
   
    public void UpdateProfile(ActionEvent actionEvent) {
    }

    public void ShowStore(ActionEvent actionEvent) throws IOException {
        GUI.setRoot("user_store");
        UserStore userStore = (UserStore) GUI.getController();
        userStore.setAccount(accountInfo);
    }

    public void ShowCart(ActionEvent actionEvent) throws IOException {
        GUI.setRoot("user_cart");
        UserCart userStore = (UserCart) GUI.getController();
        userStore.setAccount(accountInfo);
    }

    public void Logout(ActionEvent actionEvent) {
        try {
            BaseRequestController.sendRequest(UserCommands.class, UserCommands.LOGOUT);
            GUI.setRoot("authorization");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void delAccount(ActionEvent actionEvent) {
        boolean confirmed = ConfirmWindow.PrintOut("Вы уверены, что хотите удалить свой аккаунт?");

        if (confirmed) {
            try {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Успех");
                alert.setHeaderText(null);
                alert.setContentText("Ваш аккаунт успешно удален!");
                alert.showAndWait();
                GUI.setRoot("authorization");
            } catch (Exception e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Ошибка");
                alert.setHeaderText(null);
                alert.setContentText("Произошла ошибка при удалении аккаунта: " + e.getMessage());
                alert.showAndWait();
            }
        } else {
            // Пользователь отменил удаление
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Отмена");
            alert.setHeaderText(null);
            alert.setContentText("Удаление аккаунта отменено.");
            alert.showAndWait();
        }
    }


    public void ShowOrders(ActionEvent actionEvent) throws IOException {
        GUI.setRoot("user_orders");
        UserOrders userStore = (UserOrders) GUI.getController();
        userStore.setAccount(accountInfo);
    }
}
