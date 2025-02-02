package gui.controller;

import controllers.ProductController;
import gui.AddCategoryWindow;
import gui.DeleteCategoryWindow;
import gui.GUI;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.util.converter.DoubleStringConverter;
import javafx.util.converter.IntegerStringConverter;
import request.commands.ConfirmCommands;
import request.tdo.CategoryDTO;
import request.tdo.ProductDTO;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.function.UnaryOperator;

import static controllers.ProductController.*;

public class AddProduct {

    @FXML
    private TextField textFieldName;
    @FXML
    private TextField textFieldModel;
    @FXML
    private TextField textFieldBrand;
    @FXML
    private TextField textFieldPrice;
    @FXML
    private TextField textFieldAmount;
    @FXML
    private ComboBox<String> comboBoxCategory;
    @FXML
    private Button buttonAddCategory;
    @FXML
    private Button buttonBack;
    @FXML
    private Button buttonSubmit;

    @FXML
    public void initialize() {
        // цена
        UnaryOperator<TextFormatter.Change> priceValidation = change -> {
            String newText = change.getControlNewText();
            if (newText.matches("\\d*\\.?\\d*")) {
                return change;
            }
            return null;
        };

        // кол-во
        UnaryOperator<TextFormatter.Change> amountValidation = change -> {
            String newText = change.getControlNewText();
            if (newText.matches("\\d*")) {
                return change;
            }
            return null;
        };

        textFieldPrice.setTextFormatter(new TextFormatter<>(new DoubleStringConverter(), 0.0, priceValidation));
        textFieldAmount.setTextFormatter(new TextFormatter<>(new IntegerStringConverter(), 0, amountValidation));

        List<CategoryDTO> categories = getCategories();
        for (CategoryDTO category : categories) {
            comboBoxCategory.getItems().add(category.getName());
        }
    }

    @FXML
    private void onClickBack() throws IOException {
        GUI.setRoot("manage_store");
    }

    @FXML
    private void onClickSubmit() {
        if (textFieldName.getText().isEmpty() || textFieldModel.getText().isEmpty() || textFieldBrand.getText().isEmpty() || textFieldPrice.getText().isEmpty() || textFieldAmount.getText().isEmpty() || comboBoxCategory.getValue() == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Пожалуйста, заполните все поля формы!");
            alert.showAndWait();
            return; // Выход из метода, если есть ошибка
        }

        // Создание нового продукта
        ProductDTO newProduct = new ProductDTO();
        newProduct.setName(textFieldName.getText());
        newProduct.setModel(textFieldModel.getText());
        newProduct.setBrand(textFieldBrand.getText());
        newProduct.setPrice(new BigDecimal(textFieldPrice.getText()));
        newProduct.setAmount(Integer.parseInt(textFieldAmount.getText()));

        // Получение выбранной категории
        String selectedCategoryName = comboBoxCategory.getValue();
        CategoryDTO selectedCategory = getCategoryByName(selectedCategoryName);

        // Вызов метода добавления продукта
        ConfirmCommands result = ProductController.add_product(newProduct, selectedCategory);
        switch (result) {
            case SUCCESSFULLY:
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Успех");
                alert.setHeaderText(null);
                alert.setContentText("Товар успешно добавлен!");
                alert.showAndWait();
                clearFields();
                break;
            case FAILED:
                Alert alert1 = new Alert(Alert.AlertType.ERROR);
                alert1.setTitle("Ошибка");
                alert1.setHeaderText(null);
                alert1.setContentText("Товар с таким названием существует!");
                alert1.showAndWait();
                break;
            case SOMETHINGWRONG:
                Alert alert2 = new Alert(Alert.AlertType.ERROR);
                alert2.setTitle("Ошибка");
                alert2.setHeaderText(null);
                alert2.setContentText("Случилась непредвиденная ошибка");
                alert2.showAndWait();
                break;
            default:
                break;
        }
    }

    private CategoryDTO getCategoryByName(String name) {
        List<CategoryDTO> categories = getCategories();
        for (CategoryDTO category : categories) {
            if (category.getName().equals(name)) {
                return category;
            }
        }
        return null;
    }


    private void clearFields() {
        textFieldName.clear();
        textFieldModel.clear();
        textFieldBrand.clear();
        textFieldPrice.clear();
        textFieldAmount.clear();
        comboBoxCategory.setValue(null);
    }


    public void AddCategory(ActionEvent actionEvent) {
        String categoryName = AddCategoryWindow.display("Введите название новой категории:");

        if (categoryName != null && !categoryName.isEmpty()) {
            ConfirmCommands result = ProductController.add_category(categoryName);

            switch (result) {
                case SUCCESSFULLY:
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Успех");
                    alert.setHeaderText(null);
                    alert.setContentText("Категория успешно добавлена!");
                    alert.showAndWait();

                    // Добавляем новую категорию в ComboBox
                    comboBoxCategory.getItems().add(categoryName);
                    break;
                case FAILED:
                    Alert alert1 = new Alert(Alert.AlertType.ERROR);
                    alert1.setTitle("Ошибка");
                    alert1.setHeaderText(null);
                    alert1.setContentText("Категория с таким названием уже существует!");
                    alert1.showAndWait();
                    break;
                case SOMETHINGWRONG:
                    Alert alert2 = new Alert(Alert.AlertType.ERROR);
                    alert2.setTitle("Ошибка");
                    alert2.setHeaderText(null);
                    alert2.setContentText("Случилась непредвиденная ошибка при добавлении категории");
                    alert2.showAndWait();
                    break;
                default:
                    break;
            }
        }
    }


    public void DelCategory(ActionEvent actionEvent) {
            List<CategoryDTO> categories = getCategories();
            CategoryDTO categoryToDelete = DeleteCategoryWindow.display("Выберите категорию для удаления:", categories);

            if (categoryToDelete != null) {
                ConfirmCommands result = delete_category(categoryToDelete.getName());

                switch (result) {
                    case SUCCESSFULLY:
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Успех");
                        alert.setHeaderText(null);
                        alert.setContentText("Категория успешно удалена!");
                        alert.showAndWait();

                        comboBoxCategory.getItems().remove(categoryToDelete.getName());
                        break;
                    case FAILED:
                        Alert alert1 = new Alert(Alert.AlertType.ERROR);
                        alert1.setTitle("Ошибка");
                        alert1.setHeaderText(null);
                        alert1.setContentText("Категория не найдена или не может быть удалена!");
                        alert1.showAndWait();
                        break;
                    case SOMETHINGWRONG:
                        Alert alert2 = new Alert(Alert.AlertType.ERROR);
                        alert2.setTitle("Ошибка");
                        alert2.setHeaderText(null);
                        alert2.setContentText("Случилась непредвиденная ошибка при удалении категории");
                        alert2.showAndWait();
                        break;
                    default:
                        break;
                }
            }

    }
}
