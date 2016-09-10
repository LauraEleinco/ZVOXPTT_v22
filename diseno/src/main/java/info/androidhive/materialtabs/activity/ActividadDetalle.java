package info.androidhive.materialtabs.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import info.androidhive.materialtabs.R;

import android.widget.ImageView;

import com.bumptech.glide.Glide;

/**
 * Actividad que muestra la imagen del item extendida
 */
public class ActividadDetalle extends AppCompatActivity {

    public static final String EXTRA_PARAM_ID = "com.herprogramacion.coches2015.extra.ID";
    public static final String VIEW_NAME_HEADER_IMAGE = "imagen_compartida";
    private Grupo itemDetallado;
    private ImageView imagenExtendida;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.actividad_detalle);
        try {
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);

            // Obtener el coche con el identificador establecido en la actividad principal
            //  itemDetallado = OneFragment.getAllItemList(getIntent().getIntExtra(EXTRA_PARAM_ID, 0));

            imagenExtendida = (ImageView) findViewById(R.id.imagen_extendida);

           // cargarImagenExtendida();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void cargarImagenExtendida() {
        Glide.with(imagenExtendida.getContext())
                .load(itemDetallado.getIdDrawable())
                .into(imagenExtendida);
    }


}