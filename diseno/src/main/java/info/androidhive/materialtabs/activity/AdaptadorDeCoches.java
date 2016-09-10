package info.androidhive.materialtabs.activity;

/**
 * {@link BaseAdapter} para poblar coches en un grid view
 */
/*

public class AdaptadorDeCoches extends BaseAdapter {
    private Context context;

    public AdaptadorDeCoches(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
       // return GridViewAdapter.getItemCount.length;
        return  7;
    }

  */
/*  @Override
    public Grupo getItem(int position) {
      //  return Grupo.ITEMS[position];
    }
*//*

    @Override
    public long getItemId(int position) {
        return getItem(position).getId();
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {

        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.grid_item, viewGroup, false);
        }

        ImageView imagenCoche = (ImageView) view.findViewById(R.id.imagen_coche);
        TextView nombreCoche = (TextView) view.findViewById(R.id.nombre_coche);

        final Grupo item = getItem(position);
        Glide.with(imagenCoche.getContext())
                .load(item.getIdDrawable())
                .into(imagenCoche);

        nombreCoche.setText(item.getNombre());

        return view;
    }
}
*/
