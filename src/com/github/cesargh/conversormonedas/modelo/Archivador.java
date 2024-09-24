package com.github.cesargh.conversormonedas.modelo;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Archivador {

    private final String ARCHIVO_HISTORIAL = "historial.txt";

    public Archivador() {
    }

    public void EscribirHistorial(ArrayList<ConversorResultado> lista) throws IOException {
        StringBuilder sb = new StringBuilder();
        Collections.sort(lista, Comparator.comparing(ConversorResultado::getTimeStamp));
        for (ConversorResultado item : lista) {
            sb.append(item.getLog() + "\n");
        }
        Files.write(Paths.get(ARCHIVO_HISTORIAL), sb.toString().getBytes(),
                    StandardOpenOption.CREATE, StandardOpenOption.APPEND);
    }

    public List<String> LeerHistorial() throws IOException {
        if (Files.exists(Paths.get(ARCHIVO_HISTORIAL))) {
            return Files.readAllLines(Paths.get(ARCHIVO_HISTORIAL));
        } else {
            return null;
        }
    }

}
