package uniftec.bsocial.entities;

public class Category {
    private String nome;
    private boolean selecionada;

    public Category(String nome, boolean selecionada) {
        super();

        this.nome = nome;
        this.selecionada = selecionada;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public boolean isSelecionada() {
        return selecionada;
    }

    public void setSelecionada(boolean selecionada) {
        this.selecionada = selecionada;
    }
}
