package Lesson_6.ConsoleServer;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ConsoleServer {
    public static final int  PORT = 8189;
    private Socket           socket;
    private ServerSocket     server;
    private volatile boolean isLocked;
    private DataInputStream in;
    private DataOutputStream out;
    private DataInputStream  consoleIn;

    public ConsoleServer(){
        try{
            server    = null;
            socket    = null;
            isLocked = false;
            try{
                // запуск сервера
                server = new ServerSocket(PORT);
                System.out.println("Сервер запущен");
                // подключение клиента
                socket = server.accept();
                System.out.println("Клиент подключился");
                // открытие потоков ввода вывода в сокет
                in = new DataInputStream(socket.getInputStream());
                out = new DataOutputStream(socket.getOutputStream());
                // открытие потока чтения из консоли
                consoleIn = new DataInputStream(System.in);
                String msg;

                // создание нитей обмена сообщениями
                ReadMsg  rd = new ReadMsg(in, this);
                WriteMsg wr = new WriteMsg(out, consoleIn, this);

                rd.start();
                wr.start();

                while(true){
                    if(!wr.isAlive() || !rd.isAlive())
                        break;
                }
                wr.interrupt();
                rd.interrupt();

            }finally{
                out.close();
                consoleIn.close();
                socket.close();
                server.close();
            }

        }catch(IOException e){
            e.printStackTrace();
        }
    }

    public synchronized void setLock(){
        if(!isLocked)
            isLocked = true;
    }

    public synchronized void releaseLock(){
        this.isLocked = false;
    }

    public synchronized boolean requestLock(){
        return this.isLocked;
    }
}
