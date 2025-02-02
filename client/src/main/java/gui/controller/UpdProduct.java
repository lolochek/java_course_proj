package gui.controller;

import controllers.ProductController;
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

import static controllers.ProductController.getCategories;

public class UpdProduct {

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
    private Button buttonBack;
    @FXML
    private Button buttonSubmit;

    private ProductDTO productToUpdate;

    public void initialize() {
        // Валидация для полей цены и количества
        UnaryOperator<TextFormatter.Change> priceValidation = change -> {
            String newText = change.getControlNewText();
            if (newText.matches("\\d*\\.?\\d*")) {
                return change;
            }
            return null;
        };

        UnaryOperator<TextFormatter.Change> amountValidation = change -> {
            String newText = change.getControlNewText();
            if (newText.matches("\\d*")) {
                return change;
            }
            return null;
        };

        textFieldPrice.setTextFormatter(new TextFormatter<>(new DoubleStringConverter(), 0.0, priceValidation));
        textFieldAmount.setTextFormatter(new TextFormatter<>(new IntegerStringConverter(), 0, amountValidation));

        // Заполнение комбобокса категориями
        List<CategoryDTO> categories = getCategories();
        for (CategoryDTO category : categories) {
            comboBoxCategory.getItems().add(category.getName());
        }
    }

    public void setProductToUpdate(ProductDTO product) {
        this.productToUpdate = product;
        textFieldName.setText(product.getName());
        textFieldModel.setText(product.getModel());
        textFieldBrand.setText(product.getBrand());
        textFieldPrice.setText(product.getPrice().toString());
        textFieldAmount.setText(String.valueOf(product.getAmount()));
        comboBoxCategory.setValue(getCategoryNameById(product.getCategoryId()));
    }

    @FXML
    private void onClickBack() throws IOException {
        GUI.setRoot("manage_store");
    }

    @FXML
    private void onClickSubmit() {
        if (textFieldName.getText().isEmpty() || textFieldModel.getText().isEmpty() || textFieldBrand.getText().isEmpty() || textFieldPrice.getText().isEmpty() || textFieldAmount.getText().isEmpty() || comboBoxCategory.getValue() == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Ошибка");
            alert.setHeaderText(null);
            alert.setContentText("Пожалуйста, заполните все поля формы!");
            alert.showAndWait();
            return; // Выход из метода, если есть ошибка
        }

        // Обновление информации о товаре
        productToUpdate.setName(textFieldName.getText());
        productToUpdate.setModel(textFieldModel.getText());
        productToUpdate.setBrand(textFieldBrand.getText());
        productToUpdate.setPrice(new BigDecimal(textFieldPrice.getText()));
        productToUpdate.setAmount(Integer.parseInt(textFieldAmount.getText()));

        // Получение выбранной категории
        String selectedCategoryName = comboBoxCategory.getValue();
        CategoryDTO selectedCategory = getCategoryByName(selectedCategoryName);
        productToUpdate.setCategoryId(selectedCategory.getCategoryId());

        // Вызов метода обновления продукта
        ConfirmCommands result = ProductController.update_product(productToUpdate);
        switch (result) {
            case SUCCESSFULLY:
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Успех");
                alert.setHeaderText(null);
                alert.setContentText("Товар успешно обновлен!");
                alert.showAndWait();
                break;
            case FAILED:
                Alert alert1 = new Alert(Alert.AlertType.ERROR);
                alert1.setTitle("Ошибка");
                alert1.setHeaderText(null);
                alert1.setContentText("Не удалось обновить товар.");
                alert1.showAndWait();
                break;
            case SOMETHINGWRONG:
                Alert alert2 = new Alert(Alert.AlertType.ERROR);
                alert2.setTitle("Ошибка");
                alert2.setHeaderText(null);
                alert2.setContentText("Произошла ошибка при обновлении товара.");
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

    private String getCategoryNameById(int categoryId) {
        List<CategoryDTO> categories = getCategories();
        for (CategoryDTO category : categories) {
            if (category.getCategoryId() == categoryId) {
                return category.getName();
            }
        }
        return null;
    }
}

