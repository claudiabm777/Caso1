import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase Buffer del Caso 1
 * @author Asus
 *
 */
public class Buffer {

	//---------------------------------------------------------------------------------------------
	//---------------------------------------------------------------------------------------------
	//Atributos------------------------------------------------------------------------------------
	//---------------------------------------------------------------------------------------------
	//---------------------------------------------------------------------------------------------

	/**
	 * Número de clientes.
	 */
	private int numClientes;

	/**
	 * Número de servidores.
	 */
	private int numServidores;

	/**
	 * Tamaño del buffer. Número de mensajes que puede almacenar
	 */
	private int tamanioBuffer;

	/**
	 * Esta es la lista que va a almacenar los mensajes que los clientes depositan en el buffer.
	 * El tamaño de esta lista es la capacidad del buffer (tamanioBuffer). Esta lista es el buffer.
	 */
	private List<Mensaje> mensajes;

	/**
	 * Indica si ya se terminaron de procesar todos los clientes 
	 * return  (TRUE si ya se termino, FALSE de lo contrario)
	 */
	private boolean finalizado;

	/**
	 * Arreglo de clientes que van a participar en el sistema
	 */
	private Cliente[] clientes;

	/**
	 * Arreglo de servidores que van a participar en el sistema
	 */
	private Servidor[] servidores;

	//---------------------------------------------------------------------------------------------
	//---------------------------------------------------------------------------------------------
	//Constructor----------------------------------------------------------------------------------
	//---------------------------------------------------------------------------------------------
	//---------------------------------------------------------------------------------------------
	/**
	 * Constructor de la clase <br>
	 * @param numClientes: número de clientes del sistema
	 * @param numServidores: numero de servidores del sistema
	 * @param tamanioBuffer: capacidad de almacenaje de mensajes que va a tener el buffer
	 */
	public Buffer(int numClientes,int numServidores,int tamanioBuffer){
		this.numClientes=numClientes;
		this.numServidores=numServidores;
		this.tamanioBuffer=tamanioBuffer;
		this.mensajes=new ArrayList<Mensaje>(this.tamanioBuffer);
		this.finalizado=false;
		this.clientes=new Cliente[this.numClientes];
		this.servidores=new Servidor[this.numServidores];

	}


	//---------------------------------------------------------------------------------------------
	//---------------------------------------------------------------------------------------------
	//Métodos--------------------------------------------------------------------------------------
	//---------------------------------------------------------------------------------------------
	//---------------------------------------------------------------------------------------------

	/**
	 * Método que recibe un mensaje del cliente y lo guarda en el buffer <br>
	 * @param mensaje: mensaje a guardar en el buffer, si no lo logra, queda esperando sobre el buffer
	 * @return El mensaje guardado
	 */
	public synchronized Mensaje guardarMensaje(Mensaje mensaje){
		//verifica que el buffer tenga cupo, si no es asi se duerme
		while(mensajes.size()==tamanioBuffer){
			try {
				System.out.println("En espera para guardar: "+mensaje.getNumMensaje());
				wait();
			} 
			catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		// en caso de que hay cupo el mensaje es agregado a la lista de mensajes 
		mensajes.add(mensaje);

		System.out.println("Mensaje guardado: "+mensaje.getNumMensaje());		
		return mensaje;
	}
	/**
	 * Método que saca el mensaje del buffer e incrementa este mensaje en 1 <br>
	 * @return un entero con el nuevo valor del mensaje
	 */
	public  Integer obtenerMensaje(){
		Integer respuesta=null;
		//Si no hay mensajes se responde null
		if(mensajes.size()==0){
			respuesta=null;
		}
		//en caso contrario se remueve el mensaje del buffer y 
		//se incrementa en uno. Se notifica a todos y se notifica al mensaje
		else{
			Mensaje mensajeProcesado=mensajes.remove(0);
			System.out.println("Antes: "+(mensajeProcesado.getNumMensaje()));
			mensajeProcesado.setNumMensaje(mensajeProcesado.getNumMensaje()+1);
			System.out.println("Despues: "+(mensajeProcesado.getNumMensaje()));
			notifyAll();
			synchronized(mensajeProcesado){mensajeProcesado.notify();}

			respuesta= mensajeProcesado.getNumMensaje();
		}
		return respuesta;
	}

	/**
	 * Método que determina si se terminaron de procesar los clientes <br>
	 * @return un booleano indicando si ya se atendieron a todos los clientes (TRUE) o no (FALSE)
	 */
	public  boolean seTerminoProcesarClientes(){
		//se revisa si los clientes ya fueron atendidos. si es asi  se ha  finalizado (finalizado = true)
		synchronized(clientes){
			int contadorClientes=0;
			for(int i=0;i<numClientes;i++){
				if(clientes[i].revisarEstadoMensajes()){
					contadorClientes++;
				}
			}
			if(contadorClientes==clientes.length){
				finalizado=true;
			}
			return finalizado;
		}
	}

	//---------------------------------------------------------------------------------------------
	//---------------------------------------------------------------------------------------------
	//-------------------------------------------------Main-------------------------------------
	//---------------------------------------------------------------------------------------------
	//---------------------------------------------------------------------------------------------

	/**
	 * MAIN del programa
	 * @param args
	 */
	public static void main(String[] args) {
		// se crean atributos necesarios para la lectura del archivo.

		String infoClientes=null;
		String cantidadServidores=null;
		String tamanioBuffer=null;
		String[]msjs=null;
		FileReader fw;
		//se crea un flujo de lectura
		try {
			fw = new FileReader("data/datos.txt");

			BufferedReader bw = new BufferedReader(fw);
			infoClientes=bw.readLine();
			cantidadServidores=bw.readLine();
			tamanioBuffer=bw.readLine();
			msjs=infoClientes.split(":")[1].split(",");


			bw.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		//se crea el buffer con los datos obtenidos del buffer
		Buffer buffer=new Buffer(msjs.length,Integer.parseInt(cantidadServidores.split(":")[1]),Integer.parseInt(tamanioBuffer.split(":")[1]));
		//System.out.println(msjs.length+" "+Integer.parseInt(cantidadServidores.split(":")[1])+" "+Integer.parseInt(tamanioBuffer.split(":")[1]));
		//se crea el numero de clientes y de seridores que llega por parametro y se arranca cada uno de ellos
		for(int i=0;i<msjs.length;i++){
			Cliente cliente=new Cliente(Integer.parseInt(msjs[i]),buffer);
			buffer.clientes[i]=cliente;
			buffer.clientes[i].start();
		}

		for(int i=0;i<buffer.numServidores;i++){
			Servidor servidor=new Servidor(buffer);
			buffer.servidores[i]=servidor;
			buffer.servidores[i].start();
		}

	}

}
