package com.example.pm1examen8125;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.pm1examen8125.db.DbContactos;
import com.example.pm1examen8125.db.entidades.Contactos;

public class ViewsActivity extends AppCompatActivity {

    EditText txt_pais, txt_nombre, txt_telefono, txt_nota;
    Button btn_actualizar;

    Contactos contacto;
    int id = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_views);

        txt_pais = findViewById(R.id.txtPais);
        txt_nombre = findViewById(R.id.txtNombre);
        txt_telefono = findViewById(R.id.txtTelefono);
        txt_nota = findViewById(R.id.txtNota);
        btn_actualizar = findViewById(R.id.btnActualizar);

        if(savedInstanceState == null){
            Bundle extras = getIntent().getExtras();
            if(extras==null){
                id = Integer.parseInt(null);
            } else{
                id = extras.getInt("id");
            }
        } else{
            id = (int) savedInstanceState.getSerializable("id");
        }

        DbContactos dbContactos = new DbContactos(ViewsActivity.this);
        contacto = dbContactos.verContactos(id);

        if(contacto!=null){
            txt_pais.setText(contacto.getPais());
            txt_nombre.setText(contacto.getNombre());
            txt_telefono.setText(contacto.getTelefono());
            txt_nota.setText(contacto.getNota());
            //Ocultar Boton
            btn_actualizar.setVisibility(View.INVISIBLE);
            //Inhabilitar teclado de los edittext
            txt_pais.setInputType(InputType.TYPE_NULL);
            txt_nombre.setInputType(InputType.TYPE_NULL);
            txt_telefono.setInputType(InputType.TYPE_NULL);
            txt_nota.setInputType(InputType.TYPE_NULL);
        }
    }
}