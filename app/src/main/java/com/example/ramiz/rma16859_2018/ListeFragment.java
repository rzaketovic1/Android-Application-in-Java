package com.example.ramiz.rma16859_2018;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.ListView;

import java.util.ArrayList;

import static com.example.ramiz.rma16859_2018.BazaOpenHelper.DATABASE_NAME;
import static com.example.ramiz.rma16859_2018.BazaOpenHelper.DATABASE_VERSION;
import static com.example.ramiz.rma16859_2018.KategorijeAkt.KATEGORIJA_EXTRAS;



public class ListeFragment extends Fragment {




    public ArrayList<Knjiga> knjige;
    public  ArrayList<String> kategorije;
    private boolean isKategorije = true;

     EditText txt;
     Button btnPretraga;
     Button btnDodajKat;
     Button btnDodajKnj;
     ListView lista;
    Button btnKategorije;
    Button btnAutori;
    Button btnDodajKnjiguOnline;
    ArrayAdapter<String> adapter;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.liste_fragment, container, false);


        return view;


    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        final BazaOpenHelper bazaOpenHelper=new BazaOpenHelper(getActivity(), DATABASE_NAME, null, DATABASE_VERSION);





        if(Knjige.getKategorije()==null)
        {
            Knjige.getKategorije().addAll(bazaOpenHelper.kategorijeIzBaze());
        }


        knjige=Knjige.getKnjige();
        kategorije=Knjige.getKategorije();



        txt=(EditText)getView().findViewById(R.id.tekstPretraga);
        btnPretraga=(Button)getView().findViewById(R.id.dPretraga);
        btnDodajKat=(Button)getView().findViewById(R.id.dDodajKategoriju);
        btnDodajKnj=(Button)getView().findViewById(R.id.dDodajKnjigu);
        lista =(ListView)getView().findViewById(R.id.listaKategorija);
        btnKategorije=(Button)getView().findViewById(R.id.dKategoriju);
        btnAutori=(Button)getView().findViewById(R.id.dAutori);
        btnDodajKnjiguOnline=(Button)getView().findViewById(R.id.dDodajOnline);


        if (getArguments()== null || !getArguments().getBoolean(KATEGORIJA_EXTRAS)) {
            isKategorije = false;
            podesiFragmentZaAutore();
        } else {
            podesiFragmentKategorije();
        }

        btnDodajKat.setEnabled(false);

        btnPretraga.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {


                adapter.getFilter().filter(txt.getText().toString(), new Filter.FilterListener() {
                    @Override
                    public void onFilterComplete(int count) {
                        if (count == 0)
                            btnDodajKat.setEnabled(true);
                        else
                            btnDodajKat.setEnabled(false);
                    }
                });
            }
        });


        btnDodajKat.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                kategorije.add(txt.getText().toString());
               bazaOpenHelper.dodajKategoriju(txt.getText().toString());
                adapter.notifyDataSetChanged();
                txt.setText("");
                btnDodajKat.setEnabled(false);
            }});

        btnDodajKnj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().startActivity(new Intent(getActivity(), DodavanjeKnjigeAkt.class));
            }
        });

        lista.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String itemListe = lista.getItemAtPosition(position).toString();
                ((FragmentInteraction) getActivity()).onKategorijeSelected(itemListe);
            }
        });

        btnKategorije.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isKategorije && !((KategorijeAkt)getActivity()).wideScreen) {
                    getActivity().getSupportFragmentManager().popBackStack();
                } else {

                    //  adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, Knjige.getKategorije());

                    adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, kategorije);

                    lista.setAdapter(adapter);
                    btnDodajKat.setVisibility(View.VISIBLE);
                    btnPretraga.setVisibility(View.VISIBLE);
                    txt.setVisibility(View.VISIBLE);
                }
            }
        });

        btnDodajKnjiguOnline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {




                getActivity().startActivity(new Intent(getActivity(),OnlineAkt.class));
            }
        });

        btnAutori.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((FragmentInteraction) getActivity()).onAutorFragment();
            }
        });
    }

    private void podesiFragmentKategorije() {

        //  adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, Knjige.getKategorije());
        adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, kategorije);
        lista.setAdapter(adapter);
    }

    private void podesiFragmentZaAutore() {
        btnDodajKat.setVisibility(View.GONE);
        btnPretraga.setVisibility(View.GONE);
        txt.setVisibility(View.GONE);

        ArrayList<Autor> autori=new ArrayList<>();
        autori.addAll(Knjige.getAutori());
        autori.addAll(Knjige.getAutoriOnline());

        final ListaAutoraAdapter adapter = new ListaAutoraAdapter(getActivity(),autori);
        lista.setAdapter(adapter);
    }

    public interface FragmentInteraction {
        void onKategorijeSelected(String kategorija);
        void onAutorSelected(String imeAutora);
        void onAutorFragment();
    }
}
