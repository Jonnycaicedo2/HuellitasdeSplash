package com.example.huellitasdesplash;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.huellitasdesplash.adapters.ServicioAdapter;
import com.example.huellitasdesplash.models.Servicio;
import java.util.Arrays;
import java.util.List;

public class ServiciosActivity extends AppCompatActivity implements ServicioAdapter.OnServicioClickListener {

    private RecyclerView recyclerViewServicios;
    private ServicioAdapter servicioAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_servicios);

        recyclerViewServicios = findViewById(R.id.recyclerViewServicios);
        recyclerViewServicios.setLayoutManager(new LinearLayoutManager(this));

        // Datos de ejemplo
        List<Servicio> servicios = Arrays.asList(
                new Servicio("Baño y Aseo", getString(R.string.descripcion_banio), 25000, 60),
                new Servicio("Corte de Pelo", getString(R.string.descripcion_corte), 30000, 45),
                new Servicio("Spa Completo", getString(R.string.descripcion_spa), 50000, 120),
                new Servicio("Limpieza Dental", getString(R.string.descripcion_dental), 35000, 30),
                new Servicio("Corte de Uñas", getString(R.string.descripcion_unas), 15000, 20)
        );

        servicioAdapter = new ServicioAdapter(servicios, this);
        recyclerViewServicios.setAdapter(servicioAdapter);

    }

    @Override
    public void onReservarClick(Servicio servicio) {
        // Ir a calendario para reservar
        Intent intent = new Intent(this, CalendarioActivity.class);
        intent.putExtra("servicio", servicio.getNombre());
        intent.putExtra("precio", servicio.getPrecio());
        startActivity(intent);
    }
}