package csci4311.chat;

import java.net.Socket;

/**
 * Created by nazar on 11/9/15.
 */
public interface UserAgent {

    public void packetReceiver(Socket socket);
    public void packetSender(Socket socket,String clientName);

}
