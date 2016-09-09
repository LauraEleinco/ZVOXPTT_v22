package com.eleinco.ezvoxptt.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.eleinco.ezvoxptt.common.Settings;
//actividad, para configurar el ID del equipo
public class ActConfigurarID extends Activity {

	EditText txtCodigo1 = null;
	EditText txtCodigo2 = null;
	
	ImageButton btnIngresoAdmin = null;
	Button btnAceptar = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.act_configurar_id);
		//getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.encabezado);

		//cargar objetos de interfaz		
		txtCodigo1 = (EditText)findViewById(R.id.txtCodigo1);
		txtCodigo2 = (EditText)findViewById(R.id.txtCodigo2);
		btnIngresoAdmin = (ImageButton)findViewById(R.id.btnIngresoAdminGrupos);
		btnAceptar =  (Button)findViewById(R.id.btnAceptarConfID);
		btnIngresoAdmin.setOnClickListener(btnIngresoAdmin_Click);
		
		txtCodigo1.setText("");
		txtCodigo2.setText("");
		btnAceptar.setOnClickListener(btnAceptar_Click);
		txtCodigo1.addTextChangedListener(new TextWatcher() {			
			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
				if(txtCodigo1.getText().toString().trim().length() == 3)
				txtCodigo2.requestFocus();
			}			
			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {}
			@Override
			public void afterTextChanged(Editable arg0) { } 
		});		
		txtCodigo1.requestFocus();
		
		setResult(RESULT_CANCELED);
	}

	private View.OnClickListener btnIngresoAdmin_Click = new View.OnClickListener() {			
		@Override
		public void onClick(View v) {
			//boton de ingreso master
			Intent intento = new Intent(ActConfigurarID.this, ActAdminGrupos.class);
			startActivityForResult(intento, 0);
		}
	};
	private View.OnClickListener btnAceptar_Click = new View.OnClickListener() {			
		@Override
		public void onClick(View v) {
			//validar codigo
			if(txtCodigo1.getText().toString().trim().length() == 0)
			{
				MsgBox.Show("Por favor ingrese el código número 1", ActConfigurarID.this); return;
			}
			if(txtCodigo2.getText().toString().trim().length() == 0)
			{
				MsgBox.Show("Por favor ingrese el código número 2", ActConfigurarID.this); return;
			}
			if(txtCodigo1.getText().toString().trim().length() != 3 ||
					txtCodigo2.getText().toString().trim().length() != 3)
			{
				MsgBox.Show("Cada código debe ser de 3 dígitos, por favor verifique", ActConfigurarID.this); return;
			}

			final String idEquipo = txtCodigo1.getText().toString().trim().toUpperCase() + 
					txtCodigo2.getText().toString().trim().toUpperCase();
			
			Wait.Show("Verificando con el servidor...", ActConfigurarID.this); //mostrar cargando
			new Thread(new Runnable() {				
				@Override
				public void run() {
					try {
						final com.eleinco.ezvoxptt.entities.Equipo objEquipo =
								com.eleinco.ezvoxptt.business.Equipos.Consultar(idEquipo); //consultar el equipo para validar su existencia
						runOnUiThread(new Runnable() {							
							@Override
							public void run() {
								Wait.Close();
								if(objEquipo == null)
								{
									MsgBox.Show("Lo sentimos, ha especificado un identificador NO válido. Por favor verifique.", ActConfigurarID.this);
									return;
								}
								try {
									Settings.MiID(ActConfigurarID.this, objEquipo.IDEquipo.toUpperCase());
									setResult(RESULT_OK);
									ActConfigurarID.this.finish();
								} 
								catch (Exception ex) {
									MsgBox.Show(ex.getMessage(), ActConfigurarID.this);
								}
							}
						});
					}
					catch (final Exception e) {
						runOnUiThread(new Runnable() {							
							@Override
							public void run() {
								Wait.Close();
								MsgBox.Show("Verifique su conexión a internet", ActConfigurarID.this);
							}
						});
					}
				}
			}).start();
		}
	};
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.act_configurar_id, menu);
		return true;
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		
		if(requestCode == 0)
		{
			if(resultCode == 2)
			{
				String equipoID = data.getExtras().getString("EquipoID");
				String primeraParte = equipoID.substring(0, 3);
				String segundaParte = equipoID.substring(3);
				txtCodigo1.setText(primeraParte);
				txtCodigo2.setText(segundaParte);
			}
		}
	}
}
