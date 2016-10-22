package uniftec.bsocial.entities;

public class Notification {

    private String id;
    private String message;
    private String type;

    public Notification(String id, String message, String type) {
        this.id = id;
        this.message = message;
        this.type = type;
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
}
