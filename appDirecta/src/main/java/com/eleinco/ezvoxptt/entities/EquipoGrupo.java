package com.eleinco.ezvoxptt.entities;
//entidad equipo grupo para almacenar la informacion en comun de ambos
public class EquipoGrupo {
	public String IDGrupo = null;
    public String IDEquipo = null;
    public String Nombre = null;
    public boolean EsAdministrador = false;
    public int Estado = 0;
    public boolean EsDueno = false;

    public EquipoGrupo(String idGrupo, String idEquipo,
        String nombre, boolean esAdministrador, int estado, boolean esDueno)
    {
        this.IDGrupo = idGrupo;
        this.IDEquipo = idEquipo;
        this.Nombre = nombre;
        this.EsAdministrador = esAdministrador;
        this.Estado = estado;
        this.EsDueno = esDueno;
    }
}
