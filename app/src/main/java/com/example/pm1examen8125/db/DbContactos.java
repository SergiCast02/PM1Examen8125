package com.example.pm1examen8125.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;

import androidx.annotation.Nullable;

import com.example.pm1examen8125.db.entidades.Contactos;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.sql.Blob;
import java.util.ArrayList;

public class DbContactos extends DbHelper{

    Context context;

    public DbContactos(@Nullable Context context) {
        super(context);
        this.context = context;
    }

    public long insertarContacto(Bitmap imagen, String pais, String nombre, String telefono, String nota){
        /*
        public long insertarContacto(Blob imagen, String pais, String nombre, String telefono, String nota){
        long id = 0;
        try {
            DbHelper dbHelper = new DbHelper(context);
            SQLiteDatabase db = dbHelper.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put("imagen", imagen);
            values.put("pais", pais);
            values.put("nombre", nombre);
            values.put("telefono", telefono);
            values.put("nota", nota);

            id = db.insert(TABLE_CONTACTOS, null, values);
        } catch (Exception e){
            e.toString();
        }

        return id;
        */

        // tamaño del baos depende del tamaño de tus imagenes en promedio
        long id = 0;
        ByteArrayOutputStream baos = new ByteArrayOutputStream(20480);
        imagen.compress(Bitmap.CompressFormat.PNG, 0 , baos);
        byte[] blob = baos.toByteArray();
        // aqui tenemos el byte[] con el imagen comprimido, ahora lo guardemos en SQLite
        DbHelper dbHelper = new DbHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        String sql = "INSERT INTO "+ TABLE_CONTACTOS + " (imagen, pais, nombre, telefono, nota) VALUES(?,?,?,?,?)";
        SQLiteStatement insert = db.compileStatement(sql);
        insert.clearBindings();
        insert.bindBlob(1, blob);
        insert.bindString(2,pais);
        insert.bindString(3,nombre);
        insert.bindString(4,telefono);
        insert.bindString(5,nota);
        id = insert.executeInsert();
        db.close();

        return id;
    }

    public Bitmap buscarImagen(long id){
        DbHelper dbHelper = new DbHelper(context);
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String sql = String.format("SELECT * FROM " + TABLE_CONTACTOS + " WHERE id = %d", id);
        Cursor cursor = db.rawQuery(sql, new String[] {});
        Bitmap bitmap = null;
        if(cursor.moveToFirst()){
            byte[] blob = cursor.getBlob(1);
            ByteArrayInputStream bais = new ByteArrayInputStream(blob);
            bitmap = BitmapFactory.decodeStream(bais);
        }
        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }
        db.close();
        return bitmap;
    }

    public ArrayList<Contactos> mostrarContactos(){
        DbHelper dbHelper = new DbHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ArrayList<Contactos> listaContactos = new ArrayList<>();
        Contactos contacto = null;
        Cursor cursorContactos = null;

        cursorContactos = db.rawQuery("SELECT * FROM " + TABLE_CONTACTOS,null);

        if(cursorContactos.moveToFirst()){
            do{
                contacto = new Contactos();
                contacto.setId(cursorContactos.getInt(0));
                contacto.setImagen(cursorContactos.getBlob(1));
                contacto.setPais(cursorContactos.getString(2));
                contacto.setNombre(cursorContactos.getString(3));
                contacto.setTelefono(cursorContactos.getString(4));
                contacto.setNota(cursorContactos.getString(5));
                listaContactos.add(contacto);
            }while (cursorContactos.moveToNext());
        }

        cursorContactos.close();

        return listaContactos;
    }

    public Contactos verContactos(int id){
        DbHelper dbHelper = new DbHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        Contactos contacto = null;
        Cursor cursorContactos = null;

        cursorContactos = db.rawQuery("SELECT * FROM " + TABLE_CONTACTOS + " WHERE id = " + id + " LIMIT 1",null);

        if(cursorContactos.moveToFirst()){
            contacto = new Contactos();
            contacto.setId(cursorContactos.getInt(0));
            contacto.setImagen(cursorContactos.getBlob(1));
            contacto.setPais(cursorContactos.getString(2));
            contacto.setNombre(cursorContactos.getString(3));
            contacto.setTelefono(cursorContactos.getString(4));
            contacto.setNota(cursorContactos.getString(5));
        }

        cursorContactos.close();

        return contacto;
    }


    public boolean editarContacto(int id, String pais, String nombre, String telefono, String nota){

        boolean isCorrecto = false;
        DbHelper dbHelper = new DbHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        try {
            db.execSQL("UPDATE " + TABLE_CONTACTOS + " SET pais = '" + pais + "', nombre = '" + nombre + "', telefono = '" + telefono + "', nota = '" + nota + "' WHERE id = '" + id + "'");
            isCorrecto = true;
        } catch (Exception e){
            e.toString();
            isCorrecto = false;
        } finally {
            db.close();
        }

        return isCorrecto;
    }

    public boolean eliminarContacto(int id) {

        boolean isCorrecto = false;

        DbHelper dbHelper = new DbHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        try {
            db.execSQL("DELETE FROM " + TABLE_CONTACTOS + " WHERE id = '" + id + "'");
            isCorrecto = true;
        } catch (Exception ex) {
            ex.toString();
            isCorrecto = false;
        } finally {
            db.close();
        }

        return isCorrecto;
    }
}
