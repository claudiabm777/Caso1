import java.util.ArrayList;
import java.util.List;


public class Buffer {
	
	private int numClientes;
	private int numServidores;
	private int tamanioBuffer;
	private List<Mensaje> mensajes;
	//private boolean esperaServidor;
	
	public Buffer(int numClientes,int numServidores,int tamanioBuffer){
		this.numClientes=numClientes;
		this.numServidores=numServidores;
		this.tamanioBuffer=tamanioBuffer;
		mensajes=new ArrayList<Mensaje>(this.tamanioBuffer);
		//esperaServidor=false;
	}
	
	public synchronized Mensaje guardarMensaje(Mensaje mensaje){
		while(mensajes.size()==tamanioBuffer){
			try {
				System.out.println("En espera para guardar: "+mensaje.getNumMensaje());
				wait();
			} 
			catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		mensajes.add(mensaje);
		
		System.out.println("Mensaje guardado: "+mensaje.getNumMensaje());
		
		//esperaServidor=true;
		return mensaje;
		
	}
	
	public Integer obtenerMensaje(){
		Integer respuesta=null;
		if(mensajes.size()==0){
			respuesta=null;
		}
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
	
	public static void main(String[] args) {
		Buffer buffer=new Buffer(3,15,1);
		Cliente[] clientes=new Cliente[buffer.numClientes];
		Servidor[] servidores=new Servidor[buffer.numServidores];
		int contadorClientesAtendidos=0;
		for(int i=0;i<buffer.numClientes;i++){
			Cliente cliente=new Cliente(5,buffer);
			clientes[i]=cliente;
			clientes[i].start();
		}
		
		for(int i=0;i<buffer.numServidores;i++){
			Servidor servidor=new Servidor(buffer);
			servidores[i]=servidor;
			servidores[i].start();
		}
		
//		boolean finalizado=false;
//		while(!finalizado){
//			
//			int contadorClientes=0;
//			for(int i=0;i<buffer.numClientes;i++){
//				if(clientes[i].revisarEstadoMensajes()){
//					contadorClientes++;
//				}
//			}
//			if(contadorClientes==clientes.length){
//				finalizado=true;
//			}
//			
//		}
	}
	
}
