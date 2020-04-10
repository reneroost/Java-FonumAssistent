package main.java.ee.reneroost.fonumiassistent.peamine;

import java.util.ArrayList;
import java.util.List;

import main.java.ee.reneroost.fonumiassistent.model.Esindus;
import main.java.ee.reneroost.fonumiassistent.model.Prioriteetsus;
import main.java.ee.reneroost.fonumiassistent.model.VaruosaJaotus;
import main.java.ee.reneroost.fonumiassistent.util.FailistLugemine;

public class Main {

    private static List<Esindus> esindused;
    private static List<VaruosaJaotus> varuosad;
    private static List<Prioriteetsus> prioriteetsused;

    public static void main(String[] args) {
        esindused = FailistLugemine.loeEsindusteAndmedFailist();
        varuosad = FailistLugemine.loeLaoseisFailist();
        List<List<Integer>> reeglid = FailistLugemine.loeReeglidFailist();
        prioriteetsused = FailistLugemine.loePrioriteetsusedFailist();
        
        kuvaPraeguneJaotus();
        
        for (VaruosaJaotus varuosa: varuosad) {
            int[][] jaotusmaatriks = arvutaJaotusMaatriks(varuosa);
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

    private static int[] arvutaPrioriteetsus(VaruosaJaotus varuosaJaotus) {
        int esindusteHulk = esindused.size();
        int[] jarjekord = new int[esindusteHulk];
        String tootja = varuosaJaotus.getTootja();
        Prioriteetsus prioriteetsus = prioriteetsused.get(0);
        for (Prioriteetsus p: prioriteetsused) {
            if (p.getTootja().equals(tootja)) {
                prioriteetsus = p;
            }
        }
        for (int i = 0; i < esindusteHulk; i++) {
            for (int j = 0; j < esindusteHulk; j++) {
                if (esindused.get(i).getNimi().equals(prioriteetsus.getEsindus(j))) {
                    jarjekord[i] = j;
                }
            }
        }
        return jarjekord;
    }

    private static int[] arvutaKogused(int[] prioriteetsus, int kogus, int maxKogusMittePrioriteetsetel) {
        int esindusteHulk = prioriteetsus.length;
        int[] kogused = new int[esindusteHulk];
        int jareleJaanudKogus = kogus;
        if (kogus <= maxKogusMittePrioriteetsetel * esindusteHulk) {
            for (int i = 0; i < prioriteetsus.length; i++) {
                kogused[i] = kogus / esindusteHulk;
            }
            jareleJaanudKogus -= kogus / esindusteHulk * esindusteHulk;
            int prioriteet = minTaisarv(prioriteetsus);
            if (jareleJaanudKogus > 0) {
                kogused[taisarvuPositsioon(prioriteetsus, prioriteet)] += 1;
            }
            jareleJaanudKogus--;
            while (jareleJaanudKogus > 0) {
                prioriteet = minJargmineTaisarv(prioriteetsus, prioriteet);
                kogused[taisarvuPositsioon(prioriteetsus, prioriteet)] += 1;
                jareleJaanudKogus--;
            }
            // kogused[IntStream.range(0, prioriteetsus.length)
            // .filter(i -> (Arrays.stream(prioriteetsus).min().getAsInt()) == prioriteetsus[i])
            // .findFirst()
            // .orElse(-1)] += jareleJaanudKogus;
        } else {
            for (int i = 0; i < prioriteetsus.length; i++) {
                kogused[i] = maxKogusMittePrioriteetsetel;
            }
            jareleJaanudKogus -= maxKogusMittePrioriteetsetel * esindusteHulk;
            kogused[taisarvuPositsioon(prioriteetsus, minTaisarv(prioriteetsus))] += jareleJaanudKogus;
        }
        return kogused;
    }
    


    private static int[][] arvutaJaotusMaatriks(VaruosaJaotus varuosaJaotus) {
        int varuosadeHulk = varuosaJaotus.getJaotus().stream().mapToInt(Integer::intValue).sum();
        int esindusteHulk = esindused.size();
        int[] erinevus = new int[esindusteHulk];
        int[][] saatmisMaatriks = new int[esindusteHulk][esindusteHulk];
        int[] jaotus =  arvutaKogused(arvutaPrioriteetsus(varuosaJaotus), varuosadeHulk, 3);
        for (int i = 0; i < esindusteHulk; i++) {
            erinevus[i] = varuosaJaotus.getJaotus().get(i) - jaotus[i];
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

    private static int[][] arvutaJaotusMaatriksVana(List<Integer> praeguneJaotus, List<List<Integer>> reeglid) {
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

    
    private static int minTaisarv(int[] massiiv) {
        int vaikseim = massiiv[0];
        for (int element: massiiv) {
            if (element < vaikseim) {
                vaikseim = element;
            }
        }
        return vaikseim;
    }

    private static int minJargmineTaisarv(int[] massiiv, int element) {
        int vaiksuseltJargmine = 0;
        for (int vaartus : massiiv) {
            if (vaartus > element) {
                vaiksuseltJargmine = vaartus;
                break;
            }
        }
        for (int vaartus : massiiv) {
            if (vaartus > element && vaartus < vaiksuseltJargmine) {
                vaiksuseltJargmine = vaartus;
            }
        }
        return vaiksuseltJargmine;
    }

    private static int taisarvuPositsioon(int[] massiiv, int element) {
        int positsioon = -1;
        for (int i = 0; i < massiiv.length; i++) {
            if (massiiv[i] == element) {
                positsioon = i;
            }
        }
        if (positsioon == -1) {
            System.out.println("Viga! elemendiPositsioon() ei leidnud elementi.");
        }
        return positsioon;
    }
}