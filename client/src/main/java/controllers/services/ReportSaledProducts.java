package controllers.services;

import request.tdo.OrderElementDTO;
import request.tdo.ProductDTO;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.List;
import request.tdo.CategoryDTO;

import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.HashMap;

public class ReportSaledProducts {

    public void generateReport(List<OrderElementDTO> soldProducts, List<ProductDTO> allProducts, List<CategoryDTO> categories, String filePath, String period) {
        StringBuilder reportContent = new StringBuilder();

        // Получаем текущую дату для заголовка
        String month = LocalDate.now().getMonth().getDisplayName(TextStyle.FULL, Locale.getDefault());
        reportContent.append(String.format("Отчет о проданных товарах за %s%n", month));
        reportContent.append(String.format("%-30s %-20s %-20s %-15s%n", "Название", "Бренд", "Модель", "Количество"));
        reportContent.append(new String(new char[85]).replace('\0', '-')).append("\n"); // Разделитель

        // Создаем карту для сопоставления ID категорий с их названиями
        Map<Integer, String> categoryMap = new HashMap<>();
        for (CategoryDTO category : categories) {
            categoryMap.put(category.getCategoryId(), category.getName()); // Предполагается, что у CategoryDTO есть методы getId() и getName()
        }

        // Группируем проданные товары по категориям
        Map<String, List<OrderElementDTO>> groupedByCategory = soldProducts.stream()
                .collect(Collectors.groupingBy(element -> {
                    ProductDTO product = findProductById(allProducts, element.getProductId());
                    return product != null ? categoryMap.getOrDefault(product.getCategoryId(), "Без категории") : "Без категории";
                }));

        // Формируем отчет по категориям
        for (Map.Entry<String, List<OrderElementDTO>> entry : groupedByCategory.entrySet()) {
            String category = entry.getKey();
            reportContent.append(String.format("Категория: %s%n", category));
            for (OrderElementDTO element : entry.getValue()) {
                ProductDTO product = findProductById(allProducts, element.getProductId());
                if (product != null) {
                    reportContent.append(String.format("%-30s %-20s %-20s %-15d%n",
                            product.getName(),
                            product.getBrand(),
                            product.getModel(),
                            element.getQuantity()));
                }
            }
            reportContent.append("\n"); // Разделитель между категориями
        }

        // Генерация файла отчета
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write(reportContent.toString().trim()); // Удаляем лишние пробелы в конце
        } catch (IOException e) {
            throw new RuntimeException("Ошибка при генерации отчета: " + e.getMessage(), e);
        }
    }

    // Метод для поиска продукта по ID
    private ProductDTO findProductById(List<ProductDTO> products, int productId) {
        return products.stream()
                .filter(product -> product.getProductId() == productId)
                .findFirst()
                .orElse(null); // Если продукт не найден
    }
}
