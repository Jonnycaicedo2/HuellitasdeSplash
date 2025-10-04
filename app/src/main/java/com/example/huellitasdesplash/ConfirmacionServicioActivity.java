package com.example.huellitasdesplash;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.Locale;

public class ConfirmacionServicioActivity extends AppCompatActivity {

    private TextView tvConfirmacion, tvDetallesServicio;
    private Button btnVolverMenu, btnNuevaReserva;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmacion_servicio);

        tvConfirmacion = findViewById(R.id.tvConfirmacion);
        tvDetallesServicio = findViewById(R.id.tvDetallesServicio);
        btnVolverMenu = findViewById(R.id.btnVolverMenu);
        btnNuevaReserva = findViewById(R.id.btnNuevaReserva);

        // Obtener datos del intent
        Intent intent = getIntent();
        String servicioNombre = intent.getStringExtra("servicio_nombre");
        int servicioPrecio = intent.getIntExtra("servicio_precio", 0);
        String fecha = intent.getStringExtra("fecha");
        String hora = intent.getStringExtra("hora");

        // Mostrar confirmación
        tvConfirmacion.setText("¡Reserva Confirmada! 🎉");

        String detalles = String.format(Locale.getDefault(),
                "Servicio: %s\nPrecio: $%,d\nFecha: %s\nHora: %s\n\n¡Te esperamos!",
                servicioNombre, servicioPrecio, fecha, hora);
        tvDetallesServicio.setText(detalles);

        btnVolverMenu.setOnClickListener(v -> {
            // ✅ VOLVER AL MENÚ PRINCIPAL
            Intent menuIntent = new Intent(this, DashboardActivity.class);
            menuIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(menuIntent);
            finish();
        });

        btnNuevaReserva.setOnClickListener(v -> {
            // ✅ VOLVER A SERVICIOS
            Intent serviciosIntent = new Intent(this, ServiciosActivity.class);
            startActivity(serviciosIntent);
            finish();
        });
    }
}