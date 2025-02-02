package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Server 
{
    private static ServerSocket serverSocket;
    private static List<ClientConnectionThread> connectedClientList = new ArrayList<>();
    private static void start(int port) throws IOException
    {
        if(serverSocket != null)
            return;
        serverSocket = new ServerSocket(port);
    }
    private static Socket listen() throws IOException
    {
        Socket clientLocalSocket = serverSocket.accept();
        return clientLocalSocket;
    }

    private static void acceptingloop() throws IOException
    {
        while(true)
        {
            Socket clientLocalSocket = listen();
            System.out.println("Client connected!");
            ClientConnectionThread clientThread = new ClientConnectionThread(clientLocalSocket);
            connectedClientList.add(clientThread);
            clientThread.start();
        }
    }
    public static void main( String[] args ) throws IOException
    {   
        Server.start(3333);
        Server.acceptingloop();
    }
}
