package uniftec.bsocial.entities;

public class Message {
    private String receivedUserId;
    private String sentUserId;
    private String sentUserName;
    private String message;

    public Message(String receivedUserId, String sentUserId, String sentUserName, String message) {
        this.receivedUserId = receivedUserId;
        this.sentUserId = sentUserId;
        this.sentUserName = sentUserName;
        this.message = message;
    }

    public String getReceivedUserId() {
        return receivedUserId;
    }

    public void setReceivedUserId(String receivedUserId) {
        this.receivedUserId = receivedUserId;
    }

    public String getSentUserId() {
        return sentUserId;
    }

    public void setSentUserId(String sentUserId) {
        this.sentUserId = sentUserId;
    }

    public String getSentUserName() {
        return sentUserName;
    }

    public void setSentUserName(String sentUserName) {
        this.sentUserName = sentUserName;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
