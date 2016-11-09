package uniftec.bsocial.entities;

/**
 * Created by mauri on 09/11/2016.
 */

public class Message {
    private String receivedUserId;
    private String sentUserId;
    private String sentUserName;
    private String sentUserPicUrl;
    private String message;

    public Message(String receivedUserId, String sentUserId, String sentUserName, String sentUserPicUrl, String message) {
        this.receivedUserId = receivedUserId;
        this.sentUserId = sentUserId;
        this.sentUserName = sentUserName;
        this.sentUserPicUrl = sentUserPicUrl;
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

    public String getSentUserPicUrl() {
        return sentUserPicUrl;
    }

    public void setSentUserPicUrl(String sentUserPicUrl) {
        this.sentUserPicUrl = sentUserPicUrl;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}