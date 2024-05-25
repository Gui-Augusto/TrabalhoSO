package ambiente;

import java.util.List;

public class Porta {
    String nome;
    List<Integer> posicao;

    public Porta(String nome) {
        this.nome = nome;
    }

    public String getNome() {
        return nome;
    }

    public List<Integer> getPosicao() {
        return posicao;
    }

    public void setPosicao(List<Integer> posicao) {
        this.posicao = posicao;
    }
}
