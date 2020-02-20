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
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;

import static com.example.ramiz.rma16859_2018.KategorijeAkt.AUTOR_EXTRAS;
import static com.example.ramiz.rma16859_2018.KategorijeAkt.KATEGORIJA_EXTRAS;

public class KnjigeFragment extends Fragment {

    ListView lvlistaKnjiga;
    Button btnPovratak;

    ListaKnjigaAdapter adapter;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.knjige_fragment,container,false);

        return view;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        lvlistaKnjiga=(ListView)getView().findViewById(R.id.listaKnjiga);
        btnPovratak=(Button)getView().findViewById(R.id.dPovratak);

        if (((KategorijeAkt) getActivity()).wideScreen) {
            btnPovratak.setVisibility(View.GONE);
        }

        String kategorija = getArguments().getString(KATEGORIJA_EXTRAS);

        ArrayList<Knjiga> knjigeIsteKategorije = new ArrayList<>();

        if (kategorija != null) {
            for (Knjiga knjiga : Knjige.getKnjige()) {
                if (knjiga.getKategorija().equalsIgnoreCase(kategorija))
                    knjigeIsteKategorije.add(knjiga);
            }
            for(Knjiga knjiga : Knjige.getKnjigeOnline()){
                if (knjiga.kategorijaOnline.equalsIgnoreCase(kategorija))
                    knjigeIsteKategorije.add(knjiga);
            }
        }


        String autor = getArguments().getString(AUTOR_EXTRAS);

        ArrayList<Knjiga> knjigeIstogAutora=new ArrayList<>();
        if(autor != null) {
            for (Knjiga knjiga : Knjige.getKnjige()) {
                if(knjiga.getImeAutora().equalsIgnoreCase(autor))
                    knjigeIstogAutora.add(knjiga);
            }
            for(Knjiga knjiga: Knjige.getKnjigeOnline()){
                if(KnjigeAutora(knjiga.getAutori(), autor))
                    knjigeIstogAutora.add(knjiga);
            }
        }


        if(knjigeIsteKategorije.size() != 0) {
            adapter = new ListaKnjigaAdapter(getActivity(), knjigeIsteKategorije);
        }else {
            adapter=new ListaKnjigaAdapter(getActivity(),knjigeIstogAutora);
        }

        lvlistaKnjiga.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        btnPovratak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(),KategorijeAkt.class);
                startActivity(intent);
            }
        });


        lvlistaKnjiga.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                ((Knjiga)parent.getItemAtPosition(position)).isMarked = true;
                view.setBackgroundColor(getResources().getColor(R.color.svijetloPlava));
            }
        });


    }

    boolean KnjigeAutora(ArrayList<Autor> autori, String imeAutora)
    {
        for (Autor autor: autori) {
            if(autor.imeiPrezime.equals(imeAutora))
                return true;
        }
        return false;
    }
}