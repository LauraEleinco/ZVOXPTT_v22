package com.eleinco.ezvoxptt.entities;
//entidad grupo para almacenar la informacion del mismo
public class Grupo {
	public String ID_Grupo = null;
	public String Nombre = null;
	public String ClaveIngreso = null;
	public int Equipos = 0;
	public String ID_Grupo_Padre = null;
	public String ID_Grupo_Dueno = null;

	public Grupo(String idGrupo, String nombre, String claveIngreso, int equipos, String id_Grupo_Padre, String id_grupo_dueno)
	{
		this.ID_Grupo = idGrupo;
		this.Nombre = nombre;
		this.ClaveIngreso = claveIngreso;
		this.Equipos = equipos;
		if(id_Grupo_Padre != null && id_Grupo_Padre.equals("anyType{}"))
			id_Grupo_Padre = null;
		if(id_grupo_dueno != null && id_grupo_dueno.equals("anyType{}"))
			id_grupo_dueno = null;
		this.ID_Grupo_Padre = id_Grupo_Padre;
		this.ID_Grupo_Dueno = id_grupo_dueno;
	}
}
