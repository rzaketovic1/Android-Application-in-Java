package com.example.ramiz.rma16859_2018;

import android.os.AsyncTask;

import java.util.List;

/**
 * Created by Ramiz on 18.5.2018.
 */

public class DohvatiKnjige extends AsyncTask<String, Void, List<Knjiga>> {





    @Override
    protected List<Knjiga> doInBackground(String... params) {
        if (vise) {
            return NetworkUtils.getBookInfoMultiple(params[0]);
        }
        return NetworkUtils.getBookInfo(params[0]);
    }

    @Override
    protected void onPostExecute(List<Knjiga> s) {
        super.onPostExecute(s);
        pozivatelj.onDohvatiDone( s);
    }

    public interface IDohvatiKnjigeDone{
        public void onDohvatiDone(List<Knjiga> rez);
    }

    private IDohvatiKnjigeDone pozivatelj;
    private boolean vise = false;
    public DohvatiKnjige(IDohvatiKnjigeDone s, boolean viseKnjiga){pozivatelj=s; vise = viseKnjiga;}
}
