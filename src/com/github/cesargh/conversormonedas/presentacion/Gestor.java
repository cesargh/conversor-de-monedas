package com.github.cesargh.conversormonedas.presentacion;

import com.github.cesargh.conversormonedas.modelo.Conversor;

import java.security.InvalidParameterException;
import java.util.Scanner;

public class Gestor {

    //region [Category: Registros}

    private record ParMonedas(String origen, String destino) {}

    //endregion [Category: Registros}

    //region [Category: Variables}

    private Scanner scanner;
    private Presentador presentador;

    //endregion [Category: Variables}

    //region [Category: Constructores}

    public Gestor() {
        scanner = new Scanner(System.in);
        presentador = new Presentador();
    }

    //endregion [Category: Constructores}

    //region [Category: Métodos}

    private void Convertir() {
        presentador.Imprimir();
        presentador.ImprimirTitulo("Convertir importe");

        double impOrigen = IngresarImporte();
        ParMonedas parMon = IngresarMonedas();

        Exception excep = null;
        double impCF = 0;
        double impER = 0;
        double impOE = 0;

        try {
            Conversor conversor = new Conversor();
            impCF = conversor.Convertir(Conversor.Proveedor.CURRENCY_FREAKS, parMon.origen(), parMon.destino(), impOrigen);
            impER = conversor.Convertir(Conversor.Proveedor.EXCHANGE_RATE, parMon.origen(), parMon.destino(), impOrigen);
            impOE = conversor.Convertir(Conversor.Proveedor.OPEN_EXCHANGE_RATES, parMon.origen(), parMon.destino(), impOrigen);
        } catch (Exception e) {
            excep = e;
        } finally {
            if (excep == null) {
                presentador.ImprimirResultado("----------- Resultado -----------");
                presentador.ImprimirResultado(impCF, Conversor.Proveedor.CURRENCY_FREAKS.nombre);
                presentador.ImprimirResultado(impER, Conversor.Proveedor.EXCHANGE_RATE.nombre);
                presentador.ImprimirResultado(impOE, Conversor.Proveedor.OPEN_EXCHANGE_RATES.nombre);
                presentador.ImprimirResultado("---------------------------------");
            } else {
                presentador.ImprimirError("----------- Resultado -----------");
                presentador.ImprimirError(excep);
                presentador.ImprimirError("---------------------------------");
            }

        }

    }

    private boolean Finalizar(String opcion) {
        if (opcion.compareToIgnoreCase("F") == 0) {
            presentador.Imprimir();
            presentador.Imprimir("Adios, y...");
            presentador.ImprimirFrase("No gastes tu dinero antes de ganarlo!");
            presentador.ImprimirFrase("(Thomas Jefferson)");
            return true;
        } else {
            return false;
        }
    }

    private double IngresarImporte() {
        presentador.Imprimir("---------- Paso 1 de 2 ----------");
        presentador.Imprimir("Ingrese el importe a convertir.");
        presentador.ImprimirPrompt("Por ejemplo: 1500");
        double importe = 0;
        String entrada = "";
        while (scanner.hasNext()) {
            entrada = scanner.next();
            try {
                importe = Double.valueOf(entrada);
                if (importe <= 0) {
                    throw new InvalidParameterException("El importe debe ser positivo.");
                } else {
                    break;
                }
            } catch (Exception e) {
                presentador.ImprimirError("Importe inválido!");
                presentador.ImprimirPrompt("Reintente");
            }
        }
        return importe;
    }

    private ParMonedas IngresarMonedas() {
        presentador.Imprimir("---------- Paso 2 de 2 ----------");
        presentador.Imprimir("Ingrese el par de monedas.");
        presentador.ImprimirMenu("ARS"," : Peso (Argentina)");
        presentador.ImprimirMenu("BRL"," : Real (Brasil)");
        presentador.ImprimirMenu("CNY"," : Yuan Renminbi (China)");
        presentador.ImprimirMenu("EUR"," : Euro (Unión Europea)");
        presentador.ImprimirMenu("GBP"," : Libra Esterlina (RU)");
        presentador.ImprimirMenu("MXN"," : Peso (México)");
        presentador.ImprimirMenu("USD"," : Dólar (EEUU)");
        presentador.Imprimir("...o cualquier otra moneda!");
        presentador.Imprimir("Por ejemplo, para convertir");
        presentador.Imprimir("euros en libras esterlinas");
        presentador.ImprimirPrompt("sería así: EUR-GBP");
        String monOrigen = "";
        String monDestino = "";
        String entrada = "";
        while (scanner.hasNext()) {
            entrada = scanner.next();
            try {
                String[] ent = entrada.split("-");
                monOrigen = ent[0].toUpperCase().trim();
                monDestino = ent[1].toUpperCase().trim();
                if (monOrigen.isEmpty() || monDestino.isEmpty()) {
                    throw new InvalidParameterException("No es un par de monedas.");
                } else {
                    break;
                }
            } catch (Exception e) {
                presentador.ImprimirError("Par de monedas inválido!");
                presentador.ImprimirPrompt("Reintente");
            }
        }
        return new ParMonedas(monOrigen, monDestino);
    }

    private void VerHistorial() {
        // TODO: Ver historial
        presentador.Imprimir();
        presentador.ImprimirTitulo("Ver historial");
        presentador.Imprimir("En desarrollo...");
    }

    private void VerMenu() {
        presentador.Imprimir();
        presentador.ImprimirTitulo("Menú de opciones");
        presentador.ImprimirMenu("C"," : Convertir importe");
        presentador.ImprimirMenu("H"," : Ver historial");
        presentador.ImprimirMenu("F"," : Finalizar");
        presentador.ImprimirPrompt("Ingrese opción");
    }

    public void Ejecutar() {
        presentador.ImprimirTitulo("CONVERSOR DE MONEDAS");
        VerMenu();
        String opcion;
        while (scanner.hasNext()) {
            opcion = scanner.next();
            if (Finalizar(opcion)) {
                break;
            } else {
                try {
                    if (opcion.compareToIgnoreCase("C") == 0) {
                        Convertir();
                    } else if (opcion.compareToIgnoreCase("H") == 0) {
                        VerHistorial();
                    } else {
                        presentador.ImprimirError("Opción inválida. Reintente!");
                    }
                } catch (Exception e) {
                    presentador.ImprimirError(e);
                } finally {
                    VerMenu();
                }
            }
        }
    }

    //endregion [Category: Métodos}

}
