package com.github.cesargh.conversormonedas.modelo;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.Map;

public class Conversor {

    public enum Proveedor {INDISTINTO, CURRENCY_FREAKS, EXCHANGE_RATE, OPEN_EXCHANGE_RATES};

    public Conversor() {
    }

    private Map ObtenerCoeficientes(String targetURL, String fieldName, FieldNamingPolicy fieldPolicy) {
        try {
            String rootJSON = Buscador.RequerirJSON(targetURL);
            Gson gsonTool = new GsonBuilder()
                    .setFieldNamingPolicy(fieldPolicy)
                    .setPrettyPrinting()
                    .create();
            Map rootMap = gsonTool.fromJson(rootJSON, Map.class);
            String ratesJSON = rootMap.get(fieldName).toString();
            return gsonTool.fromJson(ratesJSON, Map.class);
        } catch (Exception e) {
            throw new ConversorException("Error obteniendo coeficientes.", e);
        }

    }

    private Map ObtenerCoeficientes(Proveedor proveedor) {
        String targetURL;
        String fieldName;
        FieldNamingPolicy fieldPolicy;
        switch (proveedor) {
            case CURRENCY_FREAKS:
                targetURL = "https://api.currencyfreaks.com/v2.0/rates/latest?apikey=d543d07b05a04df7bc1a1a1e3309b643";
                fieldName = "rates";
                fieldPolicy = FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES;
                break;
            case OPEN_EXCHANGE_RATES:
                targetURL = "https://openexchangerates.org/api/latest.json?app_id=2c58e81634664e72b6adbffc18e57f30";
                fieldName = "rates";
                fieldPolicy = FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES;
                break;
            default: // EXCHANGE_RATE o INDISTINTO
                targetURL = "https://v6.exchangerate-api.com/v6/df62716ae66999f36f365ffb/latest/USD";
                fieldName = "conversion_rates";
                fieldPolicy = FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES;
                break;
        }
        return ObtenerCoeficientes(targetURL, fieldName, fieldPolicy);
    }

    public double Convertir(Proveedor proveedor, String monedaOrigen, String monedaDestino, double importe) {
        Map coeficientes = ObtenerCoeficientes(proveedor);
        try {
            double coeficienteOrigen = Double.valueOf(coeficientes.get(monedaOrigen).toString());
            double coeficienteDestino = Double.valueOf(coeficientes.get(monedaDestino).toString());
            return importe * coeficienteDestino / coeficienteOrigen;
        } catch (Exception e) {
            throw new ConversorException("Error convirtiendo importe.", e);
        }
    }

}
