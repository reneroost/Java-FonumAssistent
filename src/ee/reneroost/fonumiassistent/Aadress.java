package ee.reneroost.fonumiassistent;

public class Aadress {

    private String tanav;
    private String postiindeks;
    private String linn;
    private String riik;

    public Aadress(String tanav, String postiindeks, String linn, String riik) {
        this.tanav = tanav;
        this.postiindeks = postiindeks;
        this.linn = linn;
        this.riik = riik;
    }

    public String getTanav() {
        return tanav;
    }

    public void setTanav(String tanav) {
        this.tanav = tanav;
    }

    public String getPostiindeks() {
        return postiindeks;
    }

    public void setPostiindeks(String postiindeks) {
        this.postiindeks = postiindeks;
    }

    public String getLinn() {
        return linn;
    }

    public void setLinn(String linn) {
        this.linn = linn;
    }

    public String getRiik() {
        return riik;
    }

    public void setRiik(String riik) {
        this.riik = riik;
    }
}
