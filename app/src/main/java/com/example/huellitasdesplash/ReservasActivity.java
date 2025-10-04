package com.example.huellitasdesplash;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.huellitasdesplash.adapters.ReservaAdapter;
import com.example.huellitasdesplash.db.ReservaDAO;
import com.example.huellitasdesplash.models.Reserva;
import java.util.List;

public class ReservasActivity extends AppCompatActivity implements ReservaAdapter.OnReservaClickListener {

    private RecyclerView recyclerViewReservas;
    private TextView tvSinReservas;
    private Button btnVolverMenu, btnVolverInicio;
    private ReservaAdapter reservaAdapter;
    private ReservaDAO reservaDAO;
    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservas);

        prefs = getSharedPreferences("app_prefs", MODE_PRIVATE);
        reservaDAO = new ReservaDAO(this);

        initViews();
        configurarNavegacion();
        cargarReservas();
    }

    private void initViews() {
        recyclerViewReservas = findViewById(R.id.recyclerViewReservas);
        tvSinReservas = findViewById(R.id.tvSinReservas);
        btnVolverMenu = findViewById(R.id.btnVolverMenu);
        btnVolverInicio = findViewById(R.id.btnVolverInicio);

        recyclerViewReservas.setLayoutManager(new LinearLayoutManager(this));
    }

    private void configurarNavegacion() {
        // Botón Volver (arriba) - regresa a la actividad anterior
        btnVolverMenu.setOnClickListener(v -> {
            finish(); // Regresa a Dashboard
        });

        // Botón Menú Principal (abajo) - va al Dashboard
        btnVolverInicio.setOnClickListener(v -> {
            Intent intent = new Intent(this, DashboardActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            finish();
        });
    }

    private void cargarReservas() {
        int userId = prefs.getInt("user_id", -1);

        if (userId != -1) {
            List<Reserva> reservas = reservaDAO.obtenerReservasUsuario(userId);

            if (reservas.isEmpty()) {
                tvSinReservas.setVisibility(View.VISIBLE);
                recyclerViewReservas.setVisibility(View.GONE);
            } else {
                tvSinReservas.setVisibility(View.GONE);
                recyclerViewReservas.setVisibility(View.VISIBLE);

                reservaAdapter = new ReservaAdapter(reservas, this);
                recyclerViewReservas.setAdapter(reservaAdapter);
            }
        }
    }

    @Override
    public void onReservaClick(Reserva reserva) {
        // Abrir detalle de reserva
        Intent intent = new Intent(this, DetalleReservaActivity.class);
        intent.putExtra("reserva_id", reserva.getId());
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        cargarReservas(); // Recargar cuando vuelva a la actividad
    }

    // Manejar el botón de back físico del dispositivo
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish(); // Regresa a Dashboard
    }
}