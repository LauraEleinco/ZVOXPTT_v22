package com.eleinco.ezvoxptt.core.business;
import android.util.Log;

import com.eleinco.ezvoxptt.business.App;
import com.eleinco.ezvoxptt.business.Voz;
import com.eleinco.ezvoxptt.common.Enums.EstadoTiempoReal;
import com.eleinco.ezvoxptt.common.Enums.TipoComando;
import com.eleinco.ezvoxptt.common.Logg;
import com.eleinco.ezvoxptt.common.Settings;
import com.eleinco.ezvoxptt.core.events.EstadoContactoEventListener;
import com.eleinco.ezvoxptt.core.events.SocketEventListener;
import com.eleinco.ezvoxptt.core.events.TiempoRealEventListener;
import com.eleinco.ezvoxptt.core.servers.PersistentTCP;

import java.util.ArrayList;
import java.util.List;
//expone funcionalidad para crear un servidor de tiempo real por medio de una conexion Tcp persistente
public class TiempoRealDatos {
	private static PersistentTCP tcp = null;
	private static String TAG = TiempoRealDatos.class.getName();
	//inicia la conexion con el servidor tcp
	public static void IniciarServidores()
	{
		Logg.d(TAG, "Iniciando servidores TiempoRealDatos...");
		tcp = new PersistentTCP();
		tcp.addDataReceivedListener(new SocketEventListener() {			
			@Override
			public void DatosRecibidos(String datos) {
				ProcesarDatosRecibidos(datos);
			}
			@Override
			public void EstadoCambiado(boolean conectado) {
				ProcesarEstadoCambiado(conectado);
			}
		});		
		tcp.Iniciar(Settings.SERVER, Settings.TCP_PORT);
	}
	//detiene y cierra la conexion
	public static void DetenerServidores()
	{
		if(tcp != null)
		{
			tcp.Detener();
			tcp = null;
		}
	}
	//procesa los datos recibidos 
	public static void ProcesarDatosRecibidos(String datos) {
		try
		{
			Logg.d(TAG, "Recibi: " + datos);
			if(datos == null)
				return;
			String[] sep = datos.split(",");
			
			if(sep.length < 2)
				return;
			String comando = sep[0];
			if(comando.equals("L")) //confirmación de registro
			{
				String aceptado = sep[1]; //1: si, 0:no
				if(aceptado.equals("1"))
					EstadoCambiado(EstadoTiempoReal.ConectadoRegistrado);
				else EstadoCambiado(EstadoTiempoReal.Rechazado);
			}
			else if(comando.equals("G")) //estas en un nuevo grupo
			{
				String grupoID = sep[1];
				int puerto = Integer.parseInt(sep[2]);
				GrupoCambiado(grupoID, puerto);
			}
			else if(comando.equals("E")) //estado de un equipo
			{
				String equipoId  = sep[1];
				String e  = sep[2];
				boolean conectado = false;
				if(e.equals("1"))
					conectado = true;
				EstadoEquipoCambiado(equipoId, conectado);
			}
			else if(comando.equals("P")) //respuesta de PTT
			{
				String grupoID = sep[1];
				String respuesta = sep[2];
				if (respuesta.equals("0"))
					Voz.PttPermitido = -1; //no PTT
				else if(respuesta.equals("1"))
					Voz.PttPermitido = 1; //si PTT
				else if(respuesta.equals("-1"))
					Voz.PttPermitido= -1; //TODO: Controlar respuestas negativas como el grupo no existe.
			}
			else if(comando.equals("T") || comando.equals("H")){
				String grupoID = sep[1];
				String equipoHablando = sep[2];
				if (comando.equals("T"))
					EquipoHablando(grupoID, equipoHablando, true);
				else EquipoHablando(grupoID, equipoHablando, false);
			}
			else {
				Log.d("ADVERTENCIA", "Comando recibido desconocido :"+comando);
			}
		}
		catch(Exception ex)
		{
			Logg.e(TAG, "Error al procesar datos de TCP", ex);
		}
	}
	//Envia solicitud del login al servidor
	public static void Loguear()
	{
		Enviar(TipoComando.L.toString(), false);
	}
	//envia un cambio de grupo al servidor
	public static void CambiarGrupo(String grupoID)
	{
		Enviar(TipoComando.G.toString() + "," + grupoID, false);
	}
	//solicita el estado de un equipo
	public static void SolicitarEstadoEquipo(String equipoID){
		Enviar(TipoComando.E.toString() + "," + equipoID, true);
	}
	//solicita el estado de un equipo
	public static boolean SolicitarPTT(String equipoID, String grupo){
		return Enviar(TipoComando.P.toString() + "," + grupo, false);
	}
	//envia un dato al servidor y establece si se debe encolar
	private static boolean Enviar(String datos, boolean encolar)
	{
		String equipoID = App.Me.IDEquipo;
		return tcp.Send(equipoID + "," + datos, encolar);
	}
	public static boolean RadioHablando(String equipoID, String grupo){
		return Enviar(TipoComando.T.toString() + "," + grupo, false);
	}
	public static boolean RadioCuelga(String equipoID, String grupo){
		return Enviar(TipoComando.H.toString() + "," + grupo, false);
	}

	//procesa el cambio de estado del servidor
	public static void ProcesarEstadoCambiado(boolean conectado) {
		Logg.d(TAG, "Estado de TiempoRealDatos cambiado a " + conectado);
		if(conectado) 
		{
			EstadoCambiado(EstadoTiempoReal.ConectadoSinRegistrar);
		}
		else
		{
			EstadoCambiado(EstadoTiempoReal.Desconectado);
		}
	}
	
	/*ENVÍO DE COMANDOS*/
	public static void EntrarEnGrupo(String equipoID, String grupoID)
	{
		tcp.Send(equipoID + ",G," + grupoID, false);
	}
	
	/*MANEJO DE EVENTOS*/
	
	private static List<TiempoRealEventListener> listeners = new ArrayList<TiempoRealEventListener>();
	private static List<EstadoContactoEventListener> listenersEstado = new ArrayList<EstadoContactoEventListener>();
	

    public static void addTiempoRealListener(TiempoRealEventListener listener) {
        listeners.add(listener);
    }
    public static void addEstadoContactoListener(EstadoContactoEventListener listener){
    	listenersEstado.add(listener);
    }
    
    private static void EstadoCambiado(EstadoTiempoReal estado) {
    	for (TiempoRealEventListener dr : listeners)
    	{
    		try {
    			dr.EstadoCambiado(estado);
    		}
    		catch(Exception ex) {
    			Logg.e(TAG, "Error al notificar evento EstadoCambiado", ex);
    		}
    	}
    }
    private static void GrupoCambiado(String grupoID, int puerto) {
    	for (TiempoRealEventListener dr : listeners) {
			try {
				dr.GrupoCambiado(grupoID, puerto);
			}
			catch(Exception ex) {
				Logg.e(TAG, "Error al notificar evento EstadoCambiado", ex);
			}
    	}
    }
    private static void EstadoEquipoCambiado(String equipoID, boolean conectado){
    	for (EstadoContactoEventListener dr : listenersEstado) {
			try {
				dr.EstadoCambiado(equipoID, conectado);
			}
			catch(Exception ex) {
				Logg.e(TAG, "Error al notificar evento EstadoEquipoCambiado", ex);
			}
    	}
    }
	private static void EquipoHablando(String grupoId, String equipoId, boolean hablando){
		for (TiempoRealEventListener dr : listeners) {
			try {
				dr.EquipoHablando(grupoId, equipoId, hablando);
			}
			catch(Exception ex) {
				Logg.e(TAG, "Error al notificar evento EstadoCambiado", ex);
			}
		}
	}
}
