package com.example.huellitasdesplash.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import com.example.huellitasdesplash.models.Pedido;
import java.util.ArrayList;
import java.util.List;

public class PedidoDAO {
    private static final String TAG = "PedidoDAO";
    private DBHelper dbHelper;

    public PedidoDAO(Context context) {
        dbHelper = new DBHelper(context);
    }

    /**
     * Crear un nuevo pedido
     */
    public boolean crearPedido(int usuarioId, int total, String metodoPago) {
        SQLiteDatabase db = null;
        try {
            db = dbHelper.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put("usuario_id", usuarioId);
            values.put("total", total);
            values.put("metodo_pago", metodoPago);
            values.put("estado", "completado");
            values.put("fecha_pedido", "datetime('now')");

            long id = db.insert("pedidos", null, values);

            boolean exito = id != -1;
            Log.d(TAG, "Pedido creado - ID: " + id + ", Usuario: " + usuarioId + ", Total: $" + total);

            return exito;

        } catch (SQLException e) {
            Log.e(TAG, "Error SQL creando pedido: " + e.getMessage());
            return false;
        } catch (Exception e) {
            Log.e(TAG, "Error creando pedido: " + e.getMessage());
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
     * Obtener pedidos por usuario
     */
    public List<Pedido> obtenerPedidosPorUsuario(int usuarioId) {
        List<Pedido> pedidos = new ArrayList<>();
        SQLiteDatabase db = null;
        Cursor cursor = null;

        try {
            db = dbHelper.getReadableDatabase();
            String query = "SELECT * FROM pedidos WHERE usuario_id = ? ORDER BY fecha_pedido DESC";

            cursor = db.rawQuery(query, new String[]{String.valueOf(usuarioId)});

            if (cursor != null && cursor.moveToFirst()) {
                do {
                    try {
                        Pedido pedido = new Pedido(
                                cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                                cursor.getInt(cursor.getColumnIndexOrThrow("usuario_id")),
                                cursor.getInt(cursor.getColumnIndexOrThrow("total")),
                                cursor.getString(cursor.getColumnIndexOrThrow("metodo_pago")),
                                cursor.getString(cursor.getColumnIndexOrThrow("estado")),
                                cursor.getString(cursor.getColumnIndexOrThrow("fecha_pedido"))
                        );
                        pedidos.add(pedido);

                    } catch (Exception e) {
                        Log.e(TAG, "Error procesando pedido: " + e.getMessage());
                    }
                } while (cursor.moveToNext());
            }

            Log.d(TAG, "Pedidos encontrados para usuario " + usuarioId + ": " + pedidos.size());

        } catch (Exception e) {
            Log.e(TAG, "Error obteniendo pedidos: " + e.getMessage());
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

        return pedidos;
    }

    /**
     * Obtener un pedido por ID
     */
    public Pedido obtenerPedidoPorId(int pedidoId) {
        SQLiteDatabase db = null;
        Cursor cursor = null;

        try {
            db = dbHelper.getReadableDatabase();
            cursor = db.rawQuery(
                    "SELECT * FROM pedidos WHERE id = ?",
                    new String[]{String.valueOf(pedidoId)}
            );

            if (cursor != null && cursor.moveToFirst()) {
                Pedido pedido = new Pedido(
                        cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                        cursor.getInt(cursor.getColumnIndexOrThrow("usuario_id")),
                        cursor.getInt(cursor.getColumnIndexOrThrow("total")),
                        cursor.getString(cursor.getColumnIndexOrThrow("metodo_pago")),
                        cursor.getString(cursor.getColumnIndexOrThrow("estado")),
                        cursor.getString(cursor.getColumnIndexOrThrow("fecha_pedido"))
                );
                return pedido;
            }

            return null;

        } catch (Exception e) {
            Log.e(TAG, "Error obteniendo pedido: " + e.getMessage());
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
     * Actualizar estado de un pedido
     */
    public boolean actualizarEstadoPedido(int pedidoId, String nuevoEstado) {
        SQLiteDatabase db = null;
        try {
            db = dbHelper.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put("estado", nuevoEstado);

            int filas = db.update("pedidos", values, "id = ?",
                    new String[]{String.valueOf(pedidoId)});

            boolean exito = filas > 0;
            Log.d(TAG, "Estado actualizado - Pedido: " + pedidoId + ", Nuevo estado: " + nuevoEstado);

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
}