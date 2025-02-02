package gui.controller;

import controllers.CartController;
import gui.GUI;
import gui.QuantityInputWindow;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import request.commands.ConfirmCommands;
import request.tdo.AccountDTO;
import request.tdo.CartElementDTO;
import request.tdo.CategoryDTO;
import request.tdo.ProductDTO;
import controllers.services.ProductService;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

public class UserStore {
    @FXML
    private TableView<ProductDTO> electronicsTableView;

    @FXML
    private TableColumn<ProductDTO, String> electronicsNameColumn;
    @FXML
    private TableColumn<ProductDTO, String> electronicsBrandColumn;
    @FXML
    private TableColumn<ProductDTO, BigDecimal> electronicsPriceColumn;
    @FXML
    private TableColumn<ProductDTO, String> electronicsModelColumn;
    @FXML
    private TableColumn<ProductDTO, Integer> electronicsAmountColumn;
    @FXML
    private TableColumn<ProductDTO, String> electronicsCategoryColumn;

    @FXML
    private ComboBox<String> comboBoxCategory;
    @FXML
    private TextField textFieldPriceFrom;
    @FXML
    private TextField textFieldPriceTo;
    @FXML
    private TextField textFieldSearch;

    @FXML
    public Button btnAddToCart;

    private AccountDTO account;

    @FXML
    public void initialize() {
        electronicsNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        electronicsCategoryColumn.setCellValueFactory(cellData -> {
            int categoryId = cellData.getValue().getCategoryId();
            CategoryDTO category = ProductService.getUserCategories().stream()
                    .filter(c -> c.getCategoryId() == categoryId)
                    .findFirst()
                    .orElse(null);
            return new SimpleStringProperty(category != null ? category.getName() : "Unknown");
        });
        electronicsBrandColumn.setCellValueFactory(new PropertyValueFactory<>("brand"));
        electronicsModelColumn.setCellValueFactory(new PropertyValueFactory<>("model"));
        electronicsPriceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        electronicsAmountColumn.setCellValueFactory(new PropertyValueFactory<>("amount"));

        electronicsTableView.setOnMouseClicked(event -> {
            if (electronicsTableView.getSelectionModel().getSelectedItem() != null) {
                btnAddToCart.setDisable(false);
            }
        });

        comboBoxCategory.getItems().add("Все категории");
        List<CategoryDTO> categories = ProductService.getUserCategories();
        for (CategoryDTO category : categories) {
            comboBoxCategory.getItems().add(category.getName());
        }
        comboBoxCategory.getSelectionModel().select("Все категории");

        updateTable();
    }

    public void showProducts(ActionEvent actionEvent) {
        updateTable();
    }

    public void updateTable() {
        btnAddToCart.setDisable(true);

        List<ProductDTO> products = ProductService.getUserProducts();
        List<CategoryDTO> categories = ProductService.getUserCategories();

        electronicsTableView.getItems().clear();
        if (products == null) {
            return;
        }

        String selectedCategory = comboBoxCategory.getSelectionModel().getSelectedItem();
        String priceFromText = textFieldPriceFrom.getText().trim();
        String priceToText = textFieldPriceTo.getText().trim();

        BigDecimal priceFrom = priceFromText.isEmpty() ? null : new BigDecimal(priceFromText);
        BigDecimal priceTo = priceToText.isEmpty() ? null : new BigDecimal(priceToText);

        List<ProductDTO> filteredProducts = products.stream()
                .filter(product -> product.getAmount() > 0) // Фильтрация по количеству
                .filter(product -> (selectedCategory.equals("Все категории") || getCategoryName(product.getCategoryId()).equals(selectedCategory)))
                .filter(product -> (priceFrom == null || product.getPrice().compareTo(priceFrom) >= 0))
                .filter(product -> (priceTo == null || product.getPrice().compareTo(priceTo) <= 0))
                .collect(Collectors.toList());

        electronicsTableView.setItems(FXCollections.observableArrayList(filteredProducts));
    }


    private String getCategoryName(int categoryId) {
        return ProductService.getUserCategories().stream()
                .filter(c -> c.getCategoryId() == categoryId)
                .map(CategoryDTO::getName)
                .findFirst()
                .orElse("Unknown");
    }

    public void findByName(ActionEvent actionEvent) {
        String searchText = textFieldSearch.getText().trim().toLowerCase();

        List<ProductDTO> products = ProductService.getUserProducts();
        List<ProductDTO> searchResults = products.stream()
                .filter(product -> product.getName().toLowerCase().contains(searchText))
                .collect(Collectors.toList());

        electronicsTableView.getItems().clear();
        if (searchResults.isEmpty()) {
            electronicsTableView.setPlaceholder(new Label("По заданному запросу ничего не найдено"));
        } else {
            electronicsTableView.setItems(FXCollections.observableArrayList(searchResults));
        }
    }

    public void onClickReset(ActionEvent actionEvent) {
        textFieldSearch.clear();
        textFieldPriceFrom.clear();
        textFieldPriceTo.clear();
        comboBoxCategory.getSelectionModel().select("Все категории");
        updateTable();
    }

    public void addToCart(ActionEvent actionEvent) {
        ProductDTO selectedProduct = electronicsTableView.getSelectionModel().getSelectedItem();

        if (selectedProduct != null) {
            // Вызываем окно для ввода количества
            int quantity = QuantityInputWindow.show("Введите количество для добавления в корзину:");

            // Проверяем, что количество больше 0
            if (quantity > 0) {
                // Проверяем, достаточно ли товара в наличии
                if (quantity > selectedProduct.getAmount()) {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Предупреждение");
                    alert.setHeaderText(null);
                    alert.setContentText("Недостаточно товара в наличии. Доступно: " + selectedProduct.getAmount());
                    alert.showAndWait();
                    return; // Выходим из метода, если недостаточно товара
                }

                CartElementDTO cartElement = new CartElementDTO();
                cartElement.setProductId(selectedProduct.getProductId());
                cartElement.setQuantity(quantity); // Устанавливаем введенное количество
                cartElement.setSubtotalCost(selectedProduct.getPrice().multiply(BigDecimal.valueOf(quantity))); // Устанавливаем стоимость
                cartElement.setAccountId(account.getAccountId());

                // Отправляем объект на сервер
                ConfirmCommands result = CartController.addToCart(cartElement);
                if (result == ConfirmCommands.SUCCESSFULLY) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Успех");
                    alert.setHeaderText(null);
                    alert.setContentText("Товар добавлен в корзину!");
                    alert.showAndWait();
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Ошибка");
                    alert.setHeaderText(null);
                    alert.setContentText("Не удалось добавить товар в корзину.");
                    alert.showAndWait();
                }
            } else {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Предупреждение");
                alert.setHeaderText(null);
                alert.setContentText("Количество должно быть больше 0.");
                alert.showAndWait();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Предупреждение");
            alert.setHeaderText(null);
            alert.setContentText("Пожалуйста, выберите продукт для добавления в корзину.");
            alert.showAndWait();
        }
    }



    public void setAccount(AccountDTO account) {
        this.account = account;
    }

    public void goBack(ActionEvent actionEvent) throws IOException {
        GUI.setRoot("user_info");
    }

    public void goToCart(ActionEvent actionEvent) throws IOException {
        GUI.setRoot("user_cart");
        UserCart userStore = (UserCart) GUI.getController();
        userStore.setAccount(account);
    }
}
