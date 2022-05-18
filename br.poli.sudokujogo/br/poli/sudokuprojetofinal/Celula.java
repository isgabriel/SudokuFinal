package br.poli.sudokuprojetofinal;

import java.io.Serializable;
import java.util.Objects;

// Informações sobre uma célula individual na grade
public class Celula implements Serializable {

    // atributos da celula
    protected int valorUsuario;
    protected int valorSolucao;
    private int valorProvisorio;
    private boolean bloqueado;
    private final CelulaPosicao posicao;
    private static final String COR_AZUL = "\u001B[34m";
    private static final String COR_RESET = "\u001B[0m";

    /**
     * Constrói um objeto Celula na linha e na coluna
     * 
     * @param linha  a linha da posição da célula
     * @param coluna a coluna da posição da célula
     */
    public Celula(int linha, int coluna) {
        this.posicao = new CelulaPosicao(linha, coluna);
    }

    // @return a posição dessa célula
    public CelulaPosicao getPosicao() {
        return this.posicao;
    }

    // @return true se a célula estiver bloqueada, caso contrário false
    public boolean isBloqueado() {
        return this.bloqueado;
    }

    // @param bloqueado bloqueia / desbloqueia a célula para evitar mudanças
    public void setBloqueado(boolean bloqueado) {
        this.bloqueado = bloqueado;
    }

    /**
     * @return true se a celula estiver vazia (preenchida por 0), false se a celula
     *         nao estiver vazia (preenchida por !=0)
     */
    public boolean isVazio() {
        return getValorUsuario() == 0;
    }

    // @return o valor escolhido pelo usuário para essa célula
    public int getValorUsuario() {
        return this.valorUsuario;
    }

    // @param valorUsuario o valor escolhido pelo usuário para esta célula
    public void setValorUsuario(int valorUsuario) {
        this.valorUsuario = valorUsuario;
    }

    // @return o valor verdadeiro (solução) da célula
    public int getValorSolucao() {
        return this.valorSolucao;
    }

    // Define (set) o valor da solução da célula para valorUsuario
    public void setValorSolucao() {
        this.valorSolucao = valorUsuario;
    }

    // Define (set) o valor provisório da célula para a entrada do usuário
    public void armazenaValorProvisorio() {
        this.valorProvisorio = this.valorUsuario;
    }

    // recebe o valor provisório da célula
    public void buscaValorProvisorio() {
        this.valorUsuario = this.valorProvisorio;
    }

    // @return a representação visual desta célula na grade
    @Override
    public String toString() {
        if (this.isBloqueado()) {
            // return "[" + getValorUsuario() + "]";
            return "[" + COR_AZUL + getValorUsuario() + COR_RESET + "]";
        }
        return ("[" + (isVazio() ? "_" : getValorUsuario()) + "]");
    }

    // @return a descrição de texto da célula

    public String descricaoCelula() {
        // Descrição: Posição da célula + subgrade da célula + valor da célula/vazio
        String descricao = "celula na " + getPosicao() + " (subgrade " + (getPosicao().getSubgrade() + 1) + ")";
        descricao += (isBloqueado() ? " nao pode sser editada. "
                : (!isVazio() ? " contém " + getValorUsuario() + "." : " está limpo."));
        return descricao;
    }

    /**
     * Compara 'esta' célula com a célula de entrada
     * 
     * @param objeto o objeto de entrada que será comparado ao objeto 'this'
     *               (celula)
     * @return true se os objetos forem iguais, senão false
     */
    @Override
    public boolean equals(Object objeto) {
        return objeto != null
                && objeto.getClass() == this.getClass()
                && ((Celula) objeto).getValorUsuario() == this.getValorUsuario()
                && ((Celula) objeto).getPosicao().getLinha() == this.getPosicao().getLinha()
                && ((Celula) objeto).getPosicao().getColuna() == this.getPosicao().getColuna();
    }

    // @return o codigo gerado para a celula
    @Override
    public int hashCode() {
        int hash = 3;
        hash = 79 * hash + this.valorUsuario;
        hash = 79 * hash + this.valorSolucao;
        hash = 79 * hash + this.valorProvisorio;
        hash = 79 * hash + (this.bloqueado ? 1 : 0);
        hash = 79 * hash + Objects.hashCode(this.posicao);
        return hash;
    }
}