package br.com.zedaniel.conversormoedas.modelos;

public record Conversao(String result, String baseCode, String targetCode,
                        String conversionRate, String conversionResult) {
}
