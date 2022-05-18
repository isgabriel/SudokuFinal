package br.poli.sudokuprojetofinal;

import java.util.Random;

public enum Dificuldade {

    // Médias com variacao de ± 2, devem ser menores que 64 para que a solução seja
    // única.
    // NOTE: Quanto maior o número de células em branco, mais tempo leva para
    // produzir uma solução única.
    FACIL(20, 3),
    MEDIO(35, 5),
    DIFICIL(43, 8);

    // Atributos de dificuldade
    private final int variação = 2;
    private final int maxDicas;
    private final int celulasVazias;

    /**
     * Gere um número pseudo-aleatório de células em branco.
     * 
     * @param espacoEmBranco número de espaços em branco em média (± 2 de variacao)
     * @param maxDicas       número de dicas disponíveis para o nível de dificuldade
     */
    Dificuldade(int espacoEmBranco, int maxDicas) {
        this.celulasVazias = new Random().nextInt(((espacoEmBranco + variação) - (espacoEmBranco - variação)) + 1)
                + (espacoEmBranco - variação);
        this.maxDicas = maxDicas;
    }

    // @return o número máximo de dicas permitidas
    public int getMaximoDicas() {
        return maxDicas;
    }

    // @return o número de células vazias
    public int numCelulasVazias() {
        return celulasVazias;
    }

    // @return maiusculas
    @Override
    public String toString() {
        return new StringBuffer(this.name().length())
                .append(Character.toTitleCase(this.name().charAt(0)))
                .append(this.name().toLowerCase().substring(1)).toString();
    }
}
