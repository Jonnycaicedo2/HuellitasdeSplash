package com.example.huellitasdesplash.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import com.example.huellitasdesplash.models.Producto;
import java.util.ArrayList;
import java.util.List;

public class ProductoDAO {
    private static final String TAG = "ProductoDAO";
    private DBHelper dbHelper;

    public ProductoDAO(Context context) {
        try {
            dbHelper = new DBHelper(context);
            Log.d(TAG, "ProductoDAO inicializado correctamente");
        } catch (Exception e) {
            Log.e(TAG, "Error al inicializar ProductoDAO: " + e.getMessage(), e);
            throw new RuntimeException("No se pudo inicializar ProductoDAO", e);
        }
    }

    /**
     * Obtiene todos los productos
     */
    public List<Producto> obtenerTodosProductos() {
        List<Producto> productos = new ArrayList<>();
        SQLiteDatabase db = null;
        Cursor cursor = null;

        try {
            db = dbHelper.getReadableDatabase();
            Log.d(TAG, "Obteniendo todos los productos...");

            cursor = db.rawQuery("SELECT * FROM productos ORDER BY nombre", null);

            if (cursor != null && cursor.moveToFirst()) {
                do {
                    try {
                        Producto producto = new Producto(
                                cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                                cursor.getString(cursor.getColumnIndexOrThrow("nombre")),
                                cursor.getString(cursor.getColumnIndexOrThrow("descripcion")),
                                cursor.getInt(cursor.getColumnIndexOrThrow("precio")),
                                cursor.getString(cursor.getColumnIndexOrThrow("categoria")),
                                cursor.getInt(cursor.getColumnIndexOrThrow("stock")),
                                cursor.getString(cursor.getColumnIndexOrThrow("imagen"))
                        );
                        productos.add(producto);
                        Log.v(TAG, "Producto cargado: " + producto.getNombre());
                    } catch (Exception e) {
                        Log.e(TAG, "Error procesando fila de producto: " + e.getMessage());
                        // Continuar con el siguiente producto
                    }
                } while (cursor.moveToNext());
            } else {
                Log.w(TAG, "No se encontraron productos en la base de datos");
            }

            Log.d(TAG, "Total de productos cargados: " + productos.size());

        } catch (SQLException e) {
            Log.e(TAG, "Error de SQL al obtener productos: " + e.getMessage(), e);
        } catch (Exception e) {
            Log.e(TAG, "Error inesperado al obtener productos: " + e.getMessage(), e);
        } finally {
            // Cerrar recursos en el bloque finally
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
                    Log.e(TAG, "Error cerrando base de datos: " + e.getMessage());
                }
            }
        }

        return productos;
    }

    /**
     * Obtiene productos por categoría
     */
    public List<Producto> obtenerProductosPorCategoria(String categoria) {
        List<Producto> productos = new ArrayList<>();
        SQLiteDatabase db = null;
        Cursor cursor = null;

        try {
            if (categoria == null || categoria.trim().isEmpty()) {
                Log.w(TAG, "Categoría nula o vacía proporcionada");
                return productos;
            }

            db = dbHelper.getReadableDatabase();
            Log.d(TAG, "Obteniendo productos de categoría: " + categoria);

            cursor = db.rawQuery(
                    "SELECT * FROM productos WHERE categoria = ? ORDER BY nombre",
                    new String[]{categoria}
            );

            if (cursor != null && cursor.moveToFirst()) {
                do {
                    try {
                        Producto producto = new Producto(
                                cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                                cursor.getString(cursor.getColumnIndexOrThrow("nombre")),
                                cursor.getString(cursor.getColumnIndexOrThrow("descripcion")),
                                cursor.getInt(cursor.getColumnIndexOrThrow("precio")),
                                cursor.getString(cursor.getColumnIndexOrThrow("categoria")),
                                cursor.getInt(cursor.getColumnIndexOrThrow("stock")),
                                cursor.getString(cursor.getColumnIndexOrThrow("imagen"))
                        );
                        productos.add(producto);
                        Log.v(TAG, "Producto cargado: " + producto.getNombre() + " - " + categoria);
                    } catch (Exception e) {
                        Log.e(TAG, "Error procesando fila de producto en categoría " + categoria + ": " + e.getMessage());
                        // Continuar con el siguiente producto
                    }
                } while (cursor.moveToNext());
            } else {
                Log.w(TAG, "No se encontraron productos para la categoría: " + categoria);
            }

            Log.d(TAG, "Productos cargados para categoría " + categoria + ": " + productos.size());

        } catch (SQLException e) {
            Log.e(TAG, "Error de SQL al obtener productos por categoría: " + e.getMessage(), e);
        } catch (Exception e) {
            Log.e(TAG, "Error inesperado al obtener productos por categoría: " + e.getMessage(), e);
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
                    Log.e(TAG, "Error cerrando base de datos: " + e.getMessage());
                }
            }
        }

        return productos;
    }

    /**
     * Obtiene un producto por ID
     */
    public Producto obtenerProductoPorId(int productoId) {
        SQLiteDatabase db = null;
        Cursor cursor = null;

        try {
            if (productoId <= 0) {
                Log.w(TAG, "ID de producto inválido: " + productoId);
                return null;
            }

            db = dbHelper.getReadableDatabase();
            Log.d(TAG, "Buscando producto con ID: " + productoId);

            cursor = db.rawQuery(
                    "SELECT * FROM productos WHERE id = ?",
                    new String[]{String.valueOf(productoId)}
            );

            if (cursor != null && cursor.moveToFirst()) {
                Producto producto = new Producto(
                        cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                        cursor.getString(cursor.getColumnIndexOrThrow("nombre")),
                        cursor.getString(cursor.getColumnIndexOrThrow("descripcion")),
                        cursor.getInt(cursor.getColumnIndexOrThrow("precio")),
                        cursor.getString(cursor.getColumnIndexOrThrow("categoria")),
                        cursor.getInt(cursor.getColumnIndexOrThrow("stock")),
                        cursor.getString(cursor.getColumnIndexOrThrow("imagen"))
                );
                Log.d(TAG, "Producto encontrado: " + producto.getNombre());
                return producto;
            } else {
                Log.w(TAG, "No se encontró producto con ID: " + productoId);
                return null;
            }

        } catch (SQLException e) {
            Log.e(TAG, "Error de SQL al obtener producto por ID: " + e.getMessage(), e);
            return null;
        } catch (Exception e) {
            Log.e(TAG, "Error inesperado al obtener producto por ID: " + e.getMessage(), e);
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
                    Log.e(TAG, "Error cerrando base de datos: " + e.getMessage());
                }
            }
        }
    }

    /**
     * Verifica si la tabla de productos existe y tiene datos
     */
    public boolean verificarBaseDeDatos() {
        SQLiteDatabase db = null;
        Cursor cursor = null;

        try {
            db = dbHelper.getReadableDatabase();

            // Verificar si la tabla existe
            cursor = db.rawQuery(
                    "SELECT name FROM sqlite_master WHERE type='table' AND name='productos'",
                    null
            );

            boolean tablaExiste = cursor != null && cursor.getCount() > 0;
            if (cursor != null) cursor.close();

            if (!tablaExiste) {
                Log.e(TAG, "La tabla 'productos' no existe");
                return false;
            }

            // Verificar si hay datos
            cursor = db.rawQuery("SELECT COUNT(*) as count FROM productos", null);
            boolean tieneDatos = false;

            if (cursor != null && cursor.moveToFirst()) {
                int count = cursor.getInt(cursor.getColumnIndexOrThrow("count"));
                tieneDatos = count > 0;
                Log.d(TAG, "Número de productos en BD: " + count);
            }

            Log.d(TAG, "Verificación de BD: Tabla existe=" + tablaExiste + ", Tiene datos=" + tieneDatos);
            return tablaExiste && tieneDatos;

        } catch (Exception e) {
            Log.e(TAG, "Error verificando base de datos: " + e.getMessage(), e);
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
                    Log.e(TAG, "Error cerrando base de datos: " + e.getMessage());
                }
            }
        }
    }

    /**
     * Obtiene todas las categorías disponibles
     */
    public List<String> obtenerCategorias() {
        List<String> categorias = new ArrayList<>();
        SQLiteDatabase db = null;
        Cursor cursor = null;

        try {
            db = dbHelper.getReadableDatabase();
            cursor = db.rawQuery("SELECT DISTINCT categoria FROM productos ORDER BY categoria", null);

            if (cursor != null && cursor.moveToFirst()) {
                do {
                    try {
                        String categoria = cursor.getString(cursor.getColumnIndexOrThrow("categoria"));
                        categorias.add(categoria);
                    } catch (Exception e) {
                        Log.e(TAG, "Error procesando categoría: " + e.getMessage());
                    }
                } while (cursor.moveToNext());
            }

            Log.d(TAG, "Categorías encontradas: " + categorias.size());

        } catch (Exception e) {
            Log.e(TAG, "Error obteniendo categorías: " + e.getMessage(), e);
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
                    Log.e(TAG, "Error cerrando base de datos: " + e.getMessage());
                }
            }
        }

        return categorias;
    }

    /**
     * Actualiza el stock de un producto
     */
    public boolean actualizarStock(int productoId, int nuevoStock) {
        SQLiteDatabase db = null;

        try {
            if (productoId <= 0) {
                Log.w(TAG, "ID de producto inválido para actualizar stock: " + productoId);
                return false;
            }

            db = dbHelper.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put("stock", nuevoStock);

            int filasAfectadas = db.update("productos", values, "id = ?",
                    new String[]{String.valueOf(productoId)});

            boolean exito = filasAfectadas > 0;
            Log.d(TAG, "Actualización de stock - Producto ID: " + productoId +
                    ", Nuevo stock: " + nuevoStock + ", Éxito: " + exito);

            return exito;

        } catch (Exception e) {
            Log.e(TAG, "Error actualizando stock: " + e.getMessage(), e);
            return false;
        } finally {
            if (db != null) {
                try {
                    db.close();
                } catch (Exception e) {
                    Log.e(TAG, "Error cerrando base de datos: " + e.getMessage());
                }
            }
        }
    }
}