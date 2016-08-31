package com.eleinco.ezvoxptt.core.business.entities;

import com.eleinco.ezvoxptt.entities.EquipoGrupo;
import com.eleinco.ezvoxptt.entities.Grupo;
//clase que permite encapsular un elemento (grupo o equipo)
public class ElementoTO {
	
	public String Nombre = null;
	public String ID = null;
	
	public Grupo grupo = null;
	public EquipoGrupo equipo = null;
	
	public ElementoTO(Grupo g)
	{
		this.grupo = g;
		this.ID = g.ID_Grupo;
		this.Nombre = g.Nombre;
	}
	public ElementoTO(EquipoGrupo e)
	{
		this.equipo = e;
		this.ID = e.IDEquipo;
		this.Nombre = e.Nombre;
	}
}
