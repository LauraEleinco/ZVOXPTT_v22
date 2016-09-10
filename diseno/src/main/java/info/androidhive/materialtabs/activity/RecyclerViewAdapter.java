package info.androidhive.materialtabs.activity;

/**
 * Created by ADMIN on 26/07/2016.
 */

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import info.androidhive.materialtabs.R;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.RecyclerViewHolders> {

    private List<Equipo> itemList;
    private Context context;

    public RecyclerViewAdapter(Context context, List<Equipo> itemList) {
        this.itemList = itemList;
        this.context = context;
    }


    @Override
    public RecyclerViewHolders onCreateViewHolder(ViewGroup parent, int viewType) {

        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.equipo_item, null);
        RecyclerViewHolders rcv = new RecyclerViewHolders(layoutView);
        return rcv;
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolders holder, int position) {
        holder.nombre.setText(itemList.get(position).getNombreEquipo());
        holder.equipo_identificador.setText(itemList.get(position).getIdentificadorEquipo());
    }

    @Override
    public int getItemCount() {
        return this.itemList.size();
    }
    public class RecyclerViewHolders extends RecyclerView.ViewHolder implements View.OnClickListener{

        public TextView nombre,equipo_identificador;
        public ImageView user;

        public RecyclerViewHolders(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            nombre = (TextView)itemView.findViewById(R.id.nombre);
            equipo_identificador = (TextView)itemView.findViewById(R.id.equipo_identificador);
            user = (ImageView)itemView.findViewById(R.id.user);
        }

        @Override
        public void onClick(View view) {
            Toast.makeText(view.getContext(), "Clicked Country Position = " + getPosition(), Toast.LENGTH_SHORT).show();
        }
    }
}