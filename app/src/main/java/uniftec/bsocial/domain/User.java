package uniftec.bsocial.domain;

public class User {

    private String nome;
    private String email;
    private String idFacebook;
    private boolean oculto;
    private boolean notifica;

    public User(String nome, String email, String idFacebook, boolean oculto, boolean notifica) {
        super();

        this.nome = nome;
        this.email = email;
        this.idFacebook = idFacebook;
        this.oculto = oculto;
        this.notifica = notifica;
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
}
