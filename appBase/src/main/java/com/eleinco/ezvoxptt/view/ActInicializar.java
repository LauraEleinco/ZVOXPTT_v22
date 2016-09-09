package com.eleinco.ezvoxptt.view;

import java.io.IOException;

import com.eleinco.ezvoxptt.business.Server;
import com.eleinco.ezvoxptt.core.Servicio;
import com.eleinco.ezvoxptt.core.events.ServicioEventListener;

import android.os.Bundle;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.widget.TextView;
import android.widget.Toast;
//actividad, interfaz grafica para inicializar variables y componentes necesarios
public class ActInicializar extends Activity {
	TextView lblInicializando = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);		
		setContentView(R.layout.act_inicializar);
		
			
	}
	//comienza el proceso de incialización
	private void Inicializar()
	{
		lblInicializando = (TextView)findViewById(R.id.lblInicializando);
		//inicia un nuevo hilo
		new Thread(new Runnable() {			
			@Override
			public void run() {
					String id = com.eleinco.ezvoxptt.business.App.GetID(); //obtener el id del equiop
					if(id == "ERR") //talvez se retorne error al obtener el ID
					{
						runOnUiThread(new Runnable() {						
							@Override
							public void run() {
								MsgBox.Show("Error al verificar el identificador, revise su red. Aceptar para volver a intentarlo.", ActInicializar.this, new DialogInterface.OnClickListener() {									
									@Override
									public void onClick(DialogInterface dialog, int which) {
										Inicializar();
									}
								});
							}
						});
						return;
					}
					if (id.length() == 0)
					{
						//esperar 3 segundos
						try { Thread.sleep(3000); } catch (InterruptedException e) { e.printStackTrace(); }
						//salirse al contexto UI
						runOnUiThread(new Runnable() {						
							@Override
							public void run() {
								Intent intento = new Intent(ActInicializar.this, ActConfigurarID.class);
								startActivityForResult(intento, 0);
							}
						});
					}
					else
					{
						//if(Server.GetConnected() == false)
						//{
							Login();
						//}
						//else
						//{
						//	Intent intento = new Intent(ActInicializar.this, ActPrincipal.class);
						//	startActivityForResult(intento, 0);
						//}
					}
				
			}
		}).start();
	}
	//inicia el proceso de logeo con el servidor
	private void Login()
	{
		String mensaje = "";
		int intentos = 3;
		while(true)
		{
			try
			{
				
					if(com.eleinco.ezvoxptt.business.Login.IniciarLogin()) //iniciar el logueo
					{
						runOnUiThread(new Runnable() {						
							@Override
							public void run() {
								Intent intento = new Intent(ActInicializar.this, ActPrincipal.class); //iniciar la actividad principal
								startActivityForResult(intento, 0);
							}
						});
						return;
					}
					else
					{
						mensaje = "Tiempo de espera agotado, revise su red";
						intentos--;
						if(intentos == -1)
							break;					
					}
			} 
			catch (IOException e) 
			{
				mensaje = e.getMessage();
				e.printStackTrace();
				try { Thread.sleep(2000); } catch (InterruptedException e1) { e1.printStackTrace(); }
				intentos--;
				if(intentos == -1)
					break;
			}
			catch (Exception e) 
			{
				mensaje = e.getMessage();
				e.printStackTrace();
				try { Thread.sleep(2000); } catch (InterruptedException e1) { e1.printStackTrace(); }
				intentos--;
				if(intentos == -1)
					break;
			}
		}
		final String sms = mensaje;
		runOnUiThread(new Runnable() {						
			@Override
			public void run() {				
				MsgBox.Show(sms, ActInicializar.this, new DialogInterface.OnClickListener() {					
					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						ActInicializar.this.finish();
					}
				});
			}
		});
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode == 0)
		{
			if(resultCode == RESULT_OK)
			{
				Inicializar();
			}
			else
			{
				ActInicializar.this.finish();
			}
		}
	}
	@Override
	protected void onDestroy() {
		com.eleinco.ezvoxptt.business.Login.TerminarLogin();
		super.onDestroy();
	}
}
