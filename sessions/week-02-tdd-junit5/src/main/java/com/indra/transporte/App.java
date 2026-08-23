package com.indra.transporte;

import java.util.ArrayList;
import java.util.List;

import com.indra.transporte.model.Bus;
import com.indra.transporte.model.Ruta;

public class App {
    public static void main(String[] args) {
        System.out.println("Test Driven Development with JUnit 5");

        List<Bus> buses = new ArrayList<>();
        Bus bus = new Bus("ABC123", "Autobus");
        Bus bus2 = new Bus("XYZ789", "Minibus");
        Bus bus3 = new Bus("LMN456", "Autobus");
        buses.add(bus);
        buses.add(bus2);
        buses.add(bus3);

        List<Ruta> rutas = new ArrayList<>();
        Ruta ruta = new Ruta("R001", "Ciudad A", "Ciudad B");
        Ruta ruta2 = new Ruta("R002", "Ciudad C", "Ciudad D");
        Ruta ruta3 = new Ruta("R003", "Ciudad E", "Ciudad F");
        rutas.add(ruta);
        rutas.add(ruta2);
        rutas.add(ruta3);

        // Imprimir los buses usando stream
        System.out.println("Buses:");
        buses.stream().forEach(b -> System.out.println(b));

        // Imprimir las rutas usando stream
        System.out.println("Rutas:");
        rutas.stream().forEach(r -> System.out.println(r));

    }
}
