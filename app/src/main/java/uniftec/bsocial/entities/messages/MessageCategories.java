package uniftec.bsocial.entities.messages;

import java.util.List;

import uniftec.bsocial.entities.Category;

public class MessageCategories {

    private String message;
    private List<Category> likes;

    public MessageCategories(String message, List<Category> likes) {
        super();
        this.message = message;
        this.likes = likes;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<Category> getLikes() {
        return likes;
    }

    public void setLikes(List<Category> likes) {
        this.likes = likes;
    }
}