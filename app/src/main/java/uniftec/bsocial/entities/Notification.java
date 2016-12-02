package uniftec.bsocial.entities;

public class Notification {

    private String id;
    private String message;
    private String type;
    private String messageId;

    public Notification(String id, String message, String type, String messageId) {
        this.id = id;
        this.message = message;
        this.type = type;
        this.messageId = messageId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }
}
