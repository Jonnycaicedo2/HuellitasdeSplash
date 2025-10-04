package com.example.huellitasdesplash;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.huellitasdesplash.adapters.ProductoAdapter;
import com.example.huellitasdesplash.db.CarritoDAO;
import com.example.huellitasdesplash.db.ProductoDAO;
import com.example.huellitasdesplash.models.CarritoItem;
import com.example.huellitasdesplash.models.Producto;
import java.util.List;

public class TiendaActivity extends AppCompatActivity implements ProductoAdapter.OnProductoClickListener {

    private static final String TAG = "TiendaActivity";

    private RecyclerView recyclerViewProductos;
    private TextView tvSinProductos;
    private Button btnTodos, btnAlimentos, btnJuguetes, btnHigiene, btnAccesorios;
    private ImageButton btnCarrito;
    private ProductoAdapter productoAdapter;
    private ProductoDAO productoDAO;
    private CarritoDAO carritoDAO;
    private SharedPreferences prefs;

    private int usuarioId = 1; // ID de usuario simulado para pruebas

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tienda);

        try {
            Log.d(TAG, "Iniciando TiendaActivity...");

            prefs = getSharedPreferences("app_prefs", MODE_PRIVATE);
            productoDAO = new ProductoDAO(this);
            carritoDAO = new CarritoDAO(this);

            initViews();

            // EJECUTAR DIAGNÓSTICO
            diagnosticoCompleto();

            configurarNavegacion();
            configurarFiltros();
            cargarTodosProductos();

            Log.d(TAG, "TiendaActivity iniciada correctamente");

        } catch (Exception e) {
            Log.e(TAG, "Error crítico en onCreate: " + e.getMessage(), e);
            Toast.makeText(this, "Error al iniciar la tienda. Reintente más tarde.", Toast.LENGTH_LONG).show();
            finish();
        }
    }

    private void diagnosticoCompleto() {
        try {
            Log.d(TAG, "=== INICIANDO DIAGNÓSTICO COMPLETO ===");

            // 1. Verificar base de datos
            boolean bdOk = productoDAO.verificarBaseDeDatos();
            Log.d(TAG, "✅ Base de datos OK: " + bdOk);

            // 2. Contar productos totales
            List<Producto> todosProductos = productoDAO.obtenerTodosProductos();
            Log.d(TAG, "📦 Total productos en BD: " + todosProductos.size());

            // 3. Listar todos los productos por nombre
            for (Producto producto : todosProductos) {
                Log.d(TAG, "   - " + producto.getNombre() + " (ID: " + producto.getId() +
                        ", Categoría: " + producto.getCategoria() + ")");
            }

            // 4. Verificar por categorías
            String[] categorias = {"alimentos", "juguetes", "higiene", "accesorios"};
            for (String categoria : categorias) {
                List<Producto> productosCategoria = productoDAO.obtenerProductosPorCategoria(categoria);
                Log.d(TAG, "🎯 " + categoria + ": " + productosCategoria.size() + " productos");
            }

            // 5. Verificar RecyclerView
            Log.d(TAG, "🔄 RecyclerView: " + (recyclerViewProductos != null ? "INICIALIZADO" : "NULL"));
            Log.d(TAG, "📱 Adapter: " + (productoAdapter != null ? "INICIALIZADO" : "NULL"));

            Log.d(TAG, "=== DIAGNÓSTICO COMPLETADO ===");

        } catch (Exception e) {
            Log.e(TAG, "❌ Error en diagnóstico: " + e.getMessage(), e);
        }
    }

    private void initViews() {
        try {
            recyclerViewProductos = findViewById(R.id.recyclerViewProductos);
            tvSinProductos = findViewById(R.id.tvSinProductos);
            btnTodos = findViewById(R.id.btnTodos);
            btnAlimentos = findViewById(R.id.btnAlimentos);
            btnJuguetes = findViewById(R.id.btnJuguetes);
            btnHigiene = findViewById(R.id.btnHigiene);
            btnAccesorios = findViewById(R.id.btnAccesorios);
            btnCarrito = findViewById(R.id.btnCarrito);

            recyclerViewProductos.setLayoutManager(new LinearLayoutManager(this));

            Log.d(TAG, "Vistas inicializadas correctamente");

        } catch (Exception e) {
            Log.e(TAG, "Error inicializando vistas: " + e.getMessage(), e);
            Toast.makeText(this, "Error al cargar la interfaz", Toast.LENGTH_SHORT).show();
        }
    }

    private void configurarNavegacion() {
        try {
            // Botón Volver al Menú
            Button btnVolverMenu = findViewById(R.id.btnVolverMenu);
            btnVolverMenu.setOnClickListener(v -> {
                try {
                    finish();
                } catch (Exception e) {
                    Log.e(TAG, "Error al volver al menú: " + e.getMessage());
                    finish();
                }
            });

            // Botón Carrito
            btnCarrito.setOnClickListener(v -> {
                try {
                    // Verificar si el carrito tiene items antes de abrir
                    List<CarritoItem> itemsCarrito = carritoDAO.obtenerCarrito(usuarioId);
                    if (itemsCarrito.isEmpty()) {
                        Toast.makeText(TiendaActivity.this, "🛒 Tu carrito está vacío", Toast.LENGTH_SHORT).show();
                    } else {
                        Intent intent = new Intent(TiendaActivity.this, CarritoActivity.class);
                        startActivity(intent);
                    }
                } catch (Exception e) {
                    Log.e(TAG, "Error al abrir carrito: " + e.getMessage());
                    Toast.makeText(TiendaActivity.this, "Error al abrir el carrito", Toast.LENGTH_SHORT).show();
                }
            });

            Log.d(TAG, "Navegación configurada correctamente");

        } catch (Exception e) {
            Log.e(TAG, "Error configurando navegación: " + e.getMessage(), e);
        }
    }

    private void configurarFiltros() {
        try {
            btnTodos.setOnClickListener(v -> {
                try {
                    cargarTodosProductos();
                } catch (Exception e) {
                    Log.e(TAG, "Error al cargar todos los productos: " + e.getMessage());
                    Toast.makeText(this, "Error al cargar productos", Toast.LENGTH_SHORT).show();
                }
            });

            btnAlimentos.setOnClickListener(v -> {
                try {
                    cargarProductosPorCategoria("alimentos");
                } catch (Exception e) {
                    Log.e(TAG, "Error al cargar alimentos: " + e.getMessage());
                    Toast.makeText(this, "Error al cargar alimentos", Toast.LENGTH_SHORT).show();
                }
            });

            btnJuguetes.setOnClickListener(v -> {
                try {
                    cargarProductosPorCategoria("juguetes");
                } catch (Exception e) {
                    Log.e(TAG, "Error al cargar juguetes: " + e.getMessage());
                    Toast.makeText(this, "Error al cargar juguetes", Toast.LENGTH_SHORT).show();
                }
            });

            btnHigiene.setOnClickListener(v -> {
                try {
                    cargarProductosPorCategoria("higiene");
                } catch (Exception e) {
                    Log.e(TAG, "Error al cargar higiene: " + e.getMessage());
                    Toast.makeText(this, "Error al cargar productos de higiene", Toast.LENGTH_SHORT).show();
                }
            });

            btnAccesorios.setOnClickListener(v -> {
                try {
                    cargarProductosPorCategoria("accesorios");
                } catch (Exception e) {
                    Log.e(TAG, "Error al cargar accesorios: " + e.getMessage());
                    Toast.makeText(this, "Error al cargar accesorios", Toast.LENGTH_SHORT).show();
                }
            });

            Log.d(TAG, "Filtros configurados correctamente");

        } catch (Exception e) {
            Log.e(TAG, "Error configurando filtros: " + e.getMessage(), e);
        }
    }

    private void cargarTodosProductos() {
        try {
            Log.d(TAG, "Cargando todos los productos...");
            List<Producto> productos = productoDAO.obtenerTodosProductos();
            Log.d(TAG, "Productos encontrados: " + productos.size());

            mostrarProductos(productos);
            resetearBotonesFiltro();
            btnTodos.setBackgroundTintList(getColorStateList(R.color.colorPrimary));

        } catch (Exception e) {
            Log.e(TAG, "Error cargando todos los productos: " + e.getMessage(), e);
            Toast.makeText(this, "Error al cargar los productos", Toast.LENGTH_SHORT).show();
            mostrarProductosVacio();
        }
    }

    private void cargarProductosPorCategoria(String categoria) {
        try {
            Log.d(TAG, "Cargando productos de categoría: " + categoria);
            List<Producto> productos = productoDAO.obtenerProductosPorCategoria(categoria);
            Log.d(TAG, "Productos encontrados en " + categoria + ": " + productos.size());

            mostrarProductos(productos);
            resetearBotonesFiltro();

            // Resaltar botón activo
            switch (categoria) {
                case "alimentos":
                    btnAlimentos.setBackgroundTintList(getColorStateList(R.color.colorPrimary));
                    break;
                case "juguetes":
                    btnJuguetes.setBackgroundTintList(getColorStateList(R.color.colorPrimary));
                    break;
                case "higiene":
                    btnHigiene.setBackgroundTintList(getColorStateList(R.color.colorPrimary));
                    break;
                case "accesorios":
                    btnAccesorios.setBackgroundTintList(getColorStateList(R.color.colorPrimary));
                    break;
            }

        } catch (Exception e) {
            Log.e(TAG, "Error cargando productos por categoría " + categoria + ": " + e.getMessage(), e);
            Toast.makeText(this, "Error al cargar productos de " + categoria, Toast.LENGTH_SHORT).show();
            mostrarProductosVacio();
        }
    }

    private void resetearBotonesFiltro() {
        try {
            int defaultColor = android.R.color.darker_gray;
            btnTodos.setBackgroundTintList(getColorStateList(defaultColor));
            btnAlimentos.setBackgroundTintList(getColorStateList(defaultColor));
            btnJuguetes.setBackgroundTintList(getColorStateList(defaultColor));
            btnHigiene.setBackgroundTintList(getColorStateList(defaultColor));
            btnAccesorios.setBackgroundTintList(getColorStateList(defaultColor));
        } catch (Exception e) {
            Log.e(TAG, "Error reseteando botones de filtro: " + e.getMessage());
        }
    }

    private void mostrarProductos(List<Producto> productos) {
        try {
            Log.d(TAG, "🔄 Mostrando productos. Cantidad: " + (productos != null ? productos.size() : 0));

            if (productos == null || productos.isEmpty()) {
                Log.d(TAG, "📭 No hay productos para mostrar");
                mostrarProductosVacio();
            } else {
                Log.d(TAG, "📱 Preparando para mostrar " + productos.size() + " productos");

                tvSinProductos.setVisibility(View.GONE);
                recyclerViewProductos.setVisibility(View.VISIBLE);

                // Crear adapter si no existe
                if (productoAdapter == null) {
                    Log.d(TAG, "🆕 Creando nuevo adapter");
                    productoAdapter = new ProductoAdapter(productos, this);
                    recyclerViewProductos.setAdapter(productoAdapter);
                } else {
                    Log.d(TAG, "🔄 Actualizando adapter existente");
                    // Si ya existe, actualizar los datos
                    productoAdapter = new ProductoAdapter(productos, this);
                    recyclerViewProductos.setAdapter(productoAdapter);
                }

                // Forzar actualización
                if (productoAdapter != null) {
                    productoAdapter.notifyDataSetChanged();
                }
                Log.d(TAG, "✅ Adapter configurado y notificado");
            }
        } catch (Exception e) {
            Log.e(TAG, "❌ Error mostrando productos: " + e.getMessage(), e);
            mostrarProductosVacio();
            Toast.makeText(this, "Error al mostrar productos", Toast.LENGTH_SHORT).show();
        }
    }

    private void mostrarProductosVacio() {
        try {
            tvSinProductos.setVisibility(View.VISIBLE);
            recyclerViewProductos.setVisibility(View.GONE);
        } catch (Exception e) {
            Log.e(TAG, "Error mostrando estado vacío: " + e.getMessage());
        }
    }

    @Override
    public void onProductoClick(Producto producto) {
        try {
            Log.d(TAG, "Abriendo detalle del producto: " + producto.getNombre());
            Intent intent = new Intent(this, DetalleProductoActivity.class);
            intent.putExtra("producto_id", producto.getId());
            startActivity(intent);
        } catch (Exception e) {
            Log.e(TAG, "Error al abrir detalle del producto: " + e.getMessage(), e);
            Toast.makeText(this, "Error al abrir detalle del producto", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onAgregarCarritoClick(Producto producto) {
        try {
            Log.d(TAG, "Agregando al carrito: " + producto.getNombre());

            // Verificar stock disponible
            if (producto.getStock() <= 0) {
                Toast.makeText(this, "❌ Producto sin stock disponible", Toast.LENGTH_SHORT).show();
                return;
            }

            // Agregar al carrito usando CarritoDAO REAL
            boolean exito = carritoDAO.agregarAlCarrito(usuarioId, producto.getId(), 1);

            if (exito) {
                Toast.makeText(this, "✅ " + producto.getNombre() + " agregado al carrito", Toast.LENGTH_SHORT).show();

                // Actualizar información del carrito
                actualizarInfoCarrito();
            } else {
                Toast.makeText(this, "❌ Error al agregar al carrito", Toast.LENGTH_SHORT).show();
            }

        } catch (Exception e) {
            Log.e(TAG, "Error al agregar al carrito: " + e.getMessage(), e);
            Toast.makeText(this, "⚠️ Error al agregar al carrito", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Método para actualizar la información del carrito (cantidad de items)
     */
    private void actualizarInfoCarrito() {
        try {
            List<CarritoItem> items = carritoDAO.obtenerCarrito(usuarioId);
            int totalItems = 0;
            for (CarritoItem item : items) {
                totalItems += item.getCantidad();
            }

            Log.d(TAG, "Total items en carrito: " + totalItems);

            // Aquí podrías mostrar un badge en el botón del carrito
            // Por ahora solo lo logueamos
            if (totalItems > 0) {
                Log.d(TAG, "🛒 Carrito tiene " + totalItems + " items");
            }

        } catch (Exception e) {
            Log.e(TAG, "Error actualizando info del carrito: " + e.getMessage());
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Actualizar información del carrito cuando la actividad se reanuda
        actualizarInfoCarrito();
        // Recargar productos por si hubo cambios
        cargarTodosProductos();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "TiendaActivity destruida");
    }

    @Override
    public void onBackPressed() {
        try {
            super.onBackPressed();
        } catch (Exception e) {
            Log.e(TAG, "Error en onBackPressed: " + e.getMessage());
            finish();
        }
    }
}