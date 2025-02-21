package com.example.asanayoga;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

/**
 * Adaptador para mostrar una lista de rutinas en un RecyclerView.
 * Permite abrir la pantalla de una rutina específica al hacer clic en ella.
 */
public class RutinaAdapter extends RecyclerView.Adapter<RutinaAdapter.RutinaViewHolder> {

    private List<Rutina> rutinas; // Lista de rutinas que se van a mostrar.
    private Context context;     // Contexto para iniciar nuevas actividades.

    /**
     * Constructor del adaptador.
     *
     * @param rutinas Lista de rutinas que se mostrarán en el RecyclerView.
     * @param context Contexto necesario para abrir nuevas actividades.
     */
    public RutinaAdapter(List<Rutina> rutinas, Context context) {
        this.rutinas = rutinas;
        this.context = context;
    }

    /**
     * Infla el diseño para cada elemento del RecyclerView.
     *
     * @param parent   El ViewGroup padre donde se añadirá la vista.
     * @param viewType El tipo de vista (por si se usan múltiples layouts, aunque aquí no aplica).
     * @return Un nuevo `RutinaViewHolder` con la vista inflada.
     */
    @NonNull
    @Override
    public RutinaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflar el diseño del elemento de la lista (item_rutina.xml).
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rutina, parent, false);
        return new RutinaViewHolder(view);
    }

    /**
     * Enlaza los datos de una rutina específica con su vista.
     *
     * @param holder   El ViewHolder que contiene la vista del elemento.
     * @param position La posición del elemento en la lista.
     */
    @Override
    public void onBindViewHolder(@NonNull RutinaViewHolder holder, int position) {
        // Obtener la rutina correspondiente a esta posición.
        Rutina rutina = rutinas.get(position);

        // Establecer el nombre de la rutina en el TextView.
        holder.nombreRutina.setText(rutina.getNombre());

        // Configurar el clic en el elemento.
        holder.itemView.setOnClickListener(v -> {
            // Crear un Intent para abrir la actividad PantallaRutinaPersonalizada.
            Intent intent = new Intent(context, PantallaRutinaPersonalizada.class);

            // Pasar el ID y nombre de la rutina como extras en el Intent.
            intent.putExtra("RUTINA_ID", rutina.getId());
            intent.putExtra("RUTINA_NOMBRE", rutina.getNombre());

            // Iniciar la actividad.
            context.startActivity(intent);
        });
    }

    /**
     * Devuelve la cantidad de elementos en la lista.
     *
     * @return El número de rutinas en `rutinas`.
     */
    @Override
    public int getItemCount() {
        return rutinas.size();
    }

    /**
     * ViewHolder que contiene las referencias a los componentes de la vista de un elemento.
     */
    public static class RutinaViewHolder extends RecyclerView.ViewHolder {
        TextView nombreRutina; // TextView que muestra el nombre de la rutina.

        /**
         * Constructor del ViewHolder.
         *
         * @param itemView La vista del elemento de lista.
         */
        public RutinaViewHolder(@NonNull View itemView) {
            super(itemView);

            // Vincular el TextView con su ID en el layout XML.
            nombreRutina = itemView.findViewById(R.id.nombreRutina);
        }
    }
}
