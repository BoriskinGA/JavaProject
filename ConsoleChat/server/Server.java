package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Vector;

public class Server{
    SimpleDateFormat formater = new SimpleDateFormat("HH:mm:ss");
    List<ClientHandler> clients;
    private AuthService authService;

    private static int PORT = 8189;
    ServerSocket server = null;
    Socket socket = null;

    public Server() {
        clients = new Vector<>();
        authService = new SimpleAuthService();

        try {
            server = new ServerSocket(PORT);
            System.out.println("Сервер запущен");

            while (true) {
                socket = server.accept();
                System.out.println("Клиент подключился");

//                clients.add(new ClientHandler(this, socket));
//                subscribe(new ClientHandler(this, socket));
                new ClientHandler(this, socket);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                server.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void broadcastMsg(ClientHandler sender, String msg) {
        String message = String.format("%s : %s", formater.format(new Date()), sender.getNickName(), msg);
        for (ClientHandler client : clients) {
            client.sendMsg(message);
        }
    }


    public void privateMsg(ClientHandler sender,String receiver, String msg) {
        String message = String.format("%s [%s] private [%s] : %s", formater.format(new Date()),sender.getNickName(), msg);
        for (ClientHandler c : clients)
        {
            if (c.getNickName().equals(receiver))
            {
                c.sendMsg(message);
                if (!c.equals(sender))
                {
                    sender.sendMsg(message);
                }
                return;
            }

        }
        sender.sendMsg("not found user :" + receiver);
    }

    public void subscribe(ClientHandler clientHandler) {
        clients.add(clientHandler);
    }

    public void unsubscribe(ClientHandler clientHandler) {
        clients.remove(clientHandler);
    }

    public AuthService getAuthService(){
        return authService;
    }
}
