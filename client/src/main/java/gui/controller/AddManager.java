package gui.controller;

import controllers.AllUsersController;
import gui.GUI;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import request.commands.ConfirmCommands;

import java.io.IOException;

public class AddManager {
    @FXML
    private Text warning;
    @FXML
    private TextField textFieldName, textFieldSurname, textFieldEmail, textFieldContact, textFieldLogin, passwordFieldPassword;

    public void onClickBack(ActionEvent actionEvent) throws IOException {
        GUI.setRoot("manage_users");
    }

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

        ConfirmCommands result = AllUsersController.add_manager(
                textFieldLogin.getText().strip(),
                passwordFieldPassword.getText(),
                textFieldName.getText().strip(),
                textFieldSurname.getText().strip(),
                textFieldEmail.getText().strip(),
                textFieldContact.getText().strip()
        );

        switch (result) {
            case SUCCESSFULLY:
                warning.setText("всё кайф");
                warning.setVisible(true);
                break;
            case FAILED:
            case SOMETHINGWRONG:
                warning.setText("Уже существует аккаунт с таким именем!");
                warning.setVisible(true);
                break;
        }
    }
}
