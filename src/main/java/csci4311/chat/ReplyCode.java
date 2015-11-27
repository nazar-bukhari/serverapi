package csci4311.chat;

/**
 * Created by nazar on 11/8/15.
 */
public enum ReplyCode {

    OK("msgp 200 OK"),
    NORESULT("msgp 201 NO result"),
    ERROR("msgp 400 Error");

    private String replyType;

    private ReplyCode(String replyType) {
        this.replyType = replyType;
    }

    public String getReplyType(){
        return replyType;
    }
}
