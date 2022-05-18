package br.poli.sudokuprojetofinal;

import java.io.Serializable;

/**
 * Informações sobre a posição de uma célula na grade/tabuleiro
 */
public class CelulaPosicao implements Serializable {

    // Atributos de Posição da Célula
    private final int linha;
    private final int coluna;
    private final int subgrade;

    /**
     * Constrói um objeto CelulaPosicao na linha e na coluna
     *
     * @param linha  a linha da posição da célula
     * @param coluna a coluna da posição da célula
     */
    public CelulaPosicao(int linha, int coluna) {
        this.linha = linha;
        this.coluna = coluna;

        // Avaliar com qual subgrade esta célula se associa
        int avaliar = this.linha < 3 ? 0 : this.linha < 6 ? 2 : 4;
        this.subgrade = (this.linha / 3) + (this.coluna / 3) + avaliar;
    }

    /**
     * @return a posição da linha
     */
    public int getLinha() {
        return this.linha;
    }

    /**
     * @return a posição da coluna
     */
    public int getColuna() {
        return this.coluna;
    }

    /**
     * @return a posição da subgrade (baseada em 0)
     */
    public int getSubgrade() {
        return this.subgrade;
    }

    /**
     * @return posição da célula formatada (por exemplo, 1A), em caso de falha
     *         retornar formato [linha, coluna]
     */
    @Override
    public String toString() {
        if (getColuna() + 65 >= 'A' && getColuna() + 65 <= 'Z') {
            return String.valueOf(getLinha() + 1) + (char) (getColuna() + 65);
        }
        return String.valueOf(getLinha() + 1) + "," + String.valueOf(getColuna() + 1);
    }
}
