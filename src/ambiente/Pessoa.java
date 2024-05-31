package ambiente;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Pessoa extends Thread {
    String nome;
    List<Integer> posicao;
    String[][] ambiente;

    public Pessoa(String nome) {
        this.nome = nome;
    }

    @Override
    public void run() {
        while (true) {
            try {
                Random rand = new Random();
                List<int[]> posicoesAdjacentes = new ArrayList<>();
                int linha = posicao.get(0);
                int coluna = posicao.get(1);
                var flag = true;
                int[] novaPosicao;

                int numLinhas = ambiente.length;
                int numColunas = ambiente[0].length;

                for (int i = linha - 1; i <= linha + 1; i++) {
                    for (int j = coluna - 1; j <= coluna + 1; j++) {
                        if (i >= 0 && i < numLinhas && j >= 0 && j < numColunas) {
                            posicoesAdjacentes.add(new int[]{i, j});
                        }
                    }
                }

                while (flag) {
                    novaPosicao = posicoesAdjacentes.get(rand.nextInt(posicoesAdjacentes.size()));
                    synchronized (ambiente[novaPosicao[0]][novaPosicao[1]]) {
                        if (ambiente[novaPosicao[0]][novaPosicao[1]] == null) {
                            ambiente[posicao.get(0)][posicao.get(1)] = null;
                            posicao.set(0, novaPosicao[0]);
                            posicao.set(1, novaPosicao[1]);
                            ambiente[posicao.get(0)][posicao.get(1)] = nome;
                            flag = false;
                        }
                    }
                }
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                break;
            }
        }
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
