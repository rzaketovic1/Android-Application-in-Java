package com.example.ramiz.rma16859_2018;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;

public class ListaAutoraAdapter extends ArrayAdapter<Autor> {

    private Activity activity;

    public ListaAutoraAdapter(Activity context, ArrayList<Autor> autori)
    {
        super(context,R.layout.autor_item,autori);
        this.activity = context;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        LayoutInflater temp = LayoutInflater.from(getContext());
        View newView = temp.inflate(R.layout.autor_item, parent, false);

        final Autor autor = getItem(position);

        if(!autor.dodanOnline) {
            TextView imeAutora = (TextView) newView.findViewById(R.id.textViewImeAutora);
            TextView brojKnjiga = (TextView) newView.findViewById(R.id.textViewBrojKnjiga);

            Spinner listaKnjiga=(Spinner) newView.findViewById(R.id.spinnerKnjigeAutora);
            listaKnjiga.setVisibility(newView.GONE);

            brojKnjiga.setText(String.valueOf(autor.brojKnjiga));
            imeAutora.setText(autor.getImeAutora());


            newView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((ListeFragment.FragmentInteraction) activity).onAutorSelected(autor.getImeAutora());
                }
            });

        }else{

            TextView imeAutora = (TextView) newView.findViewById(R.id.textViewImeAutora);
            Spinner knjigeAutora=(Spinner) newView.findViewById(R.id.spinnerKnjigeAutora);
            TextView brojKnjiga = (TextView) newView.findViewById(R.id.textViewBrojKnjiga);
            TextView txt = (TextView) newView.findViewById(R.id.editText);

            brojKnjiga.setVisibility(newView.GONE);
            txt.setVisibility(newView.GONE);

            ArrayList<String> knjige=new ArrayList<>();
            for (String id:autor.knjige) {
                for (Knjiga k: Knjige.getKnjigeOnline()) {
                    for (Autor a: k.autori) {
                        if(a.imeiPrezime.equalsIgnoreCase(autor.imeiPrezime))
                            knjige.add(k.naziv);
                    }
                }
            }



            imeAutora.setText(autor.imeiPrezime);

            if(knjige.size()==1)
            {
                TextView knjiga=(TextView)newView.findViewById(R.id.knjigaAutora);
                knjiga.setText(knjige.get(0).toString());
                knjigeAutora.setVisibility(newView.GONE);
            }else {
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, knjige);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                knjigeAutora.setAdapter(adapter);
            }
            newView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((ListeFragment.FragmentInteraction) activity).onAutorSelected(autor.imeiPrezime);
                }
            });
        }


        return newView;

    }

}