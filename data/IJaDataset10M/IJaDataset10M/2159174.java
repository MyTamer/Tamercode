package src.estadisticas;

public class EstadisticaAtaque {
int turno;
int daņoProferido =0;
int daņoRecibido=0;


/**
 * en el create, si el daņo es negativo significa que fue recibido, sino, realizado
 * @param turno
 */
public EstadisticaAtaque(int iTurno,int iDaņo){
	this.turno = iTurno;
	if(iDaņo < 0)
		daņoRecibido = -iDaņo;
	else
		daņoProferido= iDaņo;
	
}


/**
 * @return the daņoProferido
 */
public int getDaņoProferido() {
	return daņoProferido;
}


/**
 * @param daņoProferido the daņoProferido to set
 */
public void setDaņoProferido(int daņoProferido) {
	this.daņoProferido = daņoProferido;
}


/**
 * @return the daņoRecibido
 */
public int getDaņoRecibido() {
	return daņoRecibido;
}


/**
 * @param daņoRecibido the daņoRecibido to set
 */
public void setDaņoRecibido(int daņoRecibido) {
	this.daņoRecibido = daņoRecibido;
}


/**
 * @return the turno
 */
public int getTurno() {
	return turno;
}


/**
 * @param turno the turno to set
 */
public void setTurno(int turno) {
	this.turno = turno;
}
}
