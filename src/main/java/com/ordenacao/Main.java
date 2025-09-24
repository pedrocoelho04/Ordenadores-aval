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

    System.out.print("Deseja inserir um arquivo? (S/N): ");
    String resposta = scanner.nextLine().trim().toUpperCase();

    if (resposta.equals("S")) {
      Frame frame = new Frame();
      FileDialog fd = new FileDialog(frame, "Selecione um arquivo TXT", FileDialog.LOAD);
      fd.setFile("*.txt");
      fd.setVisible(true);

      String diretorio = fd.getDirectory();
      String arquivo = fd.getFile();

      if (arquivo != null) {
        File arquivoEntrada = new File(diretorio, arquivo);
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

          // numeros = class.sort(numeros);
          // Quick.sort(numeros);

          // Pedir nome do arquivo de saída
          System.out.print("Digite o nome para o arquivo de saída (sem extensão): ");
          String nomeSaida = scanner.nextLine().trim();

          File arquivoSaida = new File(nomeSaida + ".txt");
          BufferedWriter bw = new BufferedWriter(new FileWriter(arquivoSaida));

          // Escrevendo os números separados por ";"
          for (int i = 0; i < numeros.length; i++) {
            bw.write(String.valueOf(numeros[i]));
            if (i < numeros.length - 1) {
              bw.write(";");
            }
          }

          bw.close();
          System.out.println("Arquivo de saída gerado: " + arquivoSaida.getAbsolutePath());

        } catch (Exception e) {
          System.err.println("Erro ao processar o arquivo: " + e.getMessage());
        }

      } else {
        System.out.println("Nenhum arquivo selecionado.");
      }

    } else {
      System.out.println("Encerrando o programa...");
    }

    scanner.close();
  }
}