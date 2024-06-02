package ambiente;

import java.util.List;

public class Pessoa {
    private String nome;
    private List<Integer> posicao;
    private Boolean saiu = false;
    public Pessoa(String nome) {
        this.nome = nome;
    }
    public String getNome() {
        return nome;
    }
    public void setNome(String nome) {
        this.nome = nome;
    }
    public void setPosicao(List<Integer> posicao) {
        this.posicao = posicao;
    }
    public List<Integer> getPosicao() {
        return posicao;
    }
    public void setSaiu(Boolean saiu) {
        this.saiu = saiu;
    }

    public Boolean getSaiu() {
        return saiu;
    }
}
