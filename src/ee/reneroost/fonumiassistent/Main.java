package ee.reneroost.fonumiassistent;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.stream.IntStream;

public class Main {

    private static int esindusteHulk;
    private static List<Esindus> esindused = new ArrayList<>();
    private static List<List<Integer>> reeglid = new ArrayList<>();

    public static void main(String[] args) {
        loeEsindusedFailist();
        loeReeglidFailist();
//        kuvaMenuu();
//        int valik = loeMenuuValik(4);



        Scanner klaviatuur = new Scanner(System.in);
        int[] praeguneJaotus = new int[esindusteHulk];
        System.out.println("Sisesta praegune varuosa jaotus");
        System.out.print("Kristiines: ");
        praeguneJaotus[0] = klaviatuur.nextInt();
        System.out.print("T1-s: ");
        praeguneJaotus[1] = klaviatuur.nextInt();
        System.out.print("Lõunakeskuses: ");
        praeguneJaotus[2] = klaviatuur.nextInt();
        //int[] praeguneJaotus = new int[]{3, 0, 3};
        int[][] jaotusMaatriks = arvutaJaotusMaatriks(praeguneJaotus);
        System.out.println();
        kuvaJaotusJuhised(jaotusMaatriks);
        System.out.println();
        kuvaJaotusMaatriks(jaotusMaatriks);
        System.out.println();
        kuvaMuutusteTabel(praeguneJaotus, jaotusMaatriks);
    }

    private static int loeMenuuValik(int valikuteHulk) {
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
        } while(valik < 1 || valik > valikuteHulk);
        return valik;
    }

    private static void kuvaMenuu() {
        System.out.println("1. loe laoseis failist (laoseis.csv)");
        System.out.println("2. loe laoseis failist (sisesta failinimi)");
        System.out.println("3. sisesta laoseis käsitsi");
        System.out.println("4. kuva esindused");
    }

    private static void kuvaJaotusJuhised(int[][] jaotusMaatriks) {
        for (int i = 0; i < jaotusMaatriks.length; i++) {
            for (int j = 0; j < jaotusMaatriks[i].length; j++) {
                if (jaotusMaatriks[i][j] > 0) {
                    System.out.println("Saata " + jaotusMaatriks[i][j] + " varuosa " +
                            esindused.get(i).getNimiSeestytlev() + " " +
                            esindused.get(j).getNimiSisseytlev() + ".");
                }
            }
        }
    }

    private static void kuvaJaotusMaatriks(int[][] jaotusMaatriks) {
        System.out.print(String.format("%-15s", ""));
        for (Esindus esindus: esindused) {
            System.out.print(String.format("%-15s", esindus.getNimiSisseytlev()));
        }
        System.out.println();
        for (int i = 0; i < jaotusMaatriks.length; i++) {
            System.out.print(String.format("%-15s", esindused.get(i).getNimiSeestytlev()));
            for(int element: jaotusMaatriks[i]) {
                System.out.print(String.format("%-15s", element));
            }
            System.out.println();
        }
    }

    private static void kuvaMuutusteTabel(int[] praeguneJaotus, int[][] jaotusMaatriks) {
        System.out.println(String.format("%-15s%-15s%-15s%-15s", " ", "Hetkeseis", "Uus seis", "Muutus"));
        int muutus, uusSeis;
        for (int i = 0; i < jaotusMaatriks.length; i++) {
            uusSeis = praeguneJaotus[i];
            for (int j = 0; j < jaotusMaatriks[i].length; j++) {
                uusSeis -= jaotusMaatriks[i][j];
            }
            muutus = uusSeis - praeguneJaotus[i];
            System.out.println(String.format("%-15s%-15s%-15s%-15s",
                    esindused.get(i).getNimi(),
                    praeguneJaotus[i],
                    uusSeis,
                    muutus));
        }
    }

    private static void kuvaEsindused() {
        System.out.println("Kokku on " + esindused.size() + " esindust:");
        System.out.println(String.format("%-15s%-15s", "Esindus", "Linn"));
        for (Esindus esindus : esindused) {
            System.out.println(String.format("%-15s%-15s", esindus.getNimi(), esindus.getAadress().getLinn()));
        }
    }

    private static int[][] arvutaJaotusMaatriks(int[] praeguneJaotus) {
        int varuosadeHulk = IntStream.of(praeguneJaotus).sum();
        int[] erinevus = new int[esindusteHulk];
        int[][] saatmisMaatriks = new int[esindusteHulk][esindusteHulk];
        for (int i = 0; i < esindusteHulk; i++) {
            erinevus[i] = praeguneJaotus[i] - reeglid.get(varuosadeHulk).get(i);
        }
        while (!(erinevus[0] == 0 && erinevus[1] == 0 && erinevus[2] == 0)) {
            // i == puudu, j == üle;
            for (int i = 0; i < esindusteHulk; i++) {
                if (erinevus[i] < 0) {
                    for (int j = 0; j < esindusteHulk; j++) {
                        if (erinevus[j] > 0) {
                            if (Math.abs(erinevus[i]) == Math.abs(erinevus[j])) {
                                saatmisMaatriks[i][j] = erinevus[i];
                                saatmisMaatriks[j][i] = erinevus[j];
                                erinevus[i] = erinevus[j] = 0;
                            } else if (Math.abs(erinevus[i]) > Math.abs(erinevus[j])) {
                                saatmisMaatriks[i][j] = -(erinevus[j]);
                                saatmisMaatriks[j][i] = erinevus[j];
                                erinevus[i] += erinevus[j];
                                erinevus[j] = 0;
                            } else if (Math.abs(erinevus[i]) < Math.abs(erinevus[j])) {
                                saatmisMaatriks[i][j] = erinevus[i];
                                saatmisMaatriks[j][i] = -(erinevus[i]);
                                erinevus[j] += erinevus[i];
                                erinevus[i] = 0;
                            }
                        }
                    }
                }
            }
        }
        return saatmisMaatriks;
    }

    private static void loeReeglidFailist() {
        String jaotusreeglidFail = "C:\\Users\\roost\\Desktop\\Fonumi assistent\\jaotusreeglid.csv";
        BufferedReader br = null;
        String rida, csvEraldaja = ",";

        try {
            br = new BufferedReader(new FileReader(jaotusreeglidFail));
            while((rida = br.readLine()) != null) {
                String[] reegelString = rida.split(csvEraldaja);
                int[] reegelArv = Arrays.stream(reegelString).mapToInt(Integer::parseInt).toArray();
                reeglid.add(Arrays.asList(reegelArv[0], reegelArv[1], reegelArv[2]));
            }
        } catch (FileNotFoundException e) {
            System.out.println("Faili ei leitud. Stack trace:");
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("Rida ei õnnestunud lugeda. Stack trace:");
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static void loeEsindusedFailist() {
        String esindusedFail = "C:\\Users\\roost\\Desktop\\Fonumi assistent\\esindused.csv";
        BufferedReader br = null;
        String rida, csvEraldaja = ",";
        Esindus esindus;
        Aadress aadress;

        try {
            br = new BufferedReader(new FileReader(esindusedFail));
            while ((rida = br.readLine()) != null) {
                String[] esindusRida = rida.split(csvEraldaja);
                aadress = new Aadress(esindusRida[4], esindusRida[5], esindusRida[6], esindusRida[7]);
                esindus = new Esindus(esindusRida[0], esindusRida[1], esindusRida[2], esindusRida[3], aadress);
                esindused.add(esindus);
            }
        } catch (FileNotFoundException e) {
            System.out.println("Faili ei leitud. Stack trace:");
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("Rida ei õnnestunud lugeda. Stack trace:");
            e.printStackTrace();
        } finally {
            esindusteHulk = esindused.size();
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}