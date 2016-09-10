package info.androidhive.materialtabs.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import info.androidhive.materialtabs.R;
import info.androidhive.materialtabs.activity.Equipo;
import info.androidhive.materialtabs.activity.GridViewAdapter;
import info.androidhive.materialtabs.activity.RecyclerViewAdapter;


public class TwoFragment extends Fragment {
    private LinearLayoutManager lLayout;

    public TwoFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_two, container, false);
        try {
            List<Equipo> rowListItem = getAllItemList();
            lLayout = new LinearLayoutManager(this.getContext());

            RecyclerView rView = (RecyclerView) rootView.findViewById(R.id.recycler_equipos);
            lLayout.setOrientation(LinearLayoutManager.VERTICAL);
            rView.setLayoutManager(lLayout);

            RecyclerViewAdapter rcAdapter = new RecyclerViewAdapter(this.getContext(), rowListItem);
            rView.setAdapter(rcAdapter);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rootView;
    }

    private List<Equipo> getAllItemList() {

        List<Equipo> allItems = new ArrayList<Equipo>();
        allItems.add(new Equipo("United States", R.drawable.user, "908sds"));
        allItems.add(new Equipo("Canada", R.drawable.user, "908sds"));
        allItems.add(new Equipo("United Kingdom", R.drawable.user, "908sds"));
        allItems.add(new Equipo("Germany", R.drawable.user, "908sds"));
        allItems.add(new Equipo("Sweden", R.drawable.user, "908sds"));

        return allItems;
    }

}
