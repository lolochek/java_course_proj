package menu;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import request.controller.BaseRequestController;

abstract class Menu {
    protected BaseRequestController commandController;
    protected Menu(InputStream clientInputStream, OutputStream clientOutputStream) {
        commandController = new BaseRequestController(clientInputStream, clientOutputStream);
    }
    protected Menu(BaseRequestController contr)
    {
        commandController = contr;
    }
    public abstract void start() throws IOException;
}
