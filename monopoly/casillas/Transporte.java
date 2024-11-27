package monopoly.casillas;

public class Transporte extends Propiedad{
    public Transporte(String nombre,int posicion, float valor, Jugador duenho) {
        super(nombre,posicion,duenho);
        this.valor = valor;
        this.impuesto = valor * 0.10f;
        this.hipoteca = valor/2f;
    

        if(this.tipo.equals("Solar")){ //solo se edifican los solares
            this.edificios = new ArrayList<>(4); 
            for (int i = 0; i < 4; i++) {
                this.edificios.add(new ArrayList<Edificio>()); // Array de casas, hoteles, piscinas, pistas
            }
        } 
        this.estaHipotecada = false;
        
        this.dinero_recaudado = 0;
        this.veces_visitada_por_duenho = 0;
    }
}
