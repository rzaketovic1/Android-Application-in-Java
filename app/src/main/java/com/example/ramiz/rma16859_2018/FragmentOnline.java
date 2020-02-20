package com.example.ramiz.rma16859_2018;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

import static com.example.ramiz.rma16859_2018.BazaOpenHelper.DATABASE_NAME;
import static com.example.ramiz.rma16859_2018.BazaOpenHelper.DATABASE_VERSION;
import static com.example.ramiz.rma16859_2018.KnjigePoznanika.STATUS_FINISH;
import static com.example.ramiz.rma16859_2018.KnjigePoznanika.STATUS_START;

/**
 * Created by Ramiz on 18.5.2018.
 */

public class FragmentOnline extends Fragment implements DohvatiKnjige.IDohvatiKnjigeDone,DohvatiNajnovije.IDohvatiNajnovijeDone,
KnjigePoznanikaReceiver.Receiver{

    EditText txtPretragaOnline;
    Button btnPretragaOnline;
    Spinner sKategorije;
    Spinner sRezultati;
    Button btnDodajKnjiguOnline;
    Button btnPovratak;

    KnjigePoznanikaReceiver receiver;

    ArrayList<String > s=new ArrayList<>();

    static ArrayList<Knjiga> listaKnjigaPretrage;




    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

         View view = inflater.inflate(R.layout.fragment_online, container, false);


        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        txtPretragaOnline = (EditText) getView().findViewById(R.id.tekstUpit);
        btnPretragaOnline = (Button) getView().findViewById(R.id.dRun);
        sKategorije = (Spinner) getView().findViewById(R.id.sKategorije);
        sRezultati = (Spinner) getView().findViewById(R.id.sRezultati);
        btnDodajKnjiguOnline = (Button) getView().findViewById(R.id.dAdd);
        btnPovratak = (Button) getView().findViewById(R.id.dPovratak);
        btnPretragaOnline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SearchBooks(v);
            }
        });
        s = Knjige.getKategorije();

        ArrayAdapter<String> adapterS = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, s);
        adapterS.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sKategorije.setAdapter(adapterS);

        btnDodajKnjiguOnline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Knjiga knjiga=new Knjiga();
                boolean temp=false;
                String nazivKnjige=sRezultati.getSelectedItem().toString();

                for (Knjiga knjige: listaKnjigaPretrage) {
                    if (knjige.getNaziv().equals(nazivKnjige)) {
                        knjiga = knjige;
                        for (Knjiga k: Knjige.getKnjigeOnline()) {
                            if(k.id.equals(knjiga.id))
                                temp=true;
                        }
                        if(!temp) {
                            knjiga.dodanaOnline=true;
                            Knjige.getKnjigeOnline().add(knjiga);
                            knjiga.kategorijaOnline = sKategorije.getSelectedItem().toString();
                            break;
                        }
                    }
                }

                boolean tmp=false;
                for (Autor autorKnjige: knjiga.getAutori()) {
                    for (Autor autori : Knjige.getAutoriOnline()) {
                        if (autorKnjige.imeiPrezime.equals(autori.imeiPrezime))
                            tmp = true;
                    }
                    if (!tmp) {
                        autorKnjige.dodajKnjigu(knjiga.id);
                        autorKnjige.dodanOnline=true;
                        Knjige.getAutoriOnline().add(autorKnjige);
                    }
                }

                BazaOpenHelper bazaOpenHelper=new BazaOpenHelper(getActivity(), DATABASE_NAME, null, DATABASE_VERSION);
                bazaOpenHelper.dodajKnjigu(knjiga);
            }
        });


        btnPovratak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(),KategorijeAkt.class);
                startActivity(intent);
            }
        });



    }

    public  void SearchBooks(View view)
        {
            String unos=txtPretragaOnline.getText().toString();
            if (unos.contains(";")) {
                new DohvatiKnjige((DohvatiKnjige.IDohvatiKnjigeDone)FragmentOnline.this, true).execute(unos);
            } else if(unos.toLowerCase().contains("autor:")) {
                String author = unos.substring(6);
                new DohvatiNajnovije((DohvatiNajnovije.IDohvatiNajnovijeDone)FragmentOnline.this).execute(author);
            }
            else if(unos.toLowerCase().contains("korisnik:")){
                String userID = unos.substring(9);
                Intent intent=new Intent(getActivity(),KnjigePoznanika.class);
                receiver=new KnjigePoznanikaReceiver(new Handler());
                receiver.setReceiver(this);
                intent.putExtra("KEY", userID);
                intent.putExtra("RECEIVER",receiver);
                getActivity().startService(intent);
        }
            else {
                new DohvatiKnjige((DohvatiKnjige.IDohvatiKnjigeDone) FragmentOnline.this, false).execute(unos);
            }
        }

        public void onDohvatiDone(List<Knjiga> rez)
        {
            listaKnjigaPretrage =(ArrayList<Knjiga>)rez;
            setSpinnerAdapter(rez);
        }


    @Override
    public void onNajnovijeDone(List<Knjiga> lista) {

        listaKnjigaPretrage =(ArrayList<Knjiga>)lista;
        setSpinnerAdapter(lista);
    }

    @Override
    public void onReceiverResult(int resultCode, Bundle resulData) {
        if (resultCode == STATUS_FINISH) {
            ArrayList<Knjiga> listaKnjiga = resulData.getParcelableArrayList("knjige");
            listaKnjigaPretrage =listaKnjiga;
            setSpinnerAdapter(listaKnjiga);

        } else if (resultCode == STATUS_START) {

        } else {

        }
    }



    public void setSpinnerAdapter(List<Knjiga> lista)
    {
        ArrayList<String> l=new ArrayList<>();

        for (Knjiga k: lista )
        {
            l.add(k.getNaziv());
        }


        ArrayAdapter<String> adapter= new ArrayAdapter<String>(this.getActivity(),android.R.layout.simple_spinner_item, l);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sRezultati.setAdapter(adapter);
    }
}
