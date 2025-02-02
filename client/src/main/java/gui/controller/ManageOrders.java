package gui.controller;

import controllers.OrderController;
import controllers.services.ProductService;
import controllers.services.ReportSaledProducts;
import gui.GUI;
import gui.ReportPeriodSelectionWindow;
import gui.StatusSelectionWindow;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import request.commands.ConfirmCommands;
import request.tdo.CategoryDTO;
import request.tdo.OrderElementDTO;
import request.tdo.ProductDTO;
import request.tdo.UserOrderDTO;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class ManageOrders {
    @FXML
    public Button btnChangeOrderStatus;
    @FXML
    public Button btnShowOrderDetails;
    @FXML
    public ComboBox<String> comboBoxStatus;
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

    private int userAcessLevel;

    public void setUserAcessLevel(int userAcessLevel) {
        this.userAcessLevel = userAcessLevel;
    }

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

        // Загрузка заказов при инициализации
        ShowOrders(new ActionEvent());

        // Установка слушателя на выбор элемента в таблице
        mainOrdersView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            btnChangeOrderStatus.setDisable(newValue == null);
            btnShowOrderDetails.setDisable(newValue == null);
        });
    }

    public void ChangeOrderStatus(ActionEvent actionEvent) {
        UserOrderDTO selectedOrder = mainOrdersView.getSelectionModel().getSelectedItem();
        if (selectedOrder != null) {
            if (selectedOrder.getStatus().equals("Готов") || selectedOrder.getStatus().equals("Отменён")) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Отмена");
                alert.setHeaderText(null);
                alert.setContentText("Статус заказа не может быть изменён, так как заказ отменён либо готов");
                alert.showAndWait();
                return;
            }
            String newStatus = StatusSelectionWindow.display("Выберите новый статус для заказа ID: " + selectedOrder.getOrderId());
            if (newStatus != null) {
                // Логика изменения статуса заказа
                selectedOrder.setStatus(newStatus);
                ConfirmCommands result = OrderController.changeOrderStatus(selectedOrder);
                switch (result) {
                    case SUCCESSFULLY:
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Успех");
                        alert.setHeaderText(null);
                        alert.setContentText("Статус успешно изменён!");
                        alert.showAndWait();
                        ShowOrders(actionEvent);
                        break;
                    default:
                        Alert alert1 = new Alert(Alert.AlertType.ERROR);
                        alert1.setTitle("Ошибка");
                        alert1.setHeaderText(null);
                        alert1.setContentText("Произошла ошибка при изменении статуса");
                        alert1.showAndWait();
                        break;
                }
            }
        }
    }

    public void ShowOrders(ActionEvent actionEvent) {
        List<UserOrderDTO> orders = ProductService.getOrders();
        String selectedStatus = comboBoxStatus.getSelectionModel().getSelectedItem();

        // Фильтрация заказов по статусу
        if (!selectedStatus.equals("Все статусы")) {
            orders = orders.stream()
                    .filter(order -> order.getStatus().equals(selectedStatus))
                    .collect(Collectors.toList());
        }

        mainOrdersView.setItems(FXCollections.observableArrayList(orders));
    }

    public void ShowOrderDetails(ActionEvent actionEvent) {
        UserOrderDTO selectedOrder = mainOrdersView.getSelectionModel().getSelectedItem();
        if (selectedOrder != null) {
            // Получаем элементы заказа
            List<OrderElementDTO> orderElements = OrderController.showUserOrderDetails(selectedOrder); // Предполагается, что этот метод возвращает элементы заказа

            // Получаем список всех продуктов
            List<ProductDTO> allProducts = ProductService.getProducts();

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

    public void onClickReset(ActionEvent actionEvent) {
        comboBoxStatus.getSelectionModel().select("Все статусы");
        ShowOrders(actionEvent); // Показать все заказы
    }

    public void Back(ActionEvent actionEvent) throws IOException {
        if (userAcessLevel == 2) {
            GUI.setRoot("manager_info");
        }
        else {
            GUI.setRoot("admin_info");
        }
    }

    public void ReportSaledProducts(ActionEvent actionEvent) {
        // Открываем модальное окно для выбора периода отчета
        String period = ReportPeriodSelectionWindow.display();
        if (period == null) {
            return; // Если пользователь закрыл окно, ничего не делаем
        }

        // Получаем все заказы
        List<UserOrderDTO> orders = ProductService.getOrders();

        // Получаем все продукты
        List<ProductDTO> allProducts = ProductService.getProducts();

        // Фильтруем заказы по статусу и дате
        List<OrderElementDTO> soldProducts = orders.stream()
                .filter(order -> order.getStatus().equals("Готов"))
                .filter(order -> {
                    if (period.equals("MONTH")) {
                        // Фильтруем заказы за последний месяц
                        LocalDateTime oneMonthAgo = LocalDateTime.now().minusMonths(1);
                        return order.getOrderCreated().toLocalDate().isAfter(oneMonthAgo.toLocalDate());
                    }
                    return true; // Для "ALL_TIME" возвращаем все заказы
                })
                .flatMap(order -> OrderController.showUserOrderDetails(order).stream()) // Получаем элементы заказа
                .collect(Collectors.toList());

        // Открываем диалог выбора файла
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Сохранить отчет о проданных товарах");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text Files", "*.txt"));
        fileChooser.setInitialFileName("sold_products_report.txt");

        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        File file = fileChooser.showSaveDialog(stage);

        if (file != null) {
            ReportSaledProducts reportService = new ReportSaledProducts();

            // Получаем список категорий
            List<CategoryDTO> categories = ProductService.getCategories();

            try {
                reportService.generateReport(soldProducts, allProducts, categories, file.getAbsolutePath(), period);
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Успех");
                alert.setHeaderText(null);
                alert.setContentText("Отчет о проданных товарах успешно сгенерирован и сохранен!");
                alert.showAndWait();
            } catch (RuntimeException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Ошибка");
                alert.setHeaderText(null);
                alert.setContentText(e.getMessage());
                alert.showAndWait();
            }
        }
    }
}