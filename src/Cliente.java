
public class Cliente extends Thread{
	private Mensaje[] mensajes;
	private int numeroMensajes;
	private Buffer buffer;
	private boolean atendido;

	public Cliente(int numeroMensajes,Buffer buffer){
		this.numeroMensajes=numeroMensajes;
		this.buffer=buffer;
		mensajes=new Mensaje[this.numeroMensajes];
		for(int i=0;i<mensajes.length;i++){
			mensajes[i]=new Mensaje(i);
		}
		atendido=false;
	}

	public  boolean revisarEstadoMensajes(){
		int contador=0;
		for(int i=0;i<mensajes.length;i++){
			if(mensajes[i].getProcesado()){
				contador++;
			}
		}
		if(contador==mensajes.length){
			atendido=true;
		}
		return atendido;
	}
	public void run(){
		Mensaje guardado=null;
		for(int i=0;i<mensajes.length;i++){
			synchronized(mensajes[i]){try {
			buffer.guardarMensaje(mensajes[i]);
			
			mensajes[i].wait();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}}
		}
	}

}
