package request.controller;

import java.io.IOException;

import server.ServerConnection;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import request.Request;

public class BaseRequestController {
    private static ObjectInputStream objectInputStream = null;
    private static ObjectOutputStream objectOutputStream = null;

    public static void setStreams()
    {
        if(objectInputStream == null && objectOutputStream == null)
        {
            try {
                objectOutputStream = new ObjectOutputStream(ServerConnection.getOutputStream());
                objectInputStream = new ObjectInputStream(ServerConnection.getInputStream());
            } catch (IOException e) {
                e.getStackTrace();
                return;
            }
        } 
    }

    public static ObjectInputStream getObjectInputStream() {
        return objectInputStream;
    }

    public static ObjectOutputStream getObjectOutputStream() {
        return objectOutputStream;
    }
    
    @SuppressWarnings("unchecked")
    public static <T> T getCommand(Class<T> commandType) throws IOException, ClassNotFoundException
    {
        Request<T> request = null;
        try {
            request = (Request<T>)objectInputStream.readObject();
        } catch (ClassNotFoundException | IOException e) {
           throw new IOException();
        }
        return request.getCommand();
    }

    public static <T> void sendRequest(Class<T> commandType, T command)
    {
        var response = new Request<>(commandType);
        response.setCommand(command);
        try {
            objectOutputStream.writeObject(response);
        } catch (IOException e) {
           e.printStackTrace();
        }
    }
}
