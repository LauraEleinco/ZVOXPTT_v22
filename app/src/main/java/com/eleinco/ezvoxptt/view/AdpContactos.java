package com.eleinco.ezvoxptt.view;

import android.graphics.Color;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.eleinco.ezvoxptt.core.business.TiempoRealDatos;
import com.eleinco.ezvoxptt.core.events.EstadoContactoEventListener;

import java.util.ArrayList;

//adaptador de contactos, permite bindear los datos a un listview
public class AdpContactos extends ArrayAdapter<com.eleinco.ezvoxptt.entities.Equipo>
{
    //arreglo con los datos
    public ArrayList<com.eleinco.ezvoxptt.entities.Equipo> datos;
	ActContactos context;
    //constructor por fefecto
    AdpContactos(ActContactos context, ArrayList<com.eleinco.ezvoxptt.entities.Equipo> dat) 
    {
        super(context, R.layout.listitem_contacto, dat);
        this.datos = dat;
        this.context = context;
    }
	Handler myHandler = new Handler();
	//evento getView obligatorio
    public View getView(final int position, View convertView, ViewGroup parent) 
    {
    	View item = convertView;
    	ViewOlderBarrio older;
    	if (item == null)
    	{
    		//crearun inflador para setear el listitem con los adtos de un contacto
	        LayoutInflater inflater = context.getLayoutInflater();
	        item = inflater.inflate(R.layout.listitem_contacto, null);
	        //obtener los elemeentos
	        older = new ViewOlderBarrio();
	        older.lblAlias = (TextView)item.findViewById(R.id.lblAliasContacto);
	        older.lblID = (TextView)item.findViewById(R.id.lblIdContacto);
	        older.lblEstado = (TextView)item.findViewById(R.id.lblEstadoContacto);
	        //older.btnEliminar = (Button)item.findViewById(R.id.btnEliminarContacto);
	        final ViewOlderBarrio olderFinal = older;
	       /* older.btnEliminar.setOnClickListener(new View.OnClickListener() {				
				@Override
				public void onClick(final View v) {
					Confirm.Show("Deseas eliminar a " + olderFinal.lblAlias.getText().toString(), 
							v.getContext(), new DialogInterface.OnClickListener() {								
								@Override
								public void onClick(DialogInterface dialog, int which) {
									if(which == DialogInterface.BUTTON_POSITIVE)
									{
										String id = olderFinal.lblID.getText().toString();
										try 
										{
											if(com.eleinco.ezvoxptt.business.Contactos.Eliminar(id, v.getContext()))
											{
												MsgBox.Show("Eliminado correctamente.", v.getContext());
												context.CargarContactos();
											}
										}
										catch (IOException e) {
											MsgBox.Show(e.getMessage(), v.getContext());
										} 
										catch (Exception e) {
											MsgBox.Show(e.getMessage(), v.getContext());
										}
									}
								}
							});
				}
			});*/
	        item.setTag(older);
    	}
    	else
    	{
    		older = (ViewOlderBarrio)item.getTag();
    	}
    	older.lblAlias.setText(datos.get(position).Alias);
    	older.lblID.setText(datos.get(position).IDEquipo);
    	final ViewOlderBarrio tmpOlder = older;
    	//evento que indicará si un id de equipo esta activo o no
        TiempoRealDatos.addEstadoContactoListener(new EstadoContactoEventListener() {				
			@Override
			public void EstadoCambiado(String equipoID, final boolean conectado) {
				if(equipoID.equals(datos.get(position).IDEquipo))
				{
					try
					{
						myHandler.post(new Runnable() {						
							@Override
							public void run() {
								if(conectado)
									tmpOlder.lblEstado.setBackgroundColor(Color.rgb(8, 163, 0));
								else
									tmpOlder.lblEstado.setBackgroundColor(Color.rgb(191, 18, 18));
							}
						});
					}
					catch(Exception ex)
					{
						ex.printStackTrace();
					}
				}
			}
		});
        //solicita al servidor el estado de un equipo
        TiempoRealDatos.SolicitarEstadoEquipo(datos.get(position).IDEquipo);    	
        return(item);
    }
}
class ViewOlderBarrio
{
	TextView lblAlias;
	TextView lblID;
	TextView lblEstado;
}