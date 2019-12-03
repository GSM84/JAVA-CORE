package Lesson_6.ConsoleServer;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class WriteMsg extends Thread{
    private ConsoleServer    server;
    private DataOutputStream out;
    private DataInputStream  in;

    public WriteMsg(DataOutputStream _out, DataInputStream _in, ConsoleServer _server){
        this.out    = _out;
        this.in     = _in;
        this.server = _server;
    }
    @Override
    public void run() {
        System.out.println("Запуск процесса записи.");
        while (true) {
            String msg;
            try {
                msg = in.readLine();
                if (msg.toLowerCase().equals("/end")) {
                    break;
                } else if(msg.toLowerCase().equals("/send")){
                    out.flush();
                    server.releaseLock();
                }
                else {
                    server.setLock();
                    out.writeUTF(msg + "\n");
                }
            } catch (IOException e) {
                System.out.println("Процесс записи принудительно остановлен.");
                break;
            }
        }
        System.out.println("Остановка процесса записи.");
    }
}
