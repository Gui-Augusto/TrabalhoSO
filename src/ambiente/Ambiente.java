package ambiente;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Ambiente {
    int pessoas;
    int portas;
    int tamanhoAmbiente;
    int tempo;
    String[][] ambiente;

    public Ambiente(int pessoas, int portas, int tamanhoAmbiente, int tempo) {
        this.pessoas = pessoas;
        this.portas = portas;
        this.tamanhoAmbiente = tamanhoAmbiente;
        this.tempo = tempo;
        this.ambiente = new String[tamanhoAmbiente][tamanhoAmbiente];
    }

    public void criarAmbiente(){
        List<Pessoa> pessoasList = new ArrayList<>();

        for (int i = 0; i < portas; i++) {
            var porta = new Porta("[" + i + "]");
            var posicaoValida = posicaoValida(false);

            ambiente[posicaoValida.get(0)][posicaoValida.get(1)] = porta.getNome();
            porta.setPosicao(posicaoValida);
        }

        for (int i = 0; i < pessoas; i++) {
            var pessoa = new Pessoa("P" + i);
            var posicaoValida = posicaoValida(true);
            pessoasList.add(pessoa);

            ambiente[posicaoValida.get(0)][posicaoValida.get(1)] = pessoa.getNome();
            pessoa.setPosicao(posicaoValida);
        }
        printMatrix();

        long startTime = System.currentTimeMillis();
        long duration = 10000;

        while (System.currentTimeMillis() - startTime <= duration) {
            try {
                movimentarAmbiente(ambiente, pessoasList.get(0));
                movimentarAmbiente(ambiente, pessoasList.get(1));
                printMatrix();
            }catch (Exception e){

            }
        }
    }


    public List<Integer> posicaoValida(Boolean isPessoa) {
        var flag = true;
        Random rand = new Random();
        List<Integer> posicoesValidas = new ArrayList<>();
        int numeroAleatorioUm = 0;
        int numeroAleatorioDois = 0;

        while (flag) {
            numeroAleatorioUm = rand.nextInt(tamanhoAmbiente);
            numeroAleatorioDois = rand.nextInt(tamanhoAmbiente);
            if (ambiente[numeroAleatorioUm][numeroAleatorioDois] == null) {

                if (isPessoa) {
                    flag = false;
                } else if ((numeroAleatorioUm == 0 || numeroAleatorioUm == tamanhoAmbiente)
                        || (numeroAleatorioDois == 0 || numeroAleatorioDois == tamanhoAmbiente)) {
                    flag = false;
                }
            }
        }

        posicoesValidas.add(numeroAleatorioUm);
        posicoesValidas.add(numeroAleatorioDois);

        return posicoesValidas;
    }


    public void printMatrix() {
        if (ambiente == null || ambiente.length == 0 || ambiente[0].length == 0) {
            System.out.println("A matriz está vazia.");
            return;
        }
        int tamanho = ambiente.length;

        System.out.println("Matriz:");
        for (int i = 0; i < tamanho; i++) {
            for (int j = 0; j < tamanho; j++) {
                if (ambiente[i][j] == null) {
                    System.out.print("X \t");
                } else {
                    System.out.print(ambiente[i][j] + "\t");
                }
            }
            System.out.println();
        }
    }

    public void movimentarAmbiente(String[][] ambiente, Pessoa pessoa) throws InterruptedException{
        Random rand = new Random();
        List<int[]> posicoesAdjacentes = new ArrayList<>();
        int linha = pessoa.getPosicao().get(0);
        int coluna = pessoa.getPosicao().get(1);
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
            if (ambiente[novaPosicao[0]][novaPosicao[1]] == null) {
                ambiente[pessoa.posicao.get(0)][pessoa.posicao.get(1)] = null;
                pessoa.posicao.set(0, novaPosicao[0]);
                pessoa.posicao.set(1, novaPosicao[1]);
                ambiente[pessoa.posicao.get(0)][pessoa.posicao.get(1)] = pessoa.getNome();
                flag = false;
            }
        }

        Thread.sleep(1000);
    }
}
