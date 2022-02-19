package com.example.pm1examen8125.adaptadores;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pm1examen8125.ContactsActivity;
import com.example.pm1examen8125.R;
import com.example.pm1examen8125.db.DbContactos;
import com.example.pm1examen8125.db.entidades.Contactos;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class ListaContactosAdapter extends RecyclerView.Adapter<ListaContactosAdapter.ContactoViewHolder> {
    private static int id;
    ArrayList<Contactos> listaContactos;

    public ListaContactosAdapter(ArrayList<Contactos> listaContactos){
        this.listaContactos = listaContactos;
    }

    public ListaContactosAdapter(){

    }

    public static int getId() {
        return id;
    }

    public static void setId(int id) {
        ListaContactosAdapter.id = id;
    }

    @NonNull
    @Override
    public ContactoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.lista_item_contacto,null,false);
        return new ContactoViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull ContactoViewHolder holder, int position) {
        holder.viewPais.setText(listaContactos.get(position).getPais());
        holder.viewNombre.setText(listaContactos.get(position).getNombre());
        holder.viewTelefono.setText(listaContactos.get(position).getTelefono());
        holder.viewNota.setText("Nota: "+listaContactos.get(position).getNota());
    }

    @Override
    public int getItemCount() {
        return listaContactos.size();
    }

    public class ContactoViewHolder extends RecyclerView.ViewHolder {
        TextView viewPais, viewNombre, viewTelefono, viewNota;

        public ContactoViewHolder(@NonNull View itemView) {
            super(itemView);

            viewPais = itemView.findViewById(R.id.viewPais);
            viewNombre = itemView.findViewById(R.id.viewNombre);
            viewTelefono = itemView.findViewById(R.id.viewTelefono);
            viewNota = itemView.findViewById(R.id.viewNota);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    itemView.setBackgroundColor(Color.rgb(149,188,242));
                    setId(listaContactos.get(getAdapterPosition()).getId());
                    System.out.println(getId());
                }
            });

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {

                    Context context = view.getContext();

                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setMessage("¿Desea llamar a este contacto?")
                            .setPositiveButton("SI", new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    ListaContactosAdapter lca = new ListaContactosAdapter();
                                    DbContactos dbContactos = new DbContactos(context);
                                    setId(listaContactos.get(getAdapterPosition()).getId());

                                    Intent callintent = new Intent(Intent.ACTION_CALL);
                                    //System.out.println(dbContactos.verContactos(lca.getId()).getTelefono());
                                    String pais = dbContactos.verContactos(lca.getId()).getPais();

                                    boolean ciclo = true;
                                    String lada="";
                                    int c=1;
                                    while(ciclo==true){
                                        lada = lada + String.valueOf(pais.charAt(pais.length()-(c+1)));
                                        if(String.valueOf(pais.charAt(pais.length()-(c+2))).equals("(")){
                                            ciclo = false;
                                        }
                                        c++;
                                    }
                                    StringBuilder strb = new StringBuilder(lada);
                                    lada = strb.reverse().toString();
                                    System.out.println(lada);
                                    callintent.setData(Uri.parse("tel:"+lada+dbContactos.verContactos(lca.getId()).getTelefono()));
                                    context.startActivity(callintent);




                                }
                            })
                            .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                }
                            }).show();
                    return false;
                }
            });
        }
    }
}
