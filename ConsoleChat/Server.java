package ru.geekbrains.HomeTask;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class Server
{
    public static void main(String[] args)
    {
        Socket clientSocket = null;
        Scanner scanner = new Scanner(System.in);

        try (ServerSocket serverSocket = new ServerSocket(8189);)
        {
            System.out.println("Server start");

            clientSocket = serverSocket.accept();
            System.out.println("Add client" + clientSocket.getRemoteSocketAddress());
            DataInputStream in = new DataInputStream(clientSocket.getInputStream());
            DataOutputStream out = new DataOutputStream(clientSocket.getOutputStream());

            Thread threadReader = new Thread(()->
            {
                try
                {
                    while (true)
                    {
                        out.writeUTF(scanner.nextLine());
                    }
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            });

            threadReader.setDaemon(true);
            threadReader.start();

            while (true)
            {
                String str = in.readUTF();
                if (str.equals("/end"))
                {
                    System.out.println("Client offlain");
                    out.writeUTF("/end");
                    break;
                }
                else
                    {
                        System.out.println("Client" + str);
                    }
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        finally
        {
            try
            {
                clientSocket.close();
            }
            catch (IOException | NullPointerException e)
            {
                e.printStackTrace();
            }
        }
    }
}
