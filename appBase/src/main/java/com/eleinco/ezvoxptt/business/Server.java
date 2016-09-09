package com.eleinco.ezvoxptt.business;

import java.io.IOException;
import java.net.SocketException;
import java.net.UnknownHostException;
//clase tipo interfaz que permite hacer llamado a un tipo de conexion (udp o tcp)
public class Server {
	private static int protocolo = 0; //0: udp, 1, tcp
	//obtiene un valor que indica si se esta conectado
	public static boolean GetConnected()
	{
		return UDP.GetConnected();
	}
	//inicia el servidor
	public static boolean Start()
	{
		return UDP.PrepararUdp();
	}
	//detiene el servidor
	public static void Stop() throws IOException
	{
		com.eleinco.ezvoxptt.business.App.Loggeado = false;
		if(protocolo == 0)
			UDP.Finalizar();
		else TCP.Close();
	}
	//envia informacion al servidor
	public static boolean Send(byte[] data) throws IOException
	{
		if(protocolo == 0)
			return UDP.Send(data);
		else return TCP.Send(data);
	}
}
