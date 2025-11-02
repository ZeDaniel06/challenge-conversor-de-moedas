package br.com.zedaniel.conversormoedas.principal;

import br.com.zedaniel.conversormoedas.modelos.Conversao;
import br.com.zedaniel.conversormoedas.modelos.Conversor;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        Conversor conversor = new Conversor();
        String dados = conversor.coletarDados();
        String response = conversor.realizarRequisicao(dados);
        conversor.imprimirObjeto(response);

    }
}
