package info.androidhive.materialtabs.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import info.androidhive.materialtabs.R;
import info.androidhive.materialtabs.activity.Grupo;
import info.androidhive.materialtabs.activity.GridViewAdapter;

import android.widget.GridView;

import java.util.ArrayList;
import java.util.List;

public class OneFragment extends Fragment {
    private GridView gridView;
    //private AdaptadorDeCoches adaptador;
    private GridLayoutManager lLayout;

    public OneFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

          //  return inflater.inflate(R.layout.fragment_one, container, false);
        View rootView = inflater.inflate(R.layout.fragment_one, container, false);

        try {
           /* gridView = (GridView) rootView.findViewById(R.id.grid);
            adaptador = new AdaptadorDeCoches(this.getContext());
            gridView.setAdapter(adaptador);
            gridView.setOnItemClickListener(this);*/


            List<Grupo> rowListItem = getAllItemList();
            lLayout = new GridLayoutManager(this.getActivity(), 2);

            RecyclerView rView = (RecyclerView)rootView.findViewById(R.id.grid);
            rView.setHasFixedSize(true);
            rView.setLayoutManager(lLayout);

            GridViewAdapter rcAdapter = new GridViewAdapter(this.getContext(), rowListItem);
            rView.setAdapter(rcAdapter);

        } catch (Exception e) {
            e.printStackTrace();
        }


        return rootView;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

   /* @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Grupo item = (Grupo) parent.getItemAtPosition(position);

        Intent intent = new Intent(this.getContext(), ActividadDetalle.class);
        intent.putExtra(ActividadDetalle.EXTRA_PARAM_ID, item.getId());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            ActivityOptionsCompat activityOptions =
                    ActivityOptionsCompat.makeSceneTransitionAnimation(
                            this.getActivity(),
                            new Pair<View, String>(view.findViewById(R.id.imagen_coche),
                                    ActividadDetalle.VIEW_NAME_HEADER_IMAGE)
                    );

            ActivityCompat.startActivity(this.getActivity(), intent, activityOptions.toBundle());
        } else
            startActivity(intent);
    }*/

    public static List<Grupo> getAllItemList(){

        List<Grupo> allItems = new ArrayList<Grupo>();
        allItems.add(new Grupo("United States", R.drawable.grupo,"78FQ56DB"));
        allItems.add(new Grupo("Canada", R.drawable.grupo,"78FQ56DB"));
        allItems.add(new Grupo("United Kingdom", R.drawable.grupo,"78FQ56DB"));
        allItems.add(new Grupo("Germany", R.drawable.grupo,"78FQ56DB"));
        allItems.add(new Grupo("Sweden", R.drawable.grupo,"78FQ56DB"));
        allItems.add(new Grupo("United Kingdom", R.drawable.grupo,"78FQ56DB"));
        allItems.add(new Grupo("Germany", R.drawable.grupo,"78FQ56DB"));
        allItems.add(new Grupo("Sweden", R.drawable.grupo,"78FQ56DB"));
        allItems.add(new Grupo("United States", R.drawable.grupo,"78FQ56DB"));
        allItems.add(new Grupo("Canada", R.drawable.grupo,"78FQ56DB"));
        allItems.add(new Grupo("United Kingdom", R.drawable.grupo,"78FQ56DB"));
        allItems.add(new Grupo("Germany", R.drawable.grupo,"78FQ56DB"));
        allItems.add(new Grupo("Sweden", R.drawable.grupo,"78FQ56DB"));
        allItems.add(new Grupo("United Kingdom", R.drawable.grupo,"78FQ56DB"));
        allItems.add(new Grupo("Germany", R.drawable.grupo,"78FQ56DB"));
        allItems.add(new Grupo("Sweden", R.drawable.grupo,"78FQ56DB"));

        return allItems;
    }

}
