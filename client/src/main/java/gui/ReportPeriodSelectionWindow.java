package gui;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class ReportPeriodSelectionWindow {

    public static String display() {
        Dialog<String> dialog = new Dialog<>();
        dialog.setTitle("Выбор периода отчета");
        dialog.setHeaderText("Выберите период для отчета о проданных товарах:");

        ToggleGroup group = new ToggleGroup();
        RadioButton monthOption = new RadioButton("За месяц");
        monthOption.setToggleGroup(group);
        RadioButton allTimeOption = new RadioButton("За все время");
        allTimeOption.setToggleGroup(group);
        monthOption.setSelected(true); // Установить по умолчанию

        VBox vbox = new VBox(monthOption, allTimeOption);
        dialog.getDialogPane().setContent(vbox);

        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        dialog.setResultConverter(button -> {
            if (button == ButtonType.OK) {
                return monthOption.isSelected() ? "MONTH" : "ALL_TIME";
            }
            return null;
        });

        Stage stage = (Stage) dialog.getDialogPane().getScene().getWindow();
        stage.setOnCloseRequest(event -> dialog.setResult(null));

        return dialog.showAndWait().orElse(null);
    }
}
