package controllers;

import db.dao.AccountDAO;
import db.dao.CartElementDAO;
import db.dao.GenericDao;
import db.dao.ProductDAO;
import db.entity.Account;
import db.entity.CartElement;
import db.entity.Category;
import db.entity.Product;
import request.commands.ConfirmCommands;
import request.controller.BaseRequestController;
import request.tdo.AccountDTO;
import request.tdo.CartElementDTO;
import request.tdo.CategoryDTO;
import request.tdo.ProductDTO;
import request.tdo.constructors.CategoryTDOConstructor;
import request.tdo.constructors.OrderDTOConstructor;

import java.util.ArrayList;

public class UserCartController extends BaseRequestController {

    public UserCartController(BaseRequestController contr) {
        super(contr);
    }

    public void sendUserCart(AccountDTO account) {
        sendResponse(ConfirmCommands.class, ConfirmCommands.SUCCESSFULLY);
        try {
            var cartElementDAO = new CartElementDAO();
            var productList = cartElementDAO.getByAccountId(account.getAccountId());
            var orderTDOConstructor = new OrderDTOConstructor();

            var listTDO = new ArrayList<CartElementDTO>();
            for (var product : productList) {
                listTDO.add(orderTDOConstructor.cartElementToDTO(product));
            }
            System.out.println(listTDO);
            outputObjectStream.writeObject(listTDO);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addToCart() {
        sendResponse(ConfirmCommands.class, ConfirmCommands.SUCCESSFULLY);
        try {
            CartElementDTO cartElementDTO = (CartElementDTO) inputObjectStream.readObject();
            CartElementDAO cartElementDAO = new CartElementDAO();

            // Получаем аккаунт и продукт из базы данных
            AccountDAO accountDAO = new AccountDAO();
            ProductDAO productDAO = new ProductDAO();

            Account account = accountDAO.getById(cartElementDTO.getAccountId());
            Product product = productDAO.getById(cartElementDTO.getProductId());

            if (account == null || product == null) {
                sendResponse(ConfirmCommands.class, ConfirmCommands.FAILED);
                return;
            }

            CartElement cartElement = new CartElement();
            cartElement.setAccount(account);
            cartElement.setProduct(product);
            cartElement.setQuantity(cartElementDTO.getQuantity());
            cartElement.setSubtotalCost(cartElementDTO.getSubtotalCost());

            boolean isSaved = cartElementDAO.save(cartElement);
            if (isSaved) {
                sendResponse(ConfirmCommands.class, ConfirmCommands.SUCCESSFULLY);
            } else {
                sendResponse(ConfirmCommands.class, ConfirmCommands.FAILED);
            }
        } catch (Exception e) {
            e.printStackTrace();
            sendResponse(ConfirmCommands.class, ConfirmCommands.SOMETHINGWRONG);
        }
    }

    public void updateCartElement() {
        sendResponse(ConfirmCommands.class, ConfirmCommands.SUCCESSFULLY);
        try {
            CartElementDTO cartElementDTO = (CartElementDTO) inputObjectStream.readObject();
            CartElementDAO cartElementDAO = new CartElementDAO();

            // Получаем элемент корзины по ID
            CartElement cartElement = cartElementDAO.getById(cartElementDTO.getCartElementId());
            if (cartElement != null) {
                cartElement.setQuantity(cartElementDTO.getQuantity());
                cartElement.setSubtotalCost(cartElementDTO.getSubtotalCost());
                boolean isUpdated = cartElementDAO.update(cartElement);
                if (isUpdated) {
                    sendResponse(ConfirmCommands.class, ConfirmCommands.SUCCESSFULLY);
                } else {
                    sendResponse(ConfirmCommands.class, ConfirmCommands.FAILED);
                }
            } else {
                sendResponse(ConfirmCommands.class, ConfirmCommands.FAILED);
            }
        } catch (Exception e) {
            e.printStackTrace();
            sendResponse(ConfirmCommands.class, ConfirmCommands.SOMETHINGWRONG);
        }
    }

    public void removeCartElement() {
        sendResponse(ConfirmCommands.class, ConfirmCommands.SUCCESSFULLY);
        try {
            CartElementDTO cartElementDTO = (CartElementDTO) inputObjectStream.readObject();
            CartElementDAO cartElementDAO = new CartElementDAO();
            System.out.println(cartElementDTO);
            System.out.println("removeCartElement");

            CartElement cartElement = cartElementDAO.getById(cartElementDTO.getCartElementId());

            // Удаляем элемент корзины
            boolean isRemoved = cartElementDAO.remove(cartElement);
            if (isRemoved) {
                sendResponse(ConfirmCommands.class, ConfirmCommands.SUCCESSFULLY);
            } else {
                sendResponse(ConfirmCommands.class, ConfirmCommands.FAILED);
            }
        } catch (Exception e) {
            e.printStackTrace();
            sendResponse(ConfirmCommands.class, ConfirmCommands.SOMETHINGWRONG);
        }
    }

    public void clearCart() {
        sendResponse(ConfirmCommands.class, ConfirmCommands.SUCCESSFULLY);
        try {
            int accountId = (int) inputObjectStream.readObject(); // Получаем ID аккаунта
            CartElementDAO cartElementDAO = new CartElementDAO();
            System.out.println("removeCart");

            // Очищаем корзину для данного аккаунта
            boolean isCleared = cartElementDAO.clearByAccountId(accountId);
            if (isCleared) {
                sendResponse(ConfirmCommands.class, ConfirmCommands.SUCCESSFULLY);
                System.out.println("removeCartSuck");
            } else {
                sendResponse(ConfirmCommands.class, ConfirmCommands.FAILED);
            }
        } catch (Exception e) {
            e.printStackTrace();
            sendResponse(ConfirmCommands.class, ConfirmCommands.SOMETHINGWRONG);
        }
    }


}
