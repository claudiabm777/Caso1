
public class Mensaje {

	private int numMensaje;
	private boolean procesado;
	public boolean getProcesado(){
		return procesado;
	}
	public Mensaje(int numMensaje){
		 this.numMensaje=numMensaje;
	}

	public int getNumMensaje() {
		return numMensaje;
	}

	public void setNumMensaje(int numMensaje) {
		this.numMensaje = numMensaje;
		procesado=true;
	}
	
	
}
