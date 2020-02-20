package com.example.ramiz.rma16859_2018;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.net.URL;
import java.util.ArrayList;


public class Knjiga implements Serializable, Parcelable {

    String imeAutora;
    String nazivKnjige;
    String kategorija;
    Drawable slikaUpload;

    public boolean isMarked;

    public String id;
    public String naziv;
    public ArrayList<Autor> autori;
    public String opis;
    public String datumObjavljivanja;
    public URL slika;
    public int brojStranica;

    public String kategorijaOnline;
    public boolean dodanaOnline=false;

    public Knjiga(String imeAutora, String nazivKnjige, String kategorija, Drawable slika) {
        this.imeAutora = imeAutora;
        this.nazivKnjige = nazivKnjige;
        this.kategorija = kategorija;
        this.slikaUpload=slika;
        isMarked = false;
    }

    public Knjiga(String id, String naziv, ArrayList<Autor> autori, String opis, String datumObjavljivanja, URL slika, int brojStranica) {
        this.id = id;
        this.naziv = naziv;
        this.autori = autori;
        this.opis=opis;
        this.datumObjavljivanja = datumObjavljivanja;
        this.slika = slika;
        this.brojStranica = brojStranica;
        this.isMarked=false;
    }

    public Knjiga(){}

    public String getOpis(){return opis;}
    public  void setOpis(String opis){this.opis=opis;}

    public int getBrojStranica() {
        return brojStranica;
    }

    public void setBrojStranica(int brojStranica) {
        this.brojStranica = brojStranica;
    }

    public void setSlika(URL slika) {
        this.slika = slika;
    }

    public String getDatumObjavljivanja() {
        return datumObjavljivanja;
    }

    public void setDatumObjavljivanja(String datumObjavljivanja) {
        this.datumObjavljivanja = datumObjavljivanja;
    }

    public ArrayList<Autor> getAutori() {
        return autori;
    }

    public void setAutori(ArrayList<Autor> autori) {
        this.autori = autori;
    }

    public String getNaziv() {
        return naziv;
    }

    public void setNaziv(String naziv) {
        this.naziv = naziv;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Drawable getSlika() {
        return slikaUpload;
    }

    public String getImeAutora() {
        return imeAutora;
    }

    public String getNazivKnjige() {
        return nazivKnjige;
    }

    public String getKategorija() {
        return kategorija;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.imeAutora);
        dest.writeString(this.nazivKnjige);
        dest.writeString(this.kategorija);


        Bitmap bitmap=(Bitmap)((BitmapDrawable)this.slikaUpload).getBitmap();
        dest.writeParcelable(bitmap, flags);

        dest.writeByte(this.isMarked ? (byte) 1 : (byte) 0);
        dest.writeString(this.id);
        dest.writeString(this.naziv);
        dest.writeList(this.autori);
        dest.writeString(this.opis);
        dest.writeString(this.datumObjavljivanja);
        dest.writeSerializable(this.slika);
        dest.writeInt(this.brojStranica);
        dest.writeString(this.kategorijaOnline);
        dest.writeByte(this.dodanaOnline ? (byte) 1 : (byte) 0);
    }

    protected Knjiga(Parcel in) {
        this.imeAutora = in.readString();
        this.nazivKnjige = in.readString();
        this.kategorija = in.readString();
        this.slikaUpload = in.readParcelable(Drawable.class.getClassLoader());
        this.isMarked = in.readByte() != 0;
        this.id = in.readString();
        this.naziv = in.readString();
        this.autori = new ArrayList<Autor>();
        in.readList(this.autori, Autor.class.getClassLoader());
        this.opis = in.readString();
        this.datumObjavljivanja = in.readString();
        this.slika = (URL) in.readSerializable();
        this.brojStranica = in.readInt();
        this.kategorijaOnline = in.readString();
        this.dodanaOnline = in.readByte() != 0;
    }

    public static final Parcelable.Creator<Knjiga> CREATOR = new Parcelable.Creator<Knjiga>() {
        @Override
        public Knjiga createFromParcel(Parcel source) {
            return new Knjiga(source);
        }

        @Override
        public Knjiga[] newArray(int size) {
            return new Knjiga[size];
        }
    };
}
