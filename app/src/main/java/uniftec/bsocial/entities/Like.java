package uniftec.bsocial.entities;

import java.io.Serializable;

/**
 * Created by mauri on 25/08/2016.
 */

public class Like implements Serializable {
    private String id;
    private String name;
    private String pictureUrl;
    private String category;

    public Like(String id, String name, String pictureUrl, String category) {
        super();

        this.id = id;
        this.name = name;
        this.pictureUrl = pictureUrl;
        this.category = category;
    }

    public Like(Like like) {
        super();

        id = like.getId();
        name = like.getName();
        pictureUrl = like.getPictureUrl();
        category = like.getCategory();
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

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
