<<<<<<< HEAD
package uniftec.bsocial.entities.messages;

import java.util.List;

import uniftec.bsocial.entities.UserSearch;

public class MessageUserSearch {

    private String message;
    private List<UserSearch> users;

    public MessageUserSearch(String message, List<UserSearch> users) {
        super();
        this.message = message;
        this.users = users;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<UserSearch> getUsers() {
        return users;
    }

    public void setUsers(List<UserSearch> users) {
        this.users = users;
    }
}
=======
package uniftec.bsocial.entities.messages;

import java.util.List;

import uniftec.bsocial.entities.UserSearch;

public class MessageUserSearch {

    private String message;
    private List<UserSearch> users;

    public MessageUserSearch(String message, List<UserSearch> users) {
        super();
        this.message = message;
        this.users = users;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<UserSearch> getUsers() {
        return users;
    }

    public void setUsers(List<UserSearch> users) {
        this.users = users;
    }
}
>>>>>>> origin/master
