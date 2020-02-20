package com.example.ramiz.rma16859_2018;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import java.io.IOException;
import java.util.ArrayList;

import static android.app.Activity.RESULT_OK;



public class DodavanjeKnjigeFragment extends Fragment {

    Spinner spinner;
    ArrayAdapter<String> adapter;

    EditText etxtimeAutora;
    EditText etxtnazivKnjige;
    Button btnNadjiSliku;
    Button btnUpisiKnjigu;
    Button btnPonisti;
    ImageView slika;

    Drawable drawable;

    private int PICK_IMAGE_REQUEST = 1;

    public ArrayList<String> kategorije;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {

            Uri uri = data.getData();

            try {

                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), uri);

                slika = (ImageView)getActivity().findViewById(R.id.naslovnaStr);
                slika.setImageBitmap(bitmap);

                drawable=new BitmapDrawable(getResources(),bitmap);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }





    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       View view=inflater.inflate(R.layout.dodavanje_knjige_fragment,container,false);

       return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        etxtimeAutora=(EditText)getView().findViewById(R.id.imeAutora);
        etxtnazivKnjige=(EditText)getView().findViewById(R.id.nazivKnjige);
        btnNadjiSliku=(Button)getView().findViewById(R.id.dNadjiSliku);
        btnUpisiKnjigu=(Button)getView().findViewById(R.id.dUpisiKnjigu);
        btnPonisti=(Button)getView().findViewById(R.id.dPonisti);
        spinner = (Spinner)getView().findViewById(R.id.sKategorijaKnjige);

        kategorije = Knjige.getKategorije();

        adapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_item, kategorije);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        btnPonisti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });

        btnUpisiKnjigu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ime = etxtimeAutora.getText().toString();
                String naziv = etxtnazivKnjige.getText().toString();
                String kat=spinner.getSelectedItem().toString();

                Knjiga knjiga=new Knjiga(ime,naziv,kat,drawable);

                Autor autor=new Autor(ime,1);
                Boolean tmp=false;

                for (Autor a : Knjige.getAutori()) {
                    if (a.getImeAutora().equalsIgnoreCase(ime)) {
                        a.brojKnjiga++;
                        tmp=true;
                    }

                }
                if(tmp==false)
                    Knjige.getAutori().add(autor);


                Knjige.getKnjige().add(knjiga);


                etxtimeAutora.setText("");
                etxtnazivKnjige.setText("");









            }
        });


        btnNadjiSliku.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
            }
        });

    }
}
