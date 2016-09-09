package com.eleinco.ezvoxptt.business;

import android.util.Log;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.net.UnknownHostException;
//clase que expone la logica de negocio para la entidad Login
public class Login {
	private static String TAG = "Login";
	public static boolean IsLoged = false; //indica si esta logueado
	public static boolean LoginAhora = false; //indica si se debe loguear en determinado momento
	public static boolean Logining = false; //si se esa haciendo el proceso de logueol actualmente
	//inicia el logueo
	public static boolean IniciarLogin() throws IOException, Exception
	{
		Logining = true;
		com.eleinco.ezvoxptt.business.App.Loggeado = false; //se reinicia la varibable
		if (Server.GetConnected() == false) //se llama al servidor para conectar el socket
		{
			try
			{
				if (com.eleinco.ezvoxptt.business.Server.Start() == false) //se inicia el servidor de escucha
				{
					Logining = false;
					throw new Exception("Imposible conectar con el servidor");
				}
				else
				{
					Log.d(TAG, "Conectado correctamente al servidor");
				}
			}
			catch(UnknownHostException ex)
			{
				Logining = false;
				throw new Exception("Imposible conectar con el servidor");
			}			
		}
		else
		{
			Log.d(TAG, "El servidor ya está conectado");
		}
		//se obtiene el id del equipo actual
		String idEquipo = com.eleinco.ezvoxptt.business.App.GetID();
		
		//Enviar comando de logueo al servidor socket
		//try 
		//{
		//se envia la trama de login
			if (com.eleinco.ezvoxptt.business.Server.Send(
					com.eleinco.ezvoxptt.business.Protocol.Login(idEquipo)) == false)
			{
				Logining = false;
				throw new Exception("Fallo de comunicación con el servidor");
			}
			int espera = 5000;	
			int intent = 3;
			//se espera a que el server responda la autorizacion de logueo
			while (com.eleinco.ezvoxptt.business.App.Loggeado == false)
			{
				Thread.sleep(1000);
				espera -= 1000;
				if(espera == 0)
				{
					espera = 5000;
					intent--;
					if(intent == 0)
					{
						Logining = false;
						throw new Exception("Login no establecido con el servidor");
					}
				}
			}
			Log.d(TAG, "Logueo correcto");
			Logining = false;
			return true;
		//}
		//catch (Exception e) 
		//{
			//e.printStackTrace();
			//Logining = false;
			//throw new Exception("Fallo de comunicación con el servidor");
		//}		
	}
	//finaliza el login con el servidor
	public static void TerminarLogin()
	{
		//String idEquipo = com.eleinco.ezvoxptt.business.App.GetID(context); 
		//com.eleinco.ezvoxptt.business.Server.Send(
		//		com.eleinco.ezvoxptt.business.Protocol.Logout(idEquipo, 
		//				com.eleinco.ezvoxptt.business.App.GrupoActual.ID_Grupo));
		try {
			com.eleinco.ezvoxptt.business.Server.Stop();
		} catch (IOException e) 
		{
			e.printStackTrace();
		}
	}
	//loguea para acceso a la cuenta maestra
	public static boolean LoginMaster(String contrasena) throws IOException, XmlPullParserException
	{
		String obj = WebService.InvokePrimitiveMethod1Parametro("LoginMaster", "contrasena", contrasena);
		return Boolean.parseBoolean(obj);
	}
}
