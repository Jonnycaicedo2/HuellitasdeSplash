package com.example.huellitasdesplash.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.database.Cursor;

public class DBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "huellitas.db";
    public static final int DATABASE_VERSION = 3;

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d("DBHelper", "Actualizando BD de versión " + oldVersion + " a " + newVersion);

        if (oldVersion < 2) {
            crearTablasTienda(db);
        }
        if (oldVersion < 3) {
            Log.d("DBHelper", "Actualizando a versión 3");
        }
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            // Tabla usuarios
            String createUsuarios = "CREATE TABLE usuarios (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "nombre TEXT NOT NULL," +
                    "correo TEXT UNIQUE NOT NULL," +
                    "contrasena TEXT NOT NULL" +
                    ");";

            // Tabla mascotas
            String createMascotas = "CREATE TABLE mascotas (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "usuario_id INTEGER NOT NULL," +
                    "nombre TEXT NOT NULL," +
                    "tipo TEXT NOT NULL," +
                    "raza TEXT," +
                    "edad INTEGER," +
                    "foto TEXT," +
                    "FOREIGN KEY(usuario_id) REFERENCES usuarios(id)" +
                    ");";

            // Tabla reservas
            String createReservas = "CREATE TABLE reservas (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "usuario_id INTEGER NOT NULL," +
                    "servicio_nombre TEXT NOT NULL," +
                    "precio INTEGER NOT NULL," +
                    "fecha TEXT NOT NULL," +
                    "hora TEXT NOT NULL," +
                    "estado TEXT DEFAULT 'pendiente'," +
                    "fecha_creacion DATETIME DEFAULT CURRENT_TIMESTAMP," +
                    "FOREIGN KEY(usuario_id) REFERENCES usuarios(id)" +
                    ");";

            // Tabla historial_servicios
            String createHistorialServicios = "CREATE TABLE historial_servicios (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "usuario_id INTEGER NOT NULL," +
                    "nombre_servicio TEXT NOT NULL," +
                    "descripcion TEXT," +
                    "precio INTEGER NOT NULL," +
                    "duracion INTEGER NOT NULL," +
                    "fecha_servicio TEXT NOT NULL," +
                    "estado TEXT DEFAULT 'completado'," +
                    "fecha_creacion DATETIME DEFAULT CURRENT_TIMESTAMP," +
                    "FOREIGN KEY(usuario_id) REFERENCES usuarios(id)" +
                    ");";

            // Ejecutar tablas principales
            db.execSQL(createUsuarios);
            db.execSQL(createMascotas);
            db.execSQL(createReservas);
            db.execSQL(createHistorialServicios);

            // Crear tablas de tienda
            crearTablasTienda(db);

            Log.d("DBHelper", "Base de datos creada exitosamente");

        } catch (Exception e) {
            Log.e("DBHelper", "Error al crear base de datos: " + e.getMessage());
        }
    }

    private void crearTablasTienda(SQLiteDatabase db) {
        try {
            // Tabla productos
            String createProductos = "CREATE TABLE productos (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "nombre TEXT NOT NULL," +
                    "descripcion TEXT," +
                    "precio INTEGER NOT NULL," +
                    "categoria TEXT NOT NULL," +
                    "stock INTEGER DEFAULT 0," +
                    "imagen TEXT" +
                    ");";

            // Tabla carrito
            String createCarrito = "CREATE TABLE carrito (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "usuario_id INTEGER NOT NULL," +
                    "producto_id INTEGER NOT NULL," +
                    "cantidad INTEGER DEFAULT 1," +
                    "fecha_agregado DATETIME DEFAULT CURRENT_TIMESTAMP," +
                    "FOREIGN KEY(usuario_id) REFERENCES usuarios(id)," +
                    "FOREIGN KEY(producto_id) REFERENCES productos(id)" +
                    ");";

            // Tabla pedidos ← IMPORTANTE: ESTA ES LA TABLA NUEVA
            String createPedidos = "CREATE TABLE pedidos (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "usuario_id INTEGER NOT NULL," +
                    "total INTEGER NOT NULL," +
                    "metodo_pago TEXT NOT NULL," +
                    "estado TEXT DEFAULT 'pendiente'," +
                    "fecha_pedido DATETIME DEFAULT CURRENT_TIMESTAMP," +
                    "FOREIGN KEY(usuario_id) REFERENCES usuarios(id)" +
                    ");";

            db.execSQL(createProductos);
            db.execSQL(createCarrito);
            db.execSQL(createPedidos); // ← NO OLVIDAR ESTA LÍNEA

            // Insertar datos de ejemplo solo si no existen
            insertarProductosEjemplo(db);

        } catch (Exception e) {
            Log.e("DBHelper", "Error creando tablas de tienda: " + e.getMessage());
        }
    }

    private void insertarProductosEjemplo(SQLiteDatabase db) {
        try {
            // Verificar si ya hay productos para evitar duplicados
            Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM productos", null);
            boolean tieneDatos = false;
            if (cursor != null && cursor.moveToFirst()) {
                tieneDatos = cursor.getInt(0) > 0;
                cursor.close();
            }

            if (!tieneDatos) {
                // Alimentos
                db.execSQL("INSERT INTO productos (nombre, descripcion, precio, categoria, stock) VALUES " +
                        "('Alimento Premium Adulto', 'Alimento balanceado para perros adultos', 45000, 'alimentos', 50)");
                db.execSQL("INSERT INTO productos (nombre, descripcion, precio, categoria, stock) VALUES " +
                        "('Snacks Dentales', 'Snacks para limpieza dental de perros', 15000, 'alimentos', 30)");
                db.execSQL("INSERT INTO productos (nombre, descripcion, precio, categoria, stock) VALUES " +
                        "('Alimento Gatuno', 'Alimento premium para gatos adultos', 38000, 'alimentos', 40)");

                // Juguetes
                db.execSQL("INSERT INTO productos (nombre, descripcion, precio, categoria, stock) VALUES " +
                        "('Pelota Interactiva', 'Pelota con sonido para perros', 12000, 'juguetes', 25)");
                db.execSQL("INSERT INTO productos (nombre, descripcion, precio, categoria, stock) VALUES " +
                        "('Rascador para Gatos', 'Rascador de 3 niveles con juguetes', 65000, 'juguetes', 15)");

                // Higiene
                db.execSQL("INSERT INTO productos (nombre, descripcion, precio, categoria, stock) VALUES " +
                        "('Shampoo Antipulgas', 'Shampoo para control de pulgas y garrapatas', 18000, 'higiene', 20)");
                db.execSQL("INSERT INTO productos (nombre, descripcion, precio, categoria, stock) VALUES " +
                        "('Cepillo Masajeador', 'Cepillo para masaje y cuidado del pelaje', 22000, 'higiene', 35)");

                // Accesorios
                db.execSQL("INSERT INTO productos (nombre, descripcion, precio, categoria, stock) VALUES " +
                        "('Correa Retráctil', 'Correa retráctil 5 metros', 30000, 'accesorios', 20)");
                db.execSQL("INSERT INTO productos (nombre, descripcion, precio, categoria, stock) VALUES " +
                        "('Cama Ortopédica', 'Cama ortopédica para perros grandes', 85000, 'accesorios', 10)");

                Log.d("DBHelper", "Productos de ejemplo insertados");
            }
        } catch (Exception e) {
            Log.e("DBHelper", "Error insertando productos: " + e.getMessage());
        }
    }
}