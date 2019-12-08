package Lesson_7.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class MainServ {
    private HashMap<String, ClientHandler> clients;

    public MainServ() {
        clients = new HashMap<>();
        ServerSocket server = null;
        Socket socket = null;
        try {
            AuthService.connect();
            server = new ServerSocket(8189);
            System.out.println("Сервер запущен!");

            while (true) {
                socket = server.accept();
                System.out.println("Клиент подключился!");
                new ClientHandler(this, socket);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                server.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            AuthService.disconnect();
        }
    }

    public void broadcastMsg(String msg) {
        for (Map.Entry<String, ClientHandler> o: clients.entrySet()) {
            o.getValue().sendMsg(msg);
        }
    }

    public void sendPrivateMsg(String _msgRecipient,String _msgSender, String _msg){

        clients.get(_msgRecipient).sendMsg(_msgSender + " whisper: " + _msg);
    }

    public void subscribe(ClientHandler client) {

        clients.put(client.nick, client);
    }

    public void unsubscribe(ClientHandler client) {
        clients.remove(client.nick);
    }

    public HashMap getClienList(){
        return clients;
    }
}
