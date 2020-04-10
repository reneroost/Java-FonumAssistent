package ee.reneroost.fonumiassistent.main;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import main.java.ee.reneroost.fonumiassistent.model.Esindus;
import main.java.ee.reneroost.fonumiassistent.model.VaruosaJaotus;
import main.java.ee.reneroost.fonumiassistent.util.FailistLugemine;

public class Main {

    private static List<Esindus> esindused;
    private static List<List<Integer>> reeglid;
    private static List<VaruosaJaotus> varuosad;

    public static void main(String[] args) {
        esindused = FailistLugemine.loeEsindusteAndmedFailist();
        reeglid = FailistLugemine.loeReeglidFailist();
        varuosad = FailistLugemine.loeLaoseisFailist();
        kuvaPraeguneJaotus();
        
        for (VaruosaJaotus varuosa: varuosad) {
            int[][] jaotusmaatriks = arvutaJaotusMaatriks(varuosa.getJaotus());
            kuvaJaotusJuhised(varuosa, jaotusmaatriks);
        }


        // Scanner klaviatuur = new Scanner(System.in);
        // List<Integer> praeguneJaotus = new ArrayList<>();
        // System.out.println("Sisesta praegune varuosa jaotus");
        // System.out.print("Kristiines: ");
        // praeguneJaotus.add(klaviatuur.nextInt());
        // System.out.print("Lõunakeskuses: ");
        // praeguneJaotus.add(klaviatuur.nextInt());
        // System.out.print("T1-s: ");
        // praeguneJaotus.add(klaviatuur.nextInt());
        // int[][] jaotusMaatriks = arvutaJaotusMaatriks(praeguneJaotus);
        // System.out.println();
        // kuvaJaotusJuhised(varuosad.get(0), jaotusMaatriks);
        // System.out.println();
        // kuvaJaotusMaatriks(jaotusMaatriks);
        // System.out.println();
        // kuvaMuutusteTabel(praeguneJaotus, jaotusMaatriks);
        // klaviatuur.close();
    }

    private static void kuvaPraeguneJaotus() {
        System.out.println(varuosad.get(0).tabeliPealkiri(esindused));
        for (VaruosaJaotus varuosa: varuosad) {
            System.out.println(varuosa.tabeliRida());
        }
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

    private static void kuvaMuutusteTabel(List<Integer> praeguneJaotus, int[][] jaotusMaatriks) {
        System.out.println(String.format("%-15s%-15s%-15s%-15s", " ", "Hetkeseis", "Uus seis", "Muutus"));
        int muutus, uusSeis;
        for (int i = 0; i < jaotusMaatriks.length; i++) {
            uusSeis = praeguneJaotus.get(i);
            for (int j = 0; j < jaotusMaatriks[i].length; j++) {
                uusSeis -= jaotusMaatriks[i][j];
            }
            muutus = uusSeis - praeguneJaotus.get(i);
            System.out.println(String.format("%-15s%-15s%-15s%-15s",
                    esindused.get(i).getNimi(),
                    praeguneJaotus.get(i),
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


    public static List<String> loeTootjadListist() {
        List<String> tootjad = new ArrayList<>();
        for (VaruosaJaotus varuosa : varuosad) {
            if (!tootjad.contains(varuosa.getTootja())) {
                tootjad.add(varuosa.getTootja());
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