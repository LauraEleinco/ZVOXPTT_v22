package com.eleinco.ezvoxptt.business;

import android.util.Log;

import com.eleinco.ezvoxptt.common.Settings;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.net.UnknownHostException;
//clase que establece conexion TCP con el servidor
public class TCP {
	private static Socket socket = null;
	private static boolean listening = false; //variable que indica si se esta corriendo el servidor
	private static String TAG = "TCP";
	public static boolean Conectar() throws UnknownHostException, IOException
	{
		//se intenta conectar con el server
		socket = new Socket(Settings.SERVER, Settings.PORT);
		//se inicia un hilo
		new Thread(new Runnable() {			
			@Override
			public void run() {
				listening = true; 
				InputStream input;
				try {
					input = socket.getInputStream();
						
					//String text;
			        byte[] message = null;
		        	while (listening)
		        	{
		        		try
		        		{
		        			//Recibir hasta 5000 bytes
			        		message = new byte[5000];
			        		int totalBytes = input.read(message);
				        	
			        		//Obtener los primeros 4 digitos de lo recibido (Tamaño del paquete)
				        	String text = new String(message, 0, 4).trim();
				        	//Intentar convertirlo a entero
				        	totalBytes = 0;
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
				        	//procesar los adtos recibidos
			        		Voz.DatosVozRecibidos(message, totalBytes);
			        		//Liberar recursos
				        	message = null;
		        		}
		        		catch(final Exception ex)
		        		{
		        			Log.e(TAG, ex.toString());
		        		}
		        	}
		        	input.close();
				} catch (IOException e) {
					try {
						Close();
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}
			}
		}).start();
		return true;
	}
	//envia una arreglo al servidor
	public static boolean Send(byte[] data) throws IOException
	{
		socket.getOutputStream().write(data);
		return true;
	}
	//desconecta el socket
	public static void Close() throws IOException
	{
		if(socket != null && socket.isConnected())
			socket.close();
	}
}
