
package com.ordenacao.pdfmaker;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import java.awt.image.BufferedImage;
import java.io.FileOutputStream;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

public class SortReportPDF {

    // Gera relatório de performance com tabelas e gráficos para cada grupo
    public static void gerarRelatorioPerformance(long[][][] tempos, int[] sizes, String[] algoritmos, String caminhoPDF) throws Exception {
        // DEBUG: Imprime tempos por grupo, algoritmo e tamanho
        for (int grupo = 0; grupo < 3; grupo++) {
            System.out.println("==== Grupo: " + grupo + " ====");
            for (int alg = 0; alg < algoritmos.length; alg++) {
                System.out.print(algoritmos[alg] + ": ");
                for (int i = 0; i < sizes.length; i++) {
                    System.out.print(tempos[grupo][i][alg] + " ms ");
                }
                System.out.println();
            }
        }
        // tempos[grupo][tamanho][algoritmo]
        // grupo 0: crescente, 1: decrescente, 2: aleatório
        // Criação do documento
        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream(caminhoPDF));
        document.open();
        Font titleFont = new Font(Font.FontFamily.HELVETICA, 16, Font.BOLD);
        String[] nomesGrupos = {"Crescente", "Decrescente", "Aleatório"};
        for (int grupo = 0; grupo < 3; grupo++) {
            document.add(new Paragraph("Teste de Performance - Ordem " + nomesGrupos[grupo], titleFont));
            document.add(new Paragraph(" "));
            // Tabela de resultados (tamanhos no topo, algoritmos na esquerda)
            PdfPTable table = new PdfPTable(1 + sizes.length);
            table.addCell(""); // canto superior esquerdo vazio
            for (int i = 0; i < sizes.length; i++) {
                table.addCell(String.valueOf(sizes[i]));
            }
            for (int alg = 0; alg < algoritmos.length; alg++) {
                table.addCell(algoritmos[alg]);
                for (int i = 0; i < sizes.length; i++) {
                    table.addCell(String.valueOf(tempos[grupo][i][alg]) + " ms");
                }
            }
            document.add(table);
            // Gráfico
            XYSeriesCollection dataset = new XYSeriesCollection();
            for (int alg = 0; alg < algoritmos.length; alg++) {
                XYSeries series = new XYSeries(algoritmos[alg]);
                for (int i = 0; i < sizes.length; i++) {
                    series.add(sizes[i], tempos[grupo][i][alg]);
                }
                dataset.addSeries(series);
            }
            JFreeChart chart = ChartFactory.createXYLineChart(
                "Performance - " + nomesGrupos[grupo],
                "Tamanho do vetor (n)",
                "Tempo (ms)",
                dataset, org.jfree.chart.plot.PlotOrientation.VERTICAL, false, false, false
            );
            BufferedImage bufferedImage = chart.createBufferedImage(500, 400);
            Image chartImage = Image.getInstance(bufferedImage, null);
            document.add(chartImage);
            // Legenda de cores dos algoritmos
            String[] cores = {"Azul (Blue)", "Vermelho (Red)", "Verde (Green)", "Laranja (Orange)", "Magenta (Magenta)", "Ciano (Cyan)"};
            StringBuilder legenda = new StringBuilder("Legenda das cores do gráfico:\n");
            for (int alg = 0; alg < algoritmos.length; alg++) {
                legenda.append(algoritmos[alg]).append(" - ").append(cores[alg % cores.length]).append("\n");
            }
            document.add(new Paragraph(legenda.toString()));
            document.add(new Paragraph(" "));
        }
        document.close();
    }
}