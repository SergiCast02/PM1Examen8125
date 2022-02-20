package com.example.pm1examen8125.adaptadores;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.view.menu.MenuView;
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
    View elmContacto[] = new  View[1000];
    int c=-1;
    int c2 = -1;
    boolean wasPaso = false;

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

            //De momento el id de la clase es 0 porque ningun contacto esta selccionado al mostrar el RecyclerView
            setId(0);

            viewPais = itemView.findViewById(R.id.viewPais);
            viewNombre = itemView.findViewById(R.id.viewNombre);
            viewTelefono = itemView.findViewById(R.id.viewTelefono);
            viewNota = itemView.findViewById(R.id.viewNota);

            /*System.out.println("el ItemView es:"+itemView);
            System.out.println(viewPais);
            System.out.println(viewNombre);
            System.out.println(viewTelefono);
            System.out.println(viewNota);*/

            itemView.setOnClickListener(new View.OnClickListener() {
                //@RequiresApi(api = Build.VERSION_CODES.Q)
                //@Override
                public void onClick(View view) {
                    /*System.out.println("----------------------------------------------------");
                    System.out.println("el ItemView es:"+itemView);
                    System.out.println(viewPais);
                    System.out.println(viewNombre);
                    System.out.println(viewTelefono);
                    System.out.println(viewNota);
                    System.out.println("--------------------------------------------------");
                    System.out.println("----------------------------------------------------");
                    System.out.println("el ItemView es:"+view);
                    System.out.println(view);
                    System.out.println(view);
                    System.out.println(view);
                    System.out.println(view);
                    System.out.println("--------------------------------------------------");*/

                    if(wasPaso==true){
                        elmContacto[c].setBackgroundColor(Color.rgb(255,255,255));
                        //El id es 0 lo que significa que ningun contacto esta siendo seleccionado
                        setId(0);
                    }
                    wasPaso = true;
                    c++;
                    elmContacto[c] = itemView;

                    itemView.setBackgroundColor(Color.rgb(149,188,200));
                    setId(listaContactos.get(getAdapterPosition()).getId());

                    //Si ya toco dos veces o más sobre uno o varios contacto
                    if(c>0){
                        //Si el contacto q toco es el mismo del toque anterior
                        if(elmContacto[c]==elmContacto[c-1]){
                            c2++;
                            if(c2%2==0){
                                elmContacto[c].setBackgroundColor(Color.rgb(255,255,255));
                                //El id es 0 lo que significa que ningun contacto esta siendo seleccionado
                                setId(0);
                            }else{
                                elmContacto[c].setBackgroundColor(Color.rgb(149,188,200));
                                setId(listaContactos.get(getAdapterPosition()).getId());
                            }

                        }else {
                            //Inicializar el contador que verifica los toques sobre un mismo contacto
                            c2=-1;
                        }
                    }


                    System.out.println(getId());
                    //Revisar que el array no se llene
                    System.out.println("TOQUES: "+c);
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
