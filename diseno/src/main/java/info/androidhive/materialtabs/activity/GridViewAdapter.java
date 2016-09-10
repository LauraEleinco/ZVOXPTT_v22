package info.androidhive.materialtabs.activity;

/**
 * Created by ADMIN on 26/07/2016.
 */

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import info.androidhive.materialtabs.R;

public class GridViewAdapter extends RecyclerView.Adapter<GridViewAdapter.RecyclerViewHolders> {

    private List<Grupo> itemList;
    private Context context;

    public GridViewAdapter(Context context, List<Grupo> itemList) {
        this.itemList = itemList;
        this.context = context;
    }

    @Override
    public RecyclerViewHolders onCreateViewHolder(ViewGroup parent, int viewType) {

        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.grid_item, null);
        RecyclerViewHolders rcv = new RecyclerViewHolders(layoutView);
        return rcv;
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolders holder, int position) {
        holder.nombre_coche.setText(itemList.get(position).getNombre());
        holder.ImagenGrupo.setImageResource(itemList.get(position).getIdDrawable());
    }

    @Override
    public int getItemCount() {
        return this.itemList.size();
    }

    public class RecyclerViewHolders extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView nombre_coche;
        public ImageView ImagenGrupo;

        public RecyclerViewHolders(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            nombre_coche = (TextView) itemView.findViewById(R.id.nombre_coche);
            ImagenGrupo = (ImageView) itemView.findViewById(R.id.imagen_coche);
        }

        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
        @Override
        public void onClick(View view) {
            Toast.makeText(view.getContext(), "Clicked Country Position = " + getPosition(), Toast.LENGTH_SHORT).show();
            try {
                final Intent intent;

                intent = new Intent(context, ActividadDetalle.class);
                //intent.putExtra("app",Grupo.get(getAdapterPosition()));
                intent.putExtra(ActividadDetalle.EXTRA_PARAM_ID, itemView.getId());

          /*  ActivityOptionsCompat options = ActivityOptionsCompat. makeSceneTransitionAnimation(
                    (Activity)context, (View)cv, "appcard");
            context.startActivity(intent, options.toBundle());*/


                ActivityOptionsCompat activityOptions = ActivityOptionsCompat.makeSceneTransitionAnimation(
                        (Activity) context, new Pair<View, String>(view.findViewById(R.id.imagen_coche),
                                ActividadDetalle.VIEW_NAME_HEADER_IMAGE)
                );
                ActivityCompat.startActivity((Activity) context, intent, activityOptions.toBundle());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}