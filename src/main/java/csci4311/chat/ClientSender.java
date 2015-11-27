package csci4311.chat;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Created by nazar on 11/11/15.
 */
public class ClientSender extends Thread {

    private Socket socket;
    private String userName;
    private boolean isValid;

    public ClientSender(Socket socket,String userName) {

        this.socket = socket;
        this.userName = userName;
    }

    public void run(){

        try{

            BufferedReader keyRead = new BufferedReader(new InputStreamReader(System.in));
            OutputStream ostream = socket.getOutputStream();
            PrintWriter pwrite = new PrintWriter(ostream, true);
            pwrite.println("username="+userName);
            pwrite.flush();

            String  sendMessage;
            while(true)
            {
                sendMessage = keyRead.readLine();  
                TextMsgpClient messageClient = new TextMsgpClient();
                isValid = messageClient.isCommandValid(sendMessage);
                if(isValid) {
                    pwrite.println(sendMessage + " " + userName); // sending to server
                    pwrite.flush();
                }
                else{
//                    System.out.println("Not valid Command");
                }

            }

        }
        catch(Exception ex){

        }

    }
}
