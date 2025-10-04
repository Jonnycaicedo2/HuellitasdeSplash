package com.example.huellitasdesplash.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class UsuarioDAO {
    private DBHelper helper;

    public UsuarioDAO(Context context) {
        helper = new DBHelper(context);
    }

    /**
     * Registra un usuario y retorna el id de la fila insertada, o -1 si hay error
     */
    public long registrarUsuario(String nombre, String correo, String contrasenaPlain) {
        String cleanNombre = nombre.trim();
        String cleanCorreo = correo.trim().toLowerCase();
        String cleanPassword = contrasenaPlain.trim();

        String hashed = sha256(cleanPassword);

        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("nombre", cleanNombre);
        cv.put("correo", cleanCorreo);
        cv.put("contrasena", hashed);

        return db.insert("usuarios", null, cv);
    }

    /**
     * Retorna id del usuario si login correcto, -1 si no
     */
    public int login(String correo, String contrasenaPlain) {
        String cleanCorreo = correo.trim().toLowerCase();
        String cleanPassword = contrasenaPlain.trim();
        String hashed = sha256(cleanPassword);

        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor c = null;
        try {
            c = db.rawQuery("SELECT id FROM usuarios WHERE correo=? AND contrasena=?",
                    new String[]{cleanCorreo, hashed});
            if (c.moveToFirst()) {
                return c.getInt(0);
            } else {
                return -1;
            }
        } finally {
            if (c != null) c.close();
        }
    }

    /**
     * Verifica si ya existe un correo registrado
     */
    public boolean existeCorreo(String correo) {
        String cleanCorreo = correo.trim().toLowerCase();
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor c = null;
        try {
            c = db.rawQuery("SELECT id FROM usuarios WHERE correo=?", new String[]{cleanCorreo});
            return c.moveToFirst();
        } finally {
            if (c != null) c.close();
        }
    }

    /**
     * Obtiene el nombre del usuario por su ID
     */
    public String obtenerNombreUsuario(int usuarioId) {
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor c = null;
        try {
            c = db.rawQuery("SELECT nombre FROM usuarios WHERE id=?",
                    new String[]{String.valueOf(usuarioId)});
            if (c.moveToFirst()) {
                return c.getString(0);
            } else {
                return null; // No se encontró usuario
            }
        } finally {
            if (c != null) c.close();
        }
    }

    /**
     * Función de hash SHA-256
     */
    private String sha256(String base) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(base.getBytes(StandardCharsets.UTF_8));
            StringBuilder hex = new StringBuilder();
            for (byte b : hash) {
                String h = Integer.toHexString(0xff & b);
                if (h.length() == 1) hex.append('0');
                hex.append(h);
            }
            return hex.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return base; // fallback (no recomendable para producción)
        }
    }
    /**
     * Obtiene el correo del usuario por su ID
     */
    public String obtenerCorreoUsuario(int usuarioId) {
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor c = null;
        try {
            c = db.rawQuery("SELECT correo FROM usuarios WHERE id=?",
                    new String[]{String.valueOf(usuarioId)});
            if (c.moveToFirst()) {
                return c.getString(0);
            } else {
                return null;
            }
        } finally {
            if (c != null) c.close();
        }
    }

    /**
     * Actualiza el nombre del usuario
     */
    public boolean actualizarNombreUsuario(int usuarioId, String nuevoNombre) {
        SQLiteDatabase db = helper.getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put("nombre", nuevoNombre.trim());

        int rowsAffected = db.update(
                "usuarios",
                cv,
                "id = ?",
                new String[]{String.valueOf(usuarioId)}
        );

        db.close();
        return rowsAffected > 0;
    }

    /**
     * Actualiza el perfil completo del usuario (nombre y correo)
     */
    public boolean actualizarPerfilCompleto(int usuarioId, String nombre, String correo) {
        SQLiteDatabase db = helper.getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put("nombre", nombre.trim());
        cv.put("correo", correo.trim().toLowerCase());

        int rowsAffected = db.update(
                "usuarios",
                cv,
                "id = ?",
                new String[]{String.valueOf(usuarioId)}
        );

        db.close();
        return rowsAffected > 0;
    }

    /**
     * Verifica si un correo ya existe excluyendo al usuario actual
     */
    public boolean existeCorreoExcluyendoUsuario(int usuarioId, String correo) {
        String cleanCorreo = correo.trim().toLowerCase();
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor c = null;
        try {
            c = db.rawQuery("SELECT id FROM usuarios WHERE correo=? AND id != ?",
                    new String[]{cleanCorreo, String.valueOf(usuarioId)});
            return c.moveToFirst();
        } finally {
            if (c != null) c.close();
        }
    }
}
