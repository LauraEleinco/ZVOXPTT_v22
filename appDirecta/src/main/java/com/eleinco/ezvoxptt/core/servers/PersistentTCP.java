package com.eleinco.ezvoxptt.core.servers;

import android.util.Log;

import com.eleinco.ezvoxptt.common.Logg;
import com.eleinco.ezvoxptt.core.events.SocketEventListener;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
//clas que ejecuta un servidor tcp persistente
public class PersistentTCP 
{
	private Socket socket = null; //el objeto socket para establecer la oconexion
	private InputStream input = null; 
	private OutputStream output = null;
	private String TAG = "PersistentTCP";
	private boolean running = false; //indica si esta corriendo el servidor
    private byte[] bufferIn = null;
	private ArrayList<String> cola = new ArrayList<String>(); //cola de mensajes por enviar
	private String Server = null;
	private int Port = 0;
	//inicia el servidor persistente
	public void Iniciar(final String server, final int port)
	{
		EstadoCambiado(false);
		this.Server = server;
		this.Port = port;
		new Thread(new Runnable() {			
			@Override
			public void run() {
				running = true;
				while (running)
				{
					try
					{
						Log.d(TAG, "Conectando al servidor TCP " + server + ":" + port);
						socket = new Socket(server, port);
						input = socket.getInputStream();
						output = socket.getOutputStream();
						EstadoCambiado(true);
						Log.d(TAG, "Conectado correctamente! escuchando...");
					}
					catch(Exception ex)
					{
						socket = null;
						input = null;
						output = null;
						Log.e(TAG, "Error al conectar servidor TCP " + server + ":" + port + ", " + ex.toString() + "... Intentando en 10 segundos...");						
						try { Thread.sleep(10000); } 
						catch (InterruptedException e) { e.printStackTrace(); }
						continue;
					}
						
					int keepAliveLimit = 22000;
					int keepAliveCount = 0;
					byte[] bufferTmp = new byte[128];
					int posBufferTmp = 0;
					
					//persiste mientras está corriendo el servidor y el socket este conectao
		        	while (running && socket != null && socket.isConnected()) 
		        	{
		        		try
		        		{
		        			if(input.available() == 0)
		        			{
		        				Thread.sleep(200);
		        				keepAliveCount+=200;
		        				if (keepAliveCount >= keepAliveLimit)
		        				{
			        				Logg.d(TAG, "Tiempo keep alive cumplido.");
		        					//cumplido timeout alive
		        					Desconectar();
		        					break;
		        				}
		        				//intentar enviar datos de la cola
		        				while (cola.size() > 0)
		        				{
		        					synchronized(cola) 
		        					{	
	    		        				if(Write(cola.get(0)) == false)
	    		        				{
	    		        					//si no es posible enviar se sale y se intenta re conectar
	    		        					break;
	    		        				}
	        							cola.remove(0);
	    		        				Thread.sleep(50);
		        					}
		        				}
		        				if(cola.size() > 0)
		        					break;
		        				continue;
		        			}
		        			
		        			boolean ultimo = false;
		        			
		        					        			
		        			while (input.available() > 0) //lee byte por byte desde un arroba @ hasta un numeral #
		                    {
		                        byte b = (byte)input.read();
		                        if (b == '@')
		                            posBufferTmp = 0;
		                        else if (b == '#')
		                            ultimo = true;
		                            
		                        bufferTmp[posBufferTmp] = b;
		                        posBufferTmp++;
		                        if (ultimo)
		                        {
		                            bufferIn = new byte[posBufferTmp];
		                            System.arraycopy(bufferTmp, 0, bufferIn, 0, bufferIn.length);
		                            posBufferTmp = 0;
		                            break;
		                        }
		                        
		                        if(posBufferTmp >= 4)
				        		{
		                        	int i = posBufferTmp - 1;		                        	
				        			if(bufferTmp[i - 3] ==  0x00 &&
				        					bufferTmp[i - 2] == 0x01 &&
				        							bufferTmp[i - 1] == 0x04 &&
				        									bufferTmp[i] == 0x00)
				        			{
				        				Logg.d(TAG, "Keep alive recibido de " + Server + ":" + Port);
				        				keepAliveCount = 0;
				        				posBufferTmp = 0;
				        				continue;
				        			}
				        		}
		                    }
		        			
		        			if(bufferIn == null)
		                        continue;
		        			
	        				//byte[] bufferTemporal = new byte[5000];
			        		//int totalBytes = input.read(bufferTemporal);
			        		
			        		keepAliveCount=0;
			        		
			        		//bufferIn = new byte[totalBytes];
				        	//System.arraycopy(bufferTemporal, 0, bufferIn, 0, totalBytes);    		
				        	
				        	ProcessData(bufferIn);
				        	
				        	bufferIn = null;
		        		}
		        		catch(final Exception ex)
		        		{
		        			Log.e(TAG, ex.toString());
		        		}
		        	}
		        	try
		        	{
		        		//finaliza y libera recursos
		        		bufferIn = null;
		        		input.close();
		        		output.close();
		        		socket.close();
		        	}
		        	catch(Exception ex)
		        	{
		        		
		        	}
	        		input = null;
	        		output = null;
	        		socket = null;
				}
			}
		}).start();
	}
	//detiene el servidor persistente
	public void Detener()
	{
		running = false;
		Desconectar();
	}
	//desconecta el cliente tcp
	private void Desconectar()
	{
		EstadoCambiado(false);
		if(output != null)
		{
			try {
				output.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		if(input != null)
		{
			try{
				input.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		if(socket != null)
		{
			try {
				socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	//envia un dato al servidor
	public boolean Send(String data, boolean encolar)
	{
		try
		{
			if (encolar)
			{
				synchronized(cola) {
					cola.add(data);
				}
				return true;
			}
			else
			{
				return Write(data);
			}
		}
		catch(Exception ex)
		{
			Logg.e(TAG, ex.toString());
		}
		return false;
	}
	//escribe un datoa l servidor
	private boolean Write(String data)
	{
		try 
		{
			data = "@" + data + "#";
			output.write(data.getBytes());
			Logg.d(TAG, "\"" + data + "\" enviado. TCP " + Server + ":" + Port );
			return true;
		} 
		catch (IOException e) 
		{
			Logg.e(TAG, e.toString());
			return false;
		}
	}
	//pcocesa los datos recibidos
	private void ProcessData(byte[] data)
	{
		if(data != null && data.length >= 3)
		{
			if(data[0] == '@' &&
					data[data.length - 1] == '#')
			{
				String datos = new String(data, 1, data.length - 2);
				DatosRecibidos(datos);			
			}
		}
		
	}
	
	/*CONTROL DE EVENTOS*/
	
	private List<SocketEventListener> listeners = new ArrayList<SocketEventListener>();

    public void addDataReceivedListener(SocketEventListener listener) {
        listeners.add(listener);
    }

    private void DatosRecibidos(String datos) {
    	for (SocketEventListener dr : listeners)
    	{
    		try{
    			dr.DatosRecibidos(datos);
    		}
    		catch(Exception ex){
    			Logg.e(TAG, "Error al notificar evento DatosRecibidos", ex);
    		}
    	}    	
    }
    private void EstadoCambiado(boolean conectado){
    	for (SocketEventListener dr : listeners){
    		try{
    			dr.EstadoCambiado(conectado);
    		}
    		catch(Exception ex){
    			Logg.e(TAG, "Error al notificar evento EstadoCambiado", ex);
    		}
    	}
    }
}

