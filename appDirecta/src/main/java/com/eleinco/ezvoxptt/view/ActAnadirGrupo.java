package com.eleinco.ezvoxptt.view;

import android.os.Bundle;
import android.app.Activity;
import android.content.DialogInterface;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
//actividad, interfaz grafica para crear un grupo
public class ActAnadirGrupo extends Activity {
	//elementos de interfaz
	EditText txtNombre = null;
	TextView lblCantidad = null;
	EditText txtCantidad = null;
	EditText txtClaveAcceso = null;
	TextView lblCeroIlimitado = null;
	ImageButton btnAceptar = null;
	String idGrupoPadre = null;
	String idGrupoDueno = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.act_anadir_grupo);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.encabezado);
		//obtener instancias de interfaz
		txtNombre = (EditText)findViewById(R.id.txtNombreGrupo);
		lblCantidad = (TextView)findViewById(R.id.lblCantidadEquipos);
		txtCantidad = (EditText)findViewById(R.id.txtCantidadEquipos);
		txtClaveAcceso = (EditText)findViewById(R.id.txtClaveAcceso);
		lblCeroIlimitado = (TextView)findViewById(R.id.lblCeroIlimitado);
		btnAceptar = (ImageButton)findViewById(R.id.btnAceptarCrearGrupo);
		//obtener parametros de entrada
		Bundle parametrosEntrada = this.getIntent().getExtras();
		if (parametrosEntrada != null && parametrosEntrada.containsKey("IdGrupoPadre"))
		{
			idGrupoPadre = parametrosEntrada.getString("IdGrupoPadre");
			//lblCantidad.setVisibility(View.GONE);
			//txtCantidad.setVisibility(View.GONE);
			//lblCeroIlimitado.setVisibility(View.GONE);
		}
		if (parametrosEntrada != null && parametrosEntrada.containsKey("IdGrupoDueno"))
		{
			idGrupoDueno = parametrosEntrada.getString("IdGrupoDueno");
		}
		
		//eventos
		btnAceptar.setOnClickListener(btnAceptar_Click);
	}
	//evento click del boton aceptar
	View.OnClickListener btnAceptar_Click = new View.OnClickListener() {		
		@Override
		public void onClick(View arg0) {
			if (txtNombre.getText().toString().trim().length() == 0)
			{
				MsgBox.Show("Por favor ingrese el nombre del Grupo.", ActAnadirGrupo.this); return;
			}
			try
			{
				final int equipos = Integer.parseInt(txtCantidad.getText().toString());
				Wait.Show("Almacenando...", ActAnadirGrupo.this);
				//crear hilo
				new Thread(new Runnable() {					
					@Override
					public void run() {
						try
						{
							//crear el objeto grupo
							com.eleinco.ezvoxptt.entities.Grupo objGrupo = 
									new com.eleinco.ezvoxptt.entities.Grupo(null, txtNombre.getText().toString(), 
											txtClaveAcceso.getText().toString(), equipos, idGrupoPadre, idGrupoDueno);
							//almacenar el grupo
							String idGrupo = 
									com.eleinco.ezvoxptt.business.Grupos.Anadir(objGrupo);
							//mostrar respuesta		
							GrupoAgregadoCorrectamente(idGrupo);
						}
						catch(final Exception ex)
						{
							runOnUiThread(new Runnable() {								
								@Override
								public void run() {
									Wait.Close();
									MsgBox.Show(ex.getMessage(), ActAnadirGrupo.this);
								}
							});
						}
					}
				}).start();
			}
			catch(Exception ex)
			{
				MsgBox.Show("Por favor indique el número de equipos. 0 para ilimitado", ActAnadirGrupo.this);
			}
		}
	};
	//muestra informacion indicando que el gurpo fue creado
	private void GrupoAgregadoCorrectamente(final String idGrupo)
	{
		runOnUiThread(new Runnable() {			
			@Override
			public void run() {
				Wait.Close();
				String msg = "El grupo ha sido creado satisfactoriamente. ";
				
				MsgBox.Show(msg, 
						ActAnadirGrupo.this, new DialogInterface.OnClickListener() {							
							@Override
							public void onClick(DialogInterface arg0, int arg1) {
								setResult(RESULT_OK);
								ActAnadirGrupo.this.finish();
							}
						});
			}
		});
	}
}
