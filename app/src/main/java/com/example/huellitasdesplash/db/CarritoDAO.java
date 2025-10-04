package com.example.huellitasdesplash.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import com.example.huellitasdesplash.models.CarritoItem;
import com.example.huellitasdesplash.models.Producto;
import java.util.ArrayList;
import java.util.List;

public class CarritoDAO {
    private static final String TAG = "CarritoDAO";
    private DBHelper dbHelper;

    public CarritoDAO(Context context) {
        dbHelper = new DBHelper(context);
    }

    /**
     * Agregar producto al carrito (REAL)
     */
    public boolean agregarAlCarrito(int usuarioId, int productoId, int cantidad) {
        SQLiteDatabase db = null;
        try {
            db = dbHelper.getWritableDatabase();

            // Verificar si el producto ya está en el carrito
            Cursor cursor = db.rawQuery(
                    "SELECT * FROM carrito WHERE usuario_id = ? AND producto_id = ?",
                    new String[]{String.valueOf(usuarioId), String.valueOf(productoId)}
            );

            if (cursor != null && cursor.getCount() > 0) {
                // Actualizar cantidad si ya existe
                cursor.moveToFirst();
                int cantidadActual = cursor.getInt(cursor.getColumnIndexOrThrow("cantidad"));
                ContentValues values = new ContentValues();
                values.put("cantidad", cantidadActual + cantidad);

                int filas = db.update("carrito", values,
                        "usuario_id = ? AND producto_id = ?",
                        new String[]{String.valueOf(usuarioId), String.valueOf(productoId)});

                cursor.close();
                Log.d(TAG, "Producto actualizado en carrito: " + productoId);
                return filas > 0;
            } else {
                // Insertar nuevo item
                if (cursor != null) cursor.close();

                ContentValues values = new ContentValues();
                values.put("usuario_id", usuarioId);
                values.put("producto_id", productoId);
                values.put("cantidad", cantidad);

                long id = db.insert("carrito", null, values);
                Log.d(TAG, "Producto agregado al carrito. ID: " + id);
                return id != -1;
            }

        } catch (SQLException e) {
            Log.e(TAG, "Error SQL al agregar al carrito: " + e.getMessage());
            return false;
        } catch (Exception e) {
            Log.e(TAG, "Error al agregar al carrito: " + e.getMessage());
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
     * Obtener items del carrito (REAL)
     */
    public List<CarritoItem> obtenerCarrito(int usuarioId) {
        List<CarritoItem> carritoItems = new ArrayList<>();
        SQLiteDatabase db = null;
        Cursor cursor = null;

        try {
            db = dbHelper.getReadableDatabase();
            String query = "SELECT c.*, p.nombre, p.descripcion, p.precio, p.categoria, p.stock, p.imagen " +
                    "FROM carrito c " +
                    "INNER JOIN productos p ON c.producto_id = p.id " +
                    "WHERE c.usuario_id = ? " +
                    "ORDER BY c.fecha_agregado DESC";

            cursor = db.rawQuery(query, new String[]{String.valueOf(usuarioId)});

            if (cursor != null && cursor.moveToFirst()) {
                do {
                    try {
                        int productoId = cursor.getInt(cursor.getColumnIndexOrThrow("producto_id"));

                        // Crear producto
                        Producto producto = new Producto(
                                productoId,
                                cursor.getString(cursor.getColumnIndexOrThrow("nombre")),
                                cursor.getString(cursor.getColumnIndexOrThrow("descripcion")),
                                cursor.getInt(cursor.getColumnIndexOrThrow("precio")),
                                cursor.getString(cursor.getColumnIndexOrThrow("categoria")),
                                cursor.getInt(cursor.getColumnIndexOrThrow("stock")),
                                cursor.getString(cursor.getColumnIndexOrThrow("imagen"))
                        );

                        // Crear item del carrito
                        CarritoItem item = new CarritoItem(
                                cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                                usuarioId,
                                producto,
                                cursor.getInt(cursor.getColumnIndexOrThrow("cantidad"))
                        );

                        carritoItems.add(item);
                        Log.v(TAG, "Item cargado: " + producto.getNombre());

                    } catch (Exception e) {
                        Log.e(TAG, "Error procesando item del carrito: " + e.getMessage());
                    }
                } while (cursor.moveToNext());
            } else {
                Log.d(TAG, "Carrito vacío para usuario: " + usuarioId);
            }

        } catch (Exception e) {
            Log.e(TAG, "Error obteniendo carrito: " + e.getMessage());
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

        return carritoItems;
    }

    /**
     * Calcular total del carrito (REAL)
     */
    public int calcularTotalCarrito(int usuarioId) {
        List<CarritoItem> items = obtenerCarrito(usuarioId);
        int total = 0;

        for (CarritoItem item : items) {
            total += item.getSubtotal();
        }

        Log.d(TAG, "Total calculado: $" + total);
        return total;
    }

    /**
     * Eliminar item del carrito (REAL)
     */
    public boolean eliminarDelCarrito(int itemId) {
        SQLiteDatabase db = null;
        try {
            db = dbHelper.getWritableDatabase();
            int filas = db.delete("carrito", "id = ?", new String[]{String.valueOf(itemId)});
            Log.d(TAG, "Item eliminado: " + itemId + ", filas afectadas: " + filas);
            return filas > 0;
        } catch (Exception e) {
            Log.e(TAG, "Error eliminando del carrito: " + e.getMessage());
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
     * Vaciar carrito (REAL)
     */
    public boolean vaciarCarrito(int usuarioId) {
        SQLiteDatabase db = null;
        try {
            db = dbHelper.getWritableDatabase();
            int filas = db.delete("carrito", "usuario_id = ?", new String[]{String.valueOf(usuarioId)});
            Log.d(TAG, "Carrito vaciado. Filas eliminadas: " + filas);
            return true;
        } catch (Exception e) {
            Log.e(TAG, "Error vaciando carrito: " + e.getMessage());
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
     * Actualizar cantidad de un item
     */
    public boolean actualizarCantidad(int itemId, int nuevaCantidad) {
        SQLiteDatabase db = null;
        try {
            db = dbHelper.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put("cantidad", nuevaCantidad);

            int filas = db.update("carrito", values, "id = ?", new String[]{String.valueOf(itemId)});
            Log.d(TAG, "Cantidad actualizada. Item: " + itemId + ", Nueva cantidad: " + nuevaCantidad);
            return filas > 0;
        } catch (Exception e) {
            Log.e(TAG, "Error actualizando cantidad: " + e.getMessage());
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