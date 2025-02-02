package request.controller;

import request.Request;

import java.io.*;

public class BaseRequestController {

    protected ObjectInputStream inputObjectStream;
    protected ObjectOutputStream outputObjectStream;
    
    public BaseRequestController(InputStream inp, OutputStream outp)
    {
        try {
            inputObjectStream = new ObjectInputStream(inp);
            outputObjectStream = new ObjectOutputStream(outp);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public BaseRequestController(BaseRequestController contr)
    {
        this.inputObjectStream = contr.inputObjectStream;
        this.outputObjectStream = contr.outputObjectStream;
    }

    @SuppressWarnings("unchecked")
    public <T> T getCommand(Class<T> commandType) throws IOException, ClassNotFoundException
    {
        Request<T> request = null;
        try {
            request = (Request<T>)inputObjectStream.readObject();
        } catch (ClassNotFoundException | IOException e) {
           throw new IOException();
        }
        return request.getCommand();
    }

    public <T> void sendResponse(Class<T> commandType, T command)
    {
        var response = new Request<>(commandType);
        response.setCommand(command);
        try {
            outputObjectStream.writeObject(response);
        } catch (IOException e) {
           e.printStackTrace();
        }
    }
}
