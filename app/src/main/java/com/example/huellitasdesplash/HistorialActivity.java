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
import com.example.huellitasdesplash.adapters.HistorialAdapter;
import com.example.huellitasdesplash.db.HistorialDAO;
import com.example.huellitasdesplash.models.ServicioHistorial;
import java.util.List;

public class HistorialActivity extends AppCompatActivity implements HistorialAdapter.OnHistorialClickListener {

    private static final String TAG = "HistorialActivity";

    private RecyclerView recyclerViewHistorial;
    private TextView tvHistorialVacio;
    private Button btnVolverMenuHistorial;
    private HistorialAdapter historialAdapter;
    private HistorialDAO historialDAO;

    private int usuarioId = 1; // Usuario simulado

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historial);

        try {
            Log.d(TAG, "Iniciando HistorialActivity...");

            historialDAO = new HistorialDAO(this);

            initViews();
            configurarNavegacion();
            cargarHistorial();

            Log.d(TAG, "HistorialActivity iniciada correctamente");

        } catch (Exception e) {
            Log.e(TAG, "Error crítico en onCreate: " + e.getMessage(), e);
            Toast.makeText(this, "Error al cargar el historial", Toast.LENGTH_LONG).show();
            finish();
        }
    }

    private void initViews() {
        try {
            recyclerViewHistorial = findViewById(R.id.recyclerViewHistorial);
            tvHistorialVacio = findViewById(R.id.tvHistorialVacio);
            btnVolverMenuHistorial = findViewById(R.id.btnVolverMenuHistorial);

            recyclerViewHistorial.setLayoutManager(new LinearLayoutManager(this));

            Log.d(TAG, "Vistas inicializadas correctamente");

        } catch (Exception e) {
            Log.e(TAG, "Error inicializando vistas: " + e.getMessage(), e);
            Toast.makeText(this, "Error al cargar la interfaz", Toast.LENGTH_SHORT).show();
        }
    }

    private void configurarNavegacion() {
        try {
            btnVolverMenuHistorial.setOnClickListener(v -> finish());
        } catch (Exception e) {
            Log.e(TAG, "Error configurando navegación: " + e.getMessage());
        }
    }

    private void cargarHistorial() {
        try {
            Log.d(TAG, "Cargando historial de servicios...");
            List<ServicioHistorial> serviciosHistorial = historialDAO.obtenerHistorialPorUsuario(usuarioId);
            Log.d(TAG, "Servicios en historial: " + serviciosHistorial.size());

            mostrarHistorial(serviciosHistorial);

        } catch (Exception e) {
            Log.e(TAG, "Error cargando historial: " + e.getMessage(), e);
            Toast.makeText(this, "Error al cargar el historial", Toast.LENGTH_SHORT).show();
            mostrarHistorialVacio();
        }
    }

    private void mostrarHistorial(List<ServicioHistorial> servicios) {
        try {
            Log.d(TAG, "Mostrando historial. Cantidad: " + (servicios != null ? servicios.size() : 0));

            if (servicios == null || servicios.isEmpty()) {
                Log.d(TAG, "No hay servicios en el historial");
                mostrarHistorialVacio();
            } else {
                Log.d(TAG, "Preparando para mostrar " + servicios.size() + " servicios");

                tvHistorialVacio.setVisibility(View.GONE);
                recyclerViewHistorial.setVisibility(View.VISIBLE);

                historialAdapter = new HistorialAdapter(servicios, this);
                recyclerViewHistorial.setAdapter(historialAdapter);

                Log.d(TAG, "Adapter de historial configurado");
            }
        } catch (Exception e) {
            Log.e(TAG, "Error mostrando historial: " + e.getMessage(), e);
            mostrarHistorialVacio();
            Toast.makeText(this, "Error al mostrar el historial", Toast.LENGTH_SHORT).show();
        }
    }

    private void mostrarHistorialVacio() {
        try {
            tvHistorialVacio.setVisibility(View.VISIBLE);
            recyclerViewHistorial.setVisibility(View.GONE);
        } catch (Exception e) {
            Log.e(TAG, "Error mostrando estado vacío: " + e.getMessage());
        }
    }

    @Override
    public void onServicioClick(ServicioHistorial servicio) {
        try {
            Log.d(TAG, "Abriendo detalle del servicio: " + servicio.getNombreServicio());
            Intent intent = new Intent(this, DetalleServicioActivity.class);
            intent.putExtra("servicio_historial_id", servicio.getId());
            startActivity(intent);
        } catch (Exception e) {
            Log.e(TAG, "Error al abrir detalle del servicio: " + e.getMessage(), e);
            Toast.makeText(this, "Error al abrir detalle del servicio", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Recargar historial por si hubo cambios
        cargarHistorial();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "HistorialActivity destruida");
    }
}