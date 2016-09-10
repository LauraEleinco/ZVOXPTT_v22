package info.androidhive.materialtabs.activity;

/**
 * Created by ADMIN on 26/07/2016.
 */

public class Equipo {
    private String nombre, identificador;
    private int idDrawable;

    public Equipo(String nombre, int idDrawable, String identificador) {
        this.nombre = nombre;
        this.idDrawable = idDrawable;
        this.identificador = identificador;
    }

    public String getNombreEquipo() {
        return nombre;
    }


    public String getIdentificadorEquipo() {
        return identificador;
    }


    public int getIdDrawableEquipo() {
        return idDrawable;
    }

    public int getId() {
        return nombre.hashCode();
    }
}