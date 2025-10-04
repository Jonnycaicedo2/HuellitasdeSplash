package com.example.huellitasdesplash;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class UbicacionActivity extends AppCompatActivity {

    private static final String TAG = "UbicacionActivity";

    private Button btnAbrirMapa, btnComoLlegar, btnVolverMenu;
    private TextView tvDireccion, tvHorario, tvTelefono;

    // Coordenadas de la veterinaria (ejemplo)
    private final double LATITUD = 4.710989;
    private final double LONGITUD = -74.072092;
    private final String DIRECCION = "Cl. 127 #7-33, Bogotá, Colombia";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ubicacion);

        try {
            Log.d(TAG, "Iniciando UbicacionActivity...");

            initViews();
            configurarNavegacion();
            configurarBotones();

            Log.d(TAG, "UbicacionActivity iniciada correctamente");

        } catch (Exception e) {
            Log.e(TAG, "Error crítico en onCreate: " + e.getMessage(), e);
            Toast.makeText(this, "Error al cargar la ubicación", Toast.LENGTH_LONG).show();
            finish();
        }
    }

    private void initViews() {
        try {
            btnAbrirMapa = findViewById(R.id.btnAbrirMapa);
            btnComoLlegar = findViewById(R.id.btnComoLlegar);
            btnVolverMenu = findViewById(R.id.btnVolverMenu);
            tvDireccion = findViewById(R.id.tvDireccion);
            tvHorario = findViewById(R.id.tvHorario);
            tvTelefono = findViewById(R.id.tvTelefono);

            // Configurar información de la veterinaria
            tvDireccion.setText(DIRECCION);
            tvHorario.setText("Lunes a Viernes: 8:00 AM - 7:00 PM\nSábados: 9:00 AM - 5:00 PM\nDomingos: Cerrado");
            tvTelefono.setText("+57 1 123 4567");

            Log.d(TAG, "Vistas inicializadas correctamente");

        } catch (Exception e) {
            Log.e(TAG, "Error inicializando vistas: " + e.getMessage(), e);
            Toast.makeText(this, "Error al cargar la interfaz", Toast.LENGTH_SHORT).show();
        }
    }

    private void configurarNavegacion() {
        try {
            btnVolverMenu.setOnClickListener(v -> finish());
        } catch (Exception e) {
            Log.e(TAG, "Error configurando navegación: " + e.getMessage());
        }
    }

    private void configurarBotones() {
        try {
            // Botón Abrir Mapa
            btnAbrirMapa.setOnClickListener(v -> abrirMapa());

            // Botón Cómo Llegar
            btnComoLlegar.setOnClickListener(v -> abrirComoLlegar());

        } catch (Exception e) {
            Log.e(TAG, "Error configurando botones: " + e.getMessage());
        }
    }

    private void abrirMapa() {
        try {
            // Crear URI para Google Maps
            String uri = String.format("geo:%f,%f?q=%f,%f(%s)",
                    LATITUD, LONGITUD, LATITUD, LONGITUD, "Huellitas Veterinaria");

            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
            intent.setPackage("com.google.android.apps.maps"); // Forzar Google Maps

            // Verificar si Google Maps está instalado
            if (intent.resolveActivity(getPackageManager()) != null) {
                startActivity(intent);
                Log.d(TAG, "Abriendo Google Maps...");
            } else {
                // Fallback: abrir en navegador
                abrirMapaEnNavegador();
            }

        } catch (Exception e) {
            Log.e(TAG, "Error abriendo mapa: " + e.getMessage(), e);
            Toast.makeText(this, "Error al abrir el mapa", Toast.LENGTH_SHORT).show();
            abrirMapaEnNavegador(); // Fallback
        }
    }

    private void abrirComoLlegar() {
        try {
            // Crear URI para direcciones en Google Maps
            String uri = String.format("google.navigation:q=%f,%f", LATITUD, LONGITUD);

            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
            intent.setPackage("com.google.android.apps.maps"); // Forzar Google Maps

            // Verificar si Google Maps está instalado
            if (intent.resolveActivity(getPackageManager()) != null) {
                startActivity(intent);
                Log.d(TAG, "Abriendo direcciones en Google Maps...");
            } else {
                // Fallback: abrir mapa normal
                abrirMapa();
            }

        } catch (Exception e) {
            Log.e(TAG, "Error abriendo direcciones: " + e.getMessage(), e);
            Toast.makeText(this, "Error al abrir direcciones", Toast.LENGTH_SHORT).show();
            abrirMapa(); // Fallback
        }
    }

    private void abrirMapaEnNavegador() {
        try {
            String url = String.format("https://www.google.com/maps?q=%f,%f", LATITUD, LONGITUD);
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(intent);
            Log.d(TAG, "Abriendo mapa en navegador...");
        } catch (Exception e) {
            Log.e(TAG, "Error abriendo mapa en navegador: " + e.getMessage());
            Toast.makeText(this, "No se pudo abrir el mapa", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "UbicacionActivity destruida");
    }
}