package uniftec.bsocial.entities.messages;

import java.util.List;

import uniftec.bsocial.entities.Notification;

public class MessageNotifications {

    private String message;
    private List<Notification> notifications;

    public MessageNotifications(String message, List<Notification> notifications) {
        super();
        this.message = message;
        this.notifications = notifications;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<Notification> getNotifications() {
        return notifications;
    }

    public void setNotifications(List<Notification> notifications) {
        this.notifications = notifications;
    }
}
