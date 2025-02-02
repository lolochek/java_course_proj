package server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

public class ServerConnection {
    private static Socket clientSocket;
    private static boolean connected = false;
    public static void Connect(int port) throws IOException, UnknownHostException
    {
        if(clientSocket != null)
            return;
        clientSocket = new Socket("127.0.0.1", port);
        connected = true;
    }

    public static InputStream getInputStream()
    {
        if(!connected)
            return null;
        InputStream inputStream = null;
        try {
            inputStream = clientSocket.getInputStream();
        } catch (IOException e) {
            e.getStackTrace();
            return null;
        }
        return inputStream;
    }

    public static OutputStream getOutputStream()
    {
        if(!connected)
            return null;
        OutputStream outputStream = null;
        try {
            outputStream = clientSocket.getOutputStream();
        } catch (IOException e) {
            e.getStackTrace();
            return null;
        }
        return outputStream;
    }

    public static void breakConnection()
    {
        if (!connected)
            return;
        try 
        {
            clientSocket.getInputStream().close();
            clientSocket.getInputStream().close();
            clientSocket.close();
            connected = false;
        }
        catch (Exception e) {
            e.printStackTrace();
            return;
        }
    }
       
}
