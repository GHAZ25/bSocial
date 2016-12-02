package uniftec.bsocial.entities.messages;

import java.util.List;

import uniftec.bsocial.entities.Message;

public class MessageMessages {

    private String message;
    private List<Message> messages;

    public MessageMessages(String message, List<Message> messages) {
        super();
        this.message = message;
        this.messages = messages;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<Message> getMessages() {
        return messages;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }
}
