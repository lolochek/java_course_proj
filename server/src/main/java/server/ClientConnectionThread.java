package server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import menu.StartMenu;

public class ClientConnectionThread extends Thread{
    private Socket clientSocket;

    public ClientConnectionThread(Socket clientSocket)
    {
        this.clientSocket = clientSocket;
    }

    public void run()
    {
        InputStream inputClientStream;
        OutputStream outputClientStream;
        try {
            inputClientStream = clientSocket.getInputStream();
            outputClientStream = clientSocket.getOutputStream();
        }catch(IOException exception)
        {
            exception.getStackTrace();
            return;
        }
        StartMenu clientStartMenu = new StartMenu(inputClientStream, outputClientStream);
        clientStartMenu.start();
        try {
            inputClientStream.close();
            outputClientStream.close();
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

    }
}
