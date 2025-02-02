package gui.controller;

import java.io.IOException;

import controllers.RegistrationController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import request.commands.ConfirmCommands;

import gui.GUI;

public class Registration {

    @FXML
    private TextField textFieldName;
    @FXML
    private TextField textFieldEmail;
    @FXML
    private TextField textFieldSurname;
    @FXML
    private TextField textFieldContact;
    @FXML
    private TextField textFieldLogin;
    @FXML
    private PasswordField passwordFieldPassword;
    @FXML
    private Text warning;

    @FXML
    public void onClickBack(ActionEvent actionEvent) {
        try {
            GUI.setRoot("authorization");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void onClickSubmit(ActionEvent actionEvent) {
        if (textFieldLogin.getText().isBlank() ||
                passwordFieldPassword.getText().isEmpty() ||
                textFieldName.getText().isBlank() ||
                textFieldSurname.getText().isBlank() ||
                textFieldEmail.getText().isBlank()) {
            warning.setText("Заполните обязательные поля!");
            warning.setVisible(true);
            return;
        }

        ConfirmCommands result = RegistrationController.registration(
                textFieldLogin.getText().strip(),
                passwordFieldPassword.getText(),
                textFieldName.getText().strip(),
                textFieldSurname.getText().strip(),
                textFieldEmail.getText().strip(),
                textFieldContact.getText().strip()
        );

        switch (result) {
            case SUCCESSFULLY:
                try {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Успех");
                    alert.setHeaderText(null);
                    alert.setContentText("Регистрация успешна!");
                    alert.showAndWait();

                    GUI.setRoot("authorization");
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case FAILED:
                warning.setText("Уже существует аккаунт с таким именем!");
                warning.setVisible(true);
                break;
            case SOMETHINGWRONG:
                warning.setText("Произошла ошибка");
                warning.setVisible(true);
                break;
        }
    }
}
