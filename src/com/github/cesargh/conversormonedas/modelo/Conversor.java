package com.github.cesargh.conversormonedas.modelo;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.security.InvalidParameterException;
import java.util.Map;

public class Conversor {

    public enum Proveedor {
        CURRENCY_FREAKS("Currency Freaks"),
        EXCHANGE_RATE("Exchange Rate"),
        OPEN_EXCHANGE_RATES("Open Exchange Rates");

        public final String nombre;

        private Proveedor(String nombre) {
            this.nombre = nombre;
        }
    };

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
            default: // EXCHANGE_RATE
                targetURL = "https://v6.exchangerate-api.com/v6/df62716ae66999f36f365ffb/latest/USD";
                fieldName = "conversion_rates";
                fieldPolicy = FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES;
                break;
        }
        return ObtenerCoeficientes(targetURL, fieldName, fieldPolicy);
    }

    public ConversorResultado Convertir(Proveedor proveedor, String monedaOrigen, String monedaDestino, double importe) {
        Map coeficientes = ObtenerCoeficientes(proveedor);
        var monOrigen = coeficientes.get(monedaOrigen);
        var monDestino = coeficientes.get(monedaDestino);
        if (monOrigen == null && monDestino == null) {
            throw new ConversorException("Las monedas no existen.", new InvalidParameterException());
        } else if(monOrigen == null) {
            throw new ConversorException("Las moneda de origen no existe.", new InvalidParameterException());
        } else if(monDestino == null) {
            throw new ConversorException("Las moneda de destino no existe.", new InvalidParameterException());
        } else {
            try {
                double coeficienteOrigen = Double.valueOf(monOrigen.toString());
                double coeficienteDestino = Double.valueOf(monDestino.toString());
                double importeConvertido = importe * coeficienteDestino / coeficienteOrigen;
                return new ConversorResultado(proveedor, monedaOrigen, importe, monedaDestino, importeConvertido);
            } catch (Exception e) {
                throw new ConversorException("Error convirtiendo importe.", e);
            }
        }
    }

}
