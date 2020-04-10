package ee.reneroost.fonumiassistent.model;

public class Esindus {
    private String nimi;
    private int prioriteet;
    private String nimiSisseytlev;
    private String nimiSeesytlev;
    private String nimiSeestytlev;
    private Aadress aadress;

    public Esindus(
        String nimi,
        int prioriteet,
        String nimiSisseytlev,
        String nimiSeesytlev,
        String nimiSeestytlev,
        Aadress aadress) {
        this.nimi = nimi;
        this.prioriteet = prioriteet;
        this.nimiSisseytlev = nimiSisseytlev;
        this.nimiSeesytlev = nimiSeesytlev;
        this.nimiSeestytlev = nimiSeestytlev;
        this.aadress = aadress;
    }

    public String getNimi() {
        return nimi;
    }

    public void setNimi(String nimi) {
        this.nimi = nimi;
    }

    public String getNimiSisseytlev() {
        return nimiSisseytlev;
    }

    public void setNimiSisseytlev(String nimiSisseytlev) {
        this.nimiSisseytlev = nimiSisseytlev;
    }

    public String getNimiSeesytlev() {
        return nimiSeesytlev;
    }

    public void setNimiSeesytlev(String nimiSeesytlev) {
        this.nimiSeesytlev = nimiSeesytlev;
    }

    public String getNimiSeestytlev() {
        return nimiSeestytlev;
    }

    public void setNimiSeestytlev(String nimiSeestytlev) {
        this.nimiSeestytlev = nimiSeestytlev;
    }

    public Aadress getAadress() {
        return aadress;
    }

    public void setAadress(Aadress aadress) {
        this.aadress = aadress;
    }
}
