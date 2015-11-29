package csci4311.chat;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

public class UserSession implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public UserSession() {

	}

	public UserSession(String packetStr) {

		try {

			String[] firstUserMessage = packetStr.split("=");
			String userName = null;

			if (firstUserMessage.length == 2) {
				userName = firstUserMessage[1];
			}
			File currentUserFile = new File("currentUser.txt");

			PacketReader.writeIntoFile(userName, currentUserFile, true);

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

}
