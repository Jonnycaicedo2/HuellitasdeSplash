package com.example.huellitasdesplash;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.huellitasdesplash.db.ReservaDAO;
import java.util.Calendar;
import android.widget.TimePicker;

public class CalendarioActivity extends AppCompatActivity {

    private static final String TAG = "CalendarioActivity";

    private TextView tvServicioSeleccionado, tvPrecioServicio;
    private DatePicker datePicker;
    private TimePicker timePicker;
    private Button btnContinuar;

    private String servicioNombre;
    private int servicioPrecio;
    private ReservaDAO reservaDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendario);

        // Obtener datos del servicio
        Intent intent = getIntent();
        servicioNombre = intent.getStringExtra("servicio");
        servicioPrecio = intent.getIntExtra("precio", 0);

        // Inicializar ReservaDAO
        reservaDAO = new ReservaDAO(this);

        initViews();
        setupServiceInfo();
        setupButton();
    }

    private void initViews() {
        tvServicioSeleccionado = findViewById(R.id.tvServicioSeleccionado);
        tvPrecioServicio = findViewById(R.id.tvPrecioServicio);
        datePicker = findViewById(R.id.datePicker);
        timePicker = findViewById(R.id.timePicker);
        btnContinuar = findViewById(R.id.btnContinuar);

        // Configurar fecha mínima (hoy)
        Calendar calendar = Calendar.getInstance();
        datePicker.setMinDate(calendar.getTimeInMillis());

        // Configurar timePicker en formato 24h
        timePicker.setIs24HourView(true);
    }

    private void setupServiceInfo() {
        tvServicioSeleccionado.setText(getString(R.string.servicio_seleccionado, servicioNombre));
        tvPrecioServicio.setText(getString(R.string.precio_servicio, servicioPrecio));
    }

    private void setupButton() {
        btnContinuar.setOnClickListener(v -> {
            Log.d(TAG, "Click en Continuar - Servicio: " + servicioNombre);
            proceedToConfirmation();
        });
    }

    private void proceedToConfirmation() {
        try {
            // Obtener fecha seleccionada
            int day = datePicker.getDayOfMonth();
            int month = datePicker.getMonth() + 1; // Month es 0-based
            int year = datePicker.getYear();
            String fecha = String.format("%02d/%02d/%d", day, month, year);

            // Obtener hora seleccionada
            int hour = timePicker.getHour();
            int minute = timePicker.getMinute();
            String hora = String.format("%02d:%02d", hour, minute);

            Log.d(TAG, "Fecha seleccionada: " + fecha + ", Hora: " + hora);

            // ✅ GUARDAR LA RESERVA EN LA BASE DE DATOS
            guardarReservaEnBD(fecha, hora);

        } catch (Exception e) {
            Log.e(TAG, "Error en proceedToConfirmation: " + e.getMessage(), e);
            Toast.makeText(this, "Error al procesar reserva", Toast.LENGTH_SHORT).show();
        }
    }

    private void guardarReservaEnBD(String fecha, String hora) {
        try {
            // Obtener usuario ID de SharedPreferences
            SharedPreferences prefs = getSharedPreferences("app_prefs", MODE_PRIVATE);
            int usuarioId = prefs.getInt("user_id", 1); // Default 1 si no existe

            Log.d(TAG, "Guardando reserva - Usuario: " + usuarioId +
                    ", Servicio: " + servicioNombre +
                    ", Fecha: " + fecha +
                    ", Hora: " + hora);

            // Guardar la reserva usando ReservaDAO
            long resultado = reservaDAO.guardarReserva(
                    usuarioId,
                    servicioNombre,
                    servicioPrecio,
                    fecha,
                    hora
            );

            if (resultado != -1) {
                Log.d(TAG, "Reserva guardada correctamente. ID: " + resultado);
                Toast.makeText(this, "✅ Reserva confirmada", Toast.LENGTH_SHORT).show();

                // Navegar a confirmación
                navegarAConfirmacion(fecha, hora);
            } else {
                Log.e(TAG, "Error al guardar la reserva en BD");
                Toast.makeText(this, "❌ Error al guardar la reserva", Toast.LENGTH_SHORT).show();
            }

        } catch (Exception e) {
            Log.e(TAG, "Error guardando reserva: " + e.getMessage(), e);
            Toast.makeText(this, "⚠️ Error al procesar reserva", Toast.LENGTH_SHORT).show();
        }
    }

    private void navegarAConfirmacion(String fecha, String hora) {
        try {
            Intent intent = new Intent(this, ConfirmacionServicioActivity.class);
            intent.putExtra("servicio_nombre", servicioNombre);
            intent.putExtra("servicio_precio", servicioPrecio);
            intent.putExtra("fecha", fecha);
            intent.putExtra("hora", hora);
            startActivity(intent);
            finish(); // Cerrar esta actividad
        } catch (Exception e) {
            Log.e(TAG, "Error navegando a confirmación: " + e.getMessage());
            // Fallback: volver a servicios
            Intent fallbackIntent = new Intent(this, ServiciosActivity.class);
            startActivity(fallbackIntent);
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "CalendarioActivity destruida");
    }
}