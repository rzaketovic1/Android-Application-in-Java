package com.example.ramiz.rma16859_2018;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.widget.FrameLayout;

import com.facebook.stetho.Stetho;

public class KategorijeAkt extends AppCompatActivity implements ListeFragment.FragmentInteraction {

    public static final String KATEGORIJA_EXTRAS = "kategorija_extras";
    public static final String AUTOR_EXTRAS = "autor_extras";

    public boolean wideScreen = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Stetho.initializeWithDefaults(this);
        setContentView(R.layout.kategorije_akt);

        FrameLayout detalji = findViewById(R.id.mjestoFragmentKnjige);

        if (detalji != null) {
            wideScreen = true;
            otvoriKnjigeFragment(Knjige.getKategorije().get(0), KATEGORIJA_EXTRAS);
        }

        ListeFragment listeFragment = new ListeFragment();
        Bundle bundle = new Bundle();
        bundle.putBoolean(KATEGORIJA_EXTRAS, true);
        listeFragment.setArguments(bundle);
        replaceFragmentWithoutBackstack(listeFragment);
    }

    @Override
    public void onBackPressed() {
        if (wideScreen) {
            finish();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onKategorijeSelected(String kategorija) {
        otvoriKnjigeFragment(kategorija, KATEGORIJA_EXTRAS);
    }

    private void otvoriKnjigeFragment(String kategorija, String kategorijaExtras) {
        KnjigeFragment knjigeFragment = new KnjigeFragment();
        Bundle bundle = new Bundle();
        bundle.putString(kategorijaExtras, kategorija);
        knjigeFragment.setArguments(bundle);

        if (wideScreen) {
            replaceFragment(knjigeFragment, R.id.mjestoFragmentKnjige);
        } else {
            replaceFragment(knjigeFragment, R.id.mjestoFragmentliste);
        }
    }

    @Override
    public void onAutorSelected(String imeAutora) {
        otvoriKnjigeFragment(imeAutora, AUTOR_EXTRAS);
    }

    @Override
    public void onAutorFragment() {
        ListeFragment listeFragment = new ListeFragment();
        replaceFragment(listeFragment);
    }

    private void replaceFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction().replace(R.id.mjestoFragmentliste, fragment)
                .addToBackStack(null)
                .commit();
    }

    private void replaceFragmentWithoutBackstack(Fragment fragment) {
        getSupportFragmentManager().beginTransaction().replace(R.id.mjestoFragmentliste, fragment)
                .commit();
    }

    private void replaceFragment(Fragment fragment, int resId) {
        getSupportFragmentManager().beginTransaction().replace(resId, fragment)
                .addToBackStack(null)
                .commit();
    }
}
