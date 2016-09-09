package com.eleinco.ezvoxptt.view;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.support.v4.app.ActivityCompat;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.eleinco.ezvoxptt.business.App;
import com.eleinco.ezvoxptt.business.Grupos;
import com.eleinco.ezvoxptt.business.Login;
import com.eleinco.ezvoxptt.business.Voz;
import com.eleinco.ezvoxptt.common.Logg;
import com.eleinco.ezvoxptt.core.business.TiempoRealDatos;
import com.eleinco.ezvoxptt.core.events.ServicioEventListener;

import java.io.IOException;
import java.util.ArrayList;

public class ActPrincipal extends Activity {
	private boolean PttPresionado = false;
	//sonidos de eventos
	//private MediaPlayer sonidoSolicitandoPtt = null;
	//private MediaPlayer sonidoPttOK = null;
	//private MediaPlayer sonidoPttNO = null;
	Vibrator vibrador = null;
	private boolean hablando = false;
	//controles graficos
	public Button btnPTT;
	public TextView lblInfo;
	private ImageButton btnContactos = null;
	private ImageButton btnAdminGrupos = null;
	private RelativeLayout btnSeleccionarGrupo = null;
	Handler myHandler = new Handler();
	private Intent intentoAdminGrupos = null;
	private String TAG = "ActPrincipal";
	private Intent servicio = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//header personalizado
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.act_principal);
		//getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.encabezado);

		AudioManager manager = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
		manager.setMode(AudioManager.MODE_IN_COMMUNICATION);
		manager.setStreamVolume(AudioManager.STREAM_MUSIC, 20, 0);
		manager.setSpeakerphoneOn(true);


		//this.setVolumeControlStream(AudioManager.STREAM_MUSIC);
		//inicialización de controles
		btnPTT = (Button)findViewById(R.id.btnGrabar);

		vibrador = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

		
		Wait.Show("Registrando...", ActPrincipal.this, onCancelWait);
        btnContactos = (ImageButton)findViewById(R.id.btnContactos);
        lblInfo = (TextView)findViewById(R.id.lblInfo);
        btnAdminGrupos = (ImageButton)findViewById(R.id.btnIngresoAdminGrupos);
        btnSeleccionarGrupo = (RelativeLayout)findViewById(R.id.pnlDisplay);
        
        //inicialización de eventos
        btnContactos.setOnClickListener(btnContactos_Click);
        btnSeleccionarGrupo.setOnClickListener(btnSeleccionarGrupo_Click);
        //inicializar los sonidos
       // if (sonidoSolicitandoPtt == null)
        //	sonidoSolicitandoPtt = MediaPlayer.create(ActPrincipal.this, R.raw.sonidopreguntaptt);
        
        //if (sonidoPttOK == null)
        //	sonidoPttOK = MediaPlayer.create(ActPrincipal.this, R.raw.sonidopttok);
        
        //if (sonidoPttNO == null)
        //	sonidoPttNO = MediaPlayer.create(ActPrincipal.this, R.raw.sonidopttno);
        //almacenar la instancia de esta actividad
        com.eleinco.ezvoxptt.business.Voz.actPrincipal = this;
        
        btnAdminGrupos.setOnClickListener(btnAdminGrupos_Click);
        
        //servicio = (new Intent(this, Servicio.class));
		//startService(servicio);
		//actualiza la informacion del gr
		ActualizarGrupoActual();
		//iniciar el serviio androd
		IniciarServicio();
		//eventos del boton PTT
		btnPTT.setOnTouchListener(new View.OnTouchListener() {        
		    @Override
		    public boolean onTouch(View v, MotionEvent event) {
		        switch(event.getAction()) {
		            case MotionEvent.ACTION_DOWN:
		            	PttPresionado = true;
	    				PTT();
		                return true;
		            case MotionEvent.ACTION_UP:
		            	PttPresionado = false;
	    				Release();
		                return true;		                      
		        }
		        return false;
		    }
		});
	}
	//inicia el servicio android
	private void IniciarServicio()
	{
		btnPTT.setVisibility(View.INVISIBLE);
		servicio = new Intent(this, com.eleinco.ezvoxptt.core.Servicio.class);
		com.eleinco.ezvoxptt.core.Servicio.addServicioListener(listenerServicio);
		startService(servicio);
	}
	//eventos del servicio android
	ServicioEventListener listenerServicio = new ServicioEventListener() {		
		@Override
		public void NoTieneGrupos() {
			Wait.Close();
			MsgBox.Show("No está registrado en ningún grupo. La aplicación se cerrará.", ActPrincipal.this, new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					ActPrincipal.this.finish();
				}
			});
			ActualizarGrupoActual();
		}
		
		@Override
		public void ErrorDeRed() {
			Wait.Close();
			MsgBox.Show("Ha ocurrido un error de red. Verifique que está conectado.", ActPrincipal.this, new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					ActPrincipal.this.finish();
				}
			});
		}

		@Override
		public void ConectadoYRegistrado() {
			ActualizarGrupoActual();
			runOnUiThread(new Runnable() {
				@Override
				public void run() {
					//Toast.makeText(ActPrincipal.this, "Conectado y registrado!", Toast.LENGTH_SHORT).show();
					btnPTT.setVisibility(View.VISIBLE);
					Wait.Close();
					vibrador.vibrate(500);
				}
			});
		}
		@Override
		public void Desconectado() 
		{
			runOnUiThread(new Runnable() {				
				@Override
				public void run() {
					btnPTT.setVisibility(View.INVISIBLE);
					Wait.Show("Registrando...", ActPrincipal.this, onCancelWait);
				}
			});
		}

		@Override
		public void SinRegistrarID() {
			Wait.Close();
			Intent intento = new Intent(ActPrincipal.this, ActConfigurarID.class);
			startActivityForResult(intento, 0);			
		}
		
		@Override
		public void IDNoValido() {
			Wait.Close();
			MsgBox.Show("El identificador registrado no es válido.", ActPrincipal.this, new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					Intent intento = new Intent(ActPrincipal.this, ActConfigurarID.class);
					startActivityForResult(intento, 0);
				}
			});
		}
		@Override
		public void EquipoHablando(String grupoId, String equipoId, boolean hablando) {
			HablandoRadio(equipoId, hablando);
		}
	};
	//evento del boton para administrar grupos
	View.OnClickListener btnAdminGrupos_Click = new View.OnClickListener() {		
		@Override
		public void onClick(View v) {
			
			if(App.GrupoActual.Admin == false)
			{
				MsgBox.Show("No eres administrador del grupo " + App.GrupoActual.NombreGrupo, ActPrincipal.this);
				return;
			}
			if(intentoAdminGrupos == null)
				intentoAdminGrupos = new Intent(ActPrincipal.this, ActAdminGrupos.class);
			
			intentoAdminGrupos.putExtra("idGrupo", App.GrupoActual.ID_Grupo); //Para que no sea como admin
			intentoAdminGrupos.putExtra("nombreGrupo", App.GrupoActual.NombreGrupo);
			if(App.GrupoActual.ID_Grupo_Dueno == null || App.GrupoActual.ID_Grupo_Dueno.length() == 0)
				intentoAdminGrupos.putExtra("idDueno", App.GrupoActual.ID_Grupo);
			else
				intentoAdminGrupos.putExtra("idDueno", App.GrupoActual.ID_Grupo_Dueno);
			//idDueno = pars.getString("idDueno");
			
			
			startActivity(intentoAdminGrupos);
		}
	};
	View.OnClickListener btnSeleccionarGrupo_Click = new View.OnClickListener() {		
		@Override
		public void onClick(View v) {
			ArrayList<com.eleinco.ezvoxptt.entities.GrupoEquipo> todosLosGrupos = 
					com.eleinco.ezvoxptt.business.Grupos.TodosMisGrupos;
			if(todosLosGrupos == null || todosLosGrupos.size() == 1)
			{
				Wait.Show("Un momento por favor...", ActPrincipal.this);
				new Thread(new Runnable() {					
					@Override
					public void run() {
						try {
							Grupos.CargarMisGrupos(App.GetID()); //obtener lista de grupos, estos serán almacenados en com.eleinco.ezvoxptt.business.Grupos.TodosMisGrupos
							runOnUiThread(new Runnable() {									
								@Override
								public void run() {
									Wait.Close();
									if(com.eleinco.ezvoxptt.business.Grupos.TodosMisGrupos == null || com.eleinco.ezvoxptt.business.Grupos.TodosMisGrupos.size() == 1)
									{
										MsgBox.Show("No hay grupos en los que pueda entrar.", ActPrincipal.this);
										return;
									}
									MostrarGruposParaCambiar();
								}
							});
						} catch (IOException e) {
							Error(e.getMessage());
							e.printStackTrace();
						} catch (Exception e) {
							Error(e.getMessage());
							e.printStackTrace();
						}
					}
				}).start();
				return;
			}
			MostrarGruposParaCambiar();
		}
	};
	//muestra la lista de grupos disponibles para cambiarse
	private void MostrarGruposParaCambiar()
	{
		//crear un inflador que permitirá mostrar una parte de interfaz grafica en un dialogo
		LayoutInflater factory = LayoutInflater.from(ActPrincipal.this);
		final View deleteDialogView = factory.inflate(R.layout.dialog_seleccionar_grupo, null);
		final AlertDialog deleteDialog = new AlertDialog.Builder(ActPrincipal.this).create();
		deleteDialog.setView(deleteDialogView);
		//obtener el listview para mostrarlos  
		final ListView lstGrupos = (ListView)deleteDialogView.findViewById(R.id.lstGruposSeleccionar);
		Button btnRecargarLista = (Button)deleteDialogView.findViewById(R.id.btnRecargarListaGrupos);
		
		//cargar la lista de grpos
		CargarListaGruposCambiar(lstGrupos, com.eleinco.ezvoxptt.business.Grupos.TodosMisGrupos);
		
		lstGrupos.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int pos, long arg3) {
				deleteDialog.dismiss();
				if (com.eleinco.ezvoxptt.business.App.GrupoActual != null)
				{
					if(com.eleinco.ezvoxptt.business.App.GrupoActual.ID_Grupo.equals(com.eleinco.ezvoxptt.business.Grupos.TodosMisGrupos.get(pos).ID_Grupo))
					{
						return;
					}
				}
				CambiarGrupo(com.eleinco.ezvoxptt.business.Grupos.TodosMisGrupos.get(pos));
			}
		});
		//mostrar el dialogo
		deleteDialog.show();
		//racarga la lista de grupo en caso de no estar actualizados
		btnRecargarLista.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Wait.Show("Recargando la lista...", ActPrincipal.this);
				new Thread(new Runnable() {
					@Override
					public void run() {
						try {
							Grupos.CargarMisGrupos(App.GetID());
							runOnUiThread(new Runnable() {
								@Override
								public void run() {
									Wait.Close();
									CargarListaGruposCambiar(lstGrupos, com.eleinco.ezvoxptt.business.Grupos.TodosMisGrupos);
								}
							});
						} catch (IOException e) {
							Error(e.getMessage());
							e.printStackTrace();
						} catch (Exception e) {
							Error(e.getMessage());
							e.printStackTrace();
						}
					}
				}).start();
			}
		});
	}
	//carga la lista de grupos
	private void CargarListaGruposCambiar(ListView lstGruposSeleccionar, ArrayList<com.eleinco.ezvoxptt.entities.GrupoEquipo> todosLosGrupos)
	{
		String[] nombresGrupos = new String[todosLosGrupos.size()];
		int p = 0;
		for(com.eleinco.ezvoxptt.entities.GrupoEquipo grupo : todosLosGrupos)
		{
			nombresGrupos[p] = grupo.NombreGrupo;
			p++;
		}
		ArrayAdapter<String> adaptador = new ArrayAdapter<String>(ActPrincipal.this, android.R.layout.simple_list_item_1, nombresGrupos);
		lstGruposSeleccionar.setAdapter(adaptador);
	}
	//muestra un error en el contexto UI
	private void Error(final String error)
	{
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				Wait.Close();
				MsgBox.Show(error, ActPrincipal.this);
			}
		});
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.act_principal, menu);
		return true;
	}
	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		if (item.getItemId() == R.id.action_settings)
		{
			if(intentoAdminGrupos == null)
				intentoAdminGrupos = new Intent(ActPrincipal.this, ActAdminGrupos.class);
			intentoAdminGrupos.putExtra("TipoCarga", "ADMIN"); //Para que ingrese como administrador y muestre todos los grupos de alto nivel
			startActivity(intentoAdminGrupos);
			return true;
		}
		else
		{
			String admin = "No soy administrador";
			if(App.GrupoActual.Admin)
				admin = "Soy administrador";
			MsgBox.Show("Soy:\t"+ App.Me.Alias + "\n" +
					"Mi identificador es:\t" + App.Me.IDEquipo + "\n\n" +
					"Grupo actual:\t" + App.GrupoActual.NombreGrupo + "\n" +
					admin + "\n" +
					"ID de grupo:\t" + App.GrupoActual.ID_Grupo,
					ActPrincipal.this);
		}
		return super.onMenuItemSelected(featureId, item);
	}
	//realiza el pcocedimiento para cambiar de grupo en el servidor
	private void CambiarGrupo(final com.eleinco.ezvoxptt.entities.GrupoEquipo nuevoGrupo)
	{
		Wait.Show("Cambiando de grupo...", ActPrincipal.this);
		new Thread(new Runnable() {			
			@Override
			public void run() {
				TiempoRealDatos.CambiarGrupo(nuevoGrupo.ID_Grupo); //enviar el comando de cambio de grupio
				
				try 
				{
					int espera = 0;
					while (App.GrupoActual == null || App.GrupoActual.ID_Grupo.equals(nuevoGrupo.ID_Grupo) == false)
					{
						try {
							Thread.sleep(500);
						} 
						catch (InterruptedException e) {
							e.printStackTrace();
						}
						
						espera+=500;
						if(espera == 5000)
						{
							CambioGrupoFallido();
							return;
						}
					}

					runOnUiThread(new Runnable() {						
						@Override
						public void run() {
							Wait.Close();
							ActualizarGrupoActual();
						}
					});					
				} 
				catch (Exception e) {				
					runOnUiThread(new Runnable() {						
						@Override
						public void run() {
							Wait.Close();
							MsgBox.Show("Error al actualizar el grupo. Vuelva a intentarlo", ActPrincipal.this);
						}
					});
				}
			}
		}).start();
	}
	//cuando no se ha podido cambiar de grupo
	private void CambioGrupoFallido()
	{
		runOnUiThread(new Runnable() {								
			@Override
			public void run() {
				Wait.Close();
				MsgBox.Show("El cambio de grupo no ha sido respondido, por favor intente nuevamente", ActPrincipal.this);
			}
		});
	}
	
	View.OnClickListener btnContactos_Click = new View.OnClickListener() {		
		@Override
		public void onClick(View v) {
			Intent intento = new Intent(ActPrincipal.this, ActContactos.class);
			startActivity(intento);
		}
	};
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		//keyCode == 24 || Vol+
		if(keyCode == 27 ||  keyCode == 79) //boton PTT de los alcatel o boton de contestar de un audicolar
		{
			if(!PttPresionado)
			{
				PttPresionado = true;
				PTT();
			}
			return true;
		}
		else if(keyCode == KeyEvent.KEYCODE_BACK) //boton atras
        {
			Confirm.Show("Está a punto de cerrar la aplicación. Si sólo quiere minimizarla presione el botón central de su teléfono. \nDesea cerrarla ahora?", ActPrincipal.this, new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					if(which == DialogInterface.BUTTON_POSITIVE)
					{
						Voz.Finalizar();
						ActPrincipal.this.finish();
					}
				}
			});
        	return true;	
        }
		return super.onKeyDown(keyCode, event);
	}
	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		if (keyCode == 27 || keyCode == 79)
		{
			PttPresionado = false;
			Release();
			return true;
		}
		return super.onKeyUp(keyCode, event);
	}
	@Override
	protected void onDestroy() {
		com.eleinco.ezvoxptt.core.Servicio.removeServicioListener(listenerServicio);
		stopService(servicio);
		super.onDestroy();
		ActivityCompat.finishAffinity(this);
	}
	//hace ptt
	private void PTT()
	{
		if (false)//para pruebas
		{
			com.eleinco.ezvoxptt.business.Voz.PttPermitido = 1;
			RespuestaPTT(true, false);
			return;
		}
		//reiniciar el sonido
		//sonidoSolicitandoPtt.seekTo(500);
		//sonidoSolicitandoPtt.start();

		//reiniciar la variableq ue indica si puere hablar
		com.eleinco.ezvoxptt.business.Voz.PttPermitido = 0; //Defecto
		lblInfo.setText("Solicitando...");
		btnPTT.setText("...");
		EsperarConfirmacionPTT();
			//enviar el query de PTT
		boolean enviado = TiempoRealDatos.SolicitarPTT(com.eleinco.ezvoxptt.business.App.GetID(), App.GrupoActual.ID_Grupo);
		if(!enviado)
			Toast.makeText(ActPrincipal.this, "Reconectando... por favor espere.", Toast.LENGTH_SHORT).show();
			//com.eleinco.ezvoxptt.business.Server.Send(
			//		com.eleinco.ezvoxptt.business.Protocol.SolicitarPTT(
			//					com.eleinco.ezvoxptt.business.App.GetID()));

	}
	//cancelar el ptto
	private void Release()
	{
		if(hablando) {
			new Thread(new Runnable() {
				@Override
				public void run() {
					boolean enviado = TiempoRealDatos.RadioCuelga(com.eleinco.ezvoxptt.business.App.GetID(), App.GrupoActual.ID_Grupo);
				}
			}).start();
		}
		hablando = false;
		com.eleinco.ezvoxptt.business.Voz.Parar();
		btnPTT.setText("PTT");
		ActualizarGrupoActual();
		com.eleinco.ezvoxptt.business.Voz.PttPermitido = 0; //Defecto
	}
	//inicia un hilo que espera a que se pueda hablar
	private void EsperarConfirmacionPTT()
	{
		new Thread(new Runnable() {			
			@Override
			public void run() {
				int espera = 0;
				while(espera < 5000)
				{
					if(PttPresionado == false)
						return;
					if(com.eleinco.ezvoxptt.business.Voz.PttPermitido == 1) //si puede hablar
					{
						RespuestaPTT(true, false);
						return;
					}
					else if (com.eleinco.ezvoxptt.business.Voz.PttPermitido == -1) //no puede hablar
					{
						RespuestaPTT(false, false);
						return;
					}
					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {}
					espera += 100;
				}
				if(espera >= 5000)
				{
					RespuestaPTT(false, true); //no puede hablar, el servidor no responde
				}
			}
		}).start();
	}
	private void EnviarEquipoHablando()
	{
		new Thread(new Runnable() {
			@Override
			public void run() {
				while (hablando)
				{
					try
					{
						boolean enviado = TiempoRealDatos.RadioHablando(com.eleinco.ezvoxptt.business.App.GetID(), App.GrupoActual.ID_Grupo);
						Thread.sleep(1000);
					} catch (InterruptedException e) {}
				}
			}
		}).start();
	}
	//establece la respuesta del servidor al pttear
	private void RespuestaPTT(final boolean pttPermitido, final boolean timeout)
	{
		myHandler.post(new Runnable() {			
			@Override
			public void run() {
				if (pttPermitido)
				{
					hablando = true;
					//sonidoPttOK.seekTo(400);
					//sonidoPttOK.start();
					vibrador.vibrate(30);
					com.eleinco.ezvoxptt.business.Voz.Grabar();
					btnPTT.setText("STOP");
					lblInfo.setText("Hable ahora...");
					EnviarEquipoHablando();
				}
				else
				{
					hablando = false;
					//sonidoPttNO.seekTo(200);
					//sonidoPttNO.start();
					vibrador.vibrate(150);
					Release();
					if(timeout)
						Toast.makeText(ActPrincipal.this, "Servidor no responde", Toast.LENGTH_SHORT).show();
					else
					Toast.makeText(ActPrincipal.this, "Canal ocupado", Toast.LENGTH_SHORT).show();
				}
			}
		});
	}
	String idActualHablando = "";
	//int tiempoSinHablar = 0;
	//muestra que un radio está hablando
	public void HablandoRadio(final String id, final boolean hablando)
	{
		//tiempoSinHablar = 0;
		//if(idActualHablando.equals(id))
		//{
		//	return;
		//}
		if(hablando)
			idActualHablando = id;
		else idActualHablando = null;
		myHandler.post(runableShowTalking);
	}

	Runnable runableShowTalking = new Runnable() {			
		@Override
		public void run() {
			com.eleinco.ezvoxptt.entities.Equipo objContacto =
					com.eleinco.ezvoxptt.business.Contactos.Consultar(idActualHablando, ActPrincipal.this); //consultar el radio qye esta hablando

			if(idActualHablando == null)
				ActualizarGrupoActual();
			else
				lblInfo.setText(">> " + objContacto.Alias + " <<"); //mostrar el alias del radio

			//new Thread(new Runnable() {
			//	@Override
			//	public void run() {
			//		while(true)
			//		{
			//			try {
			//				Thread.sleep(1000);
			//			} catch (InterruptedException e) { }
			//			tiempoSinHablar++;
			//			if(tiempoSinHablar == 3)
			//			{
			//				myHandler.post(new Runnable() {
			//					@Override
			//					public void run() {
			//						idActualHablando = "";
			//						ActualizarGrupoActual();
			//						return;
			//					}
			//				});
			//			}
			//		}
			//	}
			//}).start();
		}
	};
	//actualiza el nombre del grupo actual
	public void ActualizarGrupoActual()
	{
		if(idActualHablando != null && idActualHablando.length() > 0)
			return;
		runOnUiThread(new Runnable() {			
			@Override
			public void run() {
					if(com.eleinco.ezvoxptt.business.App.GrupoActual == null)
					{
						lblInfo.setText("SIN GRUPO");
					}
					else
					{
						lblInfo.setText(com.eleinco.ezvoxptt.business.App.GrupoActual.NombreGrupo);
					}
			}
		});		
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode == 0) //config id
		{
			if(resultCode == RESULT_OK)
			{
				IniciarServicio();
			}
			else
			{
				ActPrincipal.this.finish();
			}
		}
	}
	
	private OnCancelListener onCancelWait = new OnCancelListener() {		
		@Override
		public void onCancel(DialogInterface arg0) {
			ActPrincipal.this.finish();
		}
	};
}
