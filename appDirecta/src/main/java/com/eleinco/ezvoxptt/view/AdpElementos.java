package com.eleinco.ezvoxptt.view;

import java.io.IOException;
import java.util.ArrayList;

import com.eleinco.ezvoxptt.core.business.entities.ElementoTO;

import android.app.Activity;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

//adaptador de un elemento (equiop o grupo)
public class AdpElementos extends ArrayAdapter<ElementoTO>
{
    //arreglo con los datos
    public ArrayList<ElementoTO> datos;
	Activity context;
    //constructor por defecto
	AdpElementos(Activity context, ArrayList<ElementoTO> dat) 
    {
        super(context, R.layout.listitem_grupo, dat);
        this.datos = dat;
        this.context = context;
    }
	//evento getview obligatorio de la clase arrayAdapter
    public View getView(int position, View convertView, ViewGroup parent) 
    {
    	View item = convertView;
    	ViewOlderElemento older;
    	if (item == null) 
    	{
    		//inflador del listitem
	        LayoutInflater inflater = context.getLayoutInflater();
	        item = inflater.inflate(R.layout.listitem_grupo, null);
	        
	        older = new ViewOlderElemento();
	        older.lblNombre = (TextView)item.findViewById(R.id.lblNombreGrupo);
	        older.lblID = (TextView)item.findViewById(R.id.lblIDGrupo);
	        older.imgElemento = (ImageView)item.findViewById(R.id.imgElemento);
	        item.setTag(older);
    	}
    	else
    	{
    		older = (ViewOlderElemento)item.getTag();
    	}
    	ElementoTO e = datos.get(position);
    	older.lblNombre.setText(e.Nombre);
    	older.lblID.setText("Identificador: " + e.ID);
    	if(e.equipo != null)
    	{
    		older.imgElemento.setImageResource(R.drawable.equipo);
    	}
    	else
    	{
    		older.imgElemento.setImageResource(R.drawable.group);
    	}
        return(item);
    }
}
class ViewOlderElemento
{
	TextView lblNombre;
	TextView lblID;
	ImageView imgElemento;
}
