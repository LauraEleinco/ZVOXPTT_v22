package com.eleinco.ezvoxptt.business;

import android.util.Log;

import com.eleinco.ezvoxptt.common.Logg;
import com.eleinco.ezvoxptt.common.Settings;
import com.eleinco.ezvoxptt.core.events.EstadoUDPListener;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

//permite establecer una conexion udp con el servidor
public class UDP {
	private static String TAG = "UDP";
	private static DatagramSocket socket_send;
	//private static DatagramSocket socket_receive;
	private static DatagramPacket packet_send;
	private static DatagramPacket packet_receive;
	//public static final String SERVER = "192.168.0.254";
	private static Thread threadUDP = null;
	private static Thread threadAlive = null;
	private static int TimeAliveMax = 100;
	private static int TimeAlive = 100;
	
	//private static InetAddress serverAddress;
	public static boolean listening = false;
	//indica si esta conectado
	public static boolean GetConnected()
	{
		if(socket_send == null)
			return false;
		return socket_send.isConnected();
	}
	//se contecta al servidor
	public static boolean PrepararUdp()
	{
		try
		{
			socket_send = new DatagramSocket();
			Log.d(TAG, "Conectando a " + Settings.SERVER + ":" + Settings.PORT + "...");
	        //serverAddress = InetAddress.getByName(App.SERVER);
			socket_send.connect(InetAddress.getByName(Settings.SERVER), Settings.PORT);		
	        Log.d(TAG, "Conectado a " + Settings.SERVER + ":" + Settings.PORT);
	        listening = true;
		}
		catch(Exception ex)
		{
			Logg.e(TAG, ex.toString(), ex);
			socket_send = null;
			return false;
		}
		//socket_send.connect(InetAddress.getByName("192.168.1.102"), 6666);
		
		
    	//socket_receive = new DatagramSocket(App.PORT_RECEIVE);
        threadUDP = new Thread(new Runnable() {
			@Override
			public void run() {
				try
				{
					String text;
			        byte[] message = null;
			        
		        	while (listening)
		        	{
		        		try
		        		{
		        			//Recibir hasta 5000 bytes
			        		message = new byte[5000];		        		
			        		packet_receive = new DatagramPacket(message, message.length);
	
		        			//Log.d(TAG, "Esperando para recibir");
			        		socket_send.receive(packet_receive);
			        		TimeAlive = TimeAliveMax;
			        		//Log.d(TAG, "Recibido");
			        		
				        	//Obtener los primeros 4 digitos de lo recibido (Tamaño del paquete)
				        	text = new String(message, 0, 4).trim();
				        	//Intentar convertirlo a entero
				        	int totalBytes = 0;
				        	try
				        	{
				        		totalBytes = Integer.parseInt(text);
				        	}
				        	catch(Exception e)
				        	{
				        		//Si no convierte, no se procesa la trama
				        		continue;
				        	}
				        	//La posicion 4 comienza la trama, dene empezar con @
				        	if (message[4] != 0x40)
				        	{
				        		continue;
				        	}
				        	App.SegundosUltimoContactoServidor = 0;
			        		Voz.DatosVozRecibidos(packet_receive.getData(), totalBytes);
			        		//Liberar recursos
				        	message = null;
				        	packet_receive = null;
		        		}
		        		catch(final Exception ex)
		        		{
		        			Log.e(TAG, ex.toString());
		        			break;
		        		}
		        	}
				}
				catch(Exception ex)
				{
					ex.printStackTrace();
					Logg.e(TAG, "Error en hilo listener UDP", ex);
				}
			}
		});
        //inicia un nuevo hilo que envia un dato de vida cada x tiempo
        threadAlive = new Thread(new Runnable() {
			@Override
			public void run() {
				try
				{
		        	while (listening)
		        	{
		        		try
		        		{
		        			Thread.sleep(1000);
			        		TimeAlive--;
			        		if(TimeAlive == 0)
			        		{
			        			Send(new byte[] { 0x00 });
			        		}
		        		}
		        		catch(final Exception ex)
		        		{
		        			Log.e(TAG, ex.toString());
		        			break;
		        		}
		        	}
				}
				catch(Exception ex)
				{
					ex.printStackTrace();
					Logg.e(TAG, "Error en hilo alive UDP", ex);
				}
			}
		});
        
        threadUDP.start();
        return true;
	}
	//finaliza el servidor udp
	public static void Finalizar()
	{
		try
		{
			Log.d(TAG, "Desconectando UDP");
			if(threadUDP != null)
			{
				Log.d(TAG, "Hilo no es nulo");
				if(threadUDP.isAlive())
				{
					Log.d(TAG, "Lo tengo vivo");
					threadUDP.interrupt();
					Log.d(TAG, "Hilo UDP destruido");					
				}
			}
			//socket_receive.close();
			listening = false;
			if(socket_send != null)
			{
				socket_send.disconnect();			
				socket_send.close();
			}
			socket_send = null;
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
		listening = false;
	}
	//envia un arrelo al servidor
	public static boolean Send(byte[] buffer) throws IOException
	{
		return Send(buffer, buffer.length);
	}
	//envía un arreglo al servidor indicando el tamaño de bytes
	public static boolean Send(byte[] buffer, int count) throws IOException
	{
		App.SumBytesSend += buffer.length;
		// place contents of buffer into the packet
        packet_send = new DatagramPacket(buffer, count);
		if(packet_send == null){
			Log.d(TAG, "packet_send es null");
			return false;
		}

		if (socket_send == null) 
		{
			EstadoCambiado(false);
			Log.d(TAG, "socket_send es null");
			if(Login.Logining == false)
			{
				Login.LoginAhora = true;
			}
			return false;
		}
		socket_send.send(packet_send);
        // send the packet
        //socket_send.send(packet_send);
		packet_send = null;
        return true;
	}
	/*CONTROL DE EVENTOS*/
	
	private static List<EstadoUDPListener> listeners = new ArrayList<EstadoUDPListener>();

    public static void addEstadoCambiadoListener(EstadoUDPListener listener) {
        listeners.add(listener);
    }
	
    private static void EstadoCambiado(boolean conectado)
    {
    	for(EstadoUDPListener l : listeners)
    	{
    		try{
    			l.EstadoCambiado(conectado);
    		}
    		catch(Exception ex){
    		
    		}
    	}
    }
}
