package com.example.huellitasdesplash;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.huellitasdesplash.adapters.ArticuloAdapter;
import com.example.huellitasdesplash.models.Articulo;
import java.util.ArrayList;
import java.util.List;

public class EducacionActivity extends AppCompatActivity implements ArticuloAdapter.OnArticuloClickListener {

    private RecyclerView recyclerViewArticulos;
    private ArticuloAdapter articuloAdapter;
    private List<Articulo> listaArticulos;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_educacion);

        inicializarVistas();
        configurarToolbar();
        cargarArticulos();
        configurarRecyclerView();
    }

    private void inicializarVistas() {
        toolbar = findViewById(R.id.toolbar);
        recyclerViewArticulos = findViewById(R.id.recyclerViewArticulos);
    }

    private void configurarToolbar() {
        setSupportActionBar(toolbar);

        // Mostrar botón de retroceso
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Artículos Educativos");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
    }

    private void cargarArticulos() {
        listaArticulos = new ArrayList<>();

        // Artículos de ejemplo con iconos del sistema Android
        listaArticulos.add(new Articulo(
                "Alimentación Canina",
                "Guía completa para una nutrición adecuada de tu perro",
                "Consejos sobre porciones, frecuencia y tipos de alimento según la raza, edad y nivel de actividad de tu mascota. Aprende a leer etiquetas de comida y a identificar ingredientes de calidad.",
                android.R.drawable.ic_dialog_info,
                "Nutrición",
                15
        ));

        listaArticulos.add(new Articulo(
                "Cuidado Dental en Gatos",
                "Mantén la salud bucal de tu felino",
                "Aprende técnicas de cepillado, juguetes dentales y señales de alerta para problemas bucales en gatos. Descubre cómo prevenir el sarro y las enfermedades periodontales.",
                android.R.drawable.ic_dialog_info,
                "Salud",
                12
        ));

        listaArticulos.add(new Articulo(
                "Entrenamiento Básico para Cachorros",
                "Primeros pasos en la educación canina",
                "Comandos básicos, socialización y manejo de comportamientos comunes en cachorros. Desde 'sentado' hasta 'quieto', guía paso a paso para un entrenamiento efectivo.",
                android.R.drawable.ic_dialog_info,
                "Entrenamiento",
                20
        ));

        listaArticulos.add(new Articulo(
                "Señales de Estrés en Mascotas",
                "Cómo identificar y manejar el estrés",
                "Reconoce las señales de estrés en perros y gatos, y aprende técnicas para reducirlo. Comportamientos comunes, causas y soluciones prácticas.",
                android.R.drawable.ic_dialog_info,
                "Bienestar",
                10
        ));

        listaArticulos.add(new Articulo(
                "Preparación para Viajes",
                "Viaja seguro con tu mascota",
                "Checklist completo para viajes en auto, avión y recomendaciones de seguridad. Documentación necesaria, kit de emergencia y tips para viajes largos.",
                android.R.drawable.ic_dialog_info,
                "Viajes",
                18
        ));

        listaArticulos.add(new Articulo(
                "Primeros Auxilios Básicos",
                "Qué hacer en caso de emergencia",
                "Aprende técnicas básicas de primeros auxilios para mascotas. Heridas, quemaduras, atragantamiento y RCP canino/felino.",
                android.R.drawable.ic_dialog_info,
                "Emergencias",
                25
        ));

        listaArticulos.add(new Articulo(
                "Cuidado de Mascotas Mayores",
                "Atenciones especiales para edad avanzada",
                "Adaptaciones en alimentación, ejercicio y cuidado veterinario para mascotas senior. Cómo mejorar su calidad de vida.",
                android.R.drawable.ic_dialog_info,
                "Senior",
                14
        ));

        listaArticulos.add(new Articulo(
                "Juguetes y Enriquecimiento Ambiental",
                "Mantén a tu mascota activa y feliz",
                "Ideas creativas para juguetes caseros, juegos de inteligencia y cómo crear un ambiente estimulante para tu mascota.",
                android.R.drawable.ic_dialog_info,
                "Entretenimiento",
                8
        ));
    }

    private void configurarRecyclerView() {
        articuloAdapter = new ArticuloAdapter(listaArticulos, this);
        recyclerViewArticulos.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewArticulos.setAdapter(articuloAdapter);
    }

    @Override
    public void onArticuloClick(Articulo articulo) {
        // Mostrar información del artículo seleccionado
        Toast.makeText(this,
                "Artículo: " + articulo.getTitulo() +
                        "\nCategoría: " + articulo.getCategoria() +
                        "\nTiempo de lectura: " + articulo.getTiempoLectura() + " min",
                Toast.LENGTH_LONG).show();

        // Aquí puedes agregar la navegación a un Activity de detalle más adelante
        /*
        Intent intent = new Intent(this, DetalleArticuloActivity.class);
        intent.putExtra("titulo", articulo.getTitulo());
        intent.putExtra("contenido", articulo.getContenido());
        intent.putExtra("categoria", articulo.getCategoria());
        startActivity(intent);
        */
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Manejar clic en el botón de home (flecha de retroceso)
        if (item.getItemId() == android.R.id.home) {
            onBackPressed(); // o finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        // Opcional: agregar animación personalizada
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}