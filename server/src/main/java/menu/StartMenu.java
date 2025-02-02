package menu;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import controllers.AuthenticationController;
import request.commands.StartMenuCommands;
import request.tdo.AccountDTO;

public class StartMenu extends Menu {

    private AuthenticationController authenticationController;

    public StartMenu(InputStream inputStream, OutputStream outputStream) {
        super(inputStream, outputStream);
        authenticationController = new AuthenticationController(commandController);
    }

    @Override
    public void start() {
        boolean toExit = false;
        while (!toExit) {
            StartMenuCommands command;
            try {
                command = commandController.getCommand(StartMenuCommands.class);
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Неверная команда. Ожидалась команда " + StartMenuCommands.class.getName());
                break;
            }
            switch (command) {
                case AUTHORISATION -> {
                    try {
                        AccountDTO accountDTO = authenticationController.login();
                        if (accountDTO != null) {
                            switch (accountDTO.getAccessLevel()) {
                                case 1 -> {
                                    UserMenu userMenu = new UserMenu(commandController, accountDTO, authenticationController.getUserInfo(accountDTO));
                                    userMenu.start();
                                }
                                case 2 -> {
                                    ManagerMenu managerMenu = new ManagerMenu(commandController, accountDTO, authenticationController.getUserInfo(accountDTO));
                                    managerMenu.start();
                                }
                                case 3 -> {
                                    AdminMenu adminMenu = new AdminMenu(commandController, accountDTO, authenticationController.getUserInfo(accountDTO));
                                    adminMenu.start();
                                }
                                case 0 -> {
                                    System.out.println("Ваш аккаунт заблокирован. Пожалуйста, свяжитесь с поддержкой.");
                                }
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                case USER_REGISTRATION -> {
                    try {
                        authenticationController.register();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                case EXIT -> toExit = true;
                default -> System.out.println("Такого нема");
            }
        }
    }
}
