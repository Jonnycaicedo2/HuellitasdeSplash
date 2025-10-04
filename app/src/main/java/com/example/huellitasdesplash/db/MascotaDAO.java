package com.example.huellitasdesplash.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.Cursor;


public class MascotaDAO {

    private DBHelper dbHelper;

    public MascotaDAO(Context context) {
        dbHelper = new DBHelper(context);
    }

    /**
     * Registra una mascota en la base de datos.
     * @param nombre Nombre de la mascota
     * @param tipo Tipo (perro/gato)
     * @param raza Raza (opcional)
     * @param edad Edad (opcional)
     * @param foto URI de la foto (opcional, puede ser null)
     * @param usuarioId ID del usuario dueño de la mascota
     * @return ID de la fila insertada, -1 si error
     */
    public long registrarMascota(String nombre, String tipo, String raza, int edad, String foto, int usuarioId) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("nombre", nombre);
        values.put("tipo", tipo);
        values.put("raza", raza);
        values.put("edad", edad);
        values.put("foto", foto);              // puede ser null
        values.put("usuario_id", usuarioId);   // coincide con la tabla
        return db.insert("mascotas", null, values);
    }
    // En MascotaDAO.java
    public boolean usuarioTieneMascotas(int usuarioId) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM mascotas WHERE usuario_id = ?",
                new String[]{String.valueOf(usuarioId)});
        cursor.moveToFirst();
        int count = cursor.getInt(0);
        cursor.close();
        return count > 0;
    }

}
