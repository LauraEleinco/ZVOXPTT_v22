package com.eleinco.ezvoxptt.view;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

//actividad, interfaz grafica para ver los cotnaxtos
public class ActContactos extends Activity {
	private ArrayList<com.eleinco.ezvoxptt.entities.Equipo> listaContactos;
	private AdpContactos adaptador = null;
	private ListView lstContactos = null;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.act_contactos);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.encabezado);
	
		//obtener el listview
		lstContactos = (ListView)findViewById(R.id.lstContactos);		
		
		CargarContactos();
		
	}
	//metodo que consulta y muestra los contactos en un listview
	public void CargarContactos()
	{
		try
		{
			listaContactos = com.eleinco.ezvoxptt.business.Contactos.Listar(ActContactos.this);
			adaptador = new AdpContactos(ActContactos.this, listaContactos);		
			lstContactos.setAdapter(adaptador);
			
		}
		catch(Exception ex)
		{
			MsgBox.Show(ex.toString(), ActContactos.this);
		}
	}
	public void NotificarCambios()
	{
		runOnUiThread(new Runnable() {			
			@Override
			public void run() {
				adaptador.notifyDataSetChanged();		
			}
		});
		
	}
}
