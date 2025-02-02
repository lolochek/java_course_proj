package controllers.services;

import request.tdo.ProductDTO;
import request.tdo.CategoryDTO;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class ReportGenerationService {

    public void generateReport(List<ProductDTO> products, List<CategoryDTO> categories, String filePath) {
        StringBuilder reportContent = new StringBuilder();

        // Заголовок отчета
        reportContent.append(String.format("%-20s %-30s %-15s %-30s%n", "Категория", "Название", "Количество", "Сообщение"));
        reportContent.append(new String(new char[100]).replace('\0', '-')).append("\n");

        for (ProductDTO product : products) {
            String message = "";
            if (product.getAmount() == 0) {
                message = "Товар закончился";
            } else if (product.getAmount() <= 10) {
                message = "Необходимо пополнение";
            }

            // Формируем строку отчета
            reportContent.append(String.format("%-20s %-30s %-15d %-30s%n",
                    getCategoryNameById(product.getCategoryId(), categories),
                    product.getName(),
                    product.getAmount(),
                    message));
        }

        // Генерация файла отчета
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write(reportContent.toString().trim()); // Удаляем лишние пробелы в конце
        } catch (IOException e) {
            throw new RuntimeException("Ошибка при генерации отчета: " + e.getMessage(), e);
        }
    }

    // Метод для получения имени категории по ID
    private String getCategoryNameById(int categoryId, List<CategoryDTO> categories) {
        return categories.stream()
                .filter(category -> category.getCategoryId() == categoryId)
                .map(CategoryDTO::getName)
                .findFirst()
                .orElse("Неизвестная категория");
    }
}
