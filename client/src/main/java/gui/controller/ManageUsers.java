package gui.controller;

import controllers.AllUsersController;
import gui.GUI;
import gui.RoleSelectionWindow;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.MouseEvent;
import javafx.util.StringConverter;
import request.tdo.AccountDTO;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import static controllers.AllUsersController.changeAccess;
import static controllers.AllUsersController.getAccountList;

public class ManageUsers {
    @FXML
    private TableColumn<AccountDTO, String> usernameColumn;
    @FXML
    private TableColumn<AccountDTO, Integer> accessLevelColumn;
    @FXML
    private TableColumn<AccountDTO, Integer> idColumn;

    public Button btnChangeAccess;
    public ComboBox<String> comboBoxCategory;
    public TableView<AccountDTO> mainUsersView;

    @FXML
    public void initialize() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("accountId"));
        usernameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));
        accessLevelColumn.setCellValueFactory(new PropertyValueFactory<>("accessLevel"));

        accessLevelColumn.setCellFactory(TextFieldTableCell.forTableColumn(new StringConverter<Integer>() {
            @Override
            public String toString(Integer accessLevel) {
                switch (accessLevel) {
                    case 1:
                        return "Пользователь";
                    case 2:
                        return "Менеджер";
                    case 3:
                        return "Администратор";
                    case 0:
                        return "Бан";
                    default:
                        return "Неизвестно";
                }
            }

            @Override
            public Integer fromString(String string) {
                switch (string) {
                    case "Пользователь":
                        return 1;
                    case "Менеджер":
                        return 2;
                    case "Администратор":
                        return 3;
                    case "Бан":
                        return 0;
                    default:
                        return -1;
                }
            }
        }));

        btnChangeAccess.setDisable(true);

        comboBoxCategory.setItems(FXCollections.observableArrayList("Все аккаунты", "Пользователи", "Менеджеры", "Заблокированные"));
        comboBoxCategory.getSelectionModel().select("Все аккаунты");

        mainUsersView.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if (mainUsersView.getSelectionModel().getSelectedItem() != null)
                    btnChangeAccess.setDisable(false);
            }
        });

        updateTable();
    }

    public void AddManager(ActionEvent actionEvent) throws IOException {
        GUI.setRoot("add_manager");
    }

    public void ChangeAccess(ActionEvent actionEvent) {
        btnChangeAccess.setDisable(true);
        if (mainUsersView.getSelectionModel().getSelectedItem() == null)
            return;

        int newRole = RoleSelectionWindow.display("Выберите новую роль:");

        if (newRole != -1) {
            changeAccess(mainUsersView.getSelectionModel().getSelectedItem(), newRole);
            updateTable();
        }
    }

    public void ShowUsers(ActionEvent actionEvent) {
        btnChangeAccess.setDisable(true);
        String selectedCategory = comboBoxCategory.getSelectionModel().getSelectedItem();
        List<AccountDTO> accounts = getAccountList();

        List<AccountDTO> filteredAccounts;

        if (selectedCategory.equals("Все аккаунты")) {
            filteredAccounts = accounts;
        } else if (selectedCategory.equals("Пользователи")) {
            filteredAccounts = accounts.stream().filter(account -> account.getAccessLevel() == 1).collect(Collectors.toList());
        } else if (selectedCategory.equals("Менеджеры")) {
            filteredAccounts = accounts.stream().filter(account -> account.getAccessLevel() == 2).collect(Collectors.toList());
        } else {
            filteredAccounts = accounts.stream().filter(account -> account.getAccessLevel() == 0).collect(Collectors.toList());
        }

        mainUsersView.setItems(FXCollections.observableArrayList(filteredAccounts));
    }

    public void onClickReset(ActionEvent actionEvent) {
        btnChangeAccess.setDisable(true);
        comboBoxCategory.getSelectionModel().select("Все аккаунты");
        updateTable();
    }

    public void Back(ActionEvent actionEvent) throws IOException {
        GUI.setRoot("admin_info");
    }

    public void updateTable() {
        btnChangeAccess.setDisable(true);

        List<AccountDTO> accounts = getAccountList();

        mainUsersView.getItems().clear();
        if (accounts == null) {
            return;
        }
        mainUsersView.setItems(FXCollections.observableArrayList(accounts));
    }
}
