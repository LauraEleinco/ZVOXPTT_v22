package com.eleinco.ezvoxptt.business;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import android.content.Context;
import android.util.Log;

import com.eleinco.ezvoxptt.common.Logg;
import com.eleinco.ezvoxptt.common.Settings;

//clase de exposicion del negocio de la entidad contactos
public class Contactos {

	private static String fileName = "contacts.dat";	//nombre del archivo de los contactos almacenados
	private static ArrayList<com.eleinco.ezvoxptt.entities.Equipo> TodosLosContactos; //lista de todos los cotnactos
	//consulta un contacto por su id
	public static com.eleinco.ezvoxptt.entities.Equipo Consultar(final String idContacto, final Context contexto)
	{
		if(TodosLosContactos == null) //si la lista no se ha llenado se va al servidor.
		{
			try {
				TodosLosContactos = Listar(contexto);
			} 
			catch (Exception e) {
				return new com.eleinco.ezvoxptt.entities.Equipo(idContacto, idContacto);
			}
		}
		//buscar el contacto en la lista
		for(com.eleinco.ezvoxptt.entities.Equipo contacto : TodosLosContactos)
		{
			if(contacto == null)
				continue;
			if(contacto.IDEquipo.equals(idContacto))
				return contacto;
		}
		new Thread(new Runnable() {
			@Override
			public void run() {
				try
				{
					com.eleinco.ezvoxptt.entities.Equipo objEquipo = Equipos.Consultar(idContacto);
					Agregar(objEquipo, contexto);
					Logg.d("Contactos", "Se consultó y agregó el contacto " + objEquipo.Alias);
				}
				catch (Exception ex)
				{
					ex.printStackTrace();
				}
			}
		}).start();

		return new com.eleinco.ezvoxptt.entities.Equipo(idContacto, idContacto);
	}
	//obtiene una lista de todos los contactos guardados
	public static ArrayList<com.eleinco.ezvoxptt.entities.Equipo> Listar(Context contexto) throws Exception
	{
		ArrayList<com.eleinco.ezvoxptt.entities.Equipo> contactos = 
				new ArrayList<com.eleinco.ezvoxptt.entities.Equipo>();
		String texto = Settings.Contactos(contexto, null);
		if(texto == null || texto.trim().length() == 0)
		{
			TodosLosContactos = contactos;
			return contactos;
		}

		Log.e("APAAA", "APAAA " + texto);
		String[] lineas = texto.trim().split("\n");
		String idsTemporales = "";
		for(String linea : lineas)
		{
			String[] campos = linea.trim().split("\t");
			com.eleinco.ezvoxptt.entities.Equipo equipo = new com.eleinco.ezvoxptt.entities.Equipo(campos[0].trim(), campos[1].trim());
			if(idsTemporales.contains(equipo.IDEquipo + ","))
				continue;
			contactos.add(equipo);
			idsTemporales+=equipo.IDEquipo + ",";
		}
		TodosLosContactos = contactos;
		return contactos;
	}
	//guarda un nuevo contacto
	public static boolean Agregar(com.eleinco.ezvoxptt.entities.Equipo objContacto, Context contexto) throws IOException, Exception
	{
		ArrayList<com.eleinco.ezvoxptt.entities.Equipo> contactos = Listar(contexto); 
		contactos.add(objContacto);
		return GuardarContactos(contactos, contexto);
	}
	//elimina un contacto
	public static boolean Eliminar(String id, Context contexto) throws IOException, Exception
	{
		ArrayList<com.eleinco.ezvoxptt.entities.Equipo> contactos = Listar(contexto);
		for(int i = 0; i < contactos.size(); i++)
		{
			if(contactos.get(i).IDEquipo.equals(id))
			{
				contactos.remove(i);
			}
		}
		return GuardarContactos(contactos, contexto);
	}
	//guarda la lista de contactos en el archivo
	private static boolean GuardarContactos(ArrayList<com.eleinco.ezvoxptt.entities.Equipo> contactos, Context contexto) throws IOException, Exception
	{
		StringBuilder texto = new StringBuilder();
		for(com.eleinco.ezvoxptt.entities.Equipo contacto : contactos)
		{
			texto.append(contacto.IDEquipo + "\t" + contacto.Alias + "\n");
		}
		Settings.Contactos(contexto, texto.toString());
		return true;
	}
}
