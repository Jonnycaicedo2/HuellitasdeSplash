package com.example.huellitasdesplash.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import com.example.huellitasdesplash.models.Reserva;
import java.util.ArrayList;
import java.util.List;

public class ReservaDAO {
    private static final String TAG = "ReservaDAO";
    private DBHelper dbHelper;

    public ReservaDAO(Context context) {
        dbHelper = new DBHelper(context);
    }

    /**
     * Guarda una reserva en la base de datos
     */
    public long guardarReserva(int usuarioId, String servicioNombre, int precio, String fecha, String hora) {
        SQLiteDatabase db = null;
        try {
            db = dbHelper.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put("usuario_id", usuarioId);
            values.put("servicio_nombre", servicioNombre);
            values.put("precio", precio);
            values.put("fecha", fecha);
            values.put("hora", hora);
            values.put("estado", "confirmada");

            long resultado = db.insert("reservas", null, values);

            Log.d(TAG, "Reserva guardada - ID: " + resultado +
                    ", Usuario: " + usuarioId +
                    ", Servicio: " + servicioNombre);

            return resultado;
        } catch (Exception e) {
            Log.e(TAG, "Error guardando reserva: " + e.getMessage(), e);
            return -1;
        } finally {
            if (db != null) {
                try {
                    db.close();
                } catch (Exception e) {
                    Log.e(TAG, "Error cerrando BD: " + e.getMessage());
                }
            }
        }
    }

    /**
     * Obtiene las reservas de un usuario
     */
    public List<Reserva> obtenerReservasUsuario(int usuarioId) {
        List<Reserva> reservas = new ArrayList<>();
        SQLiteDatabase db = null;
        Cursor cursor = null;

        try {
            db = dbHelper.getReadableDatabase();
            cursor = db.rawQuery(
                    "SELECT * FROM reservas WHERE usuario_id = ? ORDER BY fecha DESC, hora DESC",
                    new String[]{String.valueOf(usuarioId)}
            );

            if (cursor != null && cursor.moveToFirst()) {
                do {
                    try {
                        Reserva reserva = new Reserva(
                                cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                                cursor.getInt(cursor.getColumnIndexOrThrow("usuario_id")),
                                cursor.getString(cursor.getColumnIndexOrThrow("servicio_nombre")),
                                cursor.getInt(cursor.getColumnIndexOrThrow("precio")),
                                cursor.getString(cursor.getColumnIndexOrThrow("fecha")),
                                cursor.getString(cursor.getColumnIndexOrThrow("hora")),
                                cursor.getString(cursor.getColumnIndexOrThrow("estado"))
                        );
                        reservas.add(reserva);
                        Log.v(TAG, "Reserva cargada: " + reserva.getServicioNombre());
                    } catch (Exception e) {
                        Log.e(TAG, "Error procesando reserva: " + e.getMessage());
                    }
                } while (cursor.moveToNext());
            }

            Log.d(TAG, "Reservas encontradas: " + reservas.size() + " para usuario: " + usuarioId);

        } catch (Exception e) {
            Log.e(TAG, "Error obteniendo reservas: " + e.getMessage());
        } finally {
            if (cursor != null) {
                try {
                    cursor.close();
                } catch (Exception e) {
                    Log.e(TAG, "Error cerrando cursor: " + e.getMessage());
                }
            }
            if (db != null) {
                try {
                    db.close();
                } catch (Exception e) {
                    Log.e(TAG, "Error cerrando BD: " + e.getMessage());
                }
            }
        }

        return reservas;
    }

    /**
     * Verifica si un usuario tiene reservas
     */
    public boolean usuarioTieneReservas(int usuarioId) {
        SQLiteDatabase db = null;
        Cursor cursor = null;
        try {
            db = dbHelper.getReadableDatabase();
            cursor = db.rawQuery(
                    "SELECT COUNT(*) FROM reservas WHERE usuario_id = ?",
                    new String[]{String.valueOf(usuarioId)}
            );

            if (cursor != null && cursor.moveToFirst()) {
                int count = cursor.getInt(0);
                Log.d(TAG, "Usuario " + usuarioId + " tiene " + count + " reservas");
                return count > 0;
            }
            return false;
        } catch (Exception e) {
            Log.e(TAG, "Error verificando reservas: " + e.getMessage());
            return false;
        } finally {
            if (cursor != null) {
                try {
                    cursor.close();
                } catch (Exception e) {
                    Log.e(TAG, "Error cerrando cursor: " + e.getMessage());
                }
            }
            if (db != null) {
                try {
                    db.close();
                } catch (Exception e) {
                    Log.e(TAG, "Error cerrando BD: " + e.getMessage());
                }
            }
        }
    }

    // ... el resto de tus métodos permanecen igual
    /**
     * Obtiene una reserva por su ID
     */
    public Reserva obtenerReservaPorId(int reservaId) {
        SQLiteDatabase db = null;
        Cursor cursor = null;

        try {
            db = dbHelper.getReadableDatabase();
            cursor = db.rawQuery(
                    "SELECT * FROM reservas WHERE id = ?",
                    new String[]{String.valueOf(reservaId)}
            );

            if (cursor != null && cursor.moveToFirst()) {
                Reserva reserva = new Reserva(
                        cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                        cursor.getInt(cursor.getColumnIndexOrThrow("usuario_id")),
                        cursor.getString(cursor.getColumnIndexOrThrow("servicio_nombre")),
                        cursor.getInt(cursor.getColumnIndexOrThrow("precio")),
                        cursor.getString(cursor.getColumnIndexOrThrow("fecha")),
                        cursor.getString(cursor.getColumnIndexOrThrow("hora")),
                        cursor.getString(cursor.getColumnIndexOrThrow("estado"))
                );
                return reserva;
            }

            return null;

        } catch (Exception e) {
            Log.e(TAG, "Error obteniendo reserva por ID: " + e.getMessage());
            return null;
        } finally {
            if (cursor != null) {
                try {
                    cursor.close();
                } catch (Exception e) {
                    Log.e(TAG, "Error cerrando cursor: " + e.getMessage());
                }
            }
            if (db != null) {
                try {
                    db.close();
                } catch (Exception e) {
                    Log.e(TAG, "Error cerrando BD: " + e.getMessage());
                }
            }
        }
    }

    /**
     * Actualiza el estado de una reserva
     */
    public boolean actualizarEstadoReserva(int reservaId, String nuevoEstado) {
        SQLiteDatabase db = null;
        try {
            db = dbHelper.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put("estado", nuevoEstado);

            int filasAfectadas = db.update("reservas", values, "id = ?",
                    new String[]{String.valueOf(reservaId)});

            Log.d(TAG, "Estado actualizado - Reserva: " + reservaId + ", Nuevo estado: " + nuevoEstado);

            return filasAfectadas > 0;

        } catch (Exception e) {
            Log.e(TAG, "Error actualizando estado: " + e.getMessage());
            return false;
        } finally {
            if (db != null) {
                try {
                    db.close();
                } catch (Exception e) {
                    Log.e(TAG, "Error cerrando BD: " + e.getMessage());
                }
            }
        }
    }

    /**
     * Cancela una reserva
     */
    public boolean cancelarReserva(int reservaId) {
        return actualizarEstadoReserva(reservaId, "cancelada");
    }

    /**
     * Marca una reserva como realizada
     */
    public boolean marcarReservaRealizada(int reservaId) {
        return actualizarEstadoReserva(reservaId, "realizada");
    }
}