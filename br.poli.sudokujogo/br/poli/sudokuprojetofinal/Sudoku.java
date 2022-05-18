package br.poli.sudokuprojetofinal;

import java.io.EOFException;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * O modelo de jogo Sudoku
 */
public class Sudoku {

    // Entrada do teclado
    Scanner entrada = new Scanner(System.in);

    /**
     * Construtor de Sudoku padrão
     */
    public Sudoku() {
        // Iniciar novo jogo de Sudoku
        BannerWelcome();
        mainMenu();
    }

    /**
     * Ponto de entrada do aplicativo.
     *
     * @param args Argumentos de inicialização opcionais
     */
    public static void main(String[] args) {
        new Sudoku();
    }

    /**
     * Exibe o Menu Principal no Console.
     */
    public void mainMenu() {
        int selecao = 0;

        do {
            // Exibir seleção do menu principal
            System.out.println("\n========= MENU SUDOKU ==========");
            System.out.println("1: Iniciar um Novo Jogo");
            System.out.println("2: Continuar Seu Jogo Anterior");
            System.out.println("3: Mostrar Regras do Sudoku");
            System.out.println("4: Sair do Jogo");
            System.out.println("5: Ranking dos 10 melhores jogadores");
            System.out.println("==============================");
            System.out.print("Selecione: ");

            // Filtrar resposta do usuário
            try {
                selecao = Integer.parseInt(entrada.nextLine().trim());
            } catch (NumberFormatException ex) {
                continue;
            }

            // Age na entrada do usuário
            switch (selecao) {
                case (1): // O usuário deseja iniciar um novo jogo
                    Dificuldade diff = pergDificuldade();
                    if (diff == null) {
                        selecao = 0;
                        break;
                    }

                    Gerador puzzle = new Gerador();
                    puzzle.gerarGrade(diff);

                    menuJogo(puzzle.getNovaGrade());
                    break;
                case (2): // O usuário deseja continuar um jogo anterior
                    Tabuleiro salvo = importSudoku();
                    if (salvo != null) {
                        menuJogo(salvo);
                    } else {
                        System.err.println("Desculpe, o Sudoku salvo não pôde ser recuperado.");
                    }
                    break;
                case (3): // O usuário quer ver as regras do jogo
                    showRules();
                    break;
                case (4): // Sair da aplicação
                    System.out.println("\nObrigado por Jogar Sudoku! Esperamos você em breve!!");
                    System.exit(0);
                default: // Seleção de menu padrão
                    System.out.println("\nSeleção inválida! Tente novamente.");
            }
        } while (selecao < 1 || selecao > 4 || selecao == 3);
    }

    /**
     * Exibe o menu do jogo no console.
     *
     * @param thisDesafio atual Sudoku para agir
     */
    public void menuJogo(Tabuleiro thisDesafio) {
        int selecao = 0;

        do {
            System.out.println(thisDesafio);
            System.out.println("\n========= MENU SUDOKU =========");
            System.out.println("1: Colocar um dígito");
            System.out.println("2: Remover um dígito");
            System.out.println("3: Sair sem salvar ");
            System.out.println("4: Salvar e sair do Sudoku");
            System.out.println("5: Receber uma dica");
            System.out.println("=============================");
            System.out.print("Selecione: ");

            // Filtrar resposta do usuário
            try {
                String linha = entrada.nextLine().trim();
                selecao = Integer.parseInt(linha);
            } catch (NumberFormatException ex) {
                continue;
            }

            // Avaliar a escolha do usuário
            switch (selecao) {
                case (1): // O usuário deseja colocar um dígito em

                    // Obter célula
                    Celula cell = specificarCel(thisDesafio);
                    if (cell == null) {
                        selecao = 0;
                        break;
                    } else if (cell.isBloqueado()) {
                        System.out.println(cell.descricaoCelula());
                        break;
                    }

                    // Obter valor
                    int value = specificarValor();
                    if (value == -1) {
                        selecao = 0;
                        break;
                    }

                    editCell(cell, thisDesafio, value);
                    break;
                case (2): // O usuário deseja remover um dígito em

                    // Obter célula
                    Celula celulaVazia = specificarCel(thisDesafio);
                    if (celulaVazia == null) {
                        selecao = 0;
                        break;
                    } else if (celulaVazia.isBloqueado()) {
                        System.out.println(celulaVazia.descricaoCelula());
                        break;
                    }

                    editCell(celulaVazia, thisDesafio, 0);
                    break;
                case (3): // O usuário deseja sair sem salvar
                    mainMenu();
                    break;
                case (4): // Salvar e sair do aplicativo
                    exportSudoku(thisDesafio);
                    System.out.println("\nObrigado por Jogar Sudoku! Esperamos você em breve!!");
                    System.exit(0);
                    break;
                case (5): // O usuário quer uma dica
                    if (thisDesafio.getDicasUsadas() < thisDesafio.getDificuldade().getMaximoDicas()) {
                        thisDesafio.dica(false);
                        thisDesafio.setDicasUsadas();
                        System.out.println("\n--- Sumario ---\nDica Usada: " + thisDesafio.getStringDicasUsadas());
                    } else {
                        System.out.println(
                                "\n--- Sumario ---\nQuantidade Máxima de dicas usadas. Agora é por sua conta!");
                    }
                    break;
                default: // Seleção de menu padrão
                    System.out.println("\nSeleção inválida! Tente novamente.");
            }
        } while (selecao < 1 || selecao > 4 || !thisDesafio.isResolvido());

        // Ao resolver um quebra-cabeça:
        System.out.println(thisDesafio);
        System.out.println(parabenizar());
    }

    /**
     * Pergunta ao usuário qual é a dificuldade de Sudoku desejada.
     *
     * @return o usuário escolheu a dificuldade do Sudoku
     */
    public Dificuldade pergDificuldade() {
        int selecao = -1;

        do {
            System.out.println("\nEscolha a Dificuldade:");
            for (int i = 0; i < Dificuldade.values().length; i++) {
                System.out.println((i + 1) + ": " + Dificuldade.values()[i]);
            }
            System.out.print("Selecione: ");

            // Filtrar resposta do usuário
            String entradaUser = entrada.nextLine().trim();
            if ("x".equals(entradaUser)) {
                return null;
            } else {
                try {
                    selecao = Integer.parseInt(entradaUser);
                } catch (NumberFormatException ex) {
                    selecao = -1;
                }
            }
        } while (selecao <= 0 || selecao > Dificuldade.values().length);
        return Dificuldade.values()[selecao - 1];
    }

    /**
     * Pede ao usuário para especificar/escolher uma célula
     *
     * @param puzzle o Sudoku
     * @return a célula especificada pelo usuário
     */
    public Celula specificarCel(Tabuleiro puzzle) {
        String entradaUser;

        do {
            System.out.println("\nQual Célula? (por exemplo: 1E, x para cancelar) ");
            entradaUser = entrada.nextLine().trim();

            if ("x".equals(entradaUser)) {
                return null;
            } else {
                Pattern p = Pattern.compile("^(\\d)([a-zA-Z])$", Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
                Matcher m = p.matcher(entradaUser);
                if (m.find()) {
                    try {
                        return puzzle.getCelula(Integer.valueOf(m.group(1)), m.group(2).toUpperCase().charAt(0));
                    } catch (ArrayIndexOutOfBoundsException ex) {
                        entradaUser = "";
                    }
                } else {
                    entradaUser = "";
                }
            }
        } while ("".equals(entradaUser));
        return null;
    }

    /**
     * Pede ao usuário que especifique/escolha um valor.
     *
     * @return o valor especificado pelo usuário
     */
    public int specificarValor() {
        String entradaUser = null;

        while (entradaUser == null || !entradaUser.matches("^[1-9]$")) {
            System.out.println("Qual valor? (por exemplo: 1 - 9, x para cancelar) ");
            entradaUser = entrada.nextLine().trim();

            if ("x".equals(entradaUser)) {
                return -1;
            } else {

                try {
                    if (Integer.valueOf(entradaUser) > 0 && Integer.valueOf(entradaUser) < 10) {
                        return Integer.valueOf(entradaUser);
                    }
                } catch (NumberFormatException ex) {
                    continue;
                }
            }
        }
        return -1;
    }

    /**
     * Executa uma ação de alteração de valor na célula.
     *
     * @param celulaSelecionada a célula alvo para agir
     * @param puzzle            o Sudoku contendo a célula
     * @param valor             o valor de destino a ser gravado
     */
    public void editCell(Celula celulaSelecionada, Tabuleiro puzzle, int valor) {
        if (celulaSelecionada != null) {
            if ((valor == 0 || puzzle.atendeRestricoes(celulaSelecionada, valor) && !celulaSelecionada.isBloqueado())) {
                celulaSelecionada.setValorUsuario(valor);
                System.out.println("\n--- Sumario ---\n" + celulaSelecionada.descricaoCelula());
            } else if (celulaSelecionada.isBloqueado()) {
                System.out.println(
                        "\n--- Sumario ---\nSua célula escolhida (" + celulaSelecionada.getPosicao()
                                + ") não pode ser editada.");
            } else {
                System.out.println("\n--- Sumario ---\nSeu dígito (" + valor + ") não cabe aqui!");
            }
        } else {
            System.out.println("\n--- Sumario ---\nNenhuma alteração feita.");
        }
    }

    /**
     * Salva o Sudoku de entrada em arquivo.
     *
     * @param puzzle a entrada Sudoku para armazenarValorProvisorio
     */
    private void exportSudoku(Tabuleiro puzzle) {
        System.out.println("\n--- Salvando o Sudoku ---");
        System.out.println("PROGRESSO: Seu Sudoku está sendo salvo...");

        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("sudoku.bin", false))) {
            out.writeObject(puzzle);
            System.out.println("EXPORTADO: Seu Sudoku foi salvo em disco.");
        } catch (IOException ex) {
            System.err.println("ERRO: Sudoku falhou ao exportar para o disco.\n\n" + ex);
        } finally {
            System.out.println("---------------------");
        }
    }

    /**
     * Importa o Sudoku salvo do arquivo.
     */
    private Tabuleiro importSudoku() {
        System.out.println("\n--- Importar Sudoku ---");
        System.out.println("PROGRESSO: Seu Sudoku está sendo importado...");

        try {
            ObjectInputStream in = new ObjectInputStream(new FileInputStream("sudoku.bin"));
            boolean EOF = false;
            while (!EOF) {
                try {
                    Tabuleiro f = (Tabuleiro) in.readObject();
                    System.out.println("IMPORTADO: Seu Sudoku foi importado.");
                    return f;
                } catch (EOFException eof) {
                    in.close();
                    EOF = true;
                }
            }
        } catch (IOException | ClassNotFoundException ex) {
            return null;
        } finally {
            System.out.println("---------------------");
        }
        return null;
    }

    /**
     * Exibe o banner de boas-vindas no console.
     */
    public void BannerWelcome() {
        System.out.println("---------------------------------");
        System.out.println(" BEM-VINDO AO JOGO SUDOKU ");
        System.out.println("---------------------------------");
    }

    /**
     * Exibe as regras do Sudoku no Console.
     */
    public void showRules() {
        System.out.println("\n===== Regras do Sudoku =====");
        System.out.println(
                "Você é apresentado a uma grade de 81 quadrados, divididos em 9 subgrades, cada uma contendo 9 quadrados.");
        System.out.println("\nAs regras são simples:");
        System.out.println("> Cada um dos 9 blocos deve conter todos os números de 1 a 9 dentro de seus quadrados.");
        System.out.println("> Cada número só pode aparecer uma vez em uma linha, coluna ou caixa.");
        System.out.println(
                "> Cada coluna vertical de nove quadrados, ou linha horizontal de nove quadrados, dentro do quadrado maior,\n também deve conter os números de 1 a 9, sem repetição ou omissão.");
    }

    /**
     * Parabeniza o jogador após a conclusão do puzzle.
     *
     * @return uma mensagem de conclusão e um fato aleatório de Sudoku
     */
    public String parabenizar() {
        String saida = "\nParabéns! Você resolveu o quebra-cabeça.\n";
        saida += "Você sabia: " + gerarFato() + "?";
        return saida;
    }

    /**
     * Gere um fato aleatório de Sudoku.
     *
     * @return um fato aleatório de Sudoku
     */
    public String gerarFato() {
        List<String> fatos = new ArrayList<>();
        fatos.add("Os quebra-cabeças de Sudoku têm mais de 5,47 bilhões de soluções únicas");
        fatos.add("Ao contrário da crença popular, o Sudoku foi inventado na América");
        fatos.add("Sudokus podem prevenir e evitar a doença de Alzheimer e a demência");
        fatos.add("Para que um Sudoku seja único, ele deve conter pelo menos 17 dicas");
        return fatos.get(new Random().nextInt(fatos.size()));
    }
}
