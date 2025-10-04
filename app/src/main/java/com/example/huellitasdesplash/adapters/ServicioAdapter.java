package com.example.huellitasdesplash.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.huellitasdesplash.R;
import com.example.huellitasdesplash.models.Servicio;
import java.util.List;

public class ServicioAdapter extends RecyclerView.Adapter<ServicioAdapter.ServicioViewHolder> {

    private List<Servicio> servicios;
    private OnServicioClickListener listener;

    public interface OnServicioClickListener {
        void onReservarClick(Servicio servicio);
    }

    public ServicioAdapter(List<Servicio> servicios, OnServicioClickListener listener) {
        this.servicios = servicios;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ServicioViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_servicio, parent, false);
        return new ServicioViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ServicioViewHolder holder, int position) {
        Servicio servicio = servicios.get(position);
        holder.bind(servicio, listener);
    }

    @Override
    public int getItemCount() {
        return servicios.size();
    }

    static class ServicioViewHolder extends RecyclerView.ViewHolder {
        private TextView tvNombreServicio, tvDescripcion, tvPrecio, tvDuracion;
        private Button btnReservar;

        public ServicioViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNombreServicio = itemView.findViewById(R.id.tvNombreServicio);
            tvDescripcion = itemView.findViewById(R.id.tvDescripcion);
            tvPrecio = itemView.findViewById(R.id.tvPrecio);
            tvDuracion = itemView.findViewById(R.id.tvDuracion);
            btnReservar = itemView.findViewById(R.id.btnReservar);
        }

        public void bind(Servicio servicio, OnServicioClickListener listener) {
            tvNombreServicio.setText(servicio.getNombre());
            tvDescripcion.setText(servicio.getDescripcion());
            tvPrecio.setText(String.format("Precio: $%d", servicio.getPrecio()));
            tvDuracion.setText(String.format("Duración: %d min", servicio.getDuracion()));

            btnReservar.setOnClickListener(v -> listener.onReservarClick(servicio));
        }
    }
}