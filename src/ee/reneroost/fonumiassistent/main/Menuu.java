package ee.reneroost.fonumiassistent.main;

import java.util.Scanner;

class Menuu {

    static void kuvaMenuu() {
        System.out.println("1. loe laoseis failist (laoseis.csv)");
        System.out.println("2. loe laoseis failist (sisesta failinimi)");
        System.out.println("3. sisesta laoseis käsitsi");
        System.out.println("4. kuva esindused");
        System.out.println("5. kuva laoseis");
        System.out.println("5.0.1 kuva loas olevad tootjad");
        System.out.println("5.0.2 kuva laos olevad varuosaliigid");
        System.out.println("5.1 kuva konkreetse tootja laoseis");
        System.out.println("5.2 kuva konkreetse varuosa liigi laoseis");
        System.out.println("5.3 kuva otsalõppevate varuosade laoseis");
    }

    static int loeMenuuValik(int valikuteHulk) {
        Scanner klaviatuur = new Scanner(System.in);
        String sisend = null;
        int valik = 0;
        do {
            try {
                System.out.print("Sisesta valik (nr 1-" + valikuteHulk + "): ");
                sisend = klaviatuur.next();
                valik = Integer.parseInt(sisend);
            } catch (NumberFormatException erind) {
                System.out.print("Pole arv! ");
            }
        } while (valik < 1 || valik > valikuteHulk);
        klaviatuur.close();
        return valik;
    }

}