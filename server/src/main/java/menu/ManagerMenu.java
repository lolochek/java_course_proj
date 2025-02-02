package menu;

import controllers.*;
import request.commands.AdminCommands;
import request.commands.ManagerCommands;
import request.commands.StartMenuCommands;
import request.controller.BaseRequestController;
import request.tdo.AccountDTO;
import request.tdo.UserInfoDTO;
import request.tdo.constructors.AccountTDOConstructor;

public class ManagerMenu extends Menu {

    private AccountDTO accountDTO;
    private UserInfoDTO userInfoDTO;

    private UserController userController;
    private AllUsersController allUsersController;
    private AuthenticationController authenticationController;
    private ProductController productController;
    private UserOrderController userOrderController;

    private static AccountTDOConstructor accountTDOConstructor = new AccountTDOConstructor();

    public ManagerMenu(BaseRequestController contr, AccountDTO accountDTO, UserInfoDTO userInfoDTO) {
        super(contr);
        this.accountDTO = accountDTO;
        this.userInfoDTO = userInfoDTO;
        this.userController = new UserController(contr);
        this.allUsersController = new AllUsersController(contr);
        this.authenticationController = new AuthenticationController(contr);
        this.productController = new ProductController(contr);
        this.userOrderController = new UserOrderController(contr);
    }

    @Override
    public void start() {
        System.out.println("Добро пожаловать, менеджер: " + accountDTO.getUsername() + "!");
        // Логика для меню менеджера

        boolean toExit = false;
        while (!toExit) {
            AdminCommands command;
            try {
                command = commandController.getCommand(AdminCommands.class);
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Неверная команда. Ожидалась команда " + StartMenuCommands.class.getName());
                break;
            }
            switch (command) {
                case GET_USER_INFO:
                    userController.sendUserInfo(accountDTO, userInfoDTO);
                    break;
                case CHANGE_PERSONAL_DATA:
                    userController.changePersonalData(accountTDOConstructor.dtoToAccount(accountDTO),accountTDOConstructor.dtoToUserInfo(userInfoDTO, accountTDOConstructor.dtoToAccount(accountDTO)));
                    break;

                case GET_PRODUCT_LIST:
                    productController.sendProductList();
                    break;
                case DELETE_PRODUCT:
                    productController.del_product();
                    break;
                case ADD_PRODUCT:
                    productController.add_product();
                    break;
                case GET_CATEGORY_LIST:
                    productController.sendCategoryList();
                    break;
                case UPDATE_PRODUCT:
                    productController.update_product();
                    break;

                case ADD_CATEGORY:
                    productController.add_category();
                    break;
                case DELETE_CATEGORY:
                    productController.delete_category();
                    break;

                case GET_ORDER_LIST:
                    userOrderController.sendOrders();
                    break;
                case GET_ORDER_ELEMENTS:
                    userOrderController.sendOrderElements();
                    break;
                case CHANGE_ORDER_STATUS:
                    userOrderController.changeOrderStatus();
                    break;

                case LOGOUT:
                    toExit = true;
                    break;
                default:
                    System.out.println("Неизвестная команда: " + command);
            }
        }
    }
}
