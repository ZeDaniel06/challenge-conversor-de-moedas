package br.com.zedaniel.conversormoedas.modelos;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class Conversor {
    private String uriInicial;
    private String chaveApi;
    private String tipoConversao;
    private String moedaBase;
    private String moedaFinal;
    private double valorConversao;
    private List<String> listaMoedas;
    private Scanner scanner;
    private String uriFinal;
    private ArrayList<Historico> historico = new ArrayList<Historico>();
    private Gson gson;

    public Conversor(){
        uriInicial = "https://v6.exchangerate-api.com/v6/";
        chaveApi = "ca0cad4e9f37e46cb38616bc";
        tipoConversao = "/pair/";
        moedaBase = "";
        moedaFinal = "";
        valorConversao = 0;
        scanner = new Scanner(System.in);
        listaMoedas = new ArrayList<>();
        listaMoedas.add("ARS");
        listaMoedas.add("BOB");
        listaMoedas.add("BRL");
        listaMoedas.add("CLP");
        listaMoedas.add("COP");
        listaMoedas.add("USD");
        gson = new GsonBuilder().setPrettyPrinting()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .create();
    }

    public void menuGeral() throws IOException {
        int menu = 10;
        while (menu != 4){
            try{
                System.out.println("1 - Ver Histórico\n2 - Limpar Histórico\n3 - Fazer cotação\n" +
                        "4 - Sair");

                System.out.println("O que deseja fazer? Digite o número correspondente: ");
                menu = scanner.nextInt();
                if(menu == 1){
                    if(historico.size() == 0){
                        System.out.println("O Histórico está vazio...");
                        System.out.println("Pressione enter para continuar");
                        String bla = scanner.nextLine();
                    }else{
                        for(Historico elemento: historico){
                            System.out.println(elemento);
                        }
                        System.out.println("Pressione enter para continuar...");
                        scanner.nextLine();
                        String ble = scanner.nextLine();
                    }
                }else if(menu == 2){
                    limparHistorico();
                }else if(menu == 3){
                    String dados = coletarDados();
                    String response = realizarRequisicao(dados);
                    Conversao conversao = gerarObjeto(response);
                    imprimirObjeto(conversao);
                    Historico historico = new Historico(conversao, getValorConversao(), LocalDateTime.now().toString());
                    gerarArquivo(getHistorico());
                }else if(menu == 4){
                    System.out.println("Encerrando...");
                }else{
                    System.out.println("Opção inválida!");
                    menu = 10;
                }
            }catch(InputMismatchException e){
                System.out.println("Você deve digitar um número!");
                menu = 10;
            }
        }

    }

    public void limparHistorico() {
        historico = new ArrayList<>();

        File file = new File("historico.json");
        boolean haveDeleted = file.delete();
        if(haveDeleted){
            System.out.println("Histórico apagado com sucesso!");
        }else {
            System.out.println("Não há um histórico a ser apagado!");
        }
    }
    public String coletarDados(){
        this.moedaBase = menuMoeda("moeda base");
        this.moedaFinal = menuMoeda("moeda destino");
        this.valorConversao = menuDinheiro();
        this.uriFinal = this.uriInicial + this.chaveApi + this.tipoConversao + this.moedaBase + "/" +
                this.moedaFinal +  "/" + this.valorConversao;
        return uriFinal;
    }

    public void inicializar() throws IOException {
        try{
            Gson gson = new GsonBuilder().setPrettyPrinting()
                    .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                    .create();
            FileReader fileReader = new FileReader("historico.json");

            Type tipo = new TypeToken<ArrayList<Historico>>(){}.getType();

            historico = gson.fromJson(fileReader, tipo);
            fileReader.close();


        } catch (FileNotFoundException e) {
            System.out.println("---");
        }

    }

    public double menuDinheiro(){
        int menu = -1;
        while (menu < 0){
            try{
                System.out.println("Qual valor deseja converter? ");
                double valorConversao = scanner.nextDouble();
                if(valorConversao > 0){
                    return valorConversao;
                }else {
                    System.out.println("Você deve digitar um número positivo!");
                }
            }catch (InputMismatchException e){
                System.out.println("Você deve digitar apenas números!");
            }finally {
                scanner.nextLine();
            }
        }
        return 1;
    }

    public String menuMoeda(String mensagem){
        int menu = 9;
        while(menu != 0){
            System.out.println("""
                    1 - ARS - Peso argentino
                    2 - BOB - Boliviano boliviano
                    3 - BRL - Real brasileiro
                    4 - CLP - Peso chileno
                    5 - COP - Peso colombiano
                    6 - USD - Dólar americano
                    """);
            System.out.println("Escolha a " + mensagem + ": ");
            try{
                menu = scanner.nextInt();
                if(menu > 0 && menu < 7){
                    return listaMoedas.get(menu - 1);
                }else {
                    menu = 2;
                    System.out.println("Opção Inválida!");
                }
            }catch (InputMismatchException e){
                menu = 9;
                System.out.println("Você deve digitar apenas um número!");
            }finally {
                scanner.nextLine();
            }
        }
        return "";
    }

    public String realizarRequisicao(String dados){
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(dados))
                .GET()
                .build();

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println("Requisição realizada!");
            return response.body();
        } catch (Exception e) {
            System.out.println("erro");
        }
        return null;
    }

    public void imprimirObjeto(Conversao conversao){
        System.out.println("Moeda base: " + conversao.baseCode() +
                "\nMoeda destino: " + conversao.targetCode() +
                "\nTaxa de câmbio: " + conversao.conversionRate() +
                "\nValor base: " + valorConversao +
                "\nValor convertido: " + conversao.conversionResult());

    }

    public Conversao gerarObjeto(String responseBody){
        Gson gson = new GsonBuilder().setPrettyPrinting()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .create();

        if(responseBody != null){
            Conversao conversao = gson.fromJson(responseBody, Conversao.class);
            Historico historicoElement = new Historico(conversao, valorConversao, LocalDateTime.now().toString());
            historico.add(historicoElement);
            return conversao;
        }else{
            return null;
        }
    }



    public double getValorConversao() {
        return valorConversao;
    }

    public ArrayList<Historico> getHistorico() {
        return (ArrayList<Historico>) historico;
    }

    public void gerarArquivo(ArrayList<Historico> objeto) throws IOException {
        Gson gson = new GsonBuilder().setPrettyPrinting()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .create();

        FileWriter fileWriter = new FileWriter("historico.json");
        fileWriter.write(gson.toJson(objeto));
        fileWriter.close();

    }


}
