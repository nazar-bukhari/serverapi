package csci4311.chat;

import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by nazar on 11/10/15.
 */
public class PacketReceiver extends Thread{

    int port;
    public static volatile int readerCount;
    List<Socket> sockets ;

    public PacketReceiver(int port){

        this.port = port;
        readerCount = 0;

        if(sockets == null){
           sockets = new CopyOnWriteArrayList<Socket>();
        }
    }

    @Override
    public void run() {

        ServerSocket sSock = null;

        try {

            sSock = new ServerSocket(port);

            while(true){

                try{

                    PacketReader pr = new PacketReader(sockets);
                    pr.socket  = sSock.accept(); 
                    pr.start();
                }
                catch(Exception ex){
                    ex.printStackTrace();
                    Thread.sleep(5000);
                }
            }
        }
        catch(Exception ex){
            ex.printStackTrace();
        }

    }
}
