package com.example.huellitasdesplash.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.huellitasdesplash.R;
import com.example.huellitasdesplash.models.Reserva;
import java.util.List;

public class ReservaAdapter extends RecyclerView.Adapter<ReservaAdapter.ReservaViewHolder> {

    private List<Reserva> reservas;
    private OnReservaClickListener listener;

    public interface OnReservaClickListener {
        void onReservaClick(Reserva reserva);
    }

    public ReservaAdapter(List<Reserva> reservas, OnReservaClickListener listener) {
        this.reservas = reservas;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ReservaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_reserva, parent, false);
        return new ReservaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReservaViewHolder holder, int position) {
        Reserva reserva = reservas.get(position);
        holder.bind(reserva, listener);
    }

    @Override
    public int getItemCount() {
        return reservas.size();
    }

    static class ReservaViewHolder extends RecyclerView.ViewHolder {
        private TextView tvServicioReserva, tvFechaReserva, tvHoraReserva, tvPrecioReserva, tvEstadoReserva;

        public ReservaViewHolder(@NonNull View itemView) {
            super(itemView);
            tvServicioReserva = itemView.findViewById(R.id.tvServicioReserva);
            tvFechaReserva = itemView.findViewById(R.id.tvFechaReserva);
            tvHoraReserva = itemView.findViewById(R.id.tvHoraReserva);
            tvPrecioReserva = itemView.findViewById(R.id.tvPrecioReserva);
            tvEstadoReserva = itemView.findViewById(R.id.tvEstadoReserva);
        }

        public void bind(Reserva reserva, OnReservaClickListener listener) {
            tvServicioReserva.setText(reserva.getServicioNombre());
            tvFechaReserva.setText("Fecha: " + reserva.getFecha());
            tvHoraReserva.setText("Hora: " + reserva.getHora());
            tvPrecioReserva.setText("$" + reserva.getPrecio());
            tvEstadoReserva.setText(reserva.getEstado());

            // Color según estado
            switch (reserva.getEstado()) {
                case "pendiente":
                    tvEstadoReserva.setBackgroundColor(itemView.getContext().getColor(android.R.color.holo_orange_light));
                    break;
                case "confirmada":
                    tvEstadoReserva.setBackgroundColor(itemView.getContext().getColor(android.R.color.holo_green_light));
                    break;
                case "realizada":
                    tvEstadoReserva.setBackgroundColor(itemView.getContext().getColor(android.R.color.holo_blue_light));
                    break;
                case "cancelada":
                    tvEstadoReserva.setBackgroundColor(itemView.getContext().getColor(android.R.color.holo_red_light));
                    break;
            }

            itemView.setOnClickListener(v -> listener.onReservaClick(reserva));
        }
    }
}