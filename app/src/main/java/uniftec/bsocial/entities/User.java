package uniftec.bsocial.entities;

public class User {

    private String nome;
    private String email;
    private String idFacebook;
    private boolean oculto;
    private boolean notifica;
    private Double latitude;
    private Double longitude;

    public User(String nome, String email, String idFacebook, boolean oculto, boolean notifica, Double latitude, Double longitude) {
        super();

        this.nome = nome;
        this.email = email;
        this.idFacebook = idFacebook;
        this.oculto = oculto;
        this.notifica = notifica;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getIdFacebook() {
        return idFacebook;
    }

    public void setIdFacebook(String idFacebook) {
        this.idFacebook = idFacebook;
    }

    public boolean isOculto() {
        return oculto;
    }

    public void setOculto(boolean oculto) {
        this.oculto = oculto;
    }

    public boolean isNotifica() {
        return notifica;
    }

    public void setNotifica(boolean notifica) {
        this.notifica = notifica;
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
}
