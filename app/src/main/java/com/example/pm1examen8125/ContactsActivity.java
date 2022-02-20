package com.example.pm1examen8125;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.pm1examen8125.adaptadores.ListaContactosAdapter;
import com.example.pm1examen8125.db.DbContactos;
import com.example.pm1examen8125.db.entidades.Contactos;

import java.util.ArrayList;

public class ContactsActivity extends AppCompatActivity {
    private static final int REQUEST_CODE = 0;
    Button btn_atras;
    Button btn_actualizar;
    Button btn_verimagen;
    Button btn_eliminarimagen;
    Button btn_compartircontacto;

    RecyclerView listaContactos;
    ArrayList<Contactos> listaArrayContactos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);

        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{(Manifest.permission.CALL_PHONE)},
                    REQUEST_CODE);

        }

        btn_compartircontacto = findViewById(R.id.btncompartirContacto);
        btn_compartircontacto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DbContactos dbContactos = new DbContactos(getApplicationContext());
                ListaContactosAdapter lca = new ListaContactosAdapter();

                if(lca.getId()==0){
                    alerta("Seleccione un contacto",0);
                }else{
                    Intent sendIntent = new Intent();
                    sendIntent.setAction(Intent.ACTION_SEND);
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
                    sendIntent.putExtra(Intent.EXTRA_TEXT, dbContactos.verContactos(lca.getId()).getNombre()+"\n"+lada+" "+dbContactos.verContactos(lca.getId()).getTelefono());
                    sendIntent.setType("text/plain");

                    Intent shareIntent = Intent.createChooser(sendIntent, null);
                    startActivity(shareIntent);
                }

            }
        });
        //Se me fue el pajaro y le puse btn_eliminarimagen, debia llamarse btn_eliminarcontacto
        btn_eliminarimagen = findViewById(R.id.btneliminarContacto);
        btn_eliminarimagen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ListaContactosAdapter lca = new ListaContactosAdapter();
                DbContactos dbContactos = new DbContactos(getApplicationContext());

                if(lca.getId()==0){
                    alerta("Seleccione un contacto",0);
                }else{
                    AlertDialog.Builder builder = new AlertDialog.Builder(ContactsActivity.this);
                    builder.setMessage("¿Desea eliminar este contacto?")
                            .setPositiveButton("SI", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    if(dbContactos.eliminarContacto(lca.getId())){
                                        alerta("Eliminado con Éxito",0);
                                        finish();
                                        startActivity(getIntent());
                                    }
                                }
                            })
                            .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                }
                            }).show();
                }
            }
        });

        btn_verimagen = findViewById(R.id.btnverImagen);
        btn_verimagen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ListaContactosAdapter lca = new ListaContactosAdapter();
                Context context = view.getContext();

                if(lca.getId()==0){
                    alerta("Seleccione un contacto",0);
                }else{
                    Intent i = new Intent(context, ImagenActivity.class);
                    i.putExtra("id", lca.getId());
                    //System.out.println(lca.getId());
                    context.startActivity(i);
                }

            }
        });

        btn_actualizar = findViewById(R.id.btnactualizarContacto);
        btn_actualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ListaContactosAdapter lca = new ListaContactosAdapter();
                Context context = view.getContext();

                if(lca.getId()==0){
                    alerta("Seleccione un contacto",0);
                }else{
                    //Intent i = new Intent(context, ViewsActivity.class);
                    Intent i = new Intent(context, EditarActivity.class);
                    i.putExtra("id", lca.getId());
                    System.out.println(lca.getId());
                    context.startActivity(i);
                }
            }
        });

        listaContactos = findViewById(R.id.rv_contactos);
        listaContactos.setLayoutManager(new LinearLayoutManager(this));

        DbContactos dbContactos = new DbContactos(ContactsActivity.this);

        listaArrayContactos = new ArrayList<>();

        ListaContactosAdapter adapter = new ListaContactosAdapter(dbContactos.mostrarContactos());
        listaContactos.setAdapter(adapter);

        btn_atras = findViewById(R.id.btn_atras);
        btn_atras.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(i);
            }
        });
    }

    public void alerta(String a, int tipoduracion){
        Toast toast1 = Toast.makeText(getApplicationContext(),a, tipoduracion);
        toast1.show();
    }
}