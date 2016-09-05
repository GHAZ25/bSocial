package uniftec.bsocial.entities;

import java.util.ArrayList;

/**
 * Created by mauri on 25/08/2016.
 */

public class UserEntity {
    private String id;
    private String name;
    private String pictureUrl;
    private String age;
    private String hometown;
    private ArrayList<LikeEntity> likeEntities;

    public UserEntity() {

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

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getHometown() {
        return hometown;
    }

    public void setHometown(String hometown) {
        this.hometown = hometown;
    }

    public void setLikeEntities(ArrayList<LikeEntity> likeEntities) {
        this.likeEntities = likeEntities;
    }

    public ArrayList<LikeEntity> getLikeEntities() {
        return likeEntities;
    }
}
