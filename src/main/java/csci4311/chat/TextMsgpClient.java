package csci4311.chat;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by nazar on 11/9/15.
 */
public class TextMsgpClient implements  MsgpClient{

    private String clientMessage;
    Pattern p;
    Matcher m;
    String regEx;

    public boolean isCommandValid(String clientMessage){

        this.clientMessage = clientMessage;

        String[] clientRequest = clientMessage.split(" ");
        String baseCommand = clientRequest[0];

        if(baseCommand.equals("join")){
            return isValidJoinProtocol(clientMessage);
        }
        else if(baseCommand.equals("leave")){
            return  isValidLeaveProtocol(clientMessage);
        }
        else if(baseCommand.equals("groups")){
            return isValidGroupsProtocol(clientMessage);
        }
        else if(baseCommand.equals("users")){
            return isValidUsersProtocol(clientMessage);
        }
        else if(baseCommand.equals("history")){
            return isValidHistoryProtocol(clientMessage);
        }else if(baseCommand.equals("send")){
            return isValidSendProtocol(clientMessage);
        }
        return false;
    }


    @Override
    public boolean isValidJoinProtocol(String clientMessage) {

        regEx = "join [A-Za-z0-9]{1,32}";
        return patternMatch(regEx);
    }

    @Override
    public boolean isValidLeaveProtocol(String clientMessage) {

        regEx = "leave [A-Za-z0-9]{1,32}";
        return patternMatch(regEx);
    }

    @Override
    public boolean isValidGroupsProtocol(String clientMessage) {


        regEx = "groups";
        return patternMatch(regEx);
    }

    @Override
    public boolean isValidUsersProtocol(String clientMessage) {

        regEx = "users [A-Za-z0-9]{1,32}";
        return patternMatch(regEx);

    }

    @Override
    public boolean isValidHistoryProtocol(String clientMessage) {

        regEx = "history [A-Za-z0-9]{1,32}";
        return patternMatch(regEx);
    }

    @Override
    public boolean isValidSendProtocol(String clientMessage) {

        regEx = "send ((@|#)[A-Za-z0-9]{1,32}.*)[ A-Za-z0-9.-:;!*&^%$#?<>'']{1,100}";
        return patternMatch(regEx);
    }

    public boolean patternMatch(String regEx){

        p = Pattern.compile(regEx);
        m = p.matcher(clientMessage);

        if (m.matches()) {
            return true;
        }else {
            return false;
        }
    }
}
