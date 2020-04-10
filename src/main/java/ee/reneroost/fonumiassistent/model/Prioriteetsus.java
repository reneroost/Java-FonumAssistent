package main.java.ee.reneroost.fonumiassistent.model;

import java.util.List;

public class Prioriteetsus {
    String tootja;
    List<String> esindustePrioriteetsusJarjestus;

    public Prioriteetsus(String tootja, List<String> esindustePrioriteetsusJarjestus) {
        this.tootja = tootja;
        this.esindustePrioriteetsusJarjestus = esindustePrioriteetsusJarjestus;
    }

    public String getTootja() {
        return tootja;
    }

    public String getEsindus(int positsioon) {
        return esindustePrioriteetsusJarjestus.get(positsioon);
    }
}