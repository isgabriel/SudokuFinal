package br.poli.sudokuprojetofinal;

import java.util.ArrayList;
import java.util.List;

/**
 * Gera um novo Sudoku puzzle.
 */
public class Gerador {

    // Atributos do Gerador
    private Tabuleiro novaGrade;
    private int numVazio;
    private final Solucao solucao;

    /**
     * Constrói uma nova solução para a grade.
     */
    public Gerador() {
        // Criar uma solução para a nova instância
        this.solucao = new Solucao();
    }

    /**
     * Gera uma grade de Sudoku dependente de dificuldade.
     *
     * @param dif a dificuldade selecionada pelo usuário
     * @return uma grade válida com células abertas a serem preenchidas pelo usuário
     */
    public Tabuleiro gerarGrade(Dificuldade dif) {

        // Obter uma solução para a rede
        this.setNovaGrade(new Tabuleiro(dif));
        this.solucao.resolverPara(getNovaGrade()).acharSolucao(getNovaGrade().getListaCelula(), 1);

        // Defina o valor da solução para cada célula
        for (Celula cel : getNovaGrade()) {
            cel.setValorSolucao();
        }

        // Remova alguns dígitos da grade
        setNumVazio(dif.numCelulasVazias());
        celulasVazias(getNumVazio());

        // Salvar e retornar uma grade válida com células abertas
        this.getNovaGrade().preencherCelulas();
        return this.getNovaGrade();
    }

    /**
     * @param grade a grade que contém as células
     * @return uma lista de todas as células vazias na grade
     */
    public static List<Celula> todasCelulasVazias(Tabuleiro grade) {
        List<Celula> listaVazia = new ArrayList<>();
        // Adicione a célula a ListaVazia se a célula estiver vazia
        for (Celula cel : grade) {
            if (cel.isVazio()) {
                listaVazia.add(cel);
            }
        }
        return listaVazia;
    }

    /**
     * Remove o número de dígitos dependente da dificuldade da grade.
     *
     * @param removeNum a quantidade de dígitos (células) para esvaziar
     */
    public void celulasVazias(int removeNum) {
        // Define cada valor provisório de célula
        getNovaGrade().preencherCelulas();

        // Executar para cada célula na grade
        while (getNovaGrade().iterator().hasNext()) {

            // Se a célula não estiver vazia, esvazie-a
            Celula cel = getNovaGrade().iterator().next();
            cel.armazenaValorProvisorio();

            if (!cel.isVazio()) {
                cel.setValorUsuario(0);
            } else {
                continue;
            }

            // Encontrar uma solução para a grade
            this.solucao.resolverPara(getNovaGrade());

            // Pare de esvaziar a célula se o número especificado de células vazias for
            // atingido
            if (removeNum == todasCelulasVazias(getNovaGrade()).size()) {
                break;
            } else {
                // Se uma solução única pode ser produzida com células ausentes
                if (this.solucao.acharSolucao(todasCelulasVazias(getNovaGrade()), 3) != 1) {
                    getNovaGrade().buscarAbastecimentoCelula();
                } else {
                    cel.armazenaValorProvisorio();
                }
            }
        }

        // Bloqueie todas as células de dica
        lockDicas();
    }

    /**
     * Bloqueie as células restantes (dicas) para evitar que sejam editadas pelo
     * usuário
     */
    private void lockDicas() {
        for (Celula cell : getNovaGrade()) {
            if (cell.isVazio()) {
                cell.setBloqueado(false);
            } else {
                cell.setBloqueado(true);
            }
        }
    }

    /**
     * @return o número de células vazias desejadas
     */
    public int getNumVazio() {
        return numVazio;
    }

    /**
     * @param numVazio o número de células vazias desejadas
     */
    private void setNumVazio(int numVazio) {
        this.numVazio = numVazio;
    }

    /**
     * @return a grade
     */
    public Tabuleiro getNovaGrade() {
        return novaGrade;
    }

    /**
     * @param novaGrade a nova grade para definir
     */
    private void setNovaGrade(Tabuleiro novaGrade) {
        this.novaGrade = novaGrade;
    }
}
