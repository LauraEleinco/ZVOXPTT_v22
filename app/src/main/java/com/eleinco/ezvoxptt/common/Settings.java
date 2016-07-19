package com.eleinco.ezvoxptt.common;

import android.content.Context;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;
//contiene las configuraciones en version vieja de la app
public class Settings {
	//public static final String SERVER = "10.0.2.2";//50.63.157.41  192.168.1.101   10.0.2.2";
	//public static final String SERVER = "50.63.157.41"; //ip del servidor udp y tcp - servidor eleinco
	public static final String SERVER = "13.66.63.82"; //ip del servidor udp y tcp - servidor eleinco
	public static final int TCP_PORT = 3000; //puerto del servidor tcp
	public static int PORT = 3333; //puerto udp
	//obtiene mi identificador
	public static String MiID(Context context, String setMiID)
	{
		if(setMiID == null)
			return ReadPreference("MiID", context);
		SavePreference("MiID", setMiID, context);
		return setMiID;
	}
	public static String Contactos(Context context, String contactos){
		if(contactos == null)
			return ReadPreference("Contactos", context);
		SavePreference("Contactos", contactos, context);
		return contactos;
	}

	//lee una preferencia
	private static String ReadPreference(String key, Context context)
	{
		String val = PreferenceManager.getDefaultSharedPreferences(context).getString(key, null);
		return val;
	}
	//escribe una preferencia
	private static void SavePreference(String key, String val, Context context)
	{
		Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        editor.putString(key, val);              
        editor.commit();
	}
}
