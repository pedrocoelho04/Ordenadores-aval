package com.ordenacao;

import java.awt.FileDialog;
import java.awt.Frame;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("==============================");
            System.out.println("   MENU INICIAL - ORDENADORES  ");
            System.out.println("==============================");
            System.out.println("1. Gerar teste de performance");
            System.out.println("2. Enviar um arquivo");
            System.out.println("0. Sair");
            System.out.print("Escolha uma opção: ");
            String opcao = scanner.nextLine().trim();

            if (opcao.equals("1")) {
                gerarTestePerformance();
            } else if (opcao.equals("2")) {
                enviarArquivo(scanner);
            } else if (opcao.equals("0")) {
                System.out.println("Encerrando o programa...");
                break;
            } else {
                System.out.println("Opção inválida. Tente novamente.");
            }
        }
        scanner.close();
    }

    // Função para gerar teste de performance
    private static void gerarTestePerformance() {
        // Tamanhos dos arrays
        //int[] sizes = { 100000, 160000, 220000, 280000, 340000, 400000, 460000, 520000, 580000, 640000, 700000 };
        int[] sizes = { 1000, 1600, 2200, 2800, 3400, 4000, 4600, 5200, 5800, 6400, 7000 };
        String[] algoritmos = { "Bubble", "Heap", "Insertion", "Merge", "Quick", "Selection" };

        // 3 grupos: crescente, decrescente, aleatório
        int[][][] grupos = new int[3][sizes.length][];
        for (int i = 0; i < sizes.length; i++) {
            int n = sizes[i];
            // Crescente
            grupos[0][i] = new int[n];
            for (int j = 0; j < n; j++) grupos[0][i][j] = j;
            // Decrescente
            grupos[1][i] = new int[n];
            for (int j = 0; j < n; j++) grupos[1][i][j] = n - j - 1;
            // Aleatório
            grupos[2][i] = new int[n];
            java.util.Random rand = new java.util.Random();
            for (int j = 0; j < n; j++) grupos[2][i][j] = rand.nextInt(n);
        }

        // Medir tempo de cada algoritmo para cada array de cada grupo
        long[][][] tempos = new long[3][sizes.length][algoritmos.length];
        for (int grupo = 0; grupo < 3; grupo++) {
            for (int i = 0; i < sizes.length; i++) {
                int[] original = grupos[grupo][i];
                for (int alg = 0; alg < algoritmos.length; alg++) {
                    int[] arr = original.clone();
                    long start = System.nanoTime();
                    switch (algoritmos[alg]) {
                        case "Bubble":
                            com.ordenacao.algoritimos.Bubble.sort(arr); break;
                        case "Heap":
                            com.ordenacao.algoritimos.Heap.sort(arr); break;
                        case "Insertion":
                            com.ordenacao.algoritimos.Insertion.sort(arr); break;
                        case "Merge":
                            com.ordenacao.algoritimos.Merge.mergeSort(arr); break;
                        case "Quick":
                            com.ordenacao.algoritimos.Quick.sort(arr); break;
                        case "Selection":
                            com.ordenacao.algoritimos.Selection.selectionSort(arr); break;
                    }
                    long end = System.nanoTime();
                    tempos[grupo][i][alg] = (end - start) / 1_000_000; // ms
                    System.out.println("DEBUG: Grupo " + grupo + ", Algoritmo " + algoritmos[alg] + ", Tamanho " + sizes[i] + ": " + tempos[grupo][i][alg] + " ms");
                }
            }
        }

        // Seleciona pasta e nome base do relatório PDF
        Frame frame = new Frame();
        FileDialog fdRelatorio = new FileDialog(frame, "Selecione a pasta e digite o nome do relatório PDF", FileDialog.SAVE);
        fdRelatorio.setFile("Relatorio.pdf");
        fdRelatorio.setVisible(true);
        String dirRelatorio = fdRelatorio.getDirectory();
        String nomeRelatorio = fdRelatorio.getFile();
        if (nomeRelatorio == null) {
            System.out.println("Nenhum local de relatório selecionado.");
            return;
        }
        if (!nomeRelatorio.isEmpty() && !nomeRelatorio.toLowerCase().endsWith(".pdf")) {
            nomeRelatorio += ".pdf";
        }
        String caminhoRelatorio = dirRelatorio + nomeRelatorio;

        // Chama SortReportPDF para gerar o relatório
        try {
            com.ordenacao.pdfmaker.SortReportPDF.gerarRelatorioPerformance(
                tempos, sizes, algoritmos, caminhoRelatorio
            );
            System.out.println("Relatório gerado com sucesso em: " + caminhoRelatorio);
        } catch (Exception e) {
            System.err.println("Erro ao gerar relatório: " + e.getMessage());
        }
    }

    // Função para enviar arquivo e ordenar
    private static void enviarArquivo(Scanner scanner) {
        try {
            // Seleciona arquivo de entrada
            Frame frame = new Frame();
            FileDialog fdEntrada = new FileDialog(frame, "Selecione o arquivo TXT de entrada", FileDialog.LOAD);
            fdEntrada.setFile("*.txt");
            fdEntrada.setVisible(true);
            String dirEntrada = fdEntrada.getDirectory();
            String arqEntrada = fdEntrada.getFile();
            if (arqEntrada == null) {
                System.out.println("Nenhum arquivo selecionado.");
                return;
            }
            File arquivoEntrada = new File(dirEntrada, arqEntrada);
            // Ler números do arquivo
            BufferedReader br = new BufferedReader(new FileReader(arquivoEntrada));
            StringBuilder conteudo = new StringBuilder();
            String linha;
            while ((linha = br.readLine()) != null) {
                conteudo.append(linha);
            }
            br.close();
            String[] partes = conteudo.toString().split(";");
            int[] numeros = new int[partes.length];
            for (int i = 0; i < partes.length; i++) {
                numeros[i] = Integer.parseInt(partes[i].trim());
            }

            // Seleciona pasta e nome base de saída
            FileDialog fdSaida = new FileDialog(frame, "Selecione a pasta e digite o nome base dos arquivos de saída", FileDialog.SAVE);
            fdSaida.setFile("saida");
            fdSaida.setVisible(true);
            String dirSaida = fdSaida.getDirectory();
            String nomeBase = fdSaida.getFile();
            if (nomeBase == null) {
                System.out.println("Nenhum local de saída selecionado.");
                return;
            }
            if (!nomeBase.isEmpty() && nomeBase.endsWith(".txt")) {
                nomeBase = nomeBase.substring(0, nomeBase.length() - 4);
            }

            String[] algoritmos = { "Bubble", "Heap", "Insertion", "Merge", "Quick", "Selection" };
            long[] tempos = new long[algoritmos.length];

            for (int alg = 0; alg < algoritmos.length; alg++) {
                int[] arr = numeros.clone();
                long start = System.nanoTime();
                switch (algoritmos[alg]) {
                    case "Bubble":
                        com.ordenacao.algoritimos.Bubble.sort(arr); break;
                    case "Heap":
                        com.ordenacao.algoritimos.Heap.sort(arr); break;
                    case "Insertion":
                        com.ordenacao.algoritimos.Insertion.sort(arr); break;
                    case "Merge":
                        com.ordenacao.algoritimos.Merge.mergeSort(arr); break;
                    case "Quick":
                        com.ordenacao.algoritimos.Quick.sort(arr); break;
                    case "Selection":
                        com.ordenacao.algoritimos.Selection.selectionSort(arr); break;
                }
                long end = System.nanoTime();
                tempos[alg] = (end - start) / 1_000_000; // ms

                // Salvar arquivo de saída ordenado
                String nomeSaida = dirSaida + nomeBase + "_" + algoritmos[alg] + ".txt";
                BufferedWriter bw = new BufferedWriter(new FileWriter(nomeSaida));
                for (int i = 0; i < arr.length; i++) {
                    bw.write(String.valueOf(arr[i]));
                    if (i < arr.length - 1) bw.write(";");
                }
                bw.close();
                System.out.println("Arquivo ordenado salvo: " + nomeSaida);
            }

            // Gerar relatório de tempos
            String relatorio = dirSaida + nomeBase + "_relatorio_tempos.txt";
            BufferedWriter bwRel = new BufferedWriter(new FileWriter(relatorio));
            bwRel.write("Relatório de Tempos de Execução (ms):\n");
            for (int alg = 0; alg < algoritmos.length; alg++) {
                bwRel.write(algoritmos[alg] + ": " + tempos[alg] + " ms\n");
            }
            bwRel.close();
            System.out.println("Relatório de tempos salvo: " + relatorio);
        } catch (Exception e) {
            System.err.println("Erro ao processar o arquivo: " + e.getMessage());
        }
    }
}