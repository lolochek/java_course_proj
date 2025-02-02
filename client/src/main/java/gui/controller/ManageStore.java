package gui.controller;

import controllers.services.ReportGenerationService;
import gui.ConfirmWindow;
import gui.GUI;

import gui.RoleSelectionWindow;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import request.commands.ConfirmCommands;
import request.tdo.CategoryDTO;
import request.tdo.ProductDTO;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import static controllers.AllUsersController.changeAccess;
import static controllers.ProductController.*;

public class ManageStore {
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
    private TextField textFieldSearch;

    public Button btnDeleteProduct;
    public Button btnUpdProduct;
    public ComboBox<String> comboBoxCategory;

    private int userAcessLevel;

    public void setUserAcessLevel(int userAcessLevel) {
        this.userAcessLevel = userAcessLevel;
    }

    @FXML
    public void initialize() {
        electronicsNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        electronicsCategoryColumn.setCellValueFactory(cellData -> {
            int categoryId = cellData.getValue().getCategoryId();
            CategoryDTO category = getCategories().stream()
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
                btnDeleteProduct.setDisable(false);
                btnUpdProduct.setDisable(false);
            }
        });

        comboBoxCategory.getItems().add("Все категории");
        List<CategoryDTO> categories = getCategories();
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
        btnDeleteProduct.setDisable(true);
        btnUpdProduct.setDisable(true);

        List<ProductDTO> products = getProducts();
        List<CategoryDTO> categories = getCategories();

        electronicsTableView.getItems().clear();
        if (products == null) {
            return;
        }

        String selectedCategory = comboBoxCategory.getSelectionModel().getSelectedItem();
        List<ProductDTO> filteredProducts;

        if (selectedCategory.equals("Все категории")) {
            filteredProducts = products;
        } else {
            int selectedCategoryId = categories.stream()
                    .filter(category -> category.getName().equals(selectedCategory))
                    .findFirst()
                    .map(CategoryDTO::getCategoryId)
                    .orElse(-1);

            filteredProducts = products.stream()
                    .filter(product -> product.getCategoryId() == selectedCategoryId)
                    .collect(Collectors.toList());
        }

        electronicsTableView.setItems(FXCollections.observableArrayList(filteredProducts));
    }

    public void goBack(ActionEvent actionEvent) throws IOException {
        if (userAcessLevel == 2) {
            GUI.setRoot("manager_info");
        }
        else {
            GUI.setRoot("admin_info");
        }
    }

    public void delProduct(ActionEvent actionEvent) {
        ProductDTO selectedProduct = electronicsTableView.getSelectionModel().getSelectedItem();

        if (selectedProduct == null) {
            return;
        }
        boolean confirmed = ConfirmWindow.PrintOut("Вы уверены, что хотите удалить товар: " + selectedProduct.getName() + "?");

        if (confirmed) {
            ConfirmCommands result = deleteProduct(selectedProduct);

            // В зависимости от результата, выводим алерт
            switch (result) {
                case SUCCESSFULLY:
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Успех");
                    alert.setHeaderText(null);
                    alert.setContentText("Товар успешно удалён!");
                    alert.showAndWait();
                    break;
                default:
                    Alert alert1 = new Alert(Alert.AlertType.ERROR);
                    alert1.setTitle("Ошибка");
                    alert1.setHeaderText(null);
                    alert1.setContentText("Произошла ошибка удаления товара");
                    alert1.showAndWait();
                    break;
            }

            updateTable();
        }
    }

    public void updProduct(ActionEvent actionEvent) throws IOException {
        // Получаем выбранный товар из таблицы
        ProductDTO selectedProduct = electronicsTableView.getSelectionModel().getSelectedItem();

        if (selectedProduct == null) {
            // Если товар не выбран, ничего не делаем
            return;
        }

        GUI.setRoot("upd_product");
        UpdProduct updProductController = (UpdProduct) GUI.getController();
        updProductController.setProductToUpdate(selectedProduct);
    }


    public void findByName(ActionEvent actionEvent) {
        String searchText = textFieldSearch.getText().trim().toLowerCase();

        List<ProductDTO> products = getProducts();
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
        comboBoxCategory.getSelectionModel().select("Все категории");
        updateTable();
    }

    public void AddProduct(ActionEvent actionEvent) throws IOException {
        GUI.setRoot("add_product");
    }

    public void GenerateReport(ActionEvent actionEvent) {
        List<ProductDTO> products = getProducts();
        List<CategoryDTO> categories = getCategories();

        // Открываем диалог выбора файла алёрт
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Сохранить отчет");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text Files", "*.txt"));
        fileChooser.setInitialFileName("report.txt");

        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        File file = fileChooser.showSaveDialog(stage);

        if (file != null) {
            ReportGenerationService reportService = new ReportGenerationService();

            try {
                reportService.generateReport(products, categories, file.getAbsolutePath());
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Успех");
                alert.setHeaderText(null);
                alert.setContentText("Отчет успешно сгенерирован и сохранен!");
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
