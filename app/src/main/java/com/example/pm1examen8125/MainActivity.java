package com.example.pm1examen8125;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
//import android.icu.text.SimpleDateFormat;
import android.graphics.Bitmap;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.pm1examen8125.db.DbContactos;
import com.example.pm1examen8125.db.DbHelper;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    ImageView img_agg;
    Spinner spn_pais;
    EditText txt_nombre;
    EditText txt_telefono;
    EditText txt_nota;
    Button btn_salvarcontacto;
    Button btn_contactos;

    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int REQUEST_IMAGE_SELECT = 2;

    /*
    static final int REQUEST_IMAGE_CAPTURE = 1;

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }


    */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        System.out.println("1.1");
        /*
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            System.out.println("1.2");
            Bundle extras = data.getExtras();
            System.out.println("1.3");
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            System.out.println("1.4");
            img_agg.setImageBitmap(imageBitmap);
            System.out.println("1.5");
        }
        */
        /*
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {

            Uri imageUri = data.getData();
            img_agg.setImageURI(imageUri);
        }
        */
        if(requestCode == REQUEST_IMAGE_SELECT && resultCode == RESULT_OK) {
            Uri imagenUri = data.getData();
            img_agg.setImageURI(imagenUri);
        } else if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            System.out.println("1.1");
            galleryAddPic();
            System.out.println("1.2");
        }
    }


    String currentPhotoPath;

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }



    static final int REQUEST_TAKE_PHOTO = 1;

    private void dispatchTakePictureIntent() {
        System.out.println("1");
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        System.out.println("2");
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            System.out.println("3");
            // Create the File where the photo should go
            File photoFile = null;
            System.out.println("4");
            try {
                System.out.println("5");
                photoFile = createImageFile();
                System.out.println("6");
            } catch (IOException ex) {
                // Error occurred while creating the File
                System.out.println("ups");
            }
            // Continue only if the File was successfully created
            System.out.println("7");
            if (photoFile != null) {
                System.out.println("8");
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.example.android.fileprovider",
                        photoFile);
                System.out.println("9");
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                System.out.println("10");
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
                System.out.println("11");
            }
        }
    }

    private void galleryAddPic() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(currentPhotoPath);
        Uri contentUri = Uri.fromFile(f);
        img_agg.setImageURI(contentUri);
    }

    public void seleccionarImagen() {
        Intent galeria = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(galeria, REQUEST_IMAGE_SELECT);
    }


    public void dec_img_dialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle(R.string.dec_img)
                .setItems(R.array.decs_img, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        if(which==0){
                            dispatchTakePictureIntent();
                        } else if(which==1){
                            seleccionarImagen();
                        }
                    }
                });
        Dialog dialog = builder.create();
        dialog.show();
    }









    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DbHelper dbHelper = new DbHelper(MainActivity.this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        if(db != null){
            System.out.println("Creada la Base de Datos");
        }
        else{alerta("No se creo la Base de Datos",0);}

        //File nuevaCarpeta = new File(getFilesDir(), "miCarpeta");
        //System.out.println(getFilesDir());
        //nuevaCarpeta.mkdirs();

        //Inicializar spn_pais países
        Spinner spinner = (Spinner) findViewById(R.id.spn_pais);
        // Create an ArrayAdapter using the string array and a default spn_pais layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
        R.array.paises, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spn_pais
        spinner.setAdapter(adapter);

        //Boton Ver Contactos Salvados
        btn_contactos = findViewById(R.id.btn_vercontactos);
        btn_contactos.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), ContactsActivity.class);
                startActivity(i);
            }
        });

        //Agregar Imagen o Foto
        //DbContactos dbContactos = new DbContactos(MainActivity.this);
        img_agg = findViewById(R.id.img_agg);
        //img_agg.setImageBitmap(dbContactos.buscarImagen(1));
        img_agg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dec_img_dialog();
            }
        });

        //Salvar Contacto
        btn_salvarcontacto = findViewById(R.id.btn_salvarcontacto);
        btn_salvarcontacto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                spn_pais = findViewById(R.id.spn_pais);
                txt_nombre = findViewById(R.id.txt_nombre);
                txt_telefono = findViewById(R.id.txt_telefono);
                txt_nota = findViewById(R.id.txt_nota);
                img_agg = findViewById(R.id.img_agg);
                String pais = spn_pais.getSelectedItem().toString();
                String nombre = txt_nombre.getText().toString();
                String telefono = txt_telefono.getText().toString();
                String nota = txt_nota.getText().toString();
                img_agg.buildDrawingCache();
                Bitmap imagen = img_agg.getDrawingCache();

                if(nombre.isEmpty()){alerta("Ingrese un Nombre para continuar",0);}
                else if(telefono.isEmpty()){alerta("Ingrese un número telefónico para continuar",0);}
                else if(telefono.length()<8){alerta("El número de teléfono debe ser de 8 dígitos",0);}
                else if(nota.isEmpty()){alerta("Ingrese una Nota para continuar",0);}
                else {
                    DbContactos dbContactos = new DbContactos(MainActivity.this);
                    long id = dbContactos.insertarContacto(imagen,pais, nombre, telefono, nota);
                    if (id>0){
                        alerta("Registro Guardado",0);
                        limpiar();
                        finish();
                        startActivity(getIntent());
                    }
                    else{alerta("Error al Guardar Registro",0);}
                }
            }
        });
    }

    private void limpiar(){
        spn_pais.setSelection(0);
        txt_nombre.setText("");
        txt_telefono.setText("");
        txt_nota.setText("");
    }


    public void alerta(String a, int tipoduracion){
        Toast toast1 = Toast.makeText(getApplicationContext(),a, tipoduracion);
        toast1.show();
    }
}