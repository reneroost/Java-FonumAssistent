package ee.reneroost.fonumiassistent;

import java.util.List;

public class Varuosa {
    private String tootemark;
    private String mudel;
    private String varuosaLiik;
    private int omaHind;
    private int myygiHind;
    private List<Integer> jaotus;

    public Varuosa(
            String tootemark,
            String mudel,
            String varuosaLiik,
            int omaHind,
            int myygiHind,
            List<Integer> jaotus) {
        this.tootemark = tootemark;
        this.mudel = mudel;
        this.varuosaLiik = varuosaLiik;
        this.omaHind = omaHind;
        this.myygiHind = myygiHind;
        this.jaotus = jaotus;
    }

    public static String tabeliPealkiri() {
        return String.format("%-12s%-15s%-15s%-10s%-10s",
                "Mark", "Mudel", "Liik", "Omahind", "Müügihind");
    }

    public String tabeliRida() {
        return String.format("%-12s%-15s%-15s%-10s%-10s",
                this.tootemark, this.mudel, this.varuosaLiik, this.omaHind, this.myygiHind);
    }

    @Override
    public String toString() {
        return "Varuosa{" +
                "tootemark='" + tootemark + '\'' +
                ", mudel='" + mudel + '\'' +
                ", varuosaLiik='" + varuosaLiik + '\'' +
                ", omaHind=" + omaHind +
                ", myygiHind=" + myygiHind +
                ", jaotus=" + jaotus +
                '}';
    }
}
