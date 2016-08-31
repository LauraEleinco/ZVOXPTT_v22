package com.eleinco.ezvoxptt.common;

import android.util.Log;

//expone metodos para escribir en log
public class Logg {
	//escribe un mensaje de tipo debug
	public static void d(String TAG, String mensaje)
	{
		Log.d(TAG, mensaje);
	}
	//escribe un mensaje de tipo error
	public static void e(String TAG, String mensaje)
	{
		Log.e(TAG, mensaje);
	}
	//escribe un mensaje de tipo error indicando la excepcion
	public static void e(String TAG, String mensaje, Exception ex)
	{
		Log.e(TAG, mensaje + ":" + ex.toString());
	}
}
