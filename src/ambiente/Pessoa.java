package ambiente;

import java.util.List;

public class Pessoa {
    String nome;
    List<Integer> posicao;
    Boolean saiu = false;

    public Pessoa(String nome) {
        this.nome = nome;
    }

    public String getNome() {
        return nome;
    }

    public void setPosicao(List<Integer> posicao) {
        this.posicao = posicao;
    }

    public List<Integer> getPosicao() {
        return posicao;
    }
}
