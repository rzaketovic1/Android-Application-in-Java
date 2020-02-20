package com.example.ramiz.rma16859_2018;

import android.net.Uri;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ramiz on 18.5.2018.
 */

public class NetworkUtils {

    private static final String BOOK_BASE_URL = "https://www.googleapis.com/books/v1/volumes?q=intitle:%s&maxResults=5";
    private static final String BASE_URL_AUTHOR = "https://www.googleapis.com/books/v1/volumes?q=inauthor:%s&maxResults=5&orderBy=newest";
    private static final String BASE_URL_BOOKSHELVES = "https://www.googleapis.com/books/v1/users/%s/bookshelves";
    private static final String QUERY_PARAM = "q";
    private static final String MAX_RESULT = "maxResults";

    //  private static  final String PRINT_TYPE="printType";
    public static List<Knjiga> knjiga;


    public static ArrayList<Knjiga> knjigaNajnovije;

    static List<Knjiga> getNajnovje(String imeAutora) {
        String response = apiPoziv(BASE_URL_AUTHOR, imeAutora);

        return parsirajKnjigaObjekat(response);
    }

    static List<Knjiga> getBookInfoMultiple(String queryString) {
        String[] knjigeNaziv = queryString.split(";");
        ArrayList<Knjiga> sveKnjige = new ArrayList<>();

        for (String naziv : knjigeNaziv) {
            String bookJSONString = apiPoziv(BOOK_BASE_URL, naziv);
            sveKnjige.addAll(parsirajKnjigaObjekat(bookJSONString));
        }

        return sveKnjige;
    }


    static List<Knjiga> getBookInfo(String queryString) {
        String bookJSONString = apiPoziv(BOOK_BASE_URL, queryString);
        return parsirajKnjigaObjekat(bookJSONString);
    }

    static List<Knjiga> getBooksBookshelves(String userID) throws JSONException {

        ArrayList<Knjiga> knjigeUserID = new ArrayList<>();
        ArrayList<String> selfLinks = new ArrayList<>();
        String bookshelves = apiPoziv(BASE_URL_BOOKSHELVES, userID);

        JSONObject jsonObject = new JSONObject(bookshelves);
        JSONArray items = jsonObject.getJSONArray("items");

        for (int k = 0; k < items.length(); k++) {
            JSONObject tmp = items.getJSONObject(k);
            if (tmp.getString("access").equals("PUBLIC"))
                selfLinks.add(tmp.getString("selfLink"));
        }

        for (String links : selfLinks) {
            String link = new String(links + "%s");
            String bookJSONString = apiPoziv(link, "/volumes");
            knjigeUserID.addAll(parsirajKnjigaObjekat(bookJSONString));

        }

        return knjigeUserID;
    }


    static List<Knjiga> parsirajKnjigaObjekat(String response) {
        JSONObject jsonObject = null;
        ArrayList<Knjiga> tmp = new ArrayList<>();
        try {
            jsonObject = new JSONObject(response);

            JSONArray items = jsonObject.getJSONArray("items");


            int pageCount = 0;

            for (int i = 0; i < items.length(); i++) {

                String description = new String("");
                String publishedDate = new String("");

                JSONObject temp = items.getJSONObject(i);

                String id = temp.getString("id");
                JSONObject volumeInfo = temp.getJSONObject("volumeInfo");
                String name = volumeInfo.get("title").toString();

                if (volumeInfo.has("publishedDate"))
                    publishedDate = volumeInfo.get("publishedDate").toString();

                if (volumeInfo.has("pageCount"))
                    pageCount = Integer.parseInt(volumeInfo.get("pageCount").toString());


                if (volumeInfo.has("description"))
                    description = volumeInfo.get("description").toString();


                JSONObject imageLinks = null;
                URL url = null;

                if (volumeInfo.has("imageLinks")) {
                    imageLinks = volumeInfo.getJSONObject("imageLinks");
                    String thumbnail = imageLinks.get("thumbnail").toString();
                    url = new URL(thumbnail);
                }
                JSONArray authors = null;
                ArrayList<Autor> listaAutora = new ArrayList<>();

                if (volumeInfo.has("authors")) {
                    authors = volumeInfo.getJSONArray("authors");
                    ArrayList<String> autori = new ArrayList<>();
                    for (int j = 0; j < authors.length(); j++) {
                        autori.add(authors.getString(j));
                    }


                    for (String s : autori) {
                        Autor a = new Autor(s);
                        listaAutora.add(a);

                    }


                }

                tmp.add(new Knjiga(id, name, listaAutora, description, publishedDate, url, pageCount));


            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return tmp;
    }


    static String apiPoziv(String BASE_URL, String queryString) {
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String bookJSONString = null;
        knjiga = new ArrayList<>();

        try {
            Uri uri = Uri.parse(String.format(BASE_URL, queryString));

            URL requestURL = new URL(uri.toString());
            urlConnection = (HttpURLConnection) requestURL.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null)
                return null;

            reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = reader.readLine()) != null) {
                buffer.append(line + "\n");
            }
            if (buffer.length() == 0) {
                return null;
            }
            bookJSONString = buffer.toString();
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;

        } finally {
            if (urlConnection != null)
                urlConnection.disconnect();
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return bookJSONString;
    }
}
