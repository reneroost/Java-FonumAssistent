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
import ee.reneroost.fonumiassistent.model.Prioriteetsus;
import ee.reneroost.fonumiassistent.model.VaruosaJaotus;

public class FailistLugemine {

    public static List<List<Integer>> loeReeglidFailist(String sisendFail) {
        List<List<Integer>> reeglid = new ArrayList<>();
        String sisendFailMarsruudiga = "C:\\Users\\roost\\documents\\Java-FonumAssistent\\" + sisendFail;
        BufferedReader br = null;
        String rida, csvEraldaja = ",";

        try {
            br = new BufferedReader(new FileReader(sisendFailMarsruudiga));
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

    public static List<Esindus> loeEsindusteAndmedFailist(String sisendFail) {
        List<Esindus> esindused = new ArrayList<>();
        String sisendFailMarsruudiga = "C:\\Users\\roost\\documents\\Java-FonumAssistent\\" + sisendFail;
        BufferedReader br = null;
        String rida, csvEraldaja = ",";
        Esindus esindus;
        Aadress aadress;

        try {
            br = new BufferedReader(new FileReader(sisendFailMarsruudiga));
            while ((rida = br.readLine()) != null) {
                String[] esindusRida = rida.split(csvEraldaja);
                aadress = new Aadress(esindusRida[5], Integer.parseInt(esindusRida[6]), esindusRida[7], esindusRida[8]);
                esindus = new Esindus(esindusRida[0], Integer.parseInt(esindusRida[1]), esindusRida[2], esindusRida[3], esindusRida[4], aadress);
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

    public static List<Esindus> loeEsindusteAndmedFailist() {
        return loeEsindusteAndmedFailist("esindused_andmed.csv");
    }

    public static List<VaruosaJaotus> loeLaoseisFailist(String sisendFail) {
        List<VaruosaJaotus> varuosad = new ArrayList<>();
        String sisendFailMarsruudiga = "C:\\Users\\roost\\documents\\Java-FonumAssistent\\" + sisendFail;
        BufferedReader br = null;
        String rida, csvEraldaja = ",";
        VaruosaJaotus varuosa;
        List<Integer> jaotus;

        try {
            br = new BufferedReader((new FileReader(sisendFailMarsruudiga)));
            while ((rida = br.readLine()) != null) {
                String[] varuosaRida = rida.split(csvEraldaja);
                jaotus = new ArrayList<>();
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

    public static List<Prioriteetsus> loePrioriteetsusedFailist(String sisendFail) {
        List<Prioriteetsus> prioriteetsused = new ArrayList<>();
        String sisendFailMarsruudiga = "C:\\Users\\roost\\documents\\Java-FonumAssistent\\" + sisendFail;
        BufferedReader br = null;
        String rida, csvEraldaja = ",";
        Prioriteetsus prioriteetsus;
        try {
            br = new BufferedReader(new FileReader(sisendFailMarsruudiga));
            while ((rida = br.readLine()) != null) {
                String[] prioriteetRida = rida.split(csvEraldaja);
                String tootja = prioriteetRida[0];
                List<String> esindusedJarjestuses = new ArrayList<>();
                for (int i = 1; i < prioriteetRida.length; i++) {
                    esindusedJarjestuses.add(prioriteetRida[i]);
                }
                prioriteetsus = new Prioriteetsus(tootja, esindusedJarjestuses);
                prioriteetsused.add(prioriteetsus);
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
        return prioriteetsused;
    }

    public static List<Prioriteetsus> loePrioriteetsusedFailist() {
        return loePrioriteetsusedFailist("prioriteetsused.csv");
    }

}