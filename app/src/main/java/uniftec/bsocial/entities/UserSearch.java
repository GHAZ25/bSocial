package uniftec.bsocial.entities;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by mauri on 25/08/2016.
 */

public class UserSearch implements Serializable {
    private String id;
    private String name;
    private String pictureUrl;
    private String age;
    private String hometown;
    private Double latitude;
    private Double longitude;
    private float compatibilidade;

    public UserSearch(UserSearch userSearch) {
        super();
        id = userSearch.getId();
        name = userSearch.getName();
        pictureUrl = userSearch.getPictureUrl();
        age = userSearch.getAge();
        hometown = userSearch.getHometown();
        latitude = userSearch.getLatitude();
        longitude = userSearch.getLongitude();
        compatibilidade = userSearch.getCompatibilidade();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPictureUrl() {
        return pictureUrl;
    }

    public void setPictureUrl(String pictureUrl) {
        this.pictureUrl = pictureUrl;
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

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public float getCompatibilidade () {
        return compatibilidade;
    }

    public void setCompatibilidade(float compatibilidade) {
        this.compatibilidade = compatibilidade;
    }
}
