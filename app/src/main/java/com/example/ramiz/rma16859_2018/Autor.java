package com.example.ramiz.rma16859_2018;


import java.util.ArrayList;

public class Autor {
    //konstruktor za rucno dodavanje
    String imeAutora;
    public int brojKnjiga;

    //konstruktor za online dodavanje
    public String imeiPrezime;
    public ArrayList<String> knjige;

    public boolean dodanOnline = false;
    boolean tmp = false;

    public Autor(ArrayList<String> knjige, String imeiPrezime) {
        this.knjige = knjige;
        this.imeiPrezime = imeiPrezime;
    }

    public Autor() {
    }

    public Autor(String s) {
        this.imeiPrezime = s;
    }

    public ArrayList<String> getKnjige() {
        return knjige;
    }

    public void setKnjige(ArrayList<String> knjige) {
        this.knjige = knjige;
    }

    public String getImeiPrezime() {
        return imeiPrezime;
    }

    public void setImeiPrezime(String imeiPrezime) {
        this.imeiPrezime = imeiPrezime;
    }

    public Autor(String imeAutora, int brojKnjiga) {
        this.imeAutora = imeAutora;
        this.brojKnjiga = brojKnjiga;
    }

    public String getImeAutora() {
        return imeAutora;
    }

    public void dodajKnjigu(String id) {
        if (knjige == null)
            knjige = new ArrayList<>();

        for (String s : knjige) {
            if (s.equals(id))
                tmp = true;
        }
        if (!tmp)
            knjige.add(id);

    }


}
