package br.poli.sudokuprojetofinal;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;

public class Tabuleiro implements Iterable<Celula>, Serializable {
    // Atributos
    private final int TAMANHO = 9;
    private final Celula[][] celulas;
    private final List<Celula> listaCelulas;
    private final List<List<Celula>> subgrades;
    private final Dificuldade dificuldade;
    private int dicasUsadas = 0;

    /**
     * constroi um objeto do tabuleiro
     * 
     * @param diff o nível de dificuldade do tabuleiro
     */
    public Tabuleiro(Dificuldade diff) {
        this.celulas = new Celula[this.TAMANHO][this.TAMANHO];
        this.listaCelulas = new ArrayList<>(this.TAMANHO * this.TAMANHO);
        this.subgrades = gerarSubgrades();
        this.dificuldade = diff;
        inicializarGrade();
    }

    // Configura o tabuleiro do Sudoku

    private void inicializarGrade() {
        for (int lin = 0; lin < this.TAMANHO; lin++) {
            for (int coluna = 0; coluna < this.TAMANHO; coluna++) {
                Celula cel = new Celula(lin, coluna);
                this.celulas[lin][coluna] = cel;
                this.listaCelulas.add(cel);
                this.subgrades.get(cel.getPosicao().getSubgrade()).add(cel);
            }
        }
    }

    /**
     * Gera uma lista de listas de células pra formação da subgrade
     * 
     * @return a Listagrade contendo listas de células
     */
    private List<List<Celula>> gerarSubgrades() {
        List<List<Celula>> listaGrade = new ArrayList<>(this.TAMANHO);
        for (int i = 0; i < this.TAMANHO; i++) {
            listaGrade.add(new ArrayList<>());
        }
        return listaGrade;
    }

    // @return uma lista de todas as células nessa grade
    public List<Celula> getListaCelula() {
        return this.listaCelulas;
    }

    // @return uma lista de células que pertencem a cada subgrade
    public List<List<Celula>> getSubgrades() {
        return this.subgrades;
    }

    // @return a dificuldade
    public Dificuldade getDificuldade() {
        return dificuldade;
    }

    // @return o numero de dicas usadas
    public int getDicasUsadas() {
        return dicasUsadas;
    }

    // @param dicasUsadas o numero de dicas usadas
    public void setDicasUsadas() {
        this.dicasUsadas++;
    }

    // @return uma versão formatada em string de dicas usadas
    public String getStringDicasUsadas() {
        return this.getDicasUsadas() + "/" + this.getDificuldade().getMaximoDicas();
    }

    /**
     * Revela imediatamente a solução
     * 
     * @param gradeCompleta se cada célula na grade deve ou não ser revelada
     */
    public void dica(boolean gradeCompleta) {
        ArrayList<Celula> celulasVazias = new ArrayList();

        for (Celula cel : listaCelulas) {
            if (cel.isVazio()) {
                celulasVazias.add(cel);
            }
        }

        Collections.shuffle(celulasVazias);

        for (Celula cel : celulasVazias) {
            if (gradeCompleta) {
                cel.valorUsuario = cel.getValorSolucao();
                cel.setBloqueado(true);
            } else if (!gradeCompleta && cel.isVazio()) {
                cel.setValorUsuario(cel.getValorSolucao());
                cel.setBloqueado(true);
                return;
            }
        }
    }

    /**
     * Verifica se o tabuleiro atual está resolvido pelo usuário
     * 
     * @return true se resolvido, caso contrário false
     */
    public boolean isResolvido() {
        for (Celula cel : this) {
            if (cel.valorUsuario != cel.getValorSolucao()) {
                return false;
            }
        }
        return true;
    }

    public void preencherCelulas() {
        for (Celula cel : this) {
            cel.armazenaValorProvisorio();
        }
    }

    public void buscarAbastecimentoCelula() {
        for (Celula cel : this) {
            cel.buscaValorProvisorio();
        }
    }

    /**
     * Avalia se a grade está preenchida ou não
     * 
     * @return true se preenchida, caso contrário false
     */
    public boolean isPreenchido() {
        for (int i = 0; i < this.listaCelulas.size(); i++) {
            if (this.listaCelulas.get(i).isVazio()) {
                return false;
            }
        }
        return true;
    }

    /**
     * Verifique se o valor do usuário atende às restrições das regras de
     * posicionamento
     * 
     * @param cel   a célula em que coloca o valor
     * @param valor o valor a entrar nessa célula
     * @return true se todas as restrições foram atendidas, caso contrário false
     */
    public boolean atendeRestricoes(Celula cel, int valor) {
        return checarLinha(cel.getPosicao().getLinha(), valor)
                && checarColuna(cel.getPosicao().getColuna(), valor)
                && checarSubgrade(cel, valor);
    }

    /**
     * Verifica se o valor ocorre apenas uma vez em toda a linha
     * 
     * @param linha a linha a qual deve verificar
     * @param valor o valor para verificar
     * @return true se o valor ocorrer apenas uma vez, caso contrário false
     */
    private boolean checarLinha(int linha, int valor) {
        for (Celula cel : celulas[linha]) {
            if (valor == cel.getValorUsuario()) {
                return false;
            }
        }
        return true;
    }

    /**
     * Verifica se o valor ocorre apenas uma vez em toda a coluna
     * 
     * @param coluna a coluna na qual verificar
     * @param valor  o valor para verificar
     * @return true se o valor ocorrer apenas uma vez, senão false
     */
    private boolean checarColuna(int coluna, int valor) {
        for (Celula[] celulasColuna : celulas) {
            if (valor == celulasColuna[coluna].getValorUsuario()) {
                return false;
            }
        }
        return true;
    }

    /**
     * Verifica se o valor ocorre apenas uma vez na subgrade escolhida
     * 
     * @param celulaAtual a célula na qual verificar
     * @param valor       o valor para verificar
     * @return true se o valor ocorrer apenas uma vez, senão false
     */
    private boolean checarSubgrade(Celula celulaAtual, int valor) {
        for (Celula cel : subgrades.get(celulaAtual.getPosicao().getSubgrade())) {
            if (valor == cel.getValorUsuario()) {
                return false;
            }
        }
        return true;
    }

    /**
     * Obter célula em uma posição de grade (1A-9I)
     * 
     * @param linha  linha contendo célula
     * @param coluna coluna contendo célula
     * @return a célula na posição especificada
     */
    public Celula getCelula(int linha, char coluna) {
        return this.celulas[linha - 1][(Character.toUpperCase(coluna) - 65)];
    }

    /**
     * Obter célula na posição da coordenada inteira
     * 
     * @param xPos posição x (1-9)
     * @param yPos posição y (1-9)
     * @return a célula na posição especificada
     */
    public Celula getCel(int xPos, int yPos) {
        return this.celulas[xPos - 1][yPos - 1];
    }

    // @return Exibe uma representação visual da grade Sudoku no console
    @Override
    public String toString() {
        // String de saída para anexar
        String resultado = "\n";
        resultado += printCharsDaColuna();

        // Borda superior
        resultado += ("\n   |---------|---------|---------|\n");

        // Para cada linha de células
        for (int i = 0; i < 9; i++) {
            // Anexar números de linha com preenchimento correto
            if (i == 3 || i == 6) {
                resultado += ("   |---------|---------|---------|\n");
            }
            resultado += " " + (i + 1) + " |";

            // Anexar uma representação das células
            for (int j = 0; j < 9; j++) {
                resultado += (celulas[i][j]);
                if (j == 2 || j == 5) {
                    resultado += ("|");
                }
            }
            resultado += ("| " + (i + 1) + "\n");
        }

        // Borda inferior
        resultado += ("   |---------|---------|---------|\n");
        resultado += printCharsDaColuna();
        return resultado;
    }

    /**
     * Produz letras de coluna com preenchimento correto
     * 
     * @return uma linha de cabeçalhos de coluna separados por espaço (A-I)
     */
    private String printCharsDaColuna() {
        String saida = "";
        // Anexar letras de coluna com preenchimento correto
        for (int i = 1; i <= this.TAMANHO; i++) {
            if (i == 1) {
                saida += "     " + (char) (i + 64);
            } else {
                if (i == 4 || i == 7) {
                    saida += "   " + (char) (i + 64);
                } else {
                    saida += "  " + (char) (i + 64);
                }
            }
        }
        return saida;
    }

    // @return um iterador de célula exclusivo
    @Override
    public ListIterator<Celula> iterator() {
        return misturarCelulas().listIterator();
    }

    /**
     * Embaralha a lista de células na grade.
     * 
     * @return uma lista embaralhada de células
     */
    public ArrayList<Celula> misturarCelulas() {
        ArrayList<Celula> celulasMisturadas = new ArrayList<>(listaCelulas);
        Collections.shuffle(celulasMisturadas);
        return celulasMisturadas;
    }
}
