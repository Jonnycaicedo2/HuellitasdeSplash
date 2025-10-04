package com.example.huellitasdesplash.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import com.example.huellitasdesplash.models.ServicioHistorial;
import java.util.ArrayList;
import java.util.List;

public class HistorialDAO {
    private static final String TAG = "HistorialDAO";
    private DBHelper dbHelper;

    public HistorialDAO(Context context) {
        dbHelper = new DBHelper(context);
    }

    /**
     * Agregar servicio al historial
     */
    public boolean agregarAlHistorial(int usuarioId, String nombreServicio, String descripcion,
                                      int precio, int duracion, String fecha, String estado) {
        SQLiteDatabase db = null;
        try {
            db = dbHelper.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put("usuario_id", usuarioId);
            values.put("nombre_servicio", nombreServicio);
            values.put("descripcion", descripcion);
            values.put("precio", precio);
            values.put("duracion", duracion);
            values.put("fecha_servicio", fecha);
            values.put("estado", estado);
            values.put("fecha_creacion", "datetime('now')");

            long id = db.insert("historial_servicios", null, values);

            boolean exito = id != -1;
            Log.d(TAG, "Servicio agregado al historial - ID: " + id + ", Servicio: " + nombreServicio);

            return exito;

        } catch (SQLException e) {
            Log.e(TAG, "Error SQL agregando al historial: " + e.getMessage());
            return false;
        } catch (Exception e) {
            Log.e(TAG, "Error agregando al historial: " + e.getMessage());
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
     * Obtener historial por usuario
     */
    public List<ServicioHistorial> obtenerHistorialPorUsuario(int usuarioId) {
        List<ServicioHistorial> historial = new ArrayList<>();
        SQLiteDatabase db = null;
        Cursor cursor = null;

        try {
            db = dbHelper.getReadableDatabase();
            String query = "SELECT * FROM historial_servicios WHERE usuario_id = ? ORDER BY fecha_servicio DESC";

            cursor = db.rawQuery(query, new String[]{String.valueOf(usuarioId)});

            if (cursor != null && cursor.moveToFirst()) {
                do {
                    try {
                        ServicioHistorial servicio = new ServicioHistorial(
                                cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                                cursor.getInt(cursor.getColumnIndexOrThrow("usuario_id")),
                                cursor.getString(cursor.getColumnIndexOrThrow("nombre_servicio")),
                                cursor.getString(cursor.getColumnIndexOrThrow("descripcion")),
                                cursor.getInt(cursor.getColumnIndexOrThrow("precio")),
                                cursor.getInt(cursor.getColumnIndexOrThrow("duracion")),
                                cursor.getString(cursor.getColumnIndexOrThrow("fecha_servicio")),
                                cursor.getString(cursor.getColumnIndexOrThrow("estado")),
                                cursor.getString(cursor.getColumnIndexOrThrow("fecha_creacion"))
                        );
                        historial.add(servicio);

                        Log.v(TAG, "Servicio cargado: " + servicio.getNombreServicio());

                    } catch (Exception e) {
                        Log.e(TAG, "Error procesando servicio del historial: " + e.getMessage());
                    }
                } while (cursor.moveToNext());
            } else {
                Log.d(TAG, "Historial vacío para usuario: " + usuarioId);
            }

            Log.d(TAG, "Servicios en historial: " + historial.size());

        } catch (Exception e) {
            Log.e(TAG, "Error obteniendo historial: " + e.getMessage());
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

        return historial;
    }

    /**
     * Obtener servicio del historial por ID
     */
    public ServicioHistorial obtenerServicioHistorialPorId(int historialId) {
        SQLiteDatabase db = null;
        Cursor cursor = null;

        try {
            db = dbHelper.getReadableDatabase();
            cursor = db.rawQuery(
                    "SELECT * FROM historial_servicios WHERE id = ?",
                    new String[]{String.valueOf(historialId)}
            );

            if (cursor != null && cursor.moveToFirst()) {
                ServicioHistorial servicio = new ServicioHistorial(
                        cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                        cursor.getInt(cursor.getColumnIndexOrThrow("usuario_id")),
                        cursor.getString(cursor.getColumnIndexOrThrow("nombre_servicio")),
                        cursor.getString(cursor.getColumnIndexOrThrow("descripcion")),
                        cursor.getInt(cursor.getColumnIndexOrThrow("precio")),
                        cursor.getInt(cursor.getColumnIndexOrThrow("duracion")),
                        cursor.getString(cursor.getColumnIndexOrThrow("fecha_servicio")),
                        cursor.getString(cursor.getColumnIndexOrThrow("estado")),
                        cursor.getString(cursor.getColumnIndexOrThrow("fecha_creacion"))
                );
                return servicio;
            }

            return null;

        } catch (Exception e) {
            Log.e(TAG, "Error obteniendo servicio del historial: " + e.getMessage());
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
     * Actualizar estado de un servicio en el historial
     */
    public boolean actualizarEstadoServicio(int historialId, String nuevoEstado) {
        SQLiteDatabase db = null;
        try {
            db = dbHelper.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put("estado", nuevoEstado);

            int filas = db.update("historial_servicios", values, "id = ?",
                    new String[]{String.valueOf(historialId)});

            boolean exito = filas > 0;
            Log.d(TAG, "Estado actualizado - Servicio: " + historialId + ", Nuevo estado: " + nuevoEstado);

            return exito;

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
     * Verificar si el historial tiene servicios
     */
    public boolean tieneServicios(int usuarioId) {
        SQLiteDatabase db = null;
        Cursor cursor = null;
        try {
            db = dbHelper.getReadableDatabase();
            cursor = db.rawQuery(
                    "SELECT COUNT(*) as count FROM historial_servicios WHERE usuario_id = ?",
                    new String[]{String.valueOf(usuarioId)}
            );

            if (cursor != null && cursor.moveToFirst()) {
                int count = cursor.getInt(cursor.getColumnIndexOrThrow("count"));
                return count > 0;
            }
            return false;
        } catch (Exception e) {
            Log.e(TAG, "Error verificando historial: " + e.getMessage());
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
}