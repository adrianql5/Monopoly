package monopoly.interfaces;
import java.util.Scanner;

public class ConsolaNormal implements Consola{

    private Scanner scanner;

    public ConsolaNormal(){
        this.scanner = new Scanner(System.in);
	}

	@Override
	public void imprimir(String mensaje){
		System.out.println(mensaje);
	}

	@Override
	public String leer(	String mensaje){
		System.out.println(mensaje);
		return scanner.nextLine();
	}
}
