package com.example.ramiz.rma16859_2018;

import android.os.AsyncTask;

import java.util.List;

/**
 * Created by Ramiz on 19.5.2018.
 */

public class DohvatiNajnovije extends AsyncTask<String,Void,List<Knjiga>> {

    private  IDohvatiNajnovijeDone pozivatelj;

    @Override
    protected void onPostExecute(List<Knjiga> knjigas) {
        super.onPostExecute(knjigas);
        pozivatelj.onNajnovijeDone(knjigas);
    }

    @Override
    protected List<Knjiga> doInBackground(String... params) {
        return NetworkUtils.getNajnovje(params[0]);
    }


    public interface IDohvatiNajnovijeDone{
        void onNajnovijeDone(List<Knjiga> lista);
    }


    public DohvatiNajnovije(IDohvatiNajnovijeDone pozivatelj)
    {
        this.pozivatelj=pozivatelj;
    }
}
