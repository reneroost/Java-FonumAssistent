package ee.reneroost.fonumiassistent.main;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.stream.IntStream;

import ee.reneroost.fonumiassistent.model.Aadress;
import ee.reneroost.fonumiassistent.model.Esindus;
import ee.reneroost.fonumiassistent.model.VaruosaJaotus;

public class Main {

    private static String esindusedSisendFail = "esindused.csv";
    private static String jaotusReeglidSisendFail = "jaotusreeglid.csv";
    private static String varuosaJaotusedSisendFail = "laoseis.csv";
    private static List<Esindus> esindused = new ArrayList<>();
    private static List<List<Integer>> reeglid = new ArrayList<>();
    private static List<VaruosaJaotus> varuosad = new ArrayList<>();

    public static void main(String[] args) {
        loeEsindusedFailist();
        loeReeglidFailist();
        loeLaoseisFailist();
        kuvaPraeguneJaotus();
        
        for (VaruosaJaotus varuosa: varuosad) {
            int[][] jaotusmaatriks = arvutaJaotusMaatriks(varuosa.getJaotus());
            kuvaJaotusJuhised(varuosa, jaotusmaatriks);
        }


        // Scanner klaviatuur = new Scanner(System.in);
        // int[] praeguneJaotus = new int[esindusteHulk];
        // System.out.println("Sisesta praegune varuosa jaotus");
        // System.out.print("Kristiines: ");
        // praeguneJaotus[0] = klaviatuur.nextInt();
        // System.out.print("T1-s: ");
        // praeguneJaotus[1] = klaviatuur.nextInt();
        // System.out.print("Lõunakeskuses: ");
        // praeguneJaotus[2] = klaviatuur.nextInt();
        // //int[] praeguneJaotus = new int[]{3, 0, 3};
        // int[][] jaotusMaatriks = arvutaJaotusMaatriks(praeguneJaotus);
        // System.out.println();
        // kuvaJaotusJuhised(jaotusMaatriks);
        // System.out.println();
        // kuvaJaotusMaatriks(jaotusMaatriks);
        // System.out.println();
        // kuvaMuutusteTabel(praeguneJaotus, jaotusMaatriks);
    }

    private static void kuvaPraeguneJaotus() {
        System.out.println(varuosad.get(0).tabeliPealkiri());
        for (VaruosaJaotus varuosa: varuosad) {
            System.out.println(varuosa.tabeliRida());
        }
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
        System.out.println("5. kuva laoseis");
        System.out.println("5.0.1 kuva loas olevad tootjad");
        System.out.println("5.0.2 kuva laos olevad varuosaliigid");
        System.out.println("5.1 kuva konkreetse tootja laoseis");
        System.out.println("5.2 kuva konkreetse varuosa liigi laoseis");
        System.out.println("5.3 kuva otsalõppevate varuosade laoseis");
    }

    private static void kuvaJaotusJuhised(VaruosaJaotus varuosa, int[][] jaotusMaatriks) {
        for (int i = 0; i < jaotusMaatriks.length; i++) {
            for (int j = 0; j < jaotusMaatriks[i].length; j++) {
                if (jaotusMaatriks[i][j] > 0) {
                    System.out.println("Saata " + jaotusMaatriks[i][j] + " " +
                            varuosa.getMudel() + " " +
                            varuosa.getVaruosaLiik() + " " +
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
            for(int j = 0; j < jaotusMaatriks[i].length; j++) {
                if (i == j) {
                    System.out.print(String.format("%-15s", "---"));
                } else {
                    System.out.print(String.format("%-15s", jaotusMaatriks[i][j]));
                }
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

    private static int[][] arvutaJaotusMaatriks(List<Integer> praeguneJaotus) {
        int varuosadeHulk = praeguneJaotus.stream().mapToInt(Integer::intValue).sum();
        int esindusteHulk = esindused.size();
        int[] erinevus = new int[esindusteHulk];
        int[][] saatmisMaatriks = new int[esindusteHulk][esindusteHulk];
        for (int i = 0; i < esindusteHulk; i++) {
            erinevus[i] = praeguneJaotus.get(i) - reeglid.get(varuosadeHulk).get(i);
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

    private static int[][] arvutaJaotusMaatriksVana(int[] praeguneJaotus) {
        int varuosadeHulk = IntStream.of(praeguneJaotus).sum();
        int esindusteHulk = esindused.size();
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
        String jaotusreeglidFail = "C:\\Users\\roost\\Desktop\\Fonumi assistent\\" + jaotusReeglidSisendFail;
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
            System.out.println("Faili ei leitud.");
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
        String esindusedFail = "C:\\Users\\roost\\Desktop\\Fonumi assistent\\" + esindusedSisendFail;
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
            System.out.println("Faili ei leitud.");
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

    private static void loeLaoseisFailist() {
        String laoseisFail = "C:\\Users\\roost\\Desktop\\Fonumi assistent\\" + varuosaJaotusedSisendFail;
        BufferedReader br = null;
        String rida, csvEraldaja = ",";
        VaruosaJaotus varuosa;
        List<Integer> jaotus;

        try {
            br = new BufferedReader((new FileReader(laoseisFail)));
            while ((rida = br.readLine()) != null) {
                String[] varuosaRida = rida.split(csvEraldaja);
                jaotus = new ArrayList<>();
                // varuosa jaotus esinduste vahel algab rea 6. elemendiga
                for (int i = 6; i < varuosaRida.length; i++) {
                    jaotus.add(Integer.parseInt(varuosaRida[i]));
                }
                varuosa = new VaruosaJaotus(
                        varuosaRida[0],
                        varuosaRida[1],
                        varuosaRida[2],
                        Double.parseDouble(varuosaRida[3]),
                        Integer.parseInt(varuosaRida[4]),
                        Integer.parseInt(varuosaRida[5]),
                        jaotus);
                varuosad.add(varuosa);
            }
        } catch (FileNotFoundException erind) {
            System.out.println("Faili ei leitud.");
        } catch (IOException erind) {
            System.out.println("Rida ei õnnestunud lugeda. Stack trace");
            erind.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException erind) {
                    erind.printStackTrace();
                }
            }
        }
    }

    public static List<String> loeTootjadListist() {
        List<String> tootjad = new ArrayList<>();
        for (VaruosaJaotus varuosa : varuosad) {
            if (!tootjad.contains(varuosa.getTootemark())) {
                tootjad.add(varuosa.getTootemark());
            }
        }
        return tootjad;
    }

    public static void kuvaTootjad(List<String> tootjad) {
        for (String tootja : tootjad) {
            System.out.println(tootja);
        }
    }

    public static List<String> loeVaruosaLiikListist() {
        List<String> varuosaLiigid = new ArrayList<>();
        for (VaruosaJaotus varuosa : varuosad) {
            if (!varuosaLiigid.contains(varuosa.getVaruosaLiik())) {
                varuosaLiigid.add(varuosa.getVaruosaLiik());
            }
        }
        return varuosaLiigid;
    }

    public static void kuvaVaruosaLiigid(List<String> varuosaLiigid) {
        for (String varuosa : varuosaLiigid) {
            System.out.println(varuosa);
        }
    }

    private static void kuvaEsindused() {
        System.out.println("Kokku on " + esindused.size() + " esindust:");
        System.out.println(String.format("%-15s%-15s", "Esindus", "Linn"));
        for (Esindus esindus : esindused) {
            System.out.println(String.format("%-15s%-15s", esindus.getNimi(), esindus.getAadress().getLinn()));
        }
    }
}