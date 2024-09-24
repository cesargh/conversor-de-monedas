package com.github.cesargh.conversormonedas.modelo;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ConversorResultado {

    private Date timeStamp;
    private Conversor.Proveedor proveedor;
    private String origenMoneda;
    private double origenImporte;
    private String destinoMoneda;
    private double destinoImporte;
    private String log;

    public ConversorResultado(Conversor.Proveedor proveedor,
                              String origenMoneda, double origenImporte,
                              String destinoMoneda, double destinoImporte) {
        this.timeStamp = new Date();
        this.proveedor = proveedor;
        this.origenMoneda = origenMoneda;
        this.origenImporte = origenImporte;
        this.destinoMoneda = destinoMoneda;
        this.destinoImporte = destinoImporte;
        this.log = String.format("[%s] %s %.4f = %s %.4f (%s)",
                    new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(timeStamp),
                    origenMoneda, origenImporte, destinoMoneda, destinoImporte, proveedor.nombre);
    }

    public Conversor.Proveedor getProveedor() {
        return proveedor;
    }

    public Date getTimeStamp() {
        return timeStamp;
    }

    public String getOrigenMoneda() {
        return origenMoneda;
    }

    public double getOrigenImporte() {
        return origenImporte;
    }

    public String getDestinoMoneda() {
        return destinoMoneda;
    }

    public double getDestinoImporte() {
        return destinoImporte;
    }

    public String getLog() {
        return log;
    }

    @Override
    public String toString() {
        return log;
    }

}
