
public class Cliente extends Thread{

	//---------------------------------------------------------------------------------------------
	//--------------------------------------------------------Atributos---------------------------
	//---------------------------------------------------------------------------------------------

	/**
	 * Arreglo de mensajes que va a enviar el cliente
	 */
	private Mensaje[] mensajes;

	/**
	 * Numero de mensajes del cliente
	 */
	private int numeroMensajes;

	/**
	 * Buffer por el cual se comunican
	 */
	private Buffer buffer;

	/**
	 * Estado del cliente, atendido en caso de que todos sus mensajes hayan sido respondidos
	 */
	private boolean atendido;

	//------------------------------------------------------------
	//----------------------Constructor---------------------------
	//------------------------------------------------------------

	/**
	 * Constructor de la clase <br>
	 * @param numeroMensajes numero de mensajes del cliente
	 * @param buffer  buffer por el cual se comunicara con el servidor
	 */
	public Cliente(int numeroMensajes,Buffer buffer)
	{
		this.numeroMensajes=numeroMensajes;
		this.buffer=buffer;
		mensajes=new Mensaje[this.numeroMensajes];
		//se llenan los mensajes con numeros secuenciales  del 0 a  la cantidad de mensajes
		for(int i=0;i<mensajes.length;i++){
			mensajes[i]=new Mensaje(i);
		}
		atendido=false;
	}

	//------------------------------------------------------------
	//----------------------Metodos---------------------------
	//------------------------------------------------------------

	/**
	 * Metodo con el cual se verifica si el cliente ya fue atendido <br>
	 * @return true en caso de que todos sus mensajes hayan sido atentidos
	 * 			false en caso contrario
	 **/
	public  boolean revisarEstadoMensajes(){
		int contador=0;
		//se recorren todos los mensajes de los clientes preguntando si estos ya han sido atendidos
		for(int i=0;i<mensajes.length;i++){
			if(mensajes[i].getProcesado()){
				contador++;
			}
		}
		// se compara la cantidad de mensajes atendidos con el total se mensajes
		if(contador==mensajes.length){
			atendido=true;
		}
		return atendido;
	}

	/**
	 * Metodo run  del thread <br>
	 * <b>pre: </b> El cliente ya se ha creado<br>
	 * <b>post: </b> EL cliente guarda cada uno de sus mensajes en el buffer cumpliendo el protocolo que ese establece para la sincronizacion. <br>
	 */
	public void run()
	{
		//Se recorre la lista de los menasajes 
		for(int i=0;i<mensajes.length;i++)
		{
			// si el mensaje se puede guardar, se guarda y se duerme el cliente obre el mensaje
			// de lo contrario se lanza una excepcion
			synchronized(mensajes[i]){
				try 
				{
					buffer.guardarMensaje(mensajes[i]);
					mensajes[i].wait();
				} 
				catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}

}
