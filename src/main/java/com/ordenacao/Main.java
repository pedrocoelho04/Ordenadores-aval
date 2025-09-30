package com.ordenacao;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.stage.FileChooser;

import javax.swing.UIManager;

import org.jfree.chart.JFreeChart;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.ordenacao.algoritimos.Bubble;
import com.ordenacao.algoritimos.Heap;
import com.ordenacao.algoritimos.Insertion;
import com.ordenacao.algoritimos.Merge;
import com.ordenacao.algoritimos.Quick;
import com.ordenacao.algoritimos.Selection;
import com.ordenacao.entidadesAuxiliares.SortTime;
import com.ordenacao.graphmaker.SortComparisonChart;
import com.ordenacao.operations.CalculoTempo;
import com.ordenacao.pdfmaker.SortReportPDF;

public class Main {
    public static void main(String[] args) {
        // Inicializa JavaFX
        new JFXPanel();
        Scanner scanner = new Scanner(System.in);

        System.out.println("Deseja inserir um arquivo ou realizar um teste de performace dos algoritmos?");
        System.out.println("1 - Enviar arquivo.");
        System.out.println("2 - Teste de peformance");
        String resposta = scanner.nextLine().trim().toUpperCase();

        if (resposta.equals("1")) {
            File[] arquivoSelecionado = new File[1];
            CountDownLatch latch = new CountDownLatch(1);

            Platform.runLater(() -> {
                try {
                    FileChooser fileChooser = new FileChooser();
                    fileChooser.setTitle("Selecione um arquivo TXT");
                    fileChooser.getExtensionFilters().add(
                            new FileChooser.ExtensionFilter("Arquivos de texto", "*.txt"));

                    arquivoSelecionado[0] = fileChooser.showOpenDialog(null);
                } finally {
                    latch.countDown();
                }
            });

            try {
                if (!latch.await(30, TimeUnit.SECONDS)) {
                    System.out.println("Timeout ao selecionar arquivo");
                    scanner.close();
                    return;
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.out.println("Operação interrompida");
                scanner.close();
                return;
            }

            if (arquivoSelecionado[0] != null) {
                File arquivoEntrada = arquivoSelecionado[0];
                System.out.println("Arquivo selecionado: " + arquivoEntrada.getAbsolutePath());

                try {
                    BufferedReader br = new BufferedReader(new FileReader(arquivoEntrada));
                    StringBuilder conteudo = new StringBuilder();
                    String linha;
                    while ((linha = br.readLine()) != null) {
                        conteudo.append(linha);
                    }
                    br.close();

                    // Convertendo para array de inteiros
                    String[] partes = conteudo.toString().split(";");
                    int[] numeros = new int[partes.length];
                    for (int i = 0; i < partes.length; i++) {
                        numeros[i] = Integer.parseInt(partes[i].trim());
                    }

                    final int[] numerosOriginais = numeros.clone();
                    Runtime runtime = Runtime.getRuntime();
                    XYSeries bubbleSeries = new XYSeries("BubbleSort");
                    XYSeries heapSeries = new XYSeries("HeapSort");
                    XYSeries insertionSeries = new XYSeries("InsertionSeries");
                    XYSeries mergeSeries = new XYSeries("MergeSort");
                    XYSeries quickSeries = new XYSeries("QuickSort");
                    XYSeries selectionSeries = new XYSeries("SelectionSort");

                    int[] tempArray = numerosOriginais.clone();
                    long timeBubble = CalculoTempo.calcularTempo(runtime, tempArray, bubbleSeries, tempArray.length,
                            new Bubble());

                    tempArray = numerosOriginais.clone();
                    long timeHeap = CalculoTempo.calcularTempo(runtime, tempArray, heapSeries, tempArray.length,
                            new Heap());

                    tempArray = numerosOriginais.clone();
                    long timeInsertion = CalculoTempo.calcularTempo(runtime, tempArray, insertionSeries,
                            tempArray.length, new Insertion());

                    tempArray = numerosOriginais.clone();
                    long timeMerge = CalculoTempo.calcularTempo(runtime, tempArray, mergeSeries, tempArray.length,
                            new Merge());

                    tempArray = numerosOriginais.clone();
                    long timeQuick = CalculoTempo.calcularTempo(runtime, tempArray, quickSeries, tempArray.length,
                            new Quick());

                    tempArray = numerosOriginais.clone();
                    long timeSelection = CalculoTempo.calcularTempo(runtime, tempArray, selectionSeries,
                            tempArray.length, new Selection());

                    // Mantém o array ordenado para salvar depois
                    final int[] numeros2 = tempArray;

                    // Cria o vetor de objetos com os tempos
                    SortTime[] timeAlgoritimos = new SortTime[] {
                            new SortTime("BubbleSort", timeBubble),
                            new SortTime("HeapSort", timeHeap),
                            new SortTime("InsertionSort", timeInsertion),
                            new SortTime("MergeSort", timeMerge),
                            new SortTime("QuickSort", timeQuick),
                            new SortTime("SelectionSort", timeSelection)
                    };

                    DefaultCategoryDataset dataset = new DefaultCategoryDataset();
                    dataset.addValue(timeBubble, "Tempo (ns)", "BubbleSort");
                    dataset.addValue(timeHeap, "Tempo (ns)", "HeapSort");
                    dataset.addValue(timeInsertion, "Tempo (ns)", "InsertionSort");
                    dataset.addValue(timeMerge, "Tempo (ns)", "MergeSort");
                    dataset.addValue(timeQuick, "Tempo (ns)", "QuickSort");
                    dataset.addValue(timeSelection, "Tempo (ns)", "SelectionSort"); 

                    JFreeChart chart = SortComparisonChart.createChart(dataset, "Arquivo Generico",
                            "Algoritmo", "Tempo (ns)");

                    BufferedImage bufferedImage = chart.createBufferedImage(500, 400);
                    Image chartImage = Image.getInstance(bufferedImage, null);

                    SortReportPDF.documentoAnalise(timeAlgoritimos, numeros.length, chartImage);

                    CountDownLatch saveLatch = new CountDownLatch(1);
                    final boolean[] salvouArquivo = { false };

                    Platform.runLater(() -> {
                        try {
                            FileChooser fileChooser = new FileChooser();
                            fileChooser.setTitle("Salvar arquivo ordenado");
                            fileChooser.getExtensionFilters().add(
                                    new FileChooser.ExtensionFilter("Arquivos de texto", "*.txt"));
                            fileChooser.setInitialFileName("dadosordenados.txt");

                            File arquivoSaida = fileChooser.showSaveDialog(null);

                            if (arquivoSaida != null) {
                                if (!arquivoSaida.getName().toLowerCase().endsWith(".txt")) {
                                    arquivoSaida = new File(arquivoSaida.getAbsolutePath() + ".txt");
                                }

                                try (BufferedWriter bw = new BufferedWriter(new FileWriter(arquivoSaida))) {
                                    // Monta a string para ser salva
                                    for (int i = 0; i < numeros2.length; i++) {
                                        bw.write(String.valueOf(numeros2[i]));
                                        // Adiciona o separador ";" exceto para o último número
                                        if (i < numeros2.length - 1) {
                                            bw.write(";");
                                        }
                                    }
                                    salvouArquivo[0] = true;
                                    System.out
                                            .println("Arquivo salvo com sucesso em: " + arquivoSaida.getAbsolutePath());
                                } catch (IOException e) {
                                    System.err.println("Erro ao salvar o arquivo: " + e.getMessage());
                                    e.printStackTrace();
                                }
                            } else {
                                System.out.println("Operação de salvamento cancelada pelo usuário.");
                            }
                        } finally {
                            saveLatch.countDown();
                        }
                    });

                    try {
                        if (!saveLatch.await(30, TimeUnit.SECONDS)) {
                            System.out.println("Timeout ao salvar arquivo");
                            Platform.exit();
                            return;
                        }
                        // Encerra o JavaFX após salvar
                        Platform.exit();
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        System.out.println("Operação de salvamento interrompida");
                        Platform.exit();
                        return;
                    }

                } catch (Exception e) {
                    System.err.println("Erro ao processar o arquivo: " + e.getMessage());
                    e.printStackTrace();
                }
            } else {
                System.out.println("Nenhum arquivo selecionado.");
            }
        } else {
            SortReportPDF.testePerformance();
            Platform.exit();
        }

        scanner.close();
    }
}