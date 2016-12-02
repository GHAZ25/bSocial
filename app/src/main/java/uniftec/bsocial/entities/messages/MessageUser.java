package uniftec.bsocial.entities.messages;

import uniftec.bsocial.entities.User;

public class MessageUser {

    private String message;
    private User user;

    public MessageUser(String message, User user) {
        super();
        this.message = message;
        this.user = user;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
