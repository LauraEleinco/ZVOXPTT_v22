package com.eleinco.ezvoxptt.view;

import java.io.IOException;
import java.util.ArrayList;

import android.app.Activity;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

public class AdpEquipos extends ArrayAdapter<com.eleinco.ezvoxptt.entities.EquipoGrupo>
{
    
    public ArrayList<com.eleinco.ezvoxptt.entities.EquipoGrupo> datos;
	Activity context;
    
	AdpEquipos(Activity context, ArrayList<com.eleinco.ezvoxptt.entities.EquipoGrupo> dat) 
    {
        super(context, R.layout.listitem_equipo, dat);
        this.datos = dat;
        this.context = context;
    }
	
    public View getView(int position, View convertView, ViewGroup parent) 
    {
    	View item = convertView;
    	ViewOlderEquipo older;
    	if (item == null)
    	{
	        LayoutInflater inflater = context.getLayoutInflater();
	        item = inflater.inflate(R.layout.listitem_equipo, null);
	        
	        older = new ViewOlderEquipo();
	        older.lblAlias = (TextView)item.findViewById(R.id.lblAliasEquipoItem);
	        older.lblID = (TextView)item.findViewById(R.id.lblIDEquipo);
	        
	        item.setTag(older);
    	}
    	else
    	{
    		older = (ViewOlderEquipo)item.getTag();
    	}
    	older.lblAlias.setText(datos.get(position).Nombre);
    	older.lblID.setText("Identificador: " + datos.get(position).IDEquipo);    	
        return(item);
    }
}
class ViewOlderEquipo
{
	TextView lblAlias;
	TextView lblID;
}
