package uniftec.bsocial.entities.messages;

import java.util.List;

public class MessagePreferences {

    private String message;
    private List<String> preferences;

    public MessagePreferences(String message, List<String> preferences) {
        super();
        this.message = message;
        this.preferences = preferences;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<String> getPreferences() {
        return preferences;
    }

    public void setPreferences(List<String> preferences) {
        this.preferences = preferences;
    }
}