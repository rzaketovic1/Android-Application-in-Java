package com.example.ramiz.rma16859_2018;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;


public class DodavanjeKnjigeAkt extends AppCompatActivity {


 @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dodavanje_knjige_akt);


        FragmentManager fragmentManager = DodavanjeKnjigeAkt.this.getSupportFragmentManager();
        DodavanjeKnjigeFragment dodavanjeKnjigeFragment;
        dodavanjeKnjigeFragment = (DodavanjeKnjigeFragment) fragmentManager.findFragmentById(R.id.mjestoFragmentDodavanjeKnjige);

        if (dodavanjeKnjigeFragment == null) {
            dodavanjeKnjigeFragment = new DodavanjeKnjigeFragment();


            fragmentManager.beginTransaction().replace(R.id.mjestoFragmentDodavanjeKnjige, dodavanjeKnjigeFragment).commit();
        }


}



}
