package csci4311.chat;

import java.util.LinkedList;
import java.util.List;

public class UserSession {

	 public static List<String> loggedInUserList;
	 public static int intValue =5;
//	 private static String packetStr; 
	 
	 
	public UserSession(){
		
	}
	public static int getIntValue() {
		return intValue;
	}
	public static void setIntValue(int intValue) {
		UserSession.intValue = intValue;
	}
	public UserSession(String packetStr){
		
        if (loggedInUserList == null) {
            loggedInUserList = new LinkedList<>();
        }
        String[] firstUserMessage = packetStr.split("=");

        if (firstUserMessage.length == 2) {
            String userName = firstUserMessage[1];
            loggedInUserList.add(userName);
//            System.out.println("loggedInUserList.length = " + loggedInUserList.size());
        }
        setLoggedInUserList(loggedInUserList); 
//        setIntValue(10);
	}
	public static List<String> getLoggedInUserList() {
		return loggedInUserList;
	}
	public static void setLoggedInUserList(List<String> loggedInUserList) {
		UserSession.loggedInUserList = loggedInUserList;
	}
	
	

}
