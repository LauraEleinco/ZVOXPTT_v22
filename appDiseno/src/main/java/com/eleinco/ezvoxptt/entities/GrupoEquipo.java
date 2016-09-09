package com.eleinco.ezvoxptt.entities;
//entidad grupo equipo para almacenar la informacion en comun de los mismos
public class GrupoEquipo {
	 public String IDEquipo = null;
     public String ID_Grupo = null;
     public String NombreGrupo = null;
     public String ClaveIngreso = null;
     public int CantidadEquipos = 0;
     public String ID_Grupo_Padre = null;
     public String ID_Grupo_Dueno = null;     
     public boolean Admin = false;

     public GrupoEquipo() { }
     public GrupoEquipo(String idEquipo, String idGrupo, 
    		 String nombreGrupo, String claveIngreso, 
    		 int cantidadEquipos, String idGrupoPadre, 
    		 boolean admin, String idGrupoDueno)
     {
         this.IDEquipo = idEquipo;
         this.ID_Grupo = idGrupo;
         this.NombreGrupo = nombreGrupo;
         this.ClaveIngreso = claveIngreso;
         this.CantidadEquipos = cantidadEquipos;
         if(idGrupoPadre.equals("anyType{}"))
        	 idGrupoPadre = null;
         this.ID_Grupo_Padre = idGrupoPadre;
         if(idGrupoDueno.equals("anyType{}"))
        	 idGrupoDueno = null;
         this.ID_Grupo_Dueno = idGrupoDueno;
         this.Admin = admin;
     }
}
