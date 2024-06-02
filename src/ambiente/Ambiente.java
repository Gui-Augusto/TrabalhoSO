package ambiente;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CompletableFuture;

public class Ambiente {
    int pessoas;
    public int portas;
    int tamanhoAmbiente;
    int tempo;
    final String[][] mapa;

    public Ambiente(int pessoas, int portas, int tamanhoAmbiente, int tempo) {
        this.pessoas = pessoas;
        this.portas = portas;
        this.tamanhoAmbiente = tamanhoAmbiente;
        this.tempo = tempo;
        this.mapa = new String[tamanhoAmbiente][tamanhoAmbiente];
    }

    public void criarAmbiente() throws InterruptedException {
        List<Pessoa> pessoasList = new ArrayList<>();
        List<Porta> portaList = new ArrayList<>();

        for (int i = 0; i < portas; i++) {
            var porta = new Porta("[" + i + "]");
            var posicaoValida = posicaoValida(false);
            portaList.add(porta);

            mapa[posicaoValida.get(0)][posicaoValida.get(1)] = porta.getNome();
            porta.setPosicao(posicaoValida);
        }

        for (int i = 0; i < pessoas; i++) {
            var pessoa = new Pessoa("P" + i);
            var posicaoValida = posicaoValida(true);
            pessoasList.add(pessoa);

            mapa[posicaoValida.get(0)][posicaoValida.get(1)] = pessoa.getNome();
            pessoa.setPosicao(posicaoValida);
        }

        long startTime = System.currentTimeMillis();
        long duration = tempo * 1000L;

        while (System.currentTimeMillis() - startTime <= duration) {
            var threads = new ArrayList<CompletableFuture>();
            for (Pessoa pessoa : pessoasList) {
                var threadPessoa = CompletableFuture.runAsync(() -> {
                    movimentarAmbiente(pessoa);
                });
                threads.add(threadPessoa);
            }
            imprimirSala();
            Thread.sleep(1000);
        }

        System.out.println("Tempo acabou, saindo da sala.");

        while (obterPessoasNaSala() > 0) {
            var threads = new ArrayList<CompletableFuture>();

            for (Pessoa pessoa : pessoasList) {
                if(pessoa.saiu)
                    continue;
                Porta portaProxima = encontrarPortaMaisProxima(pessoa, portaList);
                var threadPessoa = CompletableFuture.runAsync(() -> {
                    sair(portaProxima, pessoa);
                });
                threads.add(threadPessoa);
            }
            CompletableFuture.allOf(threads.toArray(new CompletableFuture[threads.size()])).join();
            imprimirSala();
            Thread.sleep(1000);
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
            if (mapa[numeroAleatorioUm][numeroAleatorioDois] == null) {

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


    public void imprimirSala() {
        if (mapa == null || mapa.length == 0 || mapa[0].length == 0) {
            System.out.println("A matriz está vazia.");
            return;
        }
        int tamanho = mapa.length;

        System.out.println("Sala:");
        for (int i = 0; i < tamanho; i++) {
            for (int j = 0; j < tamanho; j++) {
                if (mapa[i][j] == null) {
                    System.out.print("_ \t");
                } else {
                    System.out.print(mapa[i][j] + "\t");
                }
            }
            System.out.println();
        }
    }

    public void movimentarAmbiente(Pessoa pessoa) {
        Random rand = new Random();
        List<int[]> posicoesAdjacentes = new ArrayList<>();
        var flag = true;
        int[] novaPosicao;
        int numLinhas = mapa.length;
        int numColunas = mapa[0].length;

        int linha = pessoa.getPosicao().get(0);
        int coluna = pessoa.getPosicao().get(1);
        for (int i = linha - 1; i <= linha + 1; i++) {
            for (int j = coluna - 1; j <= coluna + 1; j++) {
                if (i >= 0 && i < numLinhas && j >= 0 && j < numColunas) {
                    posicoesAdjacentes.add(new int[]{i, j});
                }
            }
        }

        while (flag) {
            novaPosicao = posicoesAdjacentes.get(rand.nextInt(posicoesAdjacentes.size()));
            synchronized (mapa) {
                if (mapa[novaPosicao[0]][novaPosicao[1]] == null) {
                    mapa[pessoa.posicao.get(0)][pessoa.posicao.get(1)] = null;
                    pessoa.posicao.set(0, novaPosicao[0]);
                    pessoa.posicao.set(1, novaPosicao[1]);
                    mapa[pessoa.posicao.get(0)][pessoa.posicao.get(1)] = pessoa.nome;
                    flag = false;
                }
            }
        }
    }

    public static double calcularDistancia(List<Integer> posicao1, List<Integer> posicao2) {
        int deltaX = posicao1.get(0) - posicao2.get(0);
        int deltaY = posicao1.get(1) - posicao2.get(1);
        return Math.sqrt(deltaX * deltaX + deltaY * deltaY);
    }

    public static Porta encontrarPortaMaisProxima(Pessoa pessoa, List<Porta> portas) {
        Porta portaMaisProxima = null;
        double distanciaMinima = Double.MAX_VALUE;

        for (Porta porta : portas) {
            double distancia = calcularDistancia(pessoa.getPosicao(), porta.getPosicao());
            if (distancia < distanciaMinima) {
                distanciaMinima = distancia;
                portaMaisProxima = porta;
            }
        }

        return portaMaisProxima;
    }

    public void sair(Porta destino, Pessoa pessoa) {
        int portaX = destino.posicao.get(0);
        int portaY = destino.posicao.get(1);
        int pessoaX = pessoa.posicao.get(0);
        int pessoaY = pessoa.posicao.get(1);

        synchronized (mapa) {
            if (pessoaX != portaX) {
                if (pessoaX < portaX) {
                    pessoaX++;
                } else {
                    pessoaX--;
                }

                if (pessoaX == portaX && pessoaY == portaY) {
                    mapa[pessoa.posicao.get(0)][pessoa.posicao.get(1)] = null;
                    pessoa.posicao = List.of(pessoaX, pessoaY);
                    System.out.println(pessoa.nome + " saiu da sala pela porta " + destino.getNome());
                    pessoa.saiu = true;
                }

                moverPessoaNaMatriz(pessoa, pessoaX, pessoaY);
                return;
            }

            if (pessoaY < portaY) {
                pessoaY++;
            } else {
                pessoaY--;
            }

            if (pessoaY == portaY) {
                mapa[pessoa.posicao.get(0)][pessoa.posicao.get(1)] = null;
                pessoa.posicao = List.of(pessoaX, pessoaY);
                System.out.println(pessoa.nome + " saiu da sala pela porta " + destino.getNome());
                pessoa.saiu = true;
            }

            moverPessoaNaMatriz(pessoa, pessoaX, pessoaY);
        }
    }

    private void moverPessoaNaMatriz(Pessoa pessoa, int newX, int newY) {
        if (mapa[newX][newY] == null) {
            mapa[pessoa.posicao.get(0)][pessoa.posicao.get(1)] = null;
            pessoa.posicao = List.of(newX, newY);
            mapa[newX][newY] = pessoa.getNome();
        }
    }

    private int obterPessoasNaSala() {
        int pessoasContadas = 0;

        for (int i = 0; i < tamanhoAmbiente; i++) {
            for (int j = 0; j < tamanhoAmbiente; j++) {
                if (mapa[i][j] != null && mapa[i][j].contains("P")) {
                    pessoasContadas++;
                }
            }
        }

        return pessoasContadas;
    }
}

