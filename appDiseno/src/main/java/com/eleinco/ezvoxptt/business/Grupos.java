package com.eleinco.ezvoxptt.business;

import com.eleinco.ezvoxptt.common.Logg;
import com.eleinco.ezvoxptt.entities.GrupoEquipo;

import org.ksoap2.serialization.SoapObject;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;
//clase de exposicion de la logica de negocio de la entidad grupos
public class Grupos {	
	private static String TAG = "Grupos";
	//obtiene una lista de todos los gruops a los que tengo acceso
	public static ArrayList<com.eleinco.ezvoxptt.entities.GrupoEquipo> TodosMisGrupos = null;
	//carga una lista de los grupos a los que tengo acceso
	public static ArrayList<com.eleinco.ezvoxptt.entities.GrupoEquipo> CargarMisGrupos(String miIdEquipo) throws IOException, Exception
	{
		TodosMisGrupos = Consultar(miIdEquipo);
		return TodosMisGrupos;
	}
	//obtiene un grupo por su id
	public static GrupoEquipo ObtenerGrupo(String grupoID)
	{
		if(TodosMisGrupos != null)
		{
			for(GrupoEquipo g : TodosMisGrupos)
			{
				if(g.ID_Grupo.equals(grupoID))
					return g;
			}
		}
		Logg.d(TAG, "No se encontró el grupo " + grupoID + " en la lista local");
		return null;
	}
	//obtiene una lista de todos los grupos de un grupo padre o dueño
	public static ArrayList<com.eleinco.ezvoxptt.entities.Grupo> Listar(String id_grupo_padre) throws IOException, Exception
	{
		SoapObject objeto = WebService.InvokeObjectMethod1Parametro("ListarGrupos", "id_grupo_padre", id_grupo_padre);
		ArrayList<com.eleinco.ezvoxptt.entities.Grupo> grupos = 
				new ArrayList<com.eleinco.ezvoxptt.entities.Grupo>();
		for (int i = 0; i < objeto.getPropertyCount(); i++)
		{
			SoapObject gr = (SoapObject)objeto.getProperty(i);
			com.eleinco.ezvoxptt.entities.Grupo objGrupo = 
					new com.eleinco.ezvoxptt.entities.Grupo(
							gr.getProperty("ID_Grupo").toString(),
							gr.getProperty("Nombre").toString(),
							gr.getProperty("ClaveIngreso").toString(),
							Integer.parseInt(gr.getProperty("Equipos").toString()),
							gr.getProperty("ID_Grupo_Padre").toString(),
							gr.getProperty("ID_Grupo_Dueno").toString());
			grupos.add(objGrupo);
		}
		return grupos;
	}
	//guarda un nuevo grupo
	public static String Anadir(com.eleinco.ezvoxptt.entities.Grupo objGrupo) throws IOException, XmlPullParserException
	{
		String obj = WebService.InvokePrimitiveMethod5Parametro("AnadirGrupo", 
				"nombre", objGrupo.Nombre, "claveIngreso", objGrupo.ClaveIngreso, 
				"equipos", objGrupo.Equipos, "id_grupo_padre", objGrupo.ID_Grupo_Padre,
				"id_grupo_dueno", objGrupo.ID_Grupo_Dueno);
		return obj;
	}
	//consulta el grupo al que pertenece un equipo
	public static ArrayList<com.eleinco.ezvoxptt.entities.GrupoEquipo> Consultar(String idEquipo) throws IOException, Exception
	{
		SoapObject objeto = WebService.InvokeObjectMethod1Parametro("ConsultarGrupos", "idEquipo", idEquipo);
		ArrayList<com.eleinco.ezvoxptt.entities.GrupoEquipo> grupos = 
				new ArrayList<com.eleinco.ezvoxptt.entities.GrupoEquipo>();
		for (int i = 0; i < objeto.getPropertyCount(); i++)
		{
			SoapObject gr = (SoapObject)objeto.getProperty(i);
			
			
			
			com.eleinco.ezvoxptt.entities.GrupoEquipo objGrupo = 
					new com.eleinco.ezvoxptt.entities.GrupoEquipo(
							idEquipo,
							gr.getProperty("ID_Grupo").toString(),
							gr.getProperty("NombreGrupo").toString(),
							gr.getProperty("ClaveIngreso").toString(),
							Integer.parseInt(gr.getProperty("CantidadEquipos").toString()),
							gr.getProperty("ID_Grupo_Padre").toString(),
							Boolean.parseBoolean(gr.getProperty("Admin").toString()),
							gr.getProperty("ID_Grupo_Dueno").toString());
			
			grupos.add(objGrupo);
		}
		return grupos;
	}
	//elimina un grupo por su id
	public static boolean Eliminar(String grupoId) throws IOException, XmlPullParserException {
		String obj = WebService.InvokePrimitiveMethod1Parametro("EliminarGrupo", 
				"grupoID", grupoId);
		return Boolean.parseBoolean(obj);
	}
}
