package uniandes.isis2304.alohandes.persistencia;

import java.util.Calendar;
import javax.jdo.PersistenceManager;
import javax.jdo.Query;

public class REQC1 {
	/* ****************************************************************
	 * 			Constantes
	 *****************************************************************/
	/**
	 * Cadena que representa el tipo de consulta que se va a realizar en las sentencias de acceso a la base de datos
	 * Se renombra acá para facilitar la escritura de las sentencias
	 */
	private final static String SQL = PersistenciaAlohandes.SQL;

	/* ****************************************************************
	 * 			Atributos
	 *****************************************************************/
	/**
	 * El manejador de persistencia general de la aplicación
	 */
	protected PersistenciaAlohandes pa;

	/* ****************************************************************
	 * 			Métodos
	 *****************************************************************/
	
	/** 
	* Constructor
	* @param pa - El Manejador de persistencia de la aplicación
	*/
	public REQC1 (PersistenciaAlohandes pa)
	{
	   this.pa = pa;
	}

	/**
	 * regresa las ganancias el anio actual
	 * @param pm
	 * @param idOperador
	 * @return
	 */
	public long gananciasAnioActual(PersistenceManager pm,long idOperador) {
		int year = Calendar.getInstance().get(Calendar.YEAR);
		//System.out.println("cual es el gran sapo id " + idOperador + " cual es el gran sapo anio "+ year);
		Query q = pm.newQuery(SQL, "SELECT  SUM(cantidad) FROM " + pa.darTablaGanancias() + " WHERE operador = ? AND to_number(to_char(fecha, 'YYYY')) = ?");
		q.setParameters(idOperador,year);
		q.setResultClass(Long.class);
		return (long)q.executeUnique();
	}
	
	/**
	 * regresa las ganacias del anio corrido
	 * @param pm
	 * @param idOperador
	 * @return
	 */
	public long gananciasAnioCorrido(PersistenceManager pm,long idOperador) {
		int year = Calendar.getInstance().get(Calendar.YEAR);
		int month = Calendar.getInstance().get(Calendar.MONTH);
		if(month == 12) 
			return gananciasAnioActual(pm, idOperador);
		int ar[] = new int[12];
		ar[0] = month;
		for(int i = 1; i < 12;++i) {
			ar[i] = ar[i-1]-1;
			if(ar[i] == 0) ar[i]+= 12;
		}
		Query q = pm.newQuery(SQL, "SELECT SUM(cantidad) FROM " + pa.darTablaGanancias() + " WHERE operador = ? AND ((to_number(to_char(fecha, 'YYYY')) = ? AND to_number(to_char(fecha, 'MM')) >= ?) OR (to_number(to_char(fecha, 'YYYY')) = ? AND to_number(to_char(fecha, 'MM')) <= ?))");
		q.setParameters(idOperador, year-1,ar[11],year,ar[0]);
		q.setResultClass(Long.class);
		return (long)q.executeUnique();
	}
}
