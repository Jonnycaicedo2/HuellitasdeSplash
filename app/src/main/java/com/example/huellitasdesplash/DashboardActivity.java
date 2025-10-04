package com.example.huellitasdesplash;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import com.example.huellitasdesplash.db.UsuarioDAO;

public class DashboardActivity extends AppCompatActivity {

    private SharedPreferences prefs;
    private UsuarioDAO usuarioDAO;
    private TextView tvWelcome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        prefs = getSharedPreferences("app_prefs", MODE_PRIVATE);
        usuarioDAO = new UsuarioDAO(this);

        tvWelcome = findViewById(R.id.tvWelcome);

        // Mostrar nombre de usuario
        mostrarNombreUsuario();

        // Configurar click listeners
        configurarMenu();
    }

    private void mostrarNombreUsuario() {
        int userId = prefs.getInt("user_id", -1);
        if (userId != -1) {
            String nombreUsuario = usuarioDAO.obtenerNombreUsuario(userId);
            String welcomeMessage = getString(R.string.welcome_message, nombreUsuario);
            tvWelcome.setText(welcomeMessage);
        }
    }

    private void configurarMenu() {
        CardView cardServicios = findViewById(R.id.cardServicios);
        CardView cardReservas = findViewById(R.id.cardReservas);
        CardView cardTienda = findViewById(R.id.cardTienda);
        CardView cardEducacion = findViewById(R.id.cardEducacion);
        CardView cardPerfil = findViewById(R.id.cardPerfil);
        CardView cardHistorial = findViewById(R.id.cardHistorial); // ← NUEVO
        CardView cardLogout = findViewById(R.id.cardLogout);
        CardView cardUbicacion = findViewById(R.id.cardUbicacion);

        cardServicios.setOnClickListener(v -> startActivity(new Intent(this, ServiciosActivity.class)));
        cardReservas.setOnClickListener(v -> startActivity(new Intent(this, ReservasActivity.class)));
        cardTienda.setOnClickListener(v -> startActivity(new Intent(this, TiendaActivity.class)));
        cardEducacion.setOnClickListener(v -> startActivity(new Intent(this, EducacionActivity.class)));
        cardPerfil.setOnClickListener(v -> startActivity(new Intent(this, PerfilActivity.class)));
        cardHistorial.setOnClickListener(v -> startActivity(new Intent(this, HistorialActivity.class)));
        cardUbicacion.setOnClickListener(v -> startActivity(new Intent(this, UbicacionActivity.class)));
        cardLogout.setOnClickListener(v -> {
            prefs.edit().clear().apply();
            Toast.makeText(this, R.string.logout_message, Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        });
    }


}