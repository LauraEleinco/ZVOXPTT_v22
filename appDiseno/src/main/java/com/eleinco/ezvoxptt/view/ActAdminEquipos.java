package com.eleinco.ezvoxptt.view;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

//actividad, ineerfaz de usuario para la administracion de equipos
public class ActAdminEquipos extends Activity {
	//se definen los controles de usuario
	private ListView lstEquipos = null;
	private TextView lblNoHayEquipos = null;
	private String idGrupo = null;
	private String NombreGrupo = null;
	private String idGrupoPadre = null;
	private AdpEquipos adaptador = null;
	private ArrayList<com.eleinco.ezvoxptt.entities.EquipoGrupo> equipos = null;
	private Intent intentoAnadirEquipo = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.act_admin_equipos);
		//getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.encabezado);
		//se llenan los contoles de interfaz
		lblNoHayEquipos = (TextView)findViewById(R.id.lblNoHayEquipos);
		lstEquipos = (ListView)findViewById(R.id.lstEquipos);
		
		
		//se obtienen los parametros de entrada a la actividad
		Bundle parametros = this.getIntent().getExtras();
		
		
		
		
		lstEquipos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int pos, long arg3) {
				if(equipos != null)
				{
					
					
				}
			}
		});
				
		this.setTitle(this.getTitle()+" - " + NombreGrupo);
		CargarEquipos();
	}
	//metodo que carga los equipos desde el servidor en una lista
	private void CargarEquipos()
	{
		Wait.Show("Carcando equipos...", this); //se abre el dialogo de carga
		//se ejecuta un hilo para no bloquear la interfaz
		new Thread(new Runnable() {			
			@Override
			public void run() {
				//Se consultan los grupos
				try {
					//traer la lista de equipos
					equipos = com.eleinco.ezvoxptt.business.Equipos.Listar(idGrupo, false);
					//salirse del hilo al contexto de la interfaz de usuario
					runOnUiThread(new Runnable() {						
						@Override
						public void run() {
							Wait.Close(); //se cierra el dialogo de carga
							try
							{
								if(equipos.size() == 0)
								{
									//se muestra al usuario que no hay equiops
									lblNoHayEquipos.setVisibility(View.VISIBLE);
									lblNoHayEquipos.setText(NombreGrupo + " No tiene equipos registrados. Presione la tecla menú para añadir uno.");
								}
								else
								{
									lblNoHayEquipos.setVisibility(View.GONE);
								}
								//se crea el adaptador del listview para los equipos
								adaptador = new AdpEquipos(ActAdminEquipos.this, equipos);
								//se llena el listview
								lstEquipos.setAdapter(adaptador);
							}
							catch(Exception ex)
							{
								MsgBox.Show(ex.getMessage(), ActAdminEquipos.this);
							}
						}
					});
				} catch (final Exception e) {
					runOnUiThread(new Runnable() {	 //salirse al contexto UI para mostrar el error					
						@Override
						public void run() {
							Wait.Close();
							MsgBox.Show(e.getMessage(), ActAdminEquipos.this);
						}
					});
				}
			}
		}).start();
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode == 0)
		{
			if(resultCode == RESULT_OK)
			{
				//cargar los equipos si se recibe respuesta positiva desde una actividad interna
				CargarEquipos(); 
			}
		}
	}
}
