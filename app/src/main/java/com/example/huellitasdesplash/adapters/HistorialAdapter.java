package com.example.huellitasdesplash.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import com.example.huellitasdesplash.R;
import com.example.huellitasdesplash.models.ServicioHistorial;
import java.util.List;

public class HistorialAdapter extends RecyclerView.Adapter<HistorialAdapter.HistorialViewHolder> {

    private List<ServicioHistorial> serviciosHistorial;
    private OnHistorialClickListener listener;

    public interface OnHistorialClickListener {
        void onServicioClick(ServicioHistorial servicio);
    }

    public HistorialAdapter(List<ServicioHistorial> serviciosHistorial, OnHistorialClickListener listener) {
        this.serviciosHistorial = serviciosHistorial;
        this.listener = listener;
    }

    @NonNull
    @Override
    public HistorialViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_historial_servicio, parent, false);
        return new HistorialViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HistorialViewHolder holder, int position) {
        ServicioHistorial servicio = serviciosHistorial.get(position);
        holder.bind(servicio);
    }

    @Override
    public int getItemCount() {
        return serviciosHistorial != null ? serviciosHistorial.size() : 0;
    }

    public class HistorialViewHolder extends RecyclerView.ViewHolder {
        private CardView cardServicio;
        private TextView tvNombreServicio, tvFechaServicio, tvPrecio, tvDuracion, tvEstado;

        public HistorialViewHolder(@NonNull View itemView) {
            super(itemView);
            cardServicio = itemView.findViewById(R.id.cardServicio);
            tvNombreServicio = itemView.findViewById(R.id.tvNombreServicio);
            tvFechaServicio = itemView.findViewById(R.id.tvFechaServicio);
            tvPrecio = itemView.findViewById(R.id.tvPrecio);
            tvDuracion = itemView.findViewById(R.id.tvDuracion);
            tvEstado = itemView.findViewById(R.id.tvEstado);
        }

        public void bind(ServicioHistorial servicio) {
            tvNombreServicio.setText(servicio.getNombreServicio());
            tvFechaServicio.setText(servicio.getFechaServicio());
            tvPrecio.setText(servicio.getPrecioFormateado());
            tvDuracion.setText(servicio.getDuracionFormateada());
            tvEstado.setText(servicio.getEstado());

            // Configurar color según estado
            int colorEstado = android.graphics.Color.parseColor(servicio.getColorEstado());
            tvEstado.setTextColor(colorEstado);

            // Click en el card
            cardServicio.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onServicioClick(servicio);
                }
            });
        }
    }
}