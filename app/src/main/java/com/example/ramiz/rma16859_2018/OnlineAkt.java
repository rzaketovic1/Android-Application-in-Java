package com.example.ramiz.rma16859_2018;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

public class OnlineAkt extends AppCompatActivity {

    boolean tmp = true;

    public static String preporucenaKnjiga;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.online_akt);

        if (getIntent().getExtras() != null)
            tmp = false;

        //  getIntent().getExtras().getBoolean("FRAGMENT_PREPORUCI")


        if (tmp) {
            FragmentManager fragmentManager = OnlineAkt.this.getSupportFragmentManager();
            FragmentOnline fragmentOnline;
            fragmentOnline = (FragmentOnline) fragmentManager.findFragmentById(R.id.mjestoFragmentOnline);

            if (fragmentOnline == null) {
                fragmentOnline = new FragmentOnline();


                fragmentManager.beginTransaction().replace(R.id.mjestoFragmentOnline, fragmentOnline).commit();
            }

        } else {
            Intent intent = this.getIntent();
            Bundle bundle = intent.getExtras();

            preporucenaKnjiga = (String) bundle.getString("KNJIGA");


            FragmentManager fm = OnlineAkt.this.getSupportFragmentManager();
            FragmentPreporuci fragmentPreporuci;
            fragmentPreporuci = (FragmentPreporuci) fm.findFragmentById(R.id.mjestoFragmentOnline);

            if (fragmentPreporuci == null) {
                fragmentPreporuci = new FragmentPreporuci();


                fm.beginTransaction().replace(R.id.mjestoFragmentOnline, fragmentPreporuci).commit();
            }
        }


    }
}
