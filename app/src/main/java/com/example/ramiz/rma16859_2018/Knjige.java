package com.example.ramiz.rma16859_2018;

import java.util.ArrayList;


public class Knjige {

    private static ArrayList<Knjiga> knjige;
    private static ArrayList<String> kategorije;
    private static ArrayList<Autor> autori;
    private  static ArrayList<Knjiga> knjigeOnline;
    private static ArrayList<Autor> autoriOnline;
    private static Knjiga knjigaZaPreporuku;


    private Knjige() {

    }

    public static ArrayList<Autor> getAutori(){
        if(autori==null){
            autori=new ArrayList<>();
        }
        return autori;
    }

    public static ArrayList<Autor> getAutoriOnline(){
        if(autoriOnline==null){
            autoriOnline=new ArrayList<>();
        }
        return autoriOnline;
    }

    public static ArrayList<Knjiga> getKnjige() {
        if(knjige == null) {
            knjige = new ArrayList<>();
        }
        return knjige;
    }

    public static ArrayList<Knjiga> getKnjigeOnline(){
        if(knjigeOnline==null){
            knjigeOnline=new ArrayList<>();
        }
        return knjigeOnline;
    }

    public static ArrayList<String> getKategorije(){
        if(kategorije==null){
            kategorije=new ArrayList<>();
            return null;
        }
        return  kategorije;
    }

    public static void setKnjigaZaPreporuku(Knjiga k){
        knjigaZaPreporuku=k;
    }

    public static Knjiga getKnjigaZaPreporuku()
    {
        return  knjigaZaPreporuku;
    }





}
