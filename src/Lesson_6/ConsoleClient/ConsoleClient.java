package Lesson_6.ConsoleClient;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class ConsoleClient {
    private Socket              socket;
    private static final String ADDRESS = "localhost";
    private static final int    PORT    = 8189;
    private DataInputStream     in;
    private DataOutputStream    out;
    private BufferedReader      consoleReader;
    private ConsoleClient       client;
    private volatile boolean    isLocked;


    public ConsoleClient(){
        this.socket        = null;
        this.in            = null;
        this.out           = null;
        this.consoleReader = null;
        this.client        = this;// не нравится этот момент, но нет идей как сделать по другому(
        this.isLocked      = false;
        try{
            try{
                socket        = new Socket(ADDRESS, PORT);
                in            = new DataInputStream(socket.getInputStream());
                out           = new DataOutputStream(socket.getOutputStream());
                consoleReader = new BufferedReader(new InputStreamReader(System.in));

                Thread wr = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        System.out.println("Клиентский процесс записи запущен.");
                        String msg;
                        while(true){
                            try {
                                msg = consoleReader.readLine();
                                if (msg.toLowerCase().equals("/end")) {
                                    out.writeUTF("stop" + "\n");
                                    break;
                                } else if(msg.toLowerCase().equals("/send")){
                                    out.flush();
                                    client.isLocked = false;
                                }
                                else {
                                    client.isLocked = true;
                                    out.writeUTF(msg + "\n");
                                }
                            } catch (IOException ex) {
                                ex.printStackTrace();
                            }
                        }
                        System.out.println("Клиентский процесс записи отсановлен.");
                    }
                });

                Thread rd = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        System.out.println("Клиентский процесс чтения запущен.");
                        String msg;
                        while(true){
                            try {
                                msg = in.readLine();
                                if (msg.toLowerCase().equals("/end")) {
                                    break;
                                }
                                while(client.isLocked){
                                }
                                System.out.println(msg);
                            } catch(java.net.SocketException e){
                                break;
                            } catch (IOException ex) {
                                ex.printStackTrace();
                                break;
                            }
                        }
                        System.out.println("Клиентский процесс чтения остановлен.");
                    }
                });

                wr.start();
                rd.start();
                try {
                    wr.join();
                    if(!wr.isAlive())
                        rd.interrupt();

                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            }finally{
                in.close();
                out.close();
                consoleReader.close();
                socket.close();
            }
        } catch(IOException e){
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new ConsoleClient();
    }
}
