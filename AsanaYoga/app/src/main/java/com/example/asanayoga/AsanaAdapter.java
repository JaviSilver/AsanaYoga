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
 * Adaptador personalizado para manejar una lista de objetos `Asana` en un RecyclerView.
 */
public class AsanaAdapter extends RecyclerView.Adapter<AsanaAdapter.AsanaViewHolder> {

    private List<Asana> asanasList; // Lista de elementos Asana que se mostrarán en el RecyclerView.
    private OnAsanaClickListener onAsanaClickListener; // Interfaz para manejar los clics en los elementos.

    /**
     * Constructor principal del adaptador.
     *
     * @param asanasList Lista de asanas a mostrar.
     * @param listener   Listener opcional para manejar clics en los elementos.
     */
    public AsanaAdapter(List<Asana> asanasList, OnAsanaClickListener listener) {
        this.asanasList = asanasList != null ? asanasList : new ArrayList<>();
        this.onAsanaClickListener = listener;
    }

    /**
     * Constructor alternativo sin listener.
     *
     * @param asanasList Lista de asanas a mostrar.
     */
    public AsanaAdapter(List<Asana> asanasList) {
        this.asanasList = asanasList != null ? asanasList : new ArrayList<>();
        this.onAsanaClickListener = null; // Listener opcional.
    }

    /**
     * Interfaz para manejar eventos de clic en los elementos del RecyclerView.
     */
    public interface OnAsanaClickListener {
        void onAsanaClick(Asana asana); // Método que se llama al hacer clic en un asana.
    }

    /**
     * Método que infla el diseño de cada elemento en la lista.
     *
     * @param parent   El ViewGroup padre donde se añadirá la vista.
     * @param viewType El tipo de vista (por si se usan múltiples layouts, aunque aquí no aplica).
     * @return Un nuevo `AsanaViewHolder` que contiene la vista inflada.
     */
    @NonNull
    @Override
    public AsanaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflar el diseño del elemento de lista (item_asana.xml).
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_asana, parent, false);
        return new AsanaViewHolder(view);
    }

    /**
     * Método que enlaza los datos de un elemento de la lista con su vista.
     *
     * @param holder   El ViewHolder que contiene la vista del elemento.
     * @param position La posición del elemento en la lista.
     */
    @Override
    public void onBindViewHolder(@NonNull AsanaViewHolder holder, int position) {
        // Obtener el objeto Asana correspondiente a esta posición.
        Asana asana = asanasList.get(position);

        // Establecer el nombre del asana en el TextView.
        holder.nombreTextView.setText(asana.getNombre());

        // Configurar el clic corto en el elemento.
        holder.itemView.setOnClickListener(v -> {
            if (onAsanaClickListener != null) {
                // Llamar al listener cuando el usuario haga clic en el elemento.
                onAsanaClickListener.onAsanaClick(asana);
            }
        });

        // Configurar el clic largo en el elemento para abrir otra actividad.
        holder.itemView.setOnLongClickListener(v -> {
            Context context = v.getContext();
            // Crear un Intent para abrir la actividad `PantallaAsana`.
            Intent intent = new Intent(context, PantallaAsana.class);
            // Pasar el nombre del asana como extra.
            intent.putExtra("NOMBRE_ASANA", asana.getNombre());
            // Iniciar la actividad.
            context.startActivity(intent);
            return true; // Indicar que el evento fue manejado.
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
     * Actualiza la lista de datos y notifica al adaptador que se han realizado cambios.
     *
     * @param nuevaLista La nueva lista de asanas.
     */
    public void actualizarLista(List<Asana> nuevaLista) {
        if (nuevaLista != null) {
            asanasList.clear(); // Vaciar la lista actual.
            asanasList.addAll(nuevaLista); // Añadir los nuevos elementos.
            notifyDataSetChanged(); // Notificar cambios para actualizar la interfaz.
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
         * @param itemView La vista del elemento de lista.
         */
        AsanaViewHolder(View itemView) {
            super(itemView);
            // Vincular el TextView con su ID en el layout XML.
            nombreTextView = itemView.findViewById(R.id.nombreAsana);
        }
    }
}
