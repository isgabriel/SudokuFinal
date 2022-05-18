package br.poli.sudokuprojetofinal;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Encontra uma solução única para a rede.
 */
public class Solucao {

    // Atributos da solução
    private Tabuleiro grade;
    private int resultado;
    private int loop;

    /**
     * Constrói uma grade bruta para resolver.
     *
     * @param grade a grade de Sudoku para resolver
     * @return a grade primordial
     */
    public Solucao resolverPara(Tabuleiro grade) {
        this.grade = grade;
        this.resultado = 0;
        this.setLoop(0);
        return this;
    }

    /**
     * Encontra uma solução única para esta grade usando recursão para validar cada
     * valor.
     *
     * @param celulasVazias a grade vazia
     * @param numVazio      quantidade absoluta de células vazias
     * @return a verdadeira solução
     */
    public int acharSolucao(List<Celula> celulasVazias, int numVazio) {
        if (getLoop() < celulasVazias.size()) {
            for (int digito : valoresAleatorios()) {
                if (grade.atendeRestricoes(celulasVazias.get(getLoop()), digito)) {
                    celulasVazias.get(getLoop()).setValorUsuario(digito);
                    setLoop(getLoop() + 1);
                    if (acharSolucao(celulasVazias, numVazio) >= numVazio) {
                        return resultado;
                    }
                }
            }
            celulasVazias.get(getLoop()).setValorUsuario(0);
            setLoop(getLoop() - 1);
            return resultado;
        } else {
            setLoop(getLoop() - 1);
            return ++resultado;
        }
    }

    /**
     * Embaralha a lista de valores possíveis.
     *
     * @return uma lista embaralhada de valores possíveis (1-9)
     */
    public List<Integer> valoresAleatorios() {
        List<Integer> valoresPossiveis = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9));
        Collections.shuffle(valoresPossiveis);
        return valoresPossiveis;
    }

    /**
     * @return o contador de loops
     */
    private int getLoop() {
        return loop;
    }

    /**
     * @param loop o loop para definir
     */
    private void setLoop(int loop) {
        this.loop = loop;
    }
}
