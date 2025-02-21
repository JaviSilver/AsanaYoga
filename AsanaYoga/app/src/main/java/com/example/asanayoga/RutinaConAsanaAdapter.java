package com.example.asanayoga;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

/**
 * Adaptador para mostrar una lista de asanas en una rutina dentro de un RecyclerView.
 * Incluye interacción mediante clics y clics largos en los elementos.
 */
public class RutinaConAsanaAdapter extends RecyclerView.Adapter<RutinaConAsanaAdapter.AsanaViewHolder> {

    private List<Asana> asanasList; // Lista de asanas que se mostrarán.
    private OnAsanaClickListener onAsanaClickListener; // Listener para manejar clics en los asanas.

    /**
     * Constructor del adaptador.
     *
     * @param asanasList Lista de asanas a mostrar en el RecyclerView.
     * @param listener   Listener para manejar la interacción de clics en los asanas.
     */
    public RutinaConAsanaAdapter(List<Asana> asanasList, OnAsanaClickListener listener) {
        this.asanasList = asanasList != null ? asanasList : new ArrayList<>();
        this.onAsanaClickListener = listener;
    }

    /**
     * Interfaz para manejar clics en los elementos del RecyclerView.
     */
    public interface OnAsanaClickListener {
        void onAsanaClick(Asana asana); // Método a implementar para manejar clics en un asana.
    }

    /**
     * Crea e infla el diseño de cada elemento del RecyclerView.
     *
     * @param parent   El ViewGroup donde se añadirá la vista.
     * @param viewType El tipo de vista (por si se usan múltiples layouts, aunque aquí no aplica).
     * @return Un nuevo `AsanaViewHolder` con la vista inflada.
     */
    @NonNull
    @Override
    public AsanaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflar el diseño del elemento (item_asana.xml).
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_asana, parent, false);
        return new AsanaViewHolder(view);
    }

    /**
     * Vincula los datos de un asana con su vista.
     *
     * @param holder   El ViewHolder que contiene la vista del elemento.
     * @param position La posición del asana en la lista.
     */
    @Override
    public void onBindViewHolder(@NonNull AsanaViewHolder holder, int position) {
        // Obtener el asana correspondiente a esta posición.
        Asana asana = asanasList.get(position);

        // Establecer el nombre del asana en el TextView.
        holder.nombreTextView.setText(asana.getNombre());

        // Configurar el clic simple en el elemento.
        holder.itemView.setOnClickListener(v -> {
            if (onAsanaClickListener != null) {
                onAsanaClickListener.onAsanaClick(asana); // Notificar al listener.
            }
        });

        // Configurar el clic largo en el elemento.
        holder.itemView.setOnLongClickListener(v -> {
            Context context = v.getContext();

            // Crear un Intent para abrir la actividad PantallaAsana.
            Intent intent = new Intent(context, PantallaAsana.class);

            // Pasar el nombre del asana como extra en el Intent.
            intent.putExtra("NOMBRE_ASANA", asana.getNombre());

            // Iniciar la actividad.
            context.startActivity(intent);
            return true; // Indicar que el evento de clic largo ha sido manejado.
        });
    }

    /**
     * Devuelve la cantidad de elementos en la lista de asanas.
     *
     * @return Número de asanas en `asanasList`.
     */
    @Override
    public int getItemCount() {
        return asanasList.size();
    }

    /**
     * Actualiza la lista de asanas mostrada en el RecyclerView.
     *
     * @param nuevaLista Nueva lista de asanas.
     */
    public void actualizarLista(List<Asana> nuevaLista) {
        if (nuevaLista != null) {
            asanasList.clear();          // Limpiar la lista actual.
            asanasList.addAll(nuevaLista); // Añadir todos los elementos de la nueva lista.
            notifyDataSetChanged();     // Notificar al RecyclerView que los datos han cambiado.
        }
    }

    /**
     * ViewHolder que contiene las referencias a los componentes de la vista de un elemento.
     */
    static class AsanaViewHolder extends RecyclerView.ViewHolder {
        TextView nombreTextView; // TextView que muestra el nombre del asana.

        /**
         * Constructor del ViewHolder.
         *
         * @param itemView La vista del elemento.
         */
        AsanaViewHolder(View itemView) {
            super(itemView);

            // Vincular el TextView con su ID en el layout XML.
            nombreTextView = itemView.findViewById(R.id.nombreAsana);
        }
    }
}
