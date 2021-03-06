package uniandes.isis2304.alohandes.negocio;

import java.sql.Timestamp;
import java.util.List;

import oracle.sql.DATE;

public interface VOOferta {
	public long getId() ;

	/**
	 * @param id the id to set
	 */
	public void setId(long id) ;

	/**
	 * @return the precio
	 */
	public long getPrecio();

	/**
	 * @param precio the precio to set
	 */
	public void setPrecio(long precio);

	/**
	 * @return the periodo
	 */
	public String getPeriodo() ;

	/**
	 * @param periodo the periodo to set
	 */
	public void setPeriodo(String periodo);

	/**
	 * @return the reservas
	 */
	public List<Object[]> getReservas() ;

	/**
	 * @param reservas the reservas to set
	 */
	public void setReservas(List<Object[]> reservas);

	/**
	 * @return the servicios
	 */
	public List<Object[]> getServicios();

	/**
	 * @param servicios the servicios to set
	 */
	public void setServicios(List<Object[]> servicios);
	
	/**
	 * @return idVivienda
	 */
	public long getVivienda();
	
	/**
	 * @param idVivienda - id de la vivienda de la oferta
	 */
	public void setVivienda(long vivienda);

	
	public DATE getFechainicio();

	public void setFechainicio(DATE fechainicio);

	public DATE getFechafin();

	public void setFechafin(DATE fechafin);
	
}
