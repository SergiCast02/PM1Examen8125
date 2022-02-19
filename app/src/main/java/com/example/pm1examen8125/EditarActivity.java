package com.example.pm1examen8125;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.pm1examen8125.db.DbContactos;
import com.example.pm1examen8125.db.entidades.Contactos;

public class EditarActivity extends AppCompatActivity {
    Spinner spn_pais;
    EditText txt_nombre, txt_telefono, txt_nota;
    Button btn_actualizar;

    int id = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        System.out.println("EditarActivity 1");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar);
        System.out.println("EditarActivity 2");

        Bundle extras = getIntent().getExtras();
        id = extras.getInt("id");
        System.out.println("EditarActivity 3");


        Contactos contacto;
        DbContactos dbContactos = new DbContactos(EditarActivity.this);
        contacto = dbContactos.verContactos(id);
        System.out.println("EditarActivity 4");

        spn_pais = findViewById(R.id.spnPais);
        txt_nombre = findViewById(R.id.txtNombre);
        txt_telefono = findViewById(R.id.txtTelefono);
        txt_nota = findViewById(R.id.txtNota);
        btn_actualizar = findViewById(R.id.btnActualizar);

        System.out.println("EditarActivity 4.1");

        // Create an ArrayAdapter using the string array and a default spn_pais layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.paises, android.R.layout.simple_spinner_item);
        System.out.println("EditarActivity 4.2");
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        System.out.println("EditarActivity 4.3");
        // Apply the adapter to the spn_pais
        spn_pais.setAdapter(adapter);
        System.out.println("EditarActivity 4.4");

        if(contacto!=null){
            System.out.println("EditarActivity 5");

            spn_pais.setSelection(adapter.getPosition(contacto.getPais()));
            txt_nombre.setText(contacto.getNombre());
            txt_telefono.setText(contacto.getTelefono());
            txt_nota.setText(contacto.getNota());
        }
        System.out.println("EditarActivity 6");

        btn_actualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("EditarActivity 7");

                String pais = spn_pais.getSelectedItem().toString();
                String nombre = txt_nombre.getText().toString();
                String telefono = txt_telefono.getText().toString();
                String nota = txt_nota.getText().toString();

                System.out.println("EditarActivity 8");

                if(nombre.isEmpty()){alerta("Ingrese un Nombre para continuar",0);}
                else if(telefono.isEmpty()){alerta("Ingrese un número telefónico para continuar",0);}
                else if(nota.isEmpty()){alerta("Ingrese una Nota para continuar",0);}
                else {
                    boolean isCorrecto = dbContactos.editarContacto(id,pais, nombre, telefono, nota);
                    if (isCorrecto){
                        alerta("Registro Actualizado",0);
                        Intent i = new Intent(getApplicationContext(),ContactsActivity.class);
                        startActivity(i);
                    }else{alerta("Error al Actualizar Registro",0);}
                }

                System.out.println("EditarActivity 9");
            }
        });


    }

    public void alerta(String a, int tipoduracion){
        Toast toast1 = Toast.makeText(getApplicationContext(),a, tipoduracion);
        toast1.show();
    }
}