package com.example.ramiz.rma16859_2018;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.os.ResultReceiver;

import java.util.ArrayList;

/**
 * Created by Ramiz on 19.5.2018.
 */

public class KnjigePoznanika extends IntentService {

    public static int STATUS_START=0;
    public static int STATUS_FINISH=1;
    public static int STATUS_ERROR=2;

    public KnjigePoznanika(String name) {
        super(name);
    }
    public KnjigePoznanika() {
        super(null);
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        String idKorisnika = intent.getStringExtra("KEY");
        ResultReceiver receiver = intent.getParcelableExtra("RECEIVER");
        receiver.send(STATUS_START, Bundle.EMPTY);
        try {
            ArrayList<Knjiga> listaKnjiga = (ArrayList<Knjiga>) NetworkUtils.getBooksBookshelves(idKorisnika);
            Bundle bundle = new Bundle();
            bundle.putParcelableArrayList("knjige", listaKnjiga);
            receiver.send(STATUS_FINISH, bundle);
        } catch (Exception e) {
            receiver.send(STATUS_ERROR, Bundle.EMPTY);
        }
    }
}
