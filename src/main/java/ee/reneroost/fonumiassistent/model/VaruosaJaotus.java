package main.java.ee.reneroost.fonumiassistent.model;

import java.util.List;

public class VaruosaJaotus {
    private String tootja;
    private String mudel;
    private String varuosaLiik;
    private double omaHind;
    private int myygiHind;
    private int kogus;
    private List<Integer> jaotus;

    public VaruosaJaotus(
            String tootemark,
            String mudel,
            String varuosaLiik,
            double omaHind,
            int myygiHind,
            int kogus,
            List<Integer> jaotus) {
        this.tootja = tootemark;
        this.mudel = mudel;
        this.varuosaLiik = varuosaLiik;
        this.omaHind = omaHind;
        this.myygiHind = myygiHind;
        this.kogus = kogus;
        this.jaotus = jaotus;
    }

    public String getTootja() {
        return tootja;
    }

    public void setTootja(String tootemark) {
        this.tootja = tootemark;
    }

    public String getMudel() {
        return mudel;
    }

    public String getVaruosaLiik() {
        return varuosaLiik;
    }

    public void setVaruosaLiik(String varuosaLiik) {
        this.varuosaLiik = varuosaLiik;
    }

    public int getKogus() {
        return kogus;
    }

    public List<Integer> getJaotus() {
        return jaotus;
    }

    public String tabeliPealkiri(List<Esindus> esindused) {
        String pealkiri = String.format("%-12s%-20s%-15s%-16s%-15s%-10s", 
        "Mark", "Mudel", "Varuosa liik", "Varuosa omahind", "Teenuse hind", "Kogus");
        for (int i = 0; i < jaotus.size(); i++) {
            pealkiri += String.format("%-12s", esindused.get(i).getNimi());
        }
        return pealkiri;        
    }

    public String tabeliRida() {
        String rida = String.format("%-12s%-20s%-15s%-16s%-15s%-10s",
        this.tootja, this.mudel, this.varuosaLiik, this.omaHind, this.myygiHind, this.kogus);
        for (Integer arv: jaotus) {
            rida += String.format("%-12s", arv);
        }
        return rida;    
    }
}
