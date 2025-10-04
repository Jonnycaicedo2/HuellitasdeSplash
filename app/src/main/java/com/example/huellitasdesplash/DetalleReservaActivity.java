package com.example.huellitasdesplash;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.huellitasdesplash.db.ReservaDAO;
import com.example.huellitasdesplash.models.Reserva;

public class DetalleReservaActivity extends AppCompatActivity {

    private TextView tvServicioDetalle, tvFechaDetalle, tvHoraDetalle, tvPrecioDetalle, tvEstadoDetalle, tvMensajeEstado;
    private Button btnCancelarReserva, btnMarcarRealizada;
    private ReservaDAO reservaDAO;
    private Reserva reserva;
    private int reservaId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_reserva);

        reservaDAO = new ReservaDAO(this);
        reservaId = getIntent().getIntExtra("reserva_id", -1);

        initViews();
        cargarDetalleReserva();
        configurarBotones();
    }

    private void initViews() {
        // Solo los views que SABEMOS que existen en tu XML
        tvServicioDetalle = findViewById(R.id.tvServicioDetalle);
        tvFechaDetalle = findViewById(R.id.tvFechaDetalle);
        tvHoraDetalle = findViewById(R.id.tvHoraDetalle);
        tvPrecioDetalle = findViewById(R.id.tvPrecioDetalle);
        tvEstadoDetalle = findViewById(R.id.tvEstadoDetalle);
        tvMensajeEstado = findViewById(R.id.tvMensajeEstado);
        btnCancelarReserva = findViewById(R.id.btnCancelarReserva);
        btnMarcarRealizada = findViewById(R.id.btnMarcarRealizada);

        // COMENTA TEMPORALMENTE LOS QUE NO EXISTEN:
        // tvFechaCreacion = findViewById(R.id.tvFechaCreacion);
        // btnVolverReservas = findViewById(R.id.btnVolverReservas);
        // btnVolverMenu = findViewById(R.id.btnVolverMenu);
    }

    private void cargarDetalleReserva() {
        if (reservaId != -1) {
            reserva = reservaDAO.obtenerReservaPorId(reservaId);

            if (reserva != null) {
                mostrarInformacionReserva();
                configurarInterfazSegunEstado();
            } else {
                Toast.makeText(this, "Error al cargar la reserva", Toast.LENGTH_SHORT).show();
                finish();
            }
        } else {
            Toast.makeText(this, "ID de reserva no válido", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void mostrarInformacionReserva() {
        tvServicioDetalle.setText(reserva.getServicioNombre());
        tvFechaDetalle.setText(reserva.getFecha());
        tvHoraDetalle.setText(reserva.getHora());
        tvPrecioDetalle.setText("$" + reserva.getPrecio());
        tvEstadoDetalle.setText(reserva.getEstado());

        // Configurar color según estado
        configurarColorEstado(reserva.getEstado());
    }

    private void configurarColorEstado(String estado) {
        int colorRes;
        switch (estado) {
            case "pendiente":
                colorRes = android.R.color.holo_orange_light;
                break;
            case "confirmada":
                colorRes = android.R.color.holo_green_light;
                break;
            case "realizada":
                colorRes = android.R.color.holo_blue_light;
                break;
            case "cancelada":
                colorRes = android.R.color.holo_red_light;
                break;
            default:
                colorRes = android.R.color.darker_gray;
        }
        tvEstadoDetalle.setBackgroundColor(getResources().getColor(colorRes));
    }

    private void configurarInterfazSegunEstado() {
        String estado = reserva.getEstado();

        switch (estado) {
            case "pendiente":
            case "confirmada":
                // Mostrar botones de acción
                btnCancelarReserva.setVisibility(View.VISIBLE);
                if ("confirmada".equals(estado)) {
                    btnMarcarRealizada.setVisibility(View.VISIBLE);
                }
                tvMensajeEstado.setVisibility(View.GONE);
                break;

            case "realizada":
                tvMensajeEstado.setText("✓ Esta reserva ya fue completada");
                tvMensajeEstado.setVisibility(View.VISIBLE);
                btnCancelarReserva.setVisibility(View.GONE);
                btnMarcarRealizada.setVisibility(View.GONE);
                break;

            case "cancelada":
                tvMensajeEstado.setText("✗ Esta reserva fue cancelada");
                tvMensajeEstado.setVisibility(View.VISIBLE);
                btnCancelarReserva.setVisibility(View.GONE);
                btnMarcarRealizada.setVisibility(View.GONE);
                break;
        }
    }

    private void configurarBotones() {
        btnCancelarReserva.setOnClickListener(v -> cancelarReserva());
        btnMarcarRealizada.setOnClickListener(v -> marcarReservaRealizada());
    }

    private void cancelarReserva() {
        boolean exito = reservaDAO.cancelarReserva(reservaId);
        if (exito) {
            Toast.makeText(this, R.string.reserva_cancelada_exito, Toast.LENGTH_SHORT).show();
            // Recargar la actividad para mostrar cambios
            recreate();
        } else {
            Toast.makeText(this, "Error al cancelar la reserva", Toast.LENGTH_SHORT).show();
        }
    }

    private void marcarReservaRealizada() {
        boolean exito = reservaDAO.marcarReservaRealizada(reservaId);
        if (exito) {
            Toast.makeText(this, R.string.reserva_realizada_exito, Toast.LENGTH_SHORT).show();
            // Recargar la actividad para mostrar cambios
            recreate();
        } else {
            Toast.makeText(this, "Error al actualizar la reserva", Toast.LENGTH_SHORT).show();
        }
    }

    // Manejar el botón de back físico del dispositivo
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish(); // Regresa a la lista de reservas
    }
}