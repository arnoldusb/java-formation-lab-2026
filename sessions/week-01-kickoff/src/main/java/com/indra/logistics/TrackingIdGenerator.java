package com.indra.logistics;

import java.security.SecureRandom;

public class TrackingIdGenerator {
    private static final String ID_FORMAT = "%s-%s-%s";
    private SecureRandom random = new SecureRandom();


    /**
     * Genera un ID de seguimiento con formato ORIG-DEST-XXXXXXXX
     * @param origin  código de origen (ej: "BOG")
     * @param destination código de destino (ej: "MED")
     * @return ID único de seguimiento
     */
    public String generate(String origin, String destination) {
        isValidCode(origin,"Origen");
        isValidCode(destination,"Destino");
        // TODO: implementar la generación del ID de seguimiento
        return String.format(ID_FORMAT, origin, destination, generarAlfanumerico(8));
    }

    private void isValidCode(String code, String fieldName) {
        if (code == null || code.trim().isEmpty() ) {
            throw new IllegalArgumentException("Código inválido para " + fieldName + ": " + code);
        }
    }

     public String generarAlfanumerico(int longitud) {
        String caracteres = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder sb = new StringBuilder(longitud);
        
        for (int i = 0; i < longitud; i++) {
            int indice = random.nextInt(caracteres.length());
            sb.append(caracteres.charAt(indice));
        }
        
        return sb.toString();
    }
}