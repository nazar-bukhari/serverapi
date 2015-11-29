package csci4311.chat;

import java.io.File;

public class MsgpServer extends Thread {

    public void run() {

        new UserCommand().run();
    }

    public static void main(String[] args) {

    	int port = Integer.parseInt(args[0]);
    	
    	//Delete the currently loggedIn users file on server restart
    	File currentUserFile = new File("currentUser.txt");
    	currentUserFile.deleteOnExit();
    	
        MsgpServer m = new MsgpServer();
        m.start();

        try {
            PacketReceiver pr = new PacketReceiver(port);
            pr.start();
        }
        catch(Exception ex){
//            ex.printStackTrace();
        }

    }
}
