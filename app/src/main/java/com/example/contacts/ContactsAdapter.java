package com.example.contacts;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;
import java.util.Locale;

public class ContactsAdapter extends ArrayAdapter<Contacts>
{
    private Context context;
    private List<Contacts> contacts;

    public ContactsAdapter(Context context, List<Contacts> list){

        super(context,R.layout.row_layout, list);
        this.context = context;
        this.contacts = list;

    }

    @NonNull
    @Override
    public View getView (int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        convertView = inflater.inflate(R.layout.row_layout, parent, false);

        TextView tvChar = convertView.findViewById(R.id.tvChar);
        TextView tvName = convertView.findViewById(R.id.tvName);
        TextView tvMail = convertView.findViewById(R.id.tvMail);


        tvChar.setText(contacts.get(position).getName().toUpperCase().charAt(0)+ "");
        tvName.setText(contacts.get(position).getName());
        tvMail.setText(contacts.get(position).getEmail());


        return convertView;
    }
}
