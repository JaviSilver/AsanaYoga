package com.example.asanayoga;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

/**
 * Adaptador para manejar una lista de asanas en la creación de una rutina.
 * Permite eliminar asanas de la lista mediante un clic.
 */
public class CrearRutinaAdapter extends RecyclerView.Adapter<CrearRutinaAdapter.RutinaViewHolder> {

    private List<Asana> asanasList; // Lista de asanas en la rutina.
    private OnRutinaClickListener onRutinaClickListener; // Interfaz para manejar eventos de clic.

    /**
     * Constructor del adaptador.
     *
     * @param asanasList Lista inicial de asanas.
     * @param listener   Listener para manejar la eliminación de asanas.
     */
    public CrearRutinaAdapter(List<Asana> asanasList, OnRutinaClickListener listener) {
        this.asanasList = asanasList != null ? asanasList : new ArrayList<>();
        this.onRutinaClickListener = listener;
    }

    /**
     * Interfaz para manejar eventos al hacer clic en un elemento de la rutina.
     */
    public interface OnRutinaClickListener {
        void onRemoveAsana(Asana asana); // Método que se llama cuando se elimina un asana.
    }

    /**
     * Método que infla el diseño de cada elemento en la lista.
     *
     * @param parent   El ViewGroup padre donde se añadirá la vista.
     * @param viewType El tipo de vista (por si se usan múltiples layouts, aunque aquí no aplica).
     * @return Un nuevo `RutinaViewHolder` que contiene la vista inflada.
     */
    @NonNull
    @Override
    public RutinaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflar el diseño del elemento de lista (item_asana.xml).
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_asana, parent, false);
        return new RutinaViewHolder(view);
    }

    /**
     * Método que enlaza los datos de un elemento de la lista con su vista.
     *
     * @param holder   El ViewHolder que contiene la vista del elemento.
     * @param position La posición del elemento en la lista.
     */
    @Override
    public void onBindViewHolder(@NonNull RutinaViewHolder holder, int position) {
        // Obtener el objeto Asana correspondiente a esta posición.
        Asana asana = asanasList.get(position);

        // Establecer el nombre del asana en el TextView.
        holder.nombreTextView.setText(asana.getNombre());

        // Configurar el clic en el elemento.
        holder.itemView.setOnClickListener(v -> {
            if (onRutinaClickListener != null) {
                // Llamar al método del listener para eliminar el asana de la rutina.
                onRutinaClickListener.onRemoveAsana(asana);

                // Mostrar un mensaje de confirmación al usuario.
                Toast.makeText(holder.itemView.getContext(), "Asana eliminada de la rutina", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Devuelve la cantidad de elementos en la lista.
     *
     * @return El número de elementos en `asanasList`.
     */
    @Override
    public int getItemCount() {
        return asanasList.size();
    }

    /**
     * ViewHolder que contiene las referencias a los componentes de la vista de un elemento.
     */
    static class RutinaViewHolder extends RecyclerView.ViewHolder {
        TextView nombreTextView; // TextView que muestra el nombre del asana.

        /**
         * Constructor del ViewHolder.
         *
         * @param itemView La vista del elemento de lista.
         */
        RutinaViewHolder(View itemView) {
            super(itemView);
            // Vincular el TextView con su ID en el layout XML.
            nombreTextView = itemView.findViewById(R.id.nombreAsana);
        }
    }
}
