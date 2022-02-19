package com.example.pm1examen8125;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.pm1examen8125.db.DbContactos;

public class ImagenActivity extends AppCompatActivity {
    ImageView img_contacto;
    Button btn_regresar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_imagen);

        DbContactos dbContactos = new DbContactos(getApplicationContext());

        Bundle extras = getIntent().getExtras();
        int id = extras.getInt("id");

        img_contacto = findViewById(R.id.imagenContacto);
        img_contacto.setImageBitmap(dbContactos.buscarImagen(id));

        btn_regresar = findViewById(R.id.btnRegresar);
        btn_regresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), ContactsActivity.class);
                startActivity(i);
            }
        });
    }
}