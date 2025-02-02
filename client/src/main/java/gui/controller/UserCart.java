package gui.controller;

import controllers.OrderController;
import controllers.services.ProductService;
import gui.ConfirmWindow;
import gui.GUI;
import gui.QuantityInputWindow;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import request.commands.ConfirmCommands;
import request.tdo.*;
import controllers.CartController;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

public class UserCart {
    @FXML
    public Label labelFinalCost;
    @FXML
    private Button btnMakeOrder;
    @FXML
    private Button btnChangeAmount;
    @FXML
    private Button btnRemoveCartElement;
    @FXML
    private Button btnClearCart;
    @FXML
    private TableView<CartElementDTO> mainCartElementsView;
    @FXML
    private TableColumn<CartElementDTO, String> nameColumn;
    @FXML
    private TableColumn<CartElementDTO, String> categoryColumn;
    @FXML
    private TableColumn<CartElementDTO, String> modelColumn;
    @FXML
    private TableColumn<CartElementDTO, String> brandColumn;
    @FXML
    private TableColumn<CartElementDTO, Integer> amountColumn;
    @FXML
    private TableColumn<CartElementDTO, BigDecimal> priceColumn;

    private AccountDTO account;
    @FXML
    public void initialize() {
        // Настройка колонок таблицы
        nameColumn.setCellValueFactory(cellData -> {
            CartElementDTO cartElement = cellData.getValue();
            ProductDTO product = ProductService.getProductById(cartElement.getProductId());
            return new SimpleStringProperty(product != null ? product.getName() : "Unknown");
        });

        categoryColumn.setCellValueFactory(cellData -> {
            CartElementDTO cartElement = cellData.getValue();
            ProductDTO product = ProductService.getProductById(cartElement.getProductId());
            return new SimpleStringProperty(product != null ? getCategoryName(product.getCategoryId()) : "Unknown");
        });

        modelColumn.setCellValueFactory(cellData -> {
            CartElementDTO cartElement = cellData.getValue();
            ProductDTO product = ProductService.getProductById(cartElement.getProductId());
            return new SimpleStringProperty(product != null ? product.getModel() : "Unknown");
        });

        brandColumn.setCellValueFactory(cellData -> {
            CartElementDTO cartElement = cellData.getValue();
            ProductDTO product = ProductService.getProductById(cartElement.getProductId());
            return new SimpleStringProperty(product != null ? product.getBrand() : "Unknown");
        });

        amountColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("subtotalCost"));

        ShowCartElements(new ActionEvent());

        mainCartElementsView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            updateButtonStates(newValue);
        });
    }

    private void updateButtonStates(CartElementDTO selectedElement) {
        boolean isElementSelected = selectedElement != null;
        btnChangeAmount.setDisable(!isElementSelected);
        btnRemoveCartElement.setDisable(!isElementSelected);
    }


    public void ChangeAmount(ActionEvent actionEvent) {
        CartElementDTO selectedElement = mainCartElementsView.getSelectionModel().getSelectedItem();
        if (selectedElement != null) {
            int newQuantity = QuantityInputWindow.show("Введите новое количество:");
            if (newQuantity > 0) {
                BigDecimal pricePerUnit = ProductService.getProductById(selectedElement.getProductId()).getPrice(); // Получаем цену за единицу
                selectedElement.setQuantity(newQuantity);
                selectedElement.setSubtotalCost(pricePerUnit.multiply(BigDecimal.valueOf(newQuantity))); // Обновляем стоимость

                ConfirmCommands result = CartController.updateCartElement(selectedElement);
                if (result == ConfirmCommands.SUCCESSFULLY) {
                    ShowCartElements(actionEvent); // Обновляем отображение корзины
                } else {
                    showAlert("Ошибка", "Не удалось обновить количество товара.");
                }
            } else {
                showAlert("Предупреждение", "Количество должно быть больше 0.");
            }
        } else {
            showAlert("Предупреждение", "Пожалуйста, выберите элемент для изменения количества.");
        }
    }

    public void RemoveCartElement(ActionEvent actionEvent) {
        CartElementDTO selectedElement = mainCartElementsView.getSelectionModel().getSelectedItem();
        if (selectedElement != null) {
            boolean confirmed = ConfirmWindow.PrintOut("Вы уверены, что хотите удалить элемент из корзины?");
            if (confirmed) {
                ConfirmCommands result = CartController.removeCartElement(selectedElement);
                if (result == ConfirmCommands.SUCCESSFULLY) {
                    ShowCartElements(actionEvent); // Обновляем отображение корзины
                } else {
                    showAlert("Ошибка", "Не удалось удалить элемент из корзины.");
                }
            }
        } else {
            showAlert("Предупреждение", "Пожалуйста, выберите элемент для удаления.");
        }
    }

    public void ClearCart(ActionEvent actionEvent) {
        boolean confirmed = ConfirmWindow.PrintOut("Вы уверены, что хотите очистить корзину?");
        if (confirmed) {
            ConfirmCommands result = CartController.clearCart(account);
            if (result == ConfirmCommands.SUCCESSFULLY) {
                ShowCartElements(actionEvent);
                showAlert("Успех", "Корзина очищена.");
            } else {
                showAlert("Ошибка", "Не удалось очистить корзину.");
            }
        }
    }

    public void ShowCartElements(ActionEvent actionEvent) {
        List<CartElementDTO> cartElements = ProductService.getCartElements();

        if (cartElements == null || cartElements.isEmpty()) {
            mainCartElementsView.setItems(FXCollections.observableArrayList());
            mainCartElementsView.setPlaceholder(new Label("Корзина пуста")); // Устанавливаем сообщение о пустой корзине
            labelFinalCost.setText("Итоговая стоимость: 0"); // Обнуляем итоговую стоимость
            btnMakeOrder.setDisable(true); // Отключаем кнопку оформления заказа
        } else {
            mainCartElementsView.setItems(FXCollections.observableArrayList(cartElements));
            updateFinalCost(cartElements); // Обновляем итоговую стоимость
            btnMakeOrder.setDisable(false); // Включаем кнопку оформления заказа
        }

        updateButtonStates(mainCartElementsView.getSelectionModel().getSelectedItem());
    }

    private String getCategoryName(int categoryId) {
        return ProductService.getUserCategories().stream()
                .filter(c -> c.getCategoryId() == categoryId)
                .map(CategoryDTO::getName)
                .findFirst()
                .orElse("Unknown");
    }

    public void MakeOrder(ActionEvent actionEvent) {
        List<CartElementDTO> storage = ProductService.getCartElements();

        ConfirmCommands result = OrderController.addOrder(storage);

        if (result == ConfirmCommands.SUCCESSFULLY) {
            showAlert("Успех", "Заказ оформлен успешно!");
            ShowCartElements(actionEvent);
        } else {
            showAlert("Ошибка", "Не удалось оформить заказ.");
        }
    }

    public void onClickReset(ActionEvent actionEvent) {
        ShowCartElements(actionEvent);
    }

    public void Back(ActionEvent actionEvent) {
        try {
            GUI.setRoot("user_info");
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Ошибка", "Не удалось вернуться на предыдущую страницу.");
        }
    }

    private void updateFinalCost(List<CartElementDTO> cartElements) {
        BigDecimal totalCost = cartElements.stream()
                .map(CartElementDTO::getSubtotalCost)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        labelFinalCost.setText("Итоговая стоимость: " + totalCost);
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void setAccount(AccountDTO account) {
        this.account = account;
    }
}


