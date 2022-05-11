import java.io.Serializable;

/**
 * Informações sobre a posição de uma celula na grade/quadro
 *
 * @author Olaf Wrieden
 * @version 1.0
 */
public class CelulaPosicao implements Serializable {

    // Atributos de Posição da Celula
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

        // Avaliar com qual subgrade esta celula se associa
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
     * @return a posição da subgrade (base 0)
     */
    public int getSubgrade() {
        return this.subgrade;
    }

    /**
     * @return posição da célula formatada (e.g. 1A), no formato de fail return
     *         [linha,coluna]
     */
    @Override
    public String toString() {
        if (getColuna() + 65 >= 'A' && getColuna() + 65 <= 'Z') {
            return String.valueOf(getLinha() + 1) + (char) (getColuna() + 65);
        }
        return String.valueOf(getLinha() + 1) + "," + String.valueOf(getColuna() + 1);
    }
}
