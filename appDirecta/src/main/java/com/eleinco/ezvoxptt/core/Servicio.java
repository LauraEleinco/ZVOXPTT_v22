package com.eleinco.ezvoxptt.core;


import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.v4.app.ActivityCompat;
import android.widget.Toast;

import com.eleinco.ezvoxptt.business.App;
import com.eleinco.ezvoxptt.business.Grupos;
import com.eleinco.ezvoxptt.business.Login;
import com.eleinco.ezvoxptt.business.UDP;
import com.eleinco.ezvoxptt.business.Voz;
import com.eleinco.ezvoxptt.common.Enums.EstadoTiempoReal;
import com.eleinco.ezvoxptt.common.Logg;
import com.eleinco.ezvoxptt.common.Settings;
import com.eleinco.ezvoxptt.core.business.TiempoRealDatos;
import com.eleinco.ezvoxptt.core.events.EstadoUDPListener;
import com.eleinco.ezvoxptt.core.events.ServicioEventListener;
import com.eleinco.ezvoxptt.core.events.TiempoRealEventListener;
import com.eleinco.ezvoxptt.entities.GrupoEquipo;

import java.util.ArrayList;
import java.util.List;
//servicio android que permite iniciar todo el core de la aplicacion sin depender de la interfaz grafica
public class Servicio extends Service {
	public static boolean ServicioIniciado = false; //indica si el servidor ya esta iniciado
	private static String TAG = "Servicio"; 
	GrupoEquipo grupoInicial = null; //indica con que grupo inicia la app
	private Intent intento = null;
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		
		intento = intent;
		if(ServicioIniciado)
		{
			Logg.d(TAG, "El servicio ya está iniciado");
			return super.onStartCommand(intent, flags, startId);
		}
		//obtener Identificador
		String equipoID = null;
		try {
			equipoID = Settings.MiID(this, null);

			if(equipoID == null || equipoID.trim().length() == 0)
			{
				SinRegistrarID();
				DetenerServicio();
				return START_STICKY;
			}	
		}
		catch (Exception e) {
			Logg.e(TAG, "Error al buscar identificador en el equipo", e);
			SinRegistrarID();
			DetenerServicio();
			return START_STICKY;
		}

		try {
			if(App.ValidateID(equipoID) == false) {

				IDNoValido();
				DetenerServicio();
				return START_STICKY;
			}
		}
		catch(Exception ex) {
			ErrorDeRed();
			DetenerServicio();
			return START_STICKY;
		}

		if (com.eleinco.ezvoxptt.business.App.GrupoActual == null)
		{			
			Logg.d(TAG, "Consultando grupos del id " + equipoID);
			ArrayList<com.eleinco.ezvoxptt.entities.GrupoEquipo> listaGrupos;
			try
			{
				listaGrupos = com.eleinco.ezvoxptt.business.Grupos.CargarMisGrupos(equipoID);
			}
			catch (Exception ex) 
			{
				Logg.e(TAG, "Error al cargar mis grupos", ex);
				ErrorDeRed();
				DetenerServicio();
				return START_STICKY;
			}				
			if(listaGrupos.size() == 0) 
			{
				NoTieneGrupos();
				DetenerServicio();
				return START_STICKY;
			}
			grupoInicial = listaGrupos.get(0);
			//com.eleinco.ezvoxptt.business.App.GrupoActual = listaGrupos.get(0);
		}
		TiempoRealDatos.addTiempoRealListener(listenerTiempoReal);
		UDP.addEstadoCambiadoListener(estadoUdpListener);
		TiempoRealDatos.IniciarServidores();
		
		com.eleinco.ezvoxptt.business.Voz.PrepararAudio(this);
		ServicioIniciado = true;
		//return super.onStartCommand(intent, flags, startId);
		return START_STICKY;
	}
	@Override
	public void onDestroy() {
		Toast.makeText(this, "ZVoxPTT se ha cerrado!", Toast.LENGTH_SHORT).show();
		TiempoRealDatos.DetenerServidores();
		com.eleinco.ezvoxptt.business.Login.TerminarLogin();
		ServicioIniciado = false;
		com.eleinco.ezvoxptt.business.Voz.Finalizar();
		super.onDestroy();

	}
	private void DetenerServicio()
	{
		try{
			//this.stopService(intento);
		}
		catch(Exception ex){
			Logg.e(TAG, "Error al detener servicio", ex);
		}
	}
	//recibe los llamados de los eventos de tiempo real
	TiempoRealEventListener listenerTiempoReal = new TiempoRealEventListener() {		
		
		@Override
		public void EstadoCambiado(final EstadoTiempoReal estado) { //se dispara cuando el estado de conexion cambia
			Logg.d(TAG, estado.toString());	
			if(estado == EstadoTiempoReal.Desconectado)
			{
				com.eleinco.ezvoxptt.business.Login.TerminarLogin();
				Desconectado();
			}
			else if(estado == EstadoTiempoReal.ConectadoSinRegistrar)
			{
				TiempoRealDatos.Loguear();
			}
			else if(estado == EstadoTiempoReal.ConectadoRegistrado)
			{
				Logg.d(TAG, "Registrado.");
				if(grupoInicial != null)
				{
					TiempoRealDatos.CambiarGrupo(grupoInicial.ID_Grupo);
				}
			}
		}
		@Override
		public void GrupoCambiado(String grupoID, int puerto) { //se dispara cuando el grupo se cambia
			Logg.d(TAG, "Grupo cambiado a " + grupoID + ": puerto " + puerto);
			Login.TerminarLogin();
			GrupoEquipo ge = Grupos.ObtenerGrupo(grupoID);
			com.eleinco.ezvoxptt.business.App.GrupoActual = ge;			
			try 
			{
				//Settings.PORT = puerto;
				com.eleinco.ezvoxptt.business.App.Loggeado = true;
                ConectadoYRegistrado();
                Voz.Asociar(puerto);
//				if(Login.IniciarLogin())
//				{
//					ConectadoYRegistrado();
//				}
//				else
//				{
//					Logg.d(TAG, "No logueado... Intentando de nuevo...");
//					if(grupoInicial != null)
//					{
//						TiempoRealDatos.CambiarGrupo(grupoInicial.ID_Grupo);
//					}
//				}
			}
			catch (Exception e)
			{
				Logg.d(TAG, "NOOOOOOOOOOOOOOO???");
				Logg.e(TAG, e.toString());
			}
		}
		@Override
		public void EquipoHablando(String grupoId, String equipoId, boolean hablando) { //se dispara cuando el estado de conexion cambia
			Logg.d("AAAAAAAAAAAAAAAAA", "Hablandoooooo " +equipoId);
			EnviarEquipoHablando(grupoId, equipoId, hablando);
		}
	};
	//recibe notifiacion del evento para indicar el estado del servidor udp
	EstadoUDPListener estadoUdpListener = new EstadoUDPListener() {			
		@Override
		public void EstadoCambiado(boolean conectado) {
			if(conectado == false){
				UDP.Finalizar();
				TiempoRealDatos.DetenerServidores();
				TiempoRealDatos.IniciarServidores();
			}
		}
	};
	/*CONTROL DE EVENTOS*/
	//guarda los eventos de servicio
	private static List<ServicioEventListener> listeners = new ArrayList<ServicioEventListener>();
	//agrega un escuchador de evento
    public static void addServicioListener(ServicioEventListener listener) {
        listeners.add(listener);
    }
    //remueve un escuchador del evento
    public static void removeServicioListener(ServicioEventListener listener) {
    	if(listeners.contains(listener))
    		listeners.remove(listener);
    }
    //se dispara cuando no hay un grupo
    private void NoTieneGrupos() {
    	for (ServicioEventListener dr : listeners)
    	{
    		try
    		{
    			dr.NoTieneGrupos();
    		}
    		catch(Exception ex) {
    			Logg.e(TAG, "Error al notificar evento NoTieneGrupos", ex);
    		}
    	}
    }
    //se dispara cuando hay un error de red
    private void ErrorDeRed() 
    {
    	for (ServicioEventListener dr : listeners) 
    	{
    		try{
    			dr.ErrorDeRed();
    		}
    		catch(Exception ex){
    			Logg.e(TAG, "Error al notificar evento ErrorDeRed", ex);
    		}
    	}
    }
    //se dispara cuando el identificador no se ha registrado
    private void SinRegistrarID()
    {
    	for (ServicioEventListener dr : listeners) 
    	{
    		try{
    			dr.SinRegistrarID();
    		}
    		catch(Exception ex){
    			Logg.e(TAG, "Error al notificar evento SinRegistrarID", ex);
    		}
    	}
    }
    //se dispara cuando el identificador no es valido
    private void IDNoValido()
    {
    	for (ServicioEventListener dr : listeners) 
    	{
    		try{
    			dr.IDNoValido();
    		}
    		catch(Exception ex){
    			Logg.e(TAG, "Error al notificar evento IDNoValido", ex);
    		}
    	}
    }
    //se dispara cuando esta conectado y registrado
    private void ConectadoYRegistrado() 
    {
    	for (ServicioEventListener dr : listeners) 
    	{
    		try{
    			dr.ConectadoYRegistrado();
    		}
    		catch(Exception ex){
    			Logg.e(TAG, "Error al notificar evento ConectadoYRegistrado", ex);
    		}
    	}
    }
    //se dispara cuando se ha desconectado
    private void Desconectado()
    {
    	for (ServicioEventListener dr : listeners) 
    	{
    		try{
    			dr.Desconectado();
    		}
    		catch(Exception ex){
    			Logg.e(TAG, "Error al notificar evento Desconectado", ex);
    		}
    	}
    }
	//se dispara cuando el identificador no se ha registrado
	private void EnviarEquipoHablando(String grupoId, String equipoId, boolean hablando)
	{
		for (ServicioEventListener dr : listeners)
		{
			try{
				dr.EquipoHablando(grupoId, equipoId, hablando);
			}
			catch(Exception ex){
				Logg.e(TAG, "Error al notificar evento EquipoHablando", ex);
			}
		}
	}
}
