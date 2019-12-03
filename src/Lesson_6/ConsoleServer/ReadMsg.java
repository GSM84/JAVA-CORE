package Lesson_6.ConsoleServer;

import java.io.DataInputStream;
import java.io.IOException;

public class ReadMsg extends Thread{
    private DataInputStream in;
    private ConsoleServer   server;
    public ReadMsg(DataInputStream _in, ConsoleServer _server){
        this.in     = _in;
        this.server = _server;
    }
    @Override
    public void run() {
        System.out.println("запуск процесса чтения.");
        String msg;
        try {
            while (true) {
                try{
                    msg = in.readUTF();
                    if (msg.toLowerCase().equals("/end")) {
                        break;
                    }
                    while(server.requestLock()){
                    }
                    System.out.println(msg);
                } catch (java.net.SocketException e){
                    System.out.println("Клиент отключился.");
                    break;
                } catch(java.io.EOFException e){
                    System.out.println("Клиент отключился.");
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("Остановка процесса чтения.");
    }
}
