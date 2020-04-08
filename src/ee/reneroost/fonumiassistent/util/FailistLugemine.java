package ee.reneroost.fonumiassistent.util;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ee.reneroost.fonumiassistent.model.Aadress;
import ee.reneroost.fonumiassistent.model.Esindus;
import ee.reneroost.fonumiassistent.model.VaruosaJaotus;

public class FailistLugemine {

    public static List<List<Integer>> loeReeglidFailist(String jaotusReeglidSisendFail) {
        List<List<Integer>> reeglid = new ArrayList<>();
        String jaotusreeglidFail = "C:\\Users\\roost\\Desktop\\Fonumi assistent\\" + jaotusReeglidSisendFail;
        BufferedReader br = null;
        String rida, csvEraldaja = ",";

        try {
            br = new BufferedReader(new FileReader(jaotusreeglidFail));
            while ((rida = br.readLine()) != null) {
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
        return reeglid;
    }

    public static List<List<Integer>> loeReeglidFailist() {
        return loeReeglidFailist("jaotusreeglid.csv");
    }

    public static List<Esindus> loeEsindusedFailist(String esindusedSisendFail) {
        List<Esindus> esindused = new ArrayList<>();
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
        return esindused;
    }

    public static List<Esindus> loeEsindusedFailist() {
        return loeEsindusedFailist("esindused.csv");
    }

    public static List<VaruosaJaotus> loeLaoseisFailist(String varuosaJaotusedSisendFail) {
        List<VaruosaJaotus> varuosad = new ArrayList<>();
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
                varuosa = new VaruosaJaotus(varuosaRida[0], varuosaRida[1], varuosaRida[2],
                        Double.parseDouble(varuosaRida[3]), Integer.parseInt(varuosaRida[4]),
                        Integer.parseInt(varuosaRida[5]), jaotus);
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
        return varuosad;
    }

    public static List<VaruosaJaotus> loeLaoseisFailist() {
        return loeLaoseisFailist("laoseis.csv");
    }
}