package uniandes.isis2304.alohandes.negocio;

public class ViviendaUniversitaria extends Operador  implements VOViviendaUniversitaria{
	
	/* ****************************************************************
	 * 			Métodos 
	 *****************************************************************/
	
	public ViviendaUniversitaria() {
		super();
	}
	
	/**
	 * Constructor con valores
	 * @param id - el id del usuario
	 * @param nombre - el nombre del usuario
	 * @param email - la dirección de correo del usuario
	 * @param numero - el numero telefonico del usuario
	 */
	public ViviendaUniversitaria(long id, String nombre, String email, String numero) {
		super(id,nombre,email,numero, "VIVIENDA_UNIVERSITARIA");
	}
}
