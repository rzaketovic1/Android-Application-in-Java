package com.example.ramiz.rma16859_2018;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by Ramiz on 26.5.2018.
 */



public class FragmentPreporuci extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_preporuci, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        TextView tvKnjiga=(TextView)getView().findViewById(R.id.textViewKnjiga);
        TextView tvAutor=(TextView)getView().findViewById(R.id.textViewAutor);
        TextView tvGodina=(TextView)getView().findViewById(R.id.textViewGodina);
        final Spinner sKontakti=(Spinner)getView().findViewById(R.id.sKontakti);
        Button btnPosalji=(Button)getView().findViewById(R.id.dPosalji);

        Knjiga knjiga=Knjige.getKnjigaZaPreporuku();

        if(knjiga.dodanaOnline)
        {
            tvKnjiga.setText(knjiga.naziv);
            if(knjiga.getAutori().size()!=0)
                tvAutor.setText(knjiga.getAutori().get(0).imeiPrezime);
            else
                tvAutor.setVisibility(getView().GONE);
            if(knjiga.datumObjavljivanja.length() != 0)
                tvGodina.setText(knjiga.datumObjavljivanja);
            else
                tvGodina.setVisibility(getView().GONE);
        }
        else {
            tvKnjiga.setText(knjiga.nazivKnjige);
            tvAutor.setText(knjiga.imeAutora);
            tvGodina.setVisibility(getView().GONE);
        }

        int i=0;
        ActivityCompat.requestPermissions(getActivity(),
                new String[]{Manifest.permission.READ_CONTACTS},
                i);

        final ArrayList<Contact> contacts=fatchContacts();
        ArrayList<String> emails=new ArrayList<>();

        for (Contact contact: contacts) {
            emails.add(contact.getContact_email());
        }

        ArrayAdapter<String> adapter;
        adapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_item, emails);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sKontakti.setAdapter(adapter);

        final Knjiga knjigaZaEmail=knjiga;

        btnPosalji.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name="";
                String email=(String)sKontakti.getSelectedItem();
                for (Contact c: contacts) {
                    if(email.equals(c.getContact_email()))
                        name=c.getContact_name();
                }

                if(knjigaZaEmail.dodanaOnline) {
                    String body = "Zdravo " + name + ",\nProcitaj knjigu " + knjigaZaEmail.naziv + " od " + knjigaZaEmail.getAutori().get(0).imeiPrezime + "!";
                    sendEmail(email, body);
                }else
                {
                    String body = "Zdravo " + name + ",\nProcitaj knjigu " + knjigaZaEmail.nazivKnjige + " od " + knjigaZaEmail.imeAutora + "!";
                    sendEmail(email, body);
                }

            }
        });

    }

    private ArrayList<Contact> fatchContacts(){

        ArrayList<Contact> contacts=new ArrayList<>();
        String name=null;
        String phoneNumber = null;
        String email = null;

        Uri CONTENT_URI = ContactsContract.Contacts.CONTENT_URI;
        String _ID = ContactsContract.Contacts._ID;
        String DISPLAY_NAME = ContactsContract.Contacts.DISPLAY_NAME;
        String HAS_PHONE_NUMBER = ContactsContract.Contacts.HAS_PHONE_NUMBER;

        Uri EmailCONTENT_URI =  ContactsContract.CommonDataKinds.Email.CONTENT_URI;
        String EmailCONTACT_ID = ContactsContract.CommonDataKinds.Email.CONTACT_ID;
        String DATA = ContactsContract.CommonDataKinds.Email.DATA;

        ContentResolver contentResolver = this.getActivity().getContentResolver();

        Cursor cursor = contentResolver.query(CONTENT_URI, null,null, null, null);


        if (cursor.getCount() > 0) {

            while (cursor.moveToNext()) {
                String contact_id = cursor.getString(cursor.getColumnIndex( _ID ));
                 name = cursor.getString(cursor.getColumnIndex( DISPLAY_NAME));

                Cursor emailCursor = contentResolver.query(EmailCONTENT_URI,    null, EmailCONTACT_ID+ " = ?", new String[] { contact_id }, null);
                    while (emailCursor.moveToNext()) {
                        email = emailCursor.getString(emailCursor.getColumnIndex(DATA));
                    }

                emailCursor.close();

                if(email != null)
                {
                    contacts.add(new Contact(name,email));
                    email=null;
                }
            }
        }

        return contacts;
    }

    private void sendEmail(String email, String body) {

        String[] TO = {email};

        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setData(Uri.parse("mailto:"));
        emailIntent.setType("text/plain");
        emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);

        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Your subject");
        emailIntent.putExtra(Intent.EXTRA_TEXT, body);
        try {
            startActivity(Intent.createChooser(emailIntent, "Send mail..."));
        }
        catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(getActivity(), "There is no email client installed.",
                    Toast.LENGTH_SHORT).show();
        }
    }
}










