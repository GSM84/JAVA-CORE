package Lesson_8.server;

import javafx.util.Pair;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.HashSet;
import java.util.Set;

public class ClientHandler {
    private Server server;
    private Socket socket;
    private DataOutputStream out;
    private DataInputStream in;
    private String nick;
    private int    id;

    Set<String> blackList;

    public String getNick() {
        return nick;
    }
    public int getId(){return  id;}

    public ClientHandler(Server server, Socket socket) {
        try {
            this.socket = socket;
            this.server = server;
            this.in = new DataInputStream(socket.getInputStream());
            this.out = new DataOutputStream(socket.getOutputStream());
            this.blackList = new HashSet<>();
            new Thread(() -> {
                try {
                    while (true) {
                        String str = in.readUTF();
                        if (str.startsWith("/auth")) { // /auth login72 pass72
                            String[] tokens = str.split(" ");
                            Pair<Integer, String> pair = AuthService.getNickByLoginAndPass(tokens[1], tokens[2]);
                            String newNick = pair.getValue();
                            if (newNick != null) {
                                if (!server.isNickBusy(newNick)) {
                                    sendMsg("/authok");
                                    nick = newNick;
                                    id   = pair.getKey();
                                    blackList = AuthService.loadBlacklist(id);
                                    server.subscribe(this);
                                    break;
                                } else {
                                    sendMsg("Учетная запись уже используется");
                                }
                            } else {
                                sendMsg("Неверный логин/пароль");
                            }
                        }
                    }
                    while (true) {
                        String str = in.readUTF();
                        if (str.startsWith("/")) {
                            if (str.equals("/end")) {
                                out.writeUTF("/serverclosed");
                                break;
                            }
                            else if (str.startsWith("/w ")) { // /w nick3 lsdfhldf sdkfjhsdf wkerhwr
                                String[] tokens = str.split(" ", 3);
                                String m = str.substring(tokens[1].length() + 4);
                                server.sendPersonalMsg(this, tokens[1], tokens[2]);
                            }
                            else if (str.startsWith("/blacklist ")) { // /blacklist nick3
                                String[] tokens = str.split(" ");
                                if(this.nick.equals(tokens[1]))
                                    sendMsg("Нельзя заблокировать самого себя.");
                                else {
                                    addToBlacklist(this.id, tokens[1]);
                                    sendMsg("Вы добавили пользователя " + tokens[1] + " в черный список");
                                }
                            }else if(str.startsWith("/unblock ")){
                                String[] tokens = str.split(" ");
                                if(checkBlackList(tokens[1])){
                                    unblockUser(this.id, tokens[1]);
                                    sendMsg("Пользователь "+ tokens[1] + " был удален из черного списка.");
                                }else
                                    sendMsg("Пользователь "+ tokens[1] + " отсутствует в черном списке.");

                            }
                        } else {
                            server.broadcastMsg(this, nick + ": " + str);
                        }
                        System.out.println("Client: " + str);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        in.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    try {
                        out.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    try {
                        socket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    server.unsubscribe(this);
                }
            }).start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean checkBlackList(String nick) {
        return blackList.contains(nick);
    }

    public void sendMsg(String msg) {
        try {
            out.writeUTF(msg);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addToBlacklist(int _id, String _blockedUser){
        blackList.add(_blockedUser);
        AuthService.putNickToBlackList(_id, _blockedUser);
        server.broadcastClientsList();
    }

    public void unblockUser(int _id, String _unblockedUser){
        blackList.remove(_unblockedUser);
        AuthService.removeFromBlackList(_id, _unblockedUser);
        server.broadcastClientsList();
    }
}
