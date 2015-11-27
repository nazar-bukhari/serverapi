package csci4311.chat;

public class MsgpServer extends Thread {

    public void run() {

        new UserCommand().run();
    }

    public static void main(String[] args) {

    	int port = Integer.parseInt(args[0]);
    	
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
