package ee.reneroost.fonumiassistent;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.stream.IntStream;

public class Main {

    private static int esindusteHulk;
    private static List<Esindus> esindused = new ArrayList<>();
    private static List<List<Integer>> reeglid = new ArrayList<>();

    public static void main(String[] args) {
        loeEsindusedFailist();
        loeReeglidFailist();
        //kuvaEsindused();

        Scanner klaviatuur = new Scanner(System.in);
        int[] praeguneJaotus = new int[esindusteHulk];
        System.out.println("Sisesta praegune varuosa jaotus");
        System.out.print("Kristiines: ");
        praeguneJaotus[0] = klaviatuur.nextInt();
        System.out.print("T1-s: ");
        praeguneJaotus[1] = klaviatuur.nextInt();
        System.out.print("Lõunakeskuses: ");
        praeguneJaotus[2] = klaviatuur.nextInt();
        int[][] saatmisMaatriks = arvutaJaotusMaatriks(praeguneJaotus);
        for (int[] massiiv: saatmisMaatriks) {
            for (int element: massiiv) {
                System.out.print(element + " ");
            }
            System.out.println();
        }
    }

    private static void jaotaVaruosad() {

    }

    private static int[][] arvutaJaotusMaatriks(int[] praeguneJaotus) {
        int varuosadeHulk = IntStream.of(praeguneJaotus).sum();
        int[] erinevus = new int[esindusteHulk];
        int[] algneErinevus = new int[esindusteHulk];
        int[][] saatmisMaatriks = new int[esindusteHulk][esindusteHulk];
        for (int i = 0; i < esindusteHulk; i++) {
            erinevus[i] = praeguneJaotus[i] - reeglid.get(varuosadeHulk).get(i);
            algneErinevus[i] = praeguneJaotus[i] - reeglid.get(varuosadeHulk).get(i);
        }
        System.out.println();
        if (erinevus[0] == 0 && erinevus[1] == 0 && erinevus[2] == 0) {
            System.out.println("Varuosad on optimaalselt jaotatud!");
        }
        while (!(erinevus[0] == 0 && erinevus[1] == 0 && erinevus[2] == 0)) {
            // i == puudu, j == üle;
            for (int i = 0; i < esindusteHulk; i++) {
                if (erinevus[i] < 0) {
                    for (int j = 0; j < esindusteHulk; j++) {
                        if (erinevus[j] > 0) {
                            if (Math.abs(erinevus[i]) == Math.abs(erinevus[j])) {
                                System.out.println("Saata " +
                                        Math.abs(erinevus[j]) + " varuosa " +
                                        esindused.get(j).getNimiSeestytlev() + " " +
                                        esindused.get(i).getNimiSisseytlev() + ".");
                                saatmisMaatriks[i][j] = erinevus[i];
                                saatmisMaatriks[j][i] = erinevus[j];
                                erinevus[i] = erinevus[j] = 0;
                            } else if (Math.abs(erinevus[i]) > Math.abs(erinevus[j])) {
                                System.out.println("Saata " +
                                        Math.abs(erinevus[j]) + " varuosa " +
                                        esindused.get(j).getNimiSeestytlev() + " " +
                                        esindused.get(i).getNimiSisseytlev() + ".");
                                saatmisMaatriks[i][j] = -(erinevus[j]);
                                saatmisMaatriks[j][i] = erinevus[j];
                                erinevus[i] += erinevus[j];
                                erinevus[j] = 0;
                            } else if (Math.abs(erinevus[i]) < Math.abs(erinevus[j])) {
                                System.out.println("Saata " +
                                        Math.abs(erinevus[i]) + " varuosa " +
                                        esindused.get(j).getNimiSeestytlev() + " " +
                                        esindused.get(i).getNimiSisseytlev() + ".");
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
        System.out.println();
        System.out.println(String.format("%-15s%-10s%-10s%-10s", "Esindus", "Hetkeseis", "Uus seis", "Erinevus"));
        for (int i = 0; i < esindusteHulk; i++) {
            System.out.println(String.format("%-15s%-10s%-10s%-10s",
                    esindused.get(i).getNimi(),
                    praeguneJaotus[i],
                    reeglid.get(varuosadeHulk).get(i),
                    algneErinevus[i]));
        }
        return saatmisMaatriks;
    }

    private static void arvutaJaotus(int[] praeguneJaotus) {
        int varuosadeHulk = IntStream.of(praeguneJaotus).sum();
        int[] erinevus = new int[esindusteHulk];
        for (int i = 0; i < esindusteHulk; i++) {
            erinevus[i] = praeguneJaotus[i] - reeglid.get(varuosadeHulk).get(i);
        }
        System.out.println();
        if (erinevus[0] == 0 && erinevus[1] == 0 && erinevus[2] == 0) {
            System.out.println("Varuosad on optimaalselt jaotatud!");
        }
        while (!(erinevus[0] == 0 && erinevus[1] == 0 && erinevus[2] == 0)) {
            // i == puudu, j == üle;
            for (int i = 0; i < esindusteHulk; i++) {
                if (erinevus[i] < 0) {
                    for (int j = 0; j < esindusteHulk; j++) {
                        if (erinevus[j] > 0) {
                            if (Math.abs(erinevus[i]) == Math.abs(erinevus[j])) {
                                System.out.println("Saata " +
                                        Math.abs(erinevus[j]) + " varuosa " +
                                        esindused.get(j).getNimiSeestytlev() + " " +
                                        esindused.get(i).getNimiSisseytlev() + ".");
                                erinevus[i] = erinevus[j] = 0;
                            } else if (Math.abs(erinevus[i]) > Math.abs(erinevus[j])) {
                                System.out.println("Saata " +
                                        Math.abs(erinevus[j]) + " varuosa " +
                                        esindused.get(j).getNimiSeestytlev() + " " +
                                        esindused.get(i).getNimiSisseytlev() + ".");
                                erinevus[i] += erinevus[j];
                                erinevus[j] = 0;
                            } else if (Math.abs(erinevus[i]) < Math.abs(erinevus[j])) {
                                System.out.println("Saata " +
                                        Math.abs(erinevus[i]) + " varuosa " +
                                        esindused.get(j).getNimiSeestytlev() + " " +
                                        esindused.get(i).getNimiSisseytlev() + ".");
                                erinevus[j] += erinevus[i];
                                erinevus[i] = 0;
                            }
                        }
                    }
                }
            }
        }
        System.out.println();
        System.out.println(String.format("%-15s%-10s%-10s", "Esindus", "Hetkeseis", "Uus seis"));
        for (int i = 0; i < esindusteHulk; i++) {
            System.out.println(String.format("%-15s%-10s%-10s",
                    esindused.get(i).getNimi(), praeguneJaotus[i], reeglid.get(varuosadeHulk).get(i)));
        }
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

    private static void kuvaEsindused() {
        System.out.println("Kokku on " + esindused.size() + " esindust:");
        for (Esindus esindus : esindused) {
            System.out.println(esindus.getNimi());
        }
    }
}