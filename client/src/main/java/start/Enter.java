package start;


import java.io.IOException;

import gui.GUI;
import request.controller.BaseRequestController;
import server.ServerConnection;

public class Enter {

    public static void main(String[] args) {
        // Запускаем JavaFX интерфейс
        new Thread(() -> {
            try {
                // Подключение к серверу выполняется в отдельном потоке
                ServerConnection.Connect(3333);
                BaseRequestController.setStreams();
            } catch (IOException e) {
                e.printStackTrace();
                System.exit(1); // Завершаем программу в случае ошибки подключения
            }
        }).start();

        // Запускаем JavaFX GUI
        GUI.main(args);

        // Разрываем соединение после закрытия GUI
        ServerConnection.breakConnection();
    }

}
