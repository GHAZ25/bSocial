package uniftec.bsocial;

/**
 * Created by mauri on 25/08/2016.
 */

public class Like {
    private String id;
    private String name;
    private String pictureUrl;

    public Like() {

    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setPictureUrl(String pictureUrl) {
        this.pictureUrl = pictureUrl;
    }

    public String getPictureUrl() {
        return pictureUrl;
    }
}
