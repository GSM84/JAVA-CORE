package Lesson_7.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ClientHandler {
    private Socket socket;
    DataInputStream in;
    DataOutputStream out;
    MainServ serv;
    String nick;
    Pattern pattern = Pattern.compile("^/w\\s[A-Za-z0-9_]+\\s");
    Matcher matcher;

    public ClientHandler(MainServ serv, Socket socket){
        try {
            this.socket = socket;
            this.serv = serv;
            this.in = new DataInputStream(socket.getInputStream());
            this.out = new DataOutputStream(socket.getOutputStream());

            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        while (true) {
                            String msg = in.readUTF();
                            if (msg.startsWith("/auth")) {
                                String[] tokens = msg.split(" ");
                                String newNick = AuthService.getNickByLoginAndPass(tokens[1], tokens[2]);
                                if (newNick != null && !serv.getClienList().containsKey(newNick)) {
                                    sendMsg("/authok");
                                    nick = newNick;
                                    serv.subscribe(ClientHandler.this);
                                    break;
                                } else if(newNick != null && serv.getClienList().containsKey(newNick)){
                                    sendMsg("Пользователь с таким\n ником уже авторизовался!");
                                }
                                else {
                                    sendMsg("Неверный логин/пароль");
                                }
                            }
                        }

                        while (true) {
                            String msg = in.readUTF();
                            if (msg.equals("/end")) {
                                out.writeUTF("/serverClosed");
                                break;
                            }else if(msg.matches("^/w\\s[A-Za-z0-9_]+\\s.+")){
                                matcher = pattern.matcher(msg);
                                String msgRecipient = null;
                                String msgText      = null;

                                while(matcher.find()){
                                    msgRecipient = msg.substring(3, matcher.end()-1);
                                    msgText      = msg.substring(matcher.end(), msg.length());
                                }
                                if (serv.getClienList().containsKey(msgRecipient)){
                                    serv.sendPrivateMsg(msgRecipient, nick + "/w " + msgText);
                                }else{
                                    out.writeUTF("Пользователя с таким ником\n не существует.");
                                }
                            }else {
                                serv.broadcastMsg(nick + " " + msg);
                            }
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
                        serv.unsubscribe(ClientHandler.this);
                    }
                }
            }).start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendMsg(String msg) {
        try {
            out.writeUTF(msg);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
