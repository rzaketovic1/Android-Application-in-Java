package com.example.ramiz.rma16859_2018;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by Ramiz on 27.5.2018.
 */

public class BazaOpenHelper extends SQLiteOpenHelper {


    public static final String DATABASE_NAME = "bazaKnjiga.db";
    public static final int DATABASE_VERSION = 1;


    public static final String KATEGORIJE_TABLE = "Kategorija";
    public static final String KATEGORIJA_ID = "_id";
    public static final String KATEGORIJA_NAZIV = "naziv";

    public static final String KNJIGA_TABLE = "Knjiga";
    public static final String KNJIGA_ID = "_id";
    public static final String KNJIGA_NAZIV = "naziv";
    public static final String KNJIGA_OPIS = "opis";
    public static final String KNJIGA_DATUM_OBJAVLJIVANJA = "datumObjavljivanja";
    public static final String KNJIGA_BROJ_STRANICA = "brojStranica";
    public static final String KNJIGA_ID_WEB_SERVIS = "idWebServis";
    public static final String KNJIGA_ID_KATEGORIJE = "idKategorije";
    public static final String KNJIGA_SLIKA = "slika";
    public static final String KNJIGA_PREGLEDANA = "pregledana";

    public static final String AUTOR_TABLE = "Autor";
    public static final String AUTOR_ID = "id";
    public static final String AUTOR_IME = "ime";

    public static final String AUTORSTVO_TABLE = "Autorstvo";
    public static final String AUTORSTVO_ID = "_id";
    public static final String AUTORSTVO_AUTOR_ID = "idAutora";
    public static final String AUTORSTVO_KNJIGA_ID = "idKnjige";


    private static final String CREATE_KATEGORIJA_TABLE = "CREATE TABLE " + KATEGORIJE_TABLE + " (" +
            KATEGORIJA_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            KATEGORIJA_NAZIV + " TEXT NOT NULL)";

    private static final String CREATE_KNJIGA_TABLE = "CREATE TABLE " + KNJIGA_TABLE + " (" +
            KNJIGA_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            KNJIGA_NAZIV + " TEXT NOT NULL, " +
            KNJIGA_OPIS + " TEXT, " +
            KNJIGA_DATUM_OBJAVLJIVANJA + " TEXT, " +
            KNJIGA_BROJ_STRANICA + " INTEGER, " +
            KNJIGA_ID_WEB_SERVIS + " TEXT, " +
            KNJIGA_ID_KATEGORIJE + " INTEGER, " +
            KNJIGA_SLIKA + " TEXT, " +
            KNJIGA_PREGLEDANA + " INTEGER)";

    private static final String CREATE_AUTOR_TABLE = "CREATE TABLE " + AUTOR_TABLE + " (" +
            AUTOR_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            AUTOR_IME + " TEXT NOT NULL)";


    private static final String CREATE_AUTORSTVO_TABLE = "CREATE TABLE " + AUTORSTVO_TABLE + " (" +
            AUTORSTVO_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            AUTORSTVO_AUTOR_ID + " INTEGER, " +
            AUTORSTVO_KNJIGA_ID + " INTEGER)";


    public BazaOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {


        db.execSQL(CREATE_KATEGORIJA_TABLE);
        db.execSQL(CREATE_AUTOR_TABLE);
        db.execSQL(CREATE_KNJIGA_TABLE);
        db.execSQL(CREATE_AUTORSTVO_TABLE);


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF IT EXISTS Kategorija");
        db.execSQL("DROP TABLE IF IT EXISTS Knjiga");
        db.execSQL("DROP TABLE IF IT EXISTS Autor");
        db.execSQL("DROP TABLE IF IT EXISTS Autorstvo");
        onCreate(db);

    }

    public long dodajKategoriju(String naziv) {


        Cursor cursor;
        long kategorijaId = -1;

        ContentValues DB_kategorije = new ContentValues();
        cursor = getWritableDatabase().rawQuery("SELECT * FROM " + KATEGORIJE_TABLE + " WHERE naziv = ?", new String[]{naziv});
        cursor.moveToFirst();

        if (cursor.getCount() == 0) {
            DB_kategorije.put(KATEGORIJA_NAZIV, naziv);
            getWritableDatabase().insert(KATEGORIJE_TABLE, null, DB_kategorije);
            cursor = getReadableDatabase().rawQuery("SELECT * FROM " + KATEGORIJE_TABLE + " WHERE " + KATEGORIJA_NAZIV + " LIKE ?", new String[]{"%" + naziv + "%"});
            cursor.moveToFirst();
            kategorijaId = cursor.getInt(cursor.getColumnIndex(KATEGORIJA_ID));
        }
        return kategorijaId;
    }

    public ArrayList<String> kategorijeIzBaze() {

        Cursor cursor;
        ArrayList<String> kategorije = new ArrayList<>();
        cursor = getReadableDatabase().rawQuery("SELECT * FROM " + KATEGORIJE_TABLE, null);

        if (cursor.moveToFirst())
            do {
                String kategorija = cursor.getString(cursor.getColumnIndex(KATEGORIJA_NAZIV));
                kategorije.add(kategorija);
            } while (cursor.moveToNext());

        return kategorije;
    }

    public ArrayList<Autor> autoriIzBaze() {

        Cursor cursor;
        ArrayList<Autor> autori = new ArrayList<>();
        cursor = getReadableDatabase().rawQuery("SELECT * FROM " + AUTOR_TABLE, null);

        if (cursor.moveToFirst())
            do {
                String autor = cursor.getString(cursor.getColumnIndex(AUTOR_IME));
                Autor tmp = new Autor(autor);
                tmp.dodanOnline = true;
                autori.add(tmp);
            } while (cursor.moveToNext());

        return autori;


    }


    long dodajKnjigu(Knjiga knjiga) {

        Cursor cursor;
        int autorstvoKnjigaID = -1;
        ArrayList<Integer> autorstvoAutorID = new ArrayList<>();
        ContentValues values = new ContentValues();
        cursor = getWritableDatabase().rawQuery("SELECT * FROM " + KNJIGA_TABLE + " WHERE naziv = ?", new String[]{knjiga.naziv});
        cursor.moveToFirst();
        int kategorijaId = -1;

        if (cursor.getCount() == 0) {
            values.put(KNJIGA_NAZIV, knjiga.naziv);
            values.put(KNJIGA_OPIS, knjiga.opis);
            values.put(KNJIGA_DATUM_OBJAVLJIVANJA, knjiga.datumObjavljivanja);
            values.put(KNJIGA_BROJ_STRANICA, knjiga.brojStranica);
            values.put(KNJIGA_ID_WEB_SERVIS, knjiga.id);

            String tmpUrl = "";
            if (knjiga.slika != null)
                tmpUrl = (String) knjiga.slika.toString();

            values.put(KNJIGA_SLIKA, tmpUrl);
            if (knjiga.isMarked) {
                values.put(KNJIGA_PREGLEDANA, 1);
            } else
                values.put(KNJIGA_PREGLEDANA, 0);

            cursor = getReadableDatabase().rawQuery("SELECT * FROM " + KATEGORIJE_TABLE, null);
            cursor.moveToFirst();


            do {
                String tmp = cursor.getString(cursor.getColumnIndex(KATEGORIJA_NAZIV));
                if (tmp.equals(knjiga.kategorijaOnline)) {
                    //   cursor.moveToFirst();
                    kategorijaId = cursor.getInt(cursor.getColumnIndex(KATEGORIJA_ID));
                    // values.put(KNJIGA_ID_KATEGORIJE, kategorijaId);
                }

            } while (cursor.moveToNext());

            if (kategorijaId != -1) {
                values.put(KNJIGA_ID_KATEGORIJE, kategorijaId);
                getWritableDatabase().insert(KNJIGA_TABLE, null, values);
            }


            cursor = getReadableDatabase().rawQuery("SELECT * FROM " + KNJIGA_TABLE + " WHERE " + KNJIGA_NAZIV + " LIKE ?", new String[]{"%" + knjiga.naziv + "%"});
            cursor.moveToFirst();
            autorstvoKnjigaID = cursor.getInt(cursor.getColumnIndex(KNJIGA_ID));


            ContentValues valuesAutor = new ContentValues();

            for (Autor autor : knjiga.autori) {
                cursor = getWritableDatabase().rawQuery("SELECT * FROM " + AUTOR_TABLE + " WHERE ime = ?", new String[]{autor.imeiPrezime});
                cursor.moveToFirst();
                if (cursor.getCount() == 0) {
                    valuesAutor.put(AUTOR_IME, autor.imeiPrezime);
                    getWritableDatabase().insert(AUTOR_TABLE, null, valuesAutor);

                }
                cursor = getReadableDatabase().rawQuery("SELECT * FROM " + AUTOR_TABLE + " WHERE " + AUTOR_IME + " LIKE ?", new String[]{"%" + autor.imeiPrezime + "%"});
                cursor.moveToFirst();
                autorstvoAutorID.add(cursor.getInt(cursor.getColumnIndex(AUTOR_ID)));

            }

            ContentValues valuesAutorstvo = new ContentValues();


            for (int id : autorstvoAutorID) {
                cursor = getWritableDatabase().rawQuery("SELECT * FROM " + AUTORSTVO_TABLE + " WHERE  idAutora = ? AND idKnjige = ?", new String[]{String.valueOf(id), String.valueOf(autorstvoKnjigaID)});
                cursor.moveToFirst();
                if (cursor.getCount() == 0) {
                    valuesAutorstvo.put(AUTORSTVO_AUTOR_ID, id);
                    valuesAutorstvo.put(AUTORSTVO_KNJIGA_ID, autorstvoKnjigaID);
                    getWritableDatabase().insert(AUTORSTVO_TABLE, null, valuesAutorstvo);
                }


            }
        }
        return autorstvoKnjigaID;

    }

    ArrayList<Knjiga> knjigeKategorije(long idKategorije) {
        idKategorije++;
        ArrayList<Knjiga> knjige = new ArrayList<>();
        ArrayList<String> test = new ArrayList<>();


        Cursor cursor;


        cursor = getReadableDatabase().rawQuery("SELECT * FROM " + KNJIGA_TABLE, null);
        cursor.moveToFirst();
        do {
            if (cursor.getInt(cursor.getColumnIndex(KNJIGA_ID_KATEGORIJE)) == idKategorije) {
                int id = cursor.getInt(cursor.getColumnIndex(KNJIGA_ID));
                String naziv = cursor.getString(cursor.getColumnIndex(KNJIGA_NAZIV));
                String opis = cursor.getString(cursor.getColumnIndex(KNJIGA_OPIS));
                String datumObjavljivanja = cursor.getString(cursor.getColumnIndex(KNJIGA_DATUM_OBJAVLJIVANJA));
                int brojStranica = cursor.getInt(cursor.getColumnIndex(KNJIGA_BROJ_STRANICA));
                String slika = cursor.getString(cursor.getColumnIndex(KNJIGA_SLIKA));
                String webId = cursor.getString(cursor.getColumnIndex(KNJIGA_ID_WEB_SERVIS));
                int pregledana = cursor.getInt(cursor.getColumnIndex(KNJIGA_PREGLEDANA));

                ArrayList<Integer> autoriId = new ArrayList<>();
                Cursor c = getReadableDatabase().rawQuery("SELECT * FROM " + AUTORSTVO_TABLE + " WHERE idKnjige  LIKE ?", new String[]{"%" + id + "%"});
                c.moveToFirst();
                do {

                    autoriId.add(c.getInt(c.getColumnIndex(AUTORSTVO_AUTOR_ID)));
                    //krahira kad pretrazuje autorstvo a knjiga nema autra

                } while (c.moveToNext());

                ArrayList<Autor> autori = new ArrayList<>();

                for (int autoriKnjige : autoriId) {
                    c = getReadableDatabase().rawQuery("SELECT * FROM " + AUTOR_TABLE + " WHERE id  LIKE ?", new String[]{"%" + autoriKnjige + "%"});
                    c.moveToFirst();
                    autori.add(new Autor(c.getString(c.getColumnIndex(AUTOR_IME))));
                }

                URL slikaTmp = null;

                try {
                    slikaTmp = new URL(slika);
                } catch (MalformedURLException e) {

                }

                Knjiga knjiga = new Knjiga(webId, naziv, autori, opis, datumObjavljivanja, slikaTmp, brojStranica);
                if (pregledana == 1)
                    knjiga.isMarked = true;

                c = getReadableDatabase().rawQuery(" SELECT * FROM " + KATEGORIJE_TABLE + " WHERE " + KATEGORIJA_ID + "  LIKE ?", new String[]{"%" + idKategorije + "%"});
                c.moveToFirst();
                knjiga.kategorijaOnline = c.getString(c.getColumnIndex(KATEGORIJA_NAZIV));
                knjiga.dodanaOnline = true;
                knjige.add(knjiga);
            }
        } while (cursor.moveToNext());


        return knjige;

    }


    ArrayList<Knjiga> knjigeAutora(long idAutora) {
        idAutora++;
        ArrayList<Knjiga> knjige = new ArrayList<>();
        Cursor cursor;
        ArrayList<Integer> knjigeId = new ArrayList<>();

        int kategorijaId = 0;


        cursor = getReadableDatabase().rawQuery("SELECT * FROM " + AUTORSTVO_TABLE + " WHERE idAutora LIKE ?", new String[]{"%" + idAutora + "%"});
        cursor.moveToFirst();
        do {

            int tmp = cursor.getInt(cursor.getColumnIndex(AUTORSTVO_KNJIGA_ID));
            knjigeId.add(tmp);
            //krahira kad pretrazuje autorstvo a knjiga nema autra

        } while (cursor.moveToNext());


        //svi autori knjiga

        for (int knjigaId : knjigeId) {
            cursor = getReadableDatabase().rawQuery("SELECT * FROM " + KNJIGA_TABLE + " WHERE " + KNJIGA_ID + " LIKE ?", new String[]{"%" + knjigaId + "%"});
            cursor.moveToFirst();

            String naziv = cursor.getString(cursor.getColumnIndex(KNJIGA_NAZIV));
            String opis = cursor.getString(cursor.getColumnIndex(KNJIGA_OPIS));
            String datumObjavljivanja = cursor.getString(cursor.getColumnIndex(KNJIGA_DATUM_OBJAVLJIVANJA));
            int brojStranica = cursor.getInt(cursor.getColumnIndex(KNJIGA_BROJ_STRANICA));
            String slika = cursor.getString(cursor.getColumnIndex(KNJIGA_SLIKA));
            String webId = cursor.getString(cursor.getColumnIndex(KNJIGA_ID_WEB_SERVIS));
            int pregledana = cursor.getInt(cursor.getColumnIndex(KNJIGA_PREGLEDANA));
            kategorijaId = cursor.getInt(cursor.getColumnIndex(KNJIGA_ID_KATEGORIJE));

            ArrayList<Integer> autoriId = new ArrayList<>();
            Cursor c = getReadableDatabase().rawQuery("SELECT * FROM " + AUTORSTVO_TABLE + " WHERE idKnjige  LIKE ?", new String[]{"%" + knjigaId + "%"});
            c.moveToFirst();
            do {
                autoriId.add(c.getInt(c.getColumnIndex(AUTORSTVO_AUTOR_ID)));
                //krahira kad pretrazuje autorstvo a knjiga nema autra

            } while (c.moveToNext());

            ArrayList<Autor> autori = new ArrayList<>();
            for (int autoriKnjige : autoriId) {
                c = getReadableDatabase().rawQuery("SELECT * FROM " + AUTOR_TABLE + " WHERE id  LIKE ?", new String[]{"%" + autoriKnjige + "%"});
                c.moveToFirst();
                autori.add(new Autor(c.getString(c.getColumnIndex(AUTOR_IME))));
            }


            URL slikaTmp = null;
            try {
                slikaTmp = new URL(slika);
            } catch (MalformedURLException e) {

            }


            Knjiga knjiga = new Knjiga(webId, naziv, autori, opis, datumObjavljivanja, slikaTmp, brojStranica);
            if (pregledana == 1)
                knjiga.isMarked = true;

            c = getReadableDatabase().rawQuery(" SELECT * FROM " + KATEGORIJE_TABLE + " WHERE " + KATEGORIJA_ID + "  LIKE ?", new String[]{"%" + kategorijaId + "%"});
            c.moveToFirst();
            knjiga.kategorijaOnline = c.getString(c.getColumnIndex(KATEGORIJA_NAZIV));
            knjiga.dodanaOnline = true;
            knjige.add(knjiga);


        }


        return knjige;
    }



 /* public void deleteKategorije()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + KATEGORIJE_TABLE);
        db.execSQL("UPDATE SQLITE_SEQUENCE SET seq = 0 WHERE name = 'Kategorija'");
   }

*/


}
