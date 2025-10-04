package com.example.huellitasdesplash;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import com.example.huellitasdesplash.db.UsuarioDAO;

public class PerfilActivity extends AppCompatActivity {

    private SharedPreferences prefs;
    private UsuarioDAO usuarioDAO;
    private TextView tvNombreUsuario, tvEmailUsuario;
    private Button btnRegresarMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);

        prefs = getSharedPreferences("app_prefs", MODE_PRIVATE);
        usuarioDAO = new UsuarioDAO(this);

        inicializarVistas();
        cargarDatosUsuario();
        configurarMenu();
    }

    private void inicializarVistas() {
        tvNombreUsuario = findViewById(R.id.tvNombreUsuario);
        tvEmailUsuario = findViewById(R.id.tvEmailUsuario);
        btnRegresarMenu = findViewById(R.id.btnRegresarMenu); // Cambiado a btnRegresarMenu
    }

    private void cargarDatosUsuario() {
        int userId = prefs.getInt("user_id", -1);
        if (userId != -1) {
            String nombreUsuario = usuarioDAO.obtenerNombreUsuario(userId);
            String emailUsuario = usuarioDAO.obtenerCorreoUsuario(userId);

            tvNombreUsuario.setText(nombreUsuario != null ? nombreUsuario : "Usuario");
            tvEmailUsuario.setText(emailUsuario != null ? emailUsuario : "Correo no disponible");
        }
    }

    private void configurarMenu() {
        CardView cardEditarPerfil = findViewById(R.id.cardEditarPerfil);
        CardView cardMascotas = findViewById(R.id.cardMascotas);
        CardView cardEstadisticas = findViewById(R.id.cardEstadisticas);

        cardEditarPerfil.setOnClickListener(v -> {
            Intent intent = new Intent(PerfilActivity.this, EditarPerfilActivity.class);
            startActivity(intent);
        });

        cardMascotas.setOnClickListener(v -> {
            // Navegar directamente a tu PerfilMascotaActivity existente
            Intent intent = new Intent(PerfilActivity.this, PerfilMascotaActivity.class);
            startActivity(intent);
        });

        cardEstadisticas.setOnClickListener(v -> {
            Toast.makeText(PerfilActivity.this, "Módulo de Estadísticas - Próximamente", Toast.LENGTH_SHORT).show();
        });

        // Botón de Regresar al Menú Principal
        btnRegresarMenu.setOnClickListener(v -> {
            // Regresar al Dashboard (menú principal)
            finish();
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        cargarDatosUsuario();
    }

    @Override
    public boolean onSupportNavigateUp() {
        // Manejar la flecha de retroceso de la toolbar - también regresa al menú principal
        finish();
        return true;
    }
}