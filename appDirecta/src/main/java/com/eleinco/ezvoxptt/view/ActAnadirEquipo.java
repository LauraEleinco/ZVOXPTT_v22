package com.eleinco.ezvoxptt.view;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
//activivad, interfaz para a�dir equipo
public class ActAnadirEquipo extends Activity {
	//controles de interfaz
	TextView lblAlias = null;
	EditText txtAlias = null;
	TextView lblIDEquipo = null;
	EditText txtIDEquipo = null;
	CheckBox ckEsAdministrador = null;
	CheckBox ckEstado = null;
	//variables de entrada y datos
	private ImageButton btnAceptar = null;
	private String idGrupo = null;
	private String tipoAccion = "";
	private String idEquipo = "";
	private String nombreEquipo = "";
	private String idDueno = "";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.act_anadir_equipo);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.encabezado);
		
		TextView lblNombreApp = (TextView)this.findViewById(R.id.lblNombreApp);
		lblNombreApp.setText("Crear o añadir equipo");
		
		//obtener parametros de entrada
		Bundle parametros = this.getIntent().getExtras();
		
		idGrupo = parametros.getString("idGrupo");
		tipoAccion = parametros.getString("tipoAccion"); //AddExistente, AddNuevo
		idEquipo = parametros.getString("idEquipo");
		nombreEquipo = parametros.getString("nombreEquipo");
		if (parametros.containsKey("idDueno"))
			idDueno =  parametros.getString("idDueno");
		//obtener isntancias de intetrfaz
		lblAlias = (TextView)findViewById(R.id.lblAliasEquipoAgregar);
		txtAlias = (EditText)findViewById(R.id.txtAliasEquipo);
		lblIDEquipo = (TextView)findViewById(R.id.lblIdEquipoAgregar);
		txtIDEquipo = (EditText)findViewById(R.id.txtIDEquipo);
		ckEsAdministrador = (CheckBox)findViewById(R.id.ckEsAdministrador);
		ckEstado = (CheckBox)findViewById(R.id.ckEstado);
		btnAceptar = (ImageButton)findViewById(R.id.btnGuardarEquipo);
		btnAceptar.setOnClickListener(btnAceptar_Click);
		//dependiendo del tipo de accion (equipo nuevo o existetne) se muestran algunos elementos y otros no
		if (tipoAccion.equals("AddNuevo"))
		{
			lblIDEquipo.setVisibility(View.GONE);
			txtIDEquipo.setVisibility(View.GONE);
			ckEstado.setChecked(true);
		}
		else if(tipoAccion.equals("AddExistente"))
		{
			txtAlias.setEnabled(false);
			txtIDEquipo.setEnabled(false);
			txtAlias.setText(nombreEquipo);
			txtIDEquipo.setText(idEquipo);
			ckEstado.setChecked(true);
		}
	}
	//evento clic del boton aceptar
	View.OnClickListener btnAceptar_Click = new View.OnClickListener() {		
		@Override
		public void onClick(View arg0) {
			//validar formulario
			if(txtAlias.getText().toString().trim().length() == 0)
			{
				MsgBox.Show("Por favor especifique el Alias del equipo", ActAnadirEquipo.this);
				return;
			}
			int estado = 0;
			if(ckEstado.isChecked())
				estado = 1;
			//crear entidad equipo
			final com.eleinco.ezvoxptt.entities.EquipoGrupo objEquipo = 
					new com.eleinco.ezvoxptt.entities.EquipoGrupo(idGrupo, idEquipo, 
							txtAlias.getText().toString(), ckEsAdministrador.isChecked(), estado, true);
			//mostrar cargando
			Wait.Show("Añadiendo a base de datos...", ActAnadirEquipo.this);
			new Thread(new Runnable() {				
				@Override
				public void run() {
					try {
						//almacenar el equipo
						String idRespuesta = 
								com.eleinco.ezvoxptt.business.Equipos.Agregar(objEquipo, idDueno);
						//leer respuesta y mostrarla al usuario
						if (tipoAccion.equals("AddNuevo"))
						{
							EquipoCreado(idRespuesta);
						}
						else if(tipoAccion.equals("AddExistente"))
						{
							EquipoAnadido();
						}
					} 
					catch (final Exception e) {
						runOnUiThread(new Runnable() {							
							@Override
							public void run() {
								Wait.Close();
								MsgBox.Show(e.getMessage(), ActAnadirEquipo.this);
							}
						});
					}
				}
			}).start();
		}
	};
	//indica que el equipo fue creado
	private void EquipoCreado(final String idRespuesta)
	{
		runOnUiThread(new Runnable() {							
			@Override
			public void run() {
				Wait.Close();
				setResult(RESULT_OK);
				if(idRespuesta.length() == 6)
				{
					MsgBox.Show("El Grupo se ha creado correctamente con el ID "+
							idRespuesta + ".",
							ActAnadirEquipo.this,
							new DialogInterface.OnClickListener() {												
								@Override
								public void onClick(DialogInterface dialog, int which) {
									ActAnadirEquipo.this.finish();
								}
							});
				}
				else
				{
					MsgBox.Show("El equipo no se ha guardado correctamente, por favor vuelva a intentarlo,",
						ActAnadirEquipo.this);
				}
			}
		});
	}
	//indica que el equipo se añadio  (equipo ya existia)
	private void EquipoAnadido()
	{
		runOnUiThread(new Runnable() {							
			@Override
			public void run() {
				Wait.Close();
				setResult(RESULT_OK);
				MsgBox.Show("El Grupo se ha añadido al grupo correctamente",
						ActAnadirEquipo.this,
						new DialogInterface.OnClickListener() {												
							@Override
							public void onClick(DialogInterface dialog, int which) {
								ActAnadirEquipo.this.finish();
							}
						});
			}
		});
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.act_anadir_equipo, menu);
		return true;
	}

}
