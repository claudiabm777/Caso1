
public class Mensaje {

	//---------------------------------------------------------------------------------------------
	//--------------------------------------------------------Atributos---------------------------
	//---------------------------------------------------------------------------------------------

	/**
	 * El numero del  mensajes (contenido del mensaje)
	 */
	private int numMensaje;

	/**
	 * Estado del mensaje 
	 * (true en caso de que ya se haya procesado, false en caso contrario)
	 */
	private boolean procesado;

	//------------------------------------------------------------
	//----------------------Constructor---------------------------
	//------------------------------------------------------------

	/**
	 * Constructor del Mensaje <br>
	 * @param numMensaje numero del mensaje
	 */
	public Mensaje(int numMensaje){
		this.numMensaje=numMensaje;
	}

	//------------------------------------------------------------
	//----------------------Metodos---------------------------
	//------------------------------------------------------------

	/**
	 * Metodo que retorna estado del mensaje <br>
	 * @return procesado: el estado del mensaje
	 */
	public boolean getProcesado(){
		return procesado;
	}
	
	/**
	 * Metodo que retorna el numero del mensaje <br>
	 * @return numMensaje: el numero del mensaje
	 */
	public int getNumMensaje() {
		return numMensaje;
	}

	/**
	 * Cambia  el numero del mensaje al numero que llega por parametro <br> 
	 * <b>pre: </b> El mensaje ya se ha creado<br>
	 * <b>post: </b> El numero del mensaje ha sido cambiado al numero que llega por parametro<br>
	 * @param numMensaje
	 */
	public void setNumMensaje(int numMensaje) {
		this.numMensaje = numMensaje;
		procesado=true;
	}


}
