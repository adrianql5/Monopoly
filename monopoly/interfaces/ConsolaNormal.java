package monopoly.interfaces;
import java.util.Scanner;

public class ConsolaNormal implements Consola{

	//Atributos:
    private Scanner scanner;

 	//Constructor.
    public ConsolaNormal(){
        this.scanner = new Scanner(System.in);
	}


	@Override
	public void imprimir(String mensaje){
		System.out.println(mensaje);
	}

	@Override
	public String leer(){
    	String leido = scanner.nextLine();
		return leido;
	}

	public void cerrarScanner() {
        if (scanner != null) {
            scanner.close();
        }
    }

}
