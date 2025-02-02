package gui.controller;

import gui.GUI;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import request.commands.AdminCommands;
import request.commands.ConfirmCommands;
import request.controller.BaseRequestController;
import request.tdo.AccountDTO;
import request.tdo.UserInfoDTO;

import java.io.IOException;
import java.util.List;

import static controllers.UserController.changePersonalData;

public class AdminInfo {
    @FXML
    private Label labelMessage;
    @FXML
    private Button btnUserManagement, btnStoreManagement, btnOrderManagement, btnLogout, btnUpdateProfile;
    @FXML
    private Label labelRole;
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
    @FXML
    private VBox profileBox;

    private static AccountDTO accountInfo;
    private static UserInfoDTO userInfo;

    public static void setAccountInfo(AccountDTO accountDTO, UserInfoDTO userInfoDTO) {
        AdminInfo.accountInfo = accountDTO;
        AdminInfo.userInfo = userInfoDTO;
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
        profileBox.getChildren().clear();

        btnLogout.setDisable(true);
        btnUpdateProfile.setDisable(true);
        btnStoreManagement.setDisable(true);
        btnOrderManagement.setDisable(true);
        btnUserManagement.setDisable(true);

        HBox loginBox = new HBox(10);
        TextField textFieldLogin = new TextField(accountInfo.getUsername());
        textFieldLogin.setPromptText("Логин");
        loginBox.getChildren().addAll(new Label("Логин"), textFieldLogin);

        HBox nameBox = new HBox(10);
        TextField textFieldName = new TextField(userInfo.getFirstName());
        textFieldName.setPromptText("Имя");
        nameBox.getChildren().addAll(new Label("Имя"), textFieldName);

        HBox surnameBox = new HBox(10);
        TextField textFieldSurname = new TextField(userInfo.getLastName());
        textFieldSurname.setPromptText("Фамилия");
        surnameBox.getChildren().addAll(new Label("Фамилия"), textFieldSurname);

        HBox emailBox = new HBox(10);
        TextField textFieldEmail = new TextField(userInfo.getEmail());
        textFieldEmail.setPromptText("Почта");
        emailBox.getChildren().addAll(new Label("Почта"), textFieldEmail);

        HBox phoneBox = new HBox(10);
        TextField textFieldPhone = new TextField(userInfo.getContactInfo());
        textFieldPhone.setPromptText("Номер телефона");
        phoneBox.getChildren().addAll(new Label("Номер телефона"), textFieldPhone);

        HBox passwordBox = new HBox(10);
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Новый пароль");
        passwordBox.getChildren().addAll(new Label("Новый пароль"), passwordField);

        profileBox.getChildren().addAll(
                loginBox,
                nameBox,
                surnameBox,
                emailBox,
                phoneBox,
                passwordBox
        );

        HBox buttonBox = new HBox(10);
        Button saveButton = new Button("Сохранить изменения");
        saveButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-border-radius: 5; -fx-background-radius: 5;");
        saveButton.setOnAction(e -> {
            try {
                saveProfileChanges(
                        textFieldLogin.getText(),
                        textFieldName.getText(),
                        textFieldSurname.getText(),
                        textFieldEmail.getText(),
                        textFieldPhone.getText(),
                        passwordField.getText()
                );
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            } catch (ClassNotFoundException ex) {
                throw new RuntimeException(ex);
            }
            loadUserInfo();
        });

        Button cancelButton = new Button("Отменить");
        cancelButton.setStyle("-fx-background-color: #F44336; -fx-text-fill: white; -fx-border-radius: 5; -fx-background-radius: 5;");
        cancelButton.setOnAction(e -> loadUserInfo());

        buttonBox.getChildren().addAll(saveButton, cancelButton);
        profileBox.getChildren().add(buttonBox);
    }

    private void saveProfileChanges(String login, String name, String surname, String email, String phone, String password) throws IOException, ClassNotFoundException {
        AccountDTO newAccount = new AccountDTO();
        UserInfoDTO newUserInfo = new UserInfoDTO();

        newAccount.setAccountId(accountInfo.getAccountId());
        newAccount.setUsername(login);
        newAccount.setPasswordHash(accountInfo.getPasswordHash());
        newAccount.setAccessLevel(3);
        newAccount.setCreatedAt(accountInfo.getCreatedAt());

        newUserInfo.setUserId(userInfo.getUserId());
        newUserInfo.setFirstName(name);
        newUserInfo.setLastName(surname);
        newUserInfo.setEmail(email);
        newUserInfo.setContactInfo(phone);
        newUserInfo.setAccountId(accountInfo.getAccountId());

        System.out.println(newAccount);
        System.out.println(newUserInfo);

        ConfirmCommands response = changePersonalData(newAccount, newUserInfo);
        switch (response) {
            case SUCCESSFULLY:
                //System.out.println("начало");
                AccountDTO newAcc = (AccountDTO)BaseRequestController.getObjectInputStream().readObject();
                //System.out.println(newAcc);
                UserInfoDTO newUser = (UserInfoDTO)BaseRequestController.getObjectInputStream().readObject();
                //System.out.println(newUser);

                accountInfo.setPasswordHash(newAcc.getPasswordHash());
                accountInfo.setUsername(newAcc.getUsername());

                userInfo.setContactInfo(newUser.getContactInfo());
                userInfo.setAccountId(newAcc.getAccountId());
                userInfo.setEmail(newUser.getEmail());
                userInfo.setFirstName(newUser.getFirstName());
                userInfo.setLastName(newUser.getLastName());

                labelMessage.setText("Редактирование профиля успешно!");
                labelMessage.setVisible(true);
                break;
            case FAILED:
                labelMessage.setText("Уже существует аккаунт с таким именем!");
                labelMessage.setVisible(true);
                break;
            default:
                labelMessage.setText("Ошибка");
                labelMessage.setVisible(true);
                break;
        }
    }

    private void loadUserInfo() {
        profileBox.getChildren().clear();
        labelLogin.setText("Логин: " + accountInfo.getUsername());
        labelName.setText("Имя: " + userInfo.getFirstName());
        labelSurname.setText("Фамилия: " + userInfo.getLastName());
        labelEmail.setText("Почта: " + userInfo.getEmail());
        labelPhone.setText("Номер телефона: " + userInfo.getContactInfo());
        labelCreatedAt.setText("Аккаунт создан: " + accountInfo.getCreatedAt());

        profileBox.getChildren().addAll(
                labelLogin,
                labelName,
                labelSurname,
                labelEmail,
                labelPhone,
                labelRole,
                labelCreatedAt
        );

        btnLogout.setDisable(false);
        btnUpdateProfile.setDisable(false);
        btnStoreManagement.setDisable(false);
        btnOrderManagement.setDisable(false);
        btnUserManagement.setDisable(false);
    }

    public void UserManagement(ActionEvent actionEvent) throws IOException {
        GUI.setRoot("manage_users");
    }

    public void StoreManagement(ActionEvent actionEvent) throws IOException {
        GUI.setRoot("manage_store");
        ManageStore userStore = (ManageStore) GUI.getController();
        userStore.setUserAcessLevel(accountInfo.getAccessLevel());
    }

    public void OrderManagement(ActionEvent actionEvent) throws IOException {
        GUI.setRoot("manage_orders");
        ManageOrders userStore = (ManageOrders) GUI.getController();
        userStore.setUserAcessLevel(accountInfo.getAccessLevel());
    }

    public void Logout(ActionEvent actionEvent) throws IOException {
        BaseRequestController.sendRequest(AdminCommands.class, AdminCommands.LOGOUT);
        GUI.setRoot("authorization");
    }
}
