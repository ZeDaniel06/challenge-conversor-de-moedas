package br.com.zedaniel.conversormoedas.modelos;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.Locale;

public class Historico {
    private String result;
    private String baseCode;
    private String targetCode;
    private String conversionRate;
    private String conversionResult;
    private String data;
    private double valorConversao;

    public Historico(Conversao conversao, double valorConversao, String data){
        result = conversao.result();
        baseCode = conversao.baseCode();
        targetCode = conversao.targetCode();
        conversionRate = conversao.conversionRate();
        conversionResult = conversao.conversionResult();
        this.data = data;
        this.valorConversao = valorConversao;
    }



    @Override
    public String toString() {
        LocalDateTime dataLocal = LocalDateTime.parse(this.data);
        DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;
        String mes = dataLocal.getMonth().getDisplayName(TextStyle.FULL, Locale.of("pt","BR"));
        String retorno = "\n----------------------------------\n" +
                "Horário da Operação: " + dataLocal.getHour() + "h" +
                dataLocal.getMinute() + "m" + ((int) dataLocal.getSecond()) + "s"+
                "\nData da operação: " + dataLocal.getDayOfMonth() +
                " de " + mes + " de " + dataLocal.getYear() +"\nMoeda base: " + baseCode +
                "\nMoeda destino: " + targetCode +
                "\nTaxa de câmbio: " + conversionRate +
                "\nValor base: " +  + valorConversao +
                "\nValor convertido: " + conversionResult +
                "\n----------------------------------\n";

        return retorno;
    }
}
