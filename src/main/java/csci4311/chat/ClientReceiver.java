package csci4311.chat;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by nazar on 11/11/15.
 */
public class ClientReceiver extends Thread {

    private Socket socket;
    InputStream istream;
    BufferedReader receiveRead;
    String receiveMessage;
    private String clientName;
    private List<String> userList;


    public ClientReceiver(Socket socket, String clientName) {

        this.socket = socket;
        this.clientName = clientName;
    }


    @Override
    public void run() {

        try

        {
            istream = socket.getInputStream();
            receiveRead = new BufferedReader(new InputStreamReader(istream));

            while (true) {

                if ((receiveMessage = receiveRead.readLine()) != null) //receive from server
                {
                    String[] sendCommandMessage = receiveMessage.split(" ", 2);


                    if (sendCommandMessage[0].equals("send")) {

                        String mainMessageWithSendCommand = receiveMessage.replaceAll("(@|#).*?\\S*", "");
                        String[] mainMessageWithoutReceiverName = mainMessageWithSendCommand.split(" ",2);
                        userList = new LinkedList<>();

                        int lastWordPosition = mainMessageWithoutReceiverName[1].lastIndexOf(" ");
                        String originalMessage = mainMessageWithoutReceiverName[1].substring(0, lastWordPosition).trim();
                        String senderName = mainMessageWithoutReceiverName[1].substring((lastWordPosition)).trim();

                        String userRegEx = "@\\s*(\\w+)";

                        Pattern p = Pattern.compile(userRegEx);
                        Matcher m = p.matcher(receiveMessage);

                        while (m.find()) {

                            userList.add(m.group(1));
                        }
                        if(userList.contains(clientName)){
                            System.out.println("["+senderName+"] "+originalMessage.trim());
                        }

                    } else {
                        System.out.println(receiveMessage); // displaying at DOS prompt
                    }

                }
            }


        } catch (Exception ex) {

            ex.printStackTrace();
        }
    }
}
