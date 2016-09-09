package com.eleinco.ezvoxptt.business;

import java.io.IOException;
import java.util.ArrayList;

import org.ksoap2.serialization.SoapObject;
import org.xmlpull.v1.XmlPullParserException;


//clase de exposicion del negocio de la entidad equipos
public class Equipos {
	//metodo que permite consultar un equipo por su id
	public static com.eleinco.ezvoxptt.entities.Equipo Consultar(String idEquipo) throws IOException, Exception
	{
		SoapObject resultado = 
				WebService.InvokeObjectMethod1Parametro("ConsultarEquipo", "idEquipo", idEquipo);
		if(resultado == null)
			return null;
		com.eleinco.ezvoxptt.entities.Equipo objEquipo = 
				new com.eleinco.ezvoxptt.entities.Equipo(
						resultado.getPropertyAsString("IDEquipo"), 
						resultado.getPropertyAsString("Alias"));
		return objEquipo;
	}
	//metodo que permite obtener una lista de equiops por de un id de grupo.
	//la varibale esDueno es para indicar si se consultan los equipos de los que el grupo es dueño o no
	public static ArrayList<com.eleinco.ezvoxptt.entities.EquipoGrupo> Listar(String idGrupo, boolean esDueno) throws IOException, Exception
	{
		SoapObject objeto = WebService.InvokeObjectMethod2Parametro("ListarEquipos", 
				"idGrupo", idGrupo,
				"esDueno", esDueno);
		
		ArrayList<com.eleinco.ezvoxptt.entities.EquipoGrupo> grupos = 
				new ArrayList<com.eleinco.ezvoxptt.entities.EquipoGrupo>();
		//se obtiene cada posicion de los equipos
		for (int i = 0; i < objeto.getPropertyCount(); i++)
		{
			SoapObject gr = (SoapObject)objeto.getProperty(i);
			com.eleinco.ezvoxptt.entities.EquipoGrupo objGrupo = 
					new com.eleinco.ezvoxptt.entities.EquipoGrupo(
							gr.getProperty("IDGrupo").toString(),
							gr.getProperty("IDEquipo").toString(),
							gr.getProperty("Nombre").toString(),
							Boolean.parseBoolean(gr.getProperty("EsAdministrador").toString()),
							Integer.parseInt(gr.getProperty("Estado").toString()),
							Boolean.parseBoolean(gr.getProperty("EsDueno").toString()));
			grupos.add(objGrupo);
		}
		return grupos;
	}
	//guarda un equipo en el servidor
	public static String Agregar(com.eleinco.ezvoxptt.entities.EquipoGrupo objEquipo, String idDueno) throws IOException, Exception
	{
		boolean estado = false;
		if(objEquipo.Estado == 1)
			estado = true;
		String resultado = 
				WebService.InvokePrimitiveMethod6Parametro("CrearEquipo",
						"idEquipo", objEquipo.IDEquipo,
						"nombreEquipo", objEquipo.Nombre, 
						"idGrupo", objEquipo.IDGrupo,
						"esAdministrador", objEquipo.EsAdministrador,
						"estado", estado,
						"idDueno", idDueno);
		return resultado;
	}
	//elimina un equipo existente
	public static boolean Eliminar(String equipoId) throws IOException, XmlPullParserException {
		String obj = WebService.InvokePrimitiveMethod1Parametro("EliminarEquipo", 
				"equipoId", equipoId);
		return Boolean.parseBoolean(obj);
	}
}
