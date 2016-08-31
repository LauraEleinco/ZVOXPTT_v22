package com.eleinco.ezvoxptt.core.events;
//interface para el evento del estado de un contacto
public interface EstadoContactoEventListener {
	public void EstadoCambiado(String equipoID, boolean conectado);
}
