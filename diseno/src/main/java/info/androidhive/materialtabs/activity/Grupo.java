package info.androidhive.materialtabs.activity;

import info.androidhive.materialtabs.R;

/**
 * Created by ADMIN on 25/07/2016.
 */

public class Grupo {
    private String nombre,identificador;
    private int idDrawable;

    public Grupo(String nombre, int idDrawable, String identificador) {
        this.nombre = nombre;
        this.idDrawable = idDrawable;
        this.identificador=identificador;
    }

    public String getNombre() {
        return nombre;
    }


    public String getIdentificador() {
        return identificador;
    }


    public int getIdDrawable() {
        return idDrawable;
    }

    public int getId() {
        return nombre.hashCode();
    }


}