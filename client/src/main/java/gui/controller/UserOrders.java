package gui.controller;

import controllers.services.ProductService;
import gui.GUI;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import request.tdo.AccountDTO;
import request.tdo.OrderElementDTO;
import request.tdo.ProductDTO;
import request.tdo.UserOrderDTO;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import static controllers.OrderController.showOrderDetails;

public class UserOrders {
    @FXML
    public ComboBox<String> comboBoxStatus;
    @FXML
    public Button btnShowOrderDetails;
    @FXML
    public Button btnResetFilter; // Кнопка для сброса фильтра
    @FXML
    public TableView<UserOrderDTO> mainOrdersView;
    @FXML
    public TableColumn<UserOrderDTO, String> idColumn;
    @FXML
    public TableColumn<UserOrderDTO, String> dateCreatedAtColumn;
    @FXML
    public TableColumn<UserOrderDTO, String> statusColumn;
    @FXML
    public TableColumn<UserOrderDTO, String> dateLastUpdatedColumn;
    @FXML
    public TableColumn<UserOrderDTO, String> totalCostColumn;

    private AccountDTO account;

    @FXML
    public void initialize() {
        // Настройка столбцов таблицы
        idColumn.setCellValueFactory(cellData -> new SimpleStringProperty(String.valueOf(cellData.getValue().getOrderId())));
        dateCreatedAtColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getOrderCreated().toString()));
        statusColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getStatus()));
        dateLastUpdatedColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getStatusLastUpdatedDate().toString()));
        totalCostColumn.setCellValueFactory(cellData -> new SimpleStringProperty(String.valueOf(cellData.getValue().getTotalCost())));

        // Добавление статусов в ComboBox
        comboBoxStatus.getItems().addAll("Все статусы", "Создан", "В обработке", "Готов", "Отменён");
        comboBoxStatus.getSelectionModel().select("Все статусы");

        // Обработчик выбора элемента в таблице
        mainOrdersView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            btnShowOrderDetails.setDisable(newValue == null);
        });
    }

    public void ShowOrderDetails(ActionEvent actionEvent) {
        UserOrderDTO selectedOrder = mainOrdersView.getSelectionModel().getSelectedItem();
        if (selectedOrder != null) {
            List<OrderElementDTO> orderElements = showOrderDetails(selectedOrder); // Получаем элементы заказа

            // Получаем список всех продуктов
            List<ProductDTO> allProducts = ProductService.getUserProducts();

            // Создаем модальное окно для отображения деталей заказа
            Stage stage = new Stage();
            stage.setTitle("Детали заказа");
            stage.initModality(Modality.APPLICATION_MODAL);

            VBox vBox = new VBox();
            vBox.setAlignment(Pos.CENTER);
            vBox.setSpacing(10);

            // Добавляем заголовок
            vBox.getChildren().add(new Label("Детали заказа ID: " + selectedOrder.getOrderId()));

            // Добавляем элементы заказа в окно
            for (OrderElementDTO element : orderElements) {
                ProductDTO product = findProductById(allProducts, element.getProductId()); // Получаем информацию о продукте
                if (product != null) {
                    String elementInfo = String.format("Название: %s, Бренд: %s, Модель: %s, Количество: %d",
                            product.getName(), product.getBrand(), product.getModel(), element.getQuantity());
                    vBox.getChildren().add(new Label(elementInfo));
                } else {
                    // Если продукт не найден, можно добавить сообщение об этом
                    vBox.getChildren().add(new Label("Продукт с ID " + element.getProductId() + " не найден."));
                }
            }

            Button closeButton = new Button("Закрыть");
            closeButton.setOnAction(e -> stage.close());
            vBox.getChildren().add(closeButton);

            BorderPane borderPane = new BorderPane();
            borderPane.setCenter(vBox);
            Scene scene = new Scene(borderPane, 500, 600);
            stage.setScene(scene);
            stage.showAndWait();
        }
    }

    // Метод для поиска продукта по ID
    private ProductDTO findProductById(List<ProductDTO> products, int productId) {
        for (ProductDTO product : products) {
            if (product.getProductId() == productId) {
                return product;
            }
        }
        return null; // Если продукт не найден
    }



    public void ShowOrders(ActionEvent actionEvent) {
        List<UserOrderDTO> orders = ProductService.getUserOrders();
        String selectedStatus = comboBoxStatus.getSelectionModel().getSelectedItem();

        // Фильтрация заказов по статусу
        if (!selectedStatus.equals("Все статусы")) {
            orders = orders.stream()
                    .filter(order -> order.getStatus().equals(selectedStatus))
                    .collect(Collectors.toList());
        }

        mainOrdersView.setItems(FXCollections.observableArrayList(orders));
    }

    public void onClickReset(ActionEvent actionEvent) {
        // Сброс фильтра
        comboBoxStatus.getSelectionModel().select("Все статусы");
        ShowOrders(actionEvent); // Показать все заказы
    }

    public void Back(ActionEvent actionEvent) throws IOException {
        GUI.setRoot("user_info");
    }

    public void setAccount(AccountDTO account) {
        this.account = account;
    }
}
