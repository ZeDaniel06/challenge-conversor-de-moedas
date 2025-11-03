package br.com.zedaniel.conversormoedas.principal;

import br.com.zedaniel.conversormoedas.modelos.Conversao;
import br.com.zedaniel.conversormoedas.modelos.Conversor;
import br.com.zedaniel.conversormoedas.modelos.Historico;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws IOException {

        Conversor conversor = new Conversor();
        conversor.inicializar();
        conversor.menuGeral();

    }
}
