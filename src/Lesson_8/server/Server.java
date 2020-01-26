package Lesson_8.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;
import java.util.logging.*;

public class Server {
    private Vector<ClientHandler> clients;
    private static final Logger logger = Logger.getLogger(Lesson_8.server.Server.class.getName());
    private Handler handler;

    public Server() {
        clients = new Vector<>();
        ServerSocket server = null;
        Socket socket = null;

        try {
            handler = new FileHandler("mylog.log", true);
            handler.setFormatter(new SimpleFormatter());
            logger.addHandler(handler);

            AuthService.connect();
            server = new ServerSocket(2020);
            logger.log(Level.INFO, "Сервер запущен. Ожидаем клиентов...");

            while (true) {
                socket = server.accept();
                logger.log(Level.INFO, "Клиент подключился");
                new ClientHandler(this, socket);
            }
        } catch (IOException e) {
            e.printStackTrace();
            logger.log(Level.SEVERE, "Произошла ошибка");
        } finally {
            try {
                socket.close();
                logger.log(Level.INFO, "Сокет закрыт");
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                server.close();
                logger.log(Level.INFO, "Сервер закрыт");
            } catch (IOException e) {
                e.printStackTrace();
            }
            AuthService.disconnect();
        }
    }

    public void sendPersonalMsg(ClientHandler from, String nickTo, String msg) {
        for (ClientHandler o : clients) {
            if (o.getNick().equals(nickTo) && !o.checkBlackList(from.getNick())) {
                o.sendMsg("from " + from.getNick() + ": " + msg);
                from.sendMsg("to " + nickTo + ": " + msg);
                return;
            }
        }
        from.sendMsg("Клиент с ником " + nickTo + " добавил вас в черный список.");
    }

    public void broadcastMsg(ClientHandler from, String msg) {
        for (ClientHandler o : clients) {
            if (!o.checkBlackList(from.getNick())) {
                o.sendMsg(msg);
            }
        }
    }

    public boolean isNickBusy(String nick) {
        for (ClientHandler o : clients) {
            if (o.getNick().equals(nick)) {
                return true;
            }
        }
        return false;
    }

    public void broadcastClientsList() {
        StringBuilder sb = new StringBuilder();
        for (ClientHandler o : clients) {
            sb.append("/clientslist ");
            for (ClientHandler cl : clients) {
                if (cl.getNick().equals(o.getNick()))
                    sb.append(cl.getNick() + "-I ");
                else if (o.checkBlackList(cl.getNick()))
                    sb.append(cl.getNick() + "-Blocked ");
                else
                    sb.append(cl.getNick() + " ");

            }
            String out = sb.toString();
            o.sendMsg(out);
            sb.setLength(0);
        }
    }

    public void subscribe(ClientHandler client) {
        clients.add(client);
        broadcastClientsList();
    }

    public void unsubscribe(ClientHandler client) {
        clients.remove(client);
        broadcastClientsList();
    }

    public static Logger getLogger() {
        return logger;
    }
}
