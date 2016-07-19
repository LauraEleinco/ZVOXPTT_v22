package com.eleinco.ezvoxptt.core.events;

import com.eleinco.ezvoxptt.common.Enums.EstadoTiempoReal;
//interfaz para los evenntos del servidor de tiempo real
public interface TiempoRealEventListener {
	public void EstadoCambiado(EstadoTiempoReal estado);
	public void GrupoCambiado(String grupoID, int puerto);
	public void EquipoHablando(String grupoId, String equipoId, boolean hablando);
}
