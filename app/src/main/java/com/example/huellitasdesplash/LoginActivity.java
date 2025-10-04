package com.example.huellitasdesplash;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.huellitasdesplash.db.UsuarioDAO;
import com.example.huellitasdesplash.db.MascotaDAO;

public class LoginActivity extends AppCompatActivity {

    private EditText etEmail, etPassword;
    private Button btnLogin, btnGoRegister;
    private UsuarioDAO usuarioDAO;
    private MascotaDAO mascotaDAO;
    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        usuarioDAO = new UsuarioDAO(this);
        mascotaDAO = new MascotaDAO(this);
        prefs = getSharedPreferences("app_prefs", MODE_PRIVATE);

        // COMENTADO: Ya no redirige automáticamente
        // int savedUserId = prefs.getInt("user_id", -1);
        // if (savedUserId != -1) {
        //     redirectUserBasedOnPets(savedUserId);
        //     return;
        // }

        // Referencias a vistas
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        btnGoRegister = findViewById(R.id.btnGoRegister);

        // Acción login
        btnLogin.setOnClickListener(v -> {
            String email = etEmail.getText().toString().trim();
            String pass = etPassword.getText().toString().trim();

            if (email.isEmpty() || pass.isEmpty()) {
                Toast.makeText(this, "Por favor llena todos los campos", Toast.LENGTH_SHORT).show();
                return;
            }

            int userId = usuarioDAO.login(email, pass);
            if (userId != -1) {
                // Guardar sesión
                prefs.edit().putInt("user_id", userId).apply();
                Toast.makeText(this, "Login exitoso", Toast.LENGTH_SHORT).show();

                // Redirigir según si tiene mascotas o no
                redirectUserBasedOnPets(userId);
            } else {
                Toast.makeText(this, "Credenciales incorrectas", Toast.LENGTH_SHORT).show();
            }
        });

        // Acción ir a registro
        btnGoRegister.setOnClickListener(v -> {
            startActivity(new Intent(this, RegisterActivity.class));
        });
    }

    // Método para redirigir según si el usuario tiene mascotas
    private void redirectUserBasedOnPets(int userId) {
        if (mascotaDAO.usuarioTieneMascotas(userId)) {
            // Ya tiene mascotas, ir al Dashboard
            startActivity(new Intent(this, DashboardActivity.class));
        } else {
            // No tiene mascotas, ir a registrar primera mascota
            startActivity(new Intent(this, PerfilMascotaActivity.class));
        }
        finish();
    }
}