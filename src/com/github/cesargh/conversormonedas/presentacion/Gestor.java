package com.github.cesargh.conversormonedas.presentacion;

import com.github.cesargh.conversormonedas.modelo.Conversor;

public class Gestor {

    public Gestor() {
    }

    // TODO: Este método es temporal y sólo para realizar pruebas
    public void TemporalDebug() {

        Conversor conversor = new Conversor();
        double importe;

        importe = conversor.Convertir(Conversor.Proveedor.CURRENCY_FREAKS, "EUR", "ARS", 1);
        System.out.printf("Importe = %.2f ---> %s\n", importe, "CURRENCY_FREAKS");

        importe = conversor.Convertir(Conversor.Proveedor.EXCHANGE_RATE, "EUR", "ARS", 1);
        System.out.printf("Importe = %.2f ---> %s\n", importe, "EXCHANGE_RATE");

        importe = conversor.Convertir(Conversor.Proveedor.OPEN_EXCHANGE_RATES, "EUR", "ARS", 1);
        System.out.printf("Importe = %.2f ---> %s\n", importe, "OPEN_EXCHANGE_RATES");

    }
}
