package com.example.ramiz.rma16859_2018;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;




public class ListaKnjigaAdapter extends ArrayAdapter<Knjiga> {






    public ListaKnjigaAdapter(Context context, ArrayList<Knjiga> knjige) {
        super(context, R.layout.knjiga_item, knjige);

    }




    @NonNull
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        final Knjiga knjiga = getItem(position);



        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.knjiga_item, parent, false);
        }

        Button btnPreporuka=(Button)convertView.findViewById(R.id.dPreporuci);
        btnPreporuka.setTag(position);


        ImageView slika = (ImageView) convertView.findViewById(R.id.eNaslovna);
        TextView nazivKnjige = (TextView) convertView.findViewById(R.id.eNaziv);
        TextView imeAutora = (TextView) convertView.findViewById(R.id.eAutor);
        Spinner autori = (Spinner) convertView.findViewById(R.id.spinnerAutoriKnjige);
        TextView deskrpcija = (TextView) convertView.findViewById(R.id.eOpis);
        TextView publishDate = (TextView) convertView.findViewById(R.id.eDatumObjavljivanja);
        TextView brojStranica=(TextView)convertView.findViewById(R.id.eBrojStranica);
        TextView txtbrojStranica=(TextView)convertView.findViewById(R.id.txtbrojStranica);
        ImageView urlToSlika=(ImageView) convertView.findViewById(R.id.eNaslovna);
        TextView tvBrojStranica=(TextView)convertView.findViewById(R.id.txtbrojStranica);



        if (!knjiga.dodanaOnline)
        {

            slika.setImageDrawable(knjiga.getSlika());
            nazivKnjige.setText(knjiga.getNazivKnjige());
            imeAutora.setText(knjiga.getImeAutora());

            autori.setVisibility(convertView.GONE);
            deskrpcija.setText("");
            publishDate.setText("");
            brojStranica.setText("");
            txtbrojStranica.setText("");
    }
        else {

            if(knjiga.slika!=null)
            {
                Picasso.with(this.getContext()).load(knjiga.slika.toString())//.placeholder(R.mipmap.ic_launcher)
                        //.error(R.mipmap.ic_launcher)
                        .into(urlToSlika, new com.squareup.picasso.Callback() {
                                    @Override
                                    public void onSuccess() {

                                    }

                                    @Override
                                    public void onError() {

                                    }
                                }

                        );


            }
            else
              urlToSlika.setImageResource(R.mipmap.ic_launcher);

            nazivKnjige.setText(knjiga.getNaziv());

            if(knjiga.getAutori().size()==1)
            {
                imeAutora.setText(knjiga.getAutori().get(0).imeiPrezime);
                autori.setVisibility(convertView.GONE);
            }
            else if(knjiga.getAutori().size()==0)
            {
                imeAutora.setText("");
                autori.setVisibility(convertView.GONE);
            }
            else {
                imeAutora.setText("");

                ArrayList<String> autors=new ArrayList<>();
                for (Autor a: knjiga.getAutori()) {
                    autors.add(a.imeiPrezime);
                }

                ArrayAdapter<String> adapter= new ArrayAdapter<String>(getContext(),android.R.layout.simple_spinner_item, autors);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                autori.setAdapter(adapter);


            }

            if(knjiga.opis.length()!=0)
            {
                deskrpcija.setText(knjiga.opis);
            }
            else
                deskrpcija.setText("");
            if(knjiga.datumObjavljivanja.equals(0))
            {
                publishDate.setText("");
            }
            else
                publishDate.setText(knjiga.datumObjavljivanja.toString());

            if(knjiga.brojStranica==0)
            {
                brojStranica.setText("Nepoznato");

            }else {
                brojStranica.setText(String.valueOf(knjiga.brojStranica));
            }



        }
            if (knjiga.isMarked) {
                convertView.setBackgroundColor(getContext().getResources().getColor(R.color.svijetloPlava));
            } else

                convertView.setBackgroundColor(getContext().getResources().getColor(R.color.bijela));



        btnPreporuka.setTag(position);

        btnPreporuka.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position=(Integer)v.getTag();
                Bundle bundle = new Bundle();
                Knjiga k=getItem(position);

                String naziv;
                if(k.dodanaOnline)
                     naziv=k.naziv;
                else
                    naziv=k.nazivKnjige;

                 Knjige.setKnjigaZaPreporuku(k);

              bundle.putString("KNJIGA", naziv);
                Intent intent= new Intent(getContext(), OnlineAkt.class);
                intent.putExtras(bundle);
                getContext().startActivity(intent);
            }
        });





            return convertView;





    }





}