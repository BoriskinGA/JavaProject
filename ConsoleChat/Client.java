package ru.geekbrains.HomeTask;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class Client<socket> {

    private static final String SERVER_ADRESS = "localhost";
    private static final int SERVER_PORT = 8189;

    public static void main(String[] args)
    {
        Socket socket = null;
        Scanner scanner = new Scanner(System.in);

        try
            {
            socket = new Socket(SERVER_ADRESS, SERVER_PORT);
            System.out.println("Add to Server" + socket.getRemoteSocketAddress());
                DataInputStream in = new DataInputStream(socket.getInputStream());
                DataOutputStream out = new DataOutputStream(socket.getOutputStream());

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
                    if(str.equals("/end"))
                        {
                            System.out.println("Server is lost");
                            out.writeUTF("/end");
                            break;
                        }
                    else
                        {
                            System.out.println("Server :" + str);
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
                    socket.close();
                }
                catch (IOException | NullPointerException e)
                {
                    e.printStackTrace();
                }
            }



    }
}
