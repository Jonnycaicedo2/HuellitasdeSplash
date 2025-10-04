package com.example.huellitasdesplash;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import com.example.huellitasdesplash.db.HistorialDAO;
import com.example.huellitasdesplash.models.ServicioHistorial;
import java.util.Locale;

public class DetalleServicioActivity extends AppCompatActivity {

    private static final String TAG = "DetalleServicioActivity";

    private TextView tvNombreServicioDetalle, tvDescripcionDetalle, tvPrecioDetalle;
    private TextView tvDuracionDetalle, tvFechaDetalle, tvEstadoDetalle;
    private Button btnVolverHistorial;

    private HistorialDAO historialDAO;
    private ServicioHistorial servicio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_servicio);

        try {
            Log.d(TAG, "Iniciando DetalleServicioActivity...");

            historialDAO = new HistorialDAO(this);

            initViews();
            configurarNavegacion();
            cargarDatosServicio();

            Log.d(TAG, "DetalleServicioActivity iniciada correctamente");

        } catch (Exception e) {
            Log.e(TAG, "Error crítico en onCreate: " + e.getMessage(), e);
            Toast.makeText(this, "Error al cargar detalle del servicio", Toast.LENGTH_LONG).show();
            finish();
        }
    }

    private void initViews() {
        try {
            tvNombreServicioDetalle = findViewById(R.id.tvNombreServicioDetalle);
            tvDescripcionDetalle = findViewById(R.id.tvDescripcionDetalle);
            tvPrecioDetalle = findViewById(R.id.tvPrecioDetalle);
            tvDuracionDetalle = findViewById(R.id.tvDuracionDetalle);
            tvFechaDetalle = findViewById(R.id.tvFechaDetalle);
            tvEstadoDetalle = findViewById(R.id.tvEstadoDetalle);
            btnVolverHistorial = findViewById(R.id.btnVolverHistorial);

            Log.d(TAG, "Vistas inicializadas correctamente");

        } catch (Exception e) {
            Log.e(TAG, "Error inicializando vistas: " + e.getMessage(), e);
            Toast.makeText(this, "Error al cargar la interfaz", Toast.LENGTH_SHORT).show();
        }
    }

    private void configurarNavegacion() {
        try {
            btnVolverHistorial.setOnClickListener(v -> finish());
        } catch (Exception e) {
            Log.e(TAG, "Error configurando navegación: " + e.getMessage());
        }
    }

    private void cargarDatosServicio() {
        try {
            int servicioHistorialId = getIntent().getIntExtra("servicio_historial_id", -1);
            Log.d(TAG, "Cargando servicio ID: " + servicioHistorialId);

            if (servicioHistorialId == -1) {
                Toast.makeText(this, "Error: Servicio no válido", Toast.LENGTH_SHORT).show();
                finish();
                return;
            }

            servicio = historialDAO.obtenerServicioHistorialPorId(servicioHistorialId);

            if (servicio == null) {
                Toast.makeText(this, "Servicio no encontrado", Toast.LENGTH_SHORT).show();
                finish();
                return;
            }

            mostrarDatosServicio();

        } catch (Exception e) {
            Log.e(TAG, "Error cargando datos del servicio: " + e.getMessage(), e);
            Toast.makeText(this, "Error al cargar información del servicio", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void mostrarDatosServicio() {
        try {
            // Nombre y descripción
            tvNombreServicioDetalle.setText(servicio.getNombreServicio());
            tvDescripcionDetalle.setText(servicio.getDescripcion());

            // Precio formateado
            tvPrecioDetalle.setText(servicio.getPrecioFormateado());

            // Duración formateada
            tvDuracionDetalle.setText(servicio.getDuracionFormateada());

            // Fecha
            tvFechaDetalle.setText(servicio.getFechaServicio());

            // Estado con color
            tvEstadoDetalle.setText(servicio.getEstado());
            int colorEstado = android.graphics.Color.parseColor(servicio.getColorEstado());
            tvEstadoDetalle.setTextColor(colorEstado);

            Log.d(TAG, "Datos del servicio mostrados: " + servicio.getNombreServicio());

        } catch (Exception e) {
            Log.e(TAG, "Error mostrando datos del servicio: " + e.getMessage());
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "DetalleServicioActivity destruida");
    }
}