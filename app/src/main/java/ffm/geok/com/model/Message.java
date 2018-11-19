package ffm.geok.com.model;

public class Message {
    int msgCode;
    String msgContent;

    public Message(int code,String message){
        msgCode=code;
        msgContent=message;
    }

    public int getMsgCode() {
        return msgCode;
    }

    public void setMsgCode(int msgCode) {
        this.msgCode = msgCode;
    }

    public String getMsgContent() {
        return msgContent;
    }

    public void setMsgContent(String msgContent) {
        this.msgContent = msgContent;
    }
}
