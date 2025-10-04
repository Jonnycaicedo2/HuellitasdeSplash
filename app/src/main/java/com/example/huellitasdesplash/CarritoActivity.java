package com.example.huellitasdesplash;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.huellitasdesplash.adapters.CarritoAdapter;
import com.example.huellitasdesplash.db.CarritoDAO;
import com.example.huellitasdesplash.models.CarritoItem;
import java.util.List;

public class CarritoActivity extends AppCompatActivity {

    private static final String TAG = "CarritoActivity";
    private TextView tvCarritoVacio, tvSubtotal, tvTotal;
    private Button btnVolverTiendaCarrito, btnContinuarCompra;
    private RecyclerView recyclerViewCarrito;
    private CarritoDAO carritoDAO;
    private CarritoAdapter carritoAdapter;
    private int usuarioId = 1; // ID de usuario simulado para pruebas

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carrito);

        try {
            carritoDAO = new CarritoDAO(this);
            initViews();
            configurarNavegacion();
            configurarRecyclerView();
            cargarCarrito();
        } catch (Exception e) {
            Log.e(TAG, "Error en onCreate: " + e.getMessage());
            Toast.makeText(this, "Error al cargar carrito", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void initViews() {
        tvCarritoVacio = findViewById(R.id.tvCarritoVacio);
        tvSubtotal = findViewById(R.id.tvSubtotal);
        tvTotal = findViewById(R.id.tvTotal);
        btnVolverTiendaCarrito = findViewById(R.id.btnVolverTiendaCarrito);
        btnContinuarCompra = findViewById(R.id.btnContinuarCompra);
        recyclerViewCarrito = findViewById(R.id.recyclerViewCarrito);
    }

    private void configurarRecyclerView() {
        recyclerViewCarrito.setLayoutManager(new LinearLayoutManager(this));
        // El adapter se configurará cuando carguemos los datos
    }

    private void configurarNavegacion() {
        btnVolverTiendaCarrito.setOnClickListener(v -> finish());

        btnContinuarCompra.setOnClickListener(v -> {
            try {
                List<CarritoItem> items = carritoDAO.obtenerCarrito(usuarioId);
                if (items.isEmpty()) {
                    Toast.makeText(this, "El carrito está vacío", Toast.LENGTH_SHORT).show();
                } else {
                    // Navegar a método de pago con el total
                    int total = carritoDAO.calcularTotalCarrito(usuarioId);
                    Intent intent = new Intent(this, MetodoPagoActivity.class);
                    intent.putExtra("total", total);
                    startActivity(intent);
                }
            } catch (Exception e) {
                Log.e(TAG, "Error al continuar compra: " + e.getMessage());
                Toast.makeText(this, "Error al procesar compra", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void cargarCarrito() {
        try {
            List<CarritoItem> carritoItems = carritoDAO.obtenerCarrito(usuarioId);
            int total = carritoDAO.calcularTotalCarrito(usuarioId);

            if (carritoItems.isEmpty()) {
                mostrarCarritoVacio();
            } else {
                mostrarCarritoConItems(carritoItems, total);
            }
        } catch (Exception e) {
            Log.e(TAG, "Error cargando carrito: " + e.getMessage());
            mostrarCarritoVacio();
            Toast.makeText(this, "Error al cargar el carrito", Toast.LENGTH_SHORT).show();
        }
    }

    private void mostrarCarritoVacio() {
        tvCarritoVacio.setVisibility(View.VISIBLE);
        recyclerViewCarrito.setVisibility(View.GONE);
        findViewById(R.id.cardResumen).setVisibility(View.GONE);
    }

    private void mostrarCarritoConItems(List<CarritoItem> items, int total) {
        tvCarritoVacio.setVisibility(View.GONE);
        recyclerViewCarrito.setVisibility(View.VISIBLE);
        findViewById(R.id.cardResumen).setVisibility(View.VISIBLE);

        // Configurar adapter
        carritoAdapter = new CarritoAdapter(items, new CarritoAdapter.OnCarritoItemClickListener() {
            @Override
            public void onEliminarClick(int itemId) {
                eliminarItem(itemId);
            }

            @Override
            public void onCantidadChange(int itemId, int nuevaCantidad) {
                actualizarCantidad(itemId, nuevaCantidad);
            }
        });

        recyclerViewCarrito.setAdapter(carritoAdapter);

        // Actualizar totales
        tvSubtotal.setText("$" + total);
        tvTotal.setText("$" + total);

        Log.d(TAG, "Carrito cargado con " + items.size() + " items. Total: $" + total);
    }

    private void eliminarItem(int itemId) {
        try {
            boolean exito = carritoDAO.eliminarDelCarrito(itemId);
            if (exito) {
                Toast.makeText(this, "Producto eliminado del carrito", Toast.LENGTH_SHORT).show();
                cargarCarrito(); // Recargar carrito
            } else {
                Toast.makeText(this, "Error al eliminar producto", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Log.e(TAG, "Error eliminando item: " + e.getMessage());
            Toast.makeText(this, "Error al eliminar producto", Toast.LENGTH_SHORT).show();
        }
    }

    private void actualizarCantidad(int itemId, int nuevaCantidad) {
        try {
            if (nuevaCantidad <= 0) {
                eliminarItem(itemId);
            } else {
                boolean exito = carritoDAO.actualizarCantidad(itemId, nuevaCantidad);
                if (exito) {
                    cargarCarrito(); // Recargar para actualizar totales
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "Error actualizando cantidad: " + e.getMessage());
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Recargar carrito por si hubo cambios
        cargarCarrito();
    }
}