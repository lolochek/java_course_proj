package menu;

import controllers.ProductController;
import controllers.UserCartController;
import controllers.UserController;
import controllers.UserOrderController;
import request.commands.StartMenuCommands;
import request.commands.UserCommands;
import request.controller.BaseRequestController;
import request.tdo.AccountDTO;
import request.tdo.OrderElementDTO;
import request.tdo.UserInfoDTO;
import request.tdo.constructors.AccountTDOConstructor;

public class UserMenu extends Menu {

    private AccountDTO accountDTO;
    private UserInfoDTO userInfoDTO;

    private UserController userController;
    private ProductController productController;
    private UserCartController userCartController;
    private UserOrderController userOrderController;

    public UserMenu(BaseRequestController contr, AccountDTO accountDTO, UserInfoDTO userInfoDTO) {
        super(contr);
        this.accountDTO = accountDTO;
        this.userInfoDTO = userInfoDTO;
        this.userController = new UserController(contr);
        this.productController = new ProductController(contr);
        this.userCartController = new UserCartController(contr);
        this.userOrderController = new UserOrderController(contr);
    }

    private static AccountTDOConstructor accountTDOConstructor = new AccountTDOConstructor();

    @Override
    public void start() {
        System.out.println("Добро пожаловать, юзер: " + accountDTO.getUsername() + "!");
        boolean toExit = false;
        while (!toExit) {
            UserCommands command;
            try {
                command = commandController.getCommand(UserCommands.class);
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
                case GET_CATEGORY_LIST:
                    productController.sendCategoryList();
                    break;

                case GET_CART:
                    userCartController.sendUserCart(accountDTO);
                    break;
                case ADD_CART_ELEMENT:
                    userCartController.addToCart();
                    break;
                case REMOVE_CART_ELEMENT:
                    userCartController.removeCartElement();
                    break;
                case CLEAR_CART:
                    userCartController.clearCart();
                    break;
                case CART_ELEMENT_CHANGE_AMOUNT:
                    userCartController.updateCartElement();
                    break;

                case CREATE_ORDER:
                    userOrderController.addOrder(accountDTO);
                    break;
                case GET_USER_ORDER_LIST:
                    userOrderController.sendUserOrders(accountDTO);
                    break;
                case GET_ORDER_ELEMENTS:
                    userOrderController.sendOrderElements();
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
