package monopoly;

import static java.lang.Math.pow;

import java.util.ArrayList;
import java.util.HashMap;

import partida.Jugador;

//cometario para probar como van las ramas
public class Tablero {

    //ATRIBUTOS
    private ArrayList<ArrayList<Casilla>> posiciones; //Posiciones del tablero: se define como un arraylist de arraylists de casillas (uno por cada lado del tablero).
    private HashMap<String, Grupo> grupos; //Grupos del tablero, almacenados como un HashMap con clave String (será el color del grupo).
    private Jugador banca; //Un jugador que será la banca.


    //SECCION DE COSNTRUIR EL TABLERO Y HACER LOS GRUPOS

    //CONSTRUCTOR: únicamente le pasamos el jugador banca (que se creará desde el menú).
    public Tablero(Jugador banca) {
        this.banca=banca;
        this.grupos = new HashMap<String,Grupo>();
        generarCasillas();   
    }



    /**Método para crear todas las casillas del tablero. Formado a su vez por cuatro métodos (1/lado).*/
    private void generarCasillas() {
        this.posiciones = new ArrayList<ArrayList<Casilla>>();
        this.insertarLadoSur();
        this.insertarLadoOeste();
        this.insertarLadoNorte();
        this.insertarLadoEste();
    }

    //Método para insertar las casillas del lado sur.
    private void insertarLadoSur() {
        ArrayList<Casilla> ladoSur = new ArrayList<Casilla>();
        ladoSur.add(new Casilla("Salida","Especial",0,banca));
        ladoSur.add(new Casilla(Valor.WHITE+"Solar 1"+Valor.RESET,"Solar",1,1000,banca));
        ladoSur.add(new Casilla("Caja","Caja de comunidad",2,banca));
        ladoSur.add(new Casilla(Valor.WHITE+"Solar 2"+Valor.RESET,"Solar",3,1000,banca));
        ladoSur.add(new Casilla("Imp 1",4,500,banca));
        ladoSur.add(new Casilla("Trans 1","Transporte",5,2000,banca));
        ladoSur.add(new Casilla(Valor.CYAN+"Solar 3"+Valor.RESET,"Solar",6,1000,banca));
        ladoSur.add(new Casilla("Suerte","Suerte",7,banca));
        ladoSur.add(new Casilla(Valor.CYAN+"Solar 4"+Valor.RESET,"Solar",8,1000,banca));
        ladoSur.add(new Casilla(Valor.CYAN+"Solar 5"+Valor.RESET,"Solar",9,1000,banca));
 
        posiciones.add(ladoSur);
        
        grupos.put("WHITE",new Grupo(getCasilla(1), getCasilla(3),"WHITE"));
        grupos.put("CYAN",new Grupo(getCasilla(6),getCasilla(8),getCasilla(9),"CYAN"));
    }

    //Método que inserta casillas del lado oeste.
    private void insertarLadoOeste() {
        ArrayList<Casilla> ladoOeste = new ArrayList<Casilla>();
        ladoOeste.add(new Casilla("Carcel","Especial",10,banca));
        ladoOeste.add(new Casilla(Valor.BLUE+"Solar 6"+Valor.RESET,"Solar",11,1000,banca));

        ladoOeste.add(new Casilla("Serv 1","Servicios",12,2500,banca));
        ladoOeste.add(new Casilla(Valor.BLUE+"Solar 7"+Valor.RESET,"Solar",13,1000,banca));
        ladoOeste.add(new Casilla(Valor.BLUE+"Solar 8"+Valor.RESET,"Solar",14,1000,banca));
        ladoOeste.add(new Casilla("Trans 2","Transporte",15,2000,banca));
        ladoOeste.add(new Casilla(Valor.YELLOW+"Solar 9"+Valor.RESET,"Solar",16,1000,banca));
        ladoOeste.add(new Casilla("Caja","Caja de comunidad",17,banca));
        ladoOeste.add(new Casilla(Valor.YELLOW+"Solar 10"+Valor.RESET,"Solar",18,1000,banca));
        ladoOeste.add(new Casilla(Valor.YELLOW+"Solar 11"+Valor.RESET,"Solar",19,banca));
        
        posiciones.add(ladoOeste);
        
        grupos.put("BLUE",new Grupo(getCasilla(11), getCasilla(13),getCasilla(14),"BLUE"));
        grupos.put("YELLOW",new Grupo(getCasilla(16), getCasilla(18),getCasilla(19),"YELLOW"));
    }

    //Método para insertar las casillas del lado norte.
    private void insertarLadoNorte() {
        ArrayList<Casilla> ladoNorte = new ArrayList<Casilla>();
        ladoNorte.add(new Casilla("Parking","Especial",20,banca));
        ladoNorte.add(new Casilla(Valor.BLACK+"Solar 12"+Valor.RESET,"Solar",21,1000,banca));
        ladoNorte.add(new Casilla("Suerte","Suerte",22,banca));
        ladoNorte.add(new Casilla(Valor.BLACK+"Solar 13"+Valor.RESET,"Solar",23,1000,banca));
        ladoNorte.add(new Casilla(Valor.BLACK+"Solar 14"+Valor.RESET,"Solar",24,1000,banca));
        ladoNorte.add(new Casilla("Trans 3","Transporte",25,2000,banca));
        ladoNorte.add(new Casilla(Valor.GREEN+"Solar 15"+Valor.RESET,"Solar",26,1000,banca));
        ladoNorte.add(new Casilla(Valor.GREEN+"Solar 16"+Valor.RESET,"Solar",27,1000,banca));
        ladoNorte.add(new Casilla("Serv 2","Servicios",28,2500,banca));
        ladoNorte.add(new Casilla(Valor.GREEN+"Solar 17"+Valor.RESET,"Solar",29,1000,banca));
        
        posiciones.add(ladoNorte);

        grupos.put("BLACK",new Grupo(getCasilla(21), getCasilla(23),getCasilla(24),"BLACK"));
        grupos.put("GREEN",new Grupo(getCasilla(26), getCasilla(27),getCasilla(29),"GREEN"));
    }


    //Método que inserta las casillas del lado este.
    private void insertarLadoEste() {
        ArrayList<Casilla> ladoEste = new ArrayList<Casilla>();
        ladoEste.add(new Casilla("IrCarcel","Especial",30,banca));
        ladoEste.add(new Casilla(Valor.RED+"Solar 18"+Valor.RESET,"Solar",31,1000,banca));
        ladoEste.add(new Casilla(Valor.RED+"Serv 19"+Valor.RESET,"Servicios",32,2500,banca));
        ladoEste.add(new Casilla("Caja","Caja de comunidad",33,banca));
        ladoEste.add(new Casilla(Valor.RED+"Solar 20"+Valor.RESET,"Solar",34,1000,banca));
        ladoEste.add(new Casilla("Trans 4","Transporte",35,2000,banca));
        ladoEste.add(new Casilla("Suerte","Suerte",36,banca));
        ladoEste.add(new Casilla(Valor.PURPLE+"Solar 21"+Valor.RESET,"Solar",37,1000,banca));
        ladoEste.add(new Casilla("Imp 2",38,500,banca));
        ladoEste.add(new Casilla(Valor.PURPLE+"Solar 22"+Valor.RESET,"Solar",39,1000,banca));
        
        
        posiciones.add(ladoEste);
        
        grupos.put("RED",new Grupo(getCasilla(31), getCasilla(32),getCasilla(34),"RED"));
        grupos.put("PURPLE",new Grupo(getCasilla(37), getCasilla(39),"PURPLE"));
    }


    //SECCION DE IMPRIMIR EL TABLERO Y LOS AVATARES

    /**Método auxiliar para calcular el precio de un Solar.
     * @param nsolares_grupo Número de solares que tiene el grupo (2 o 3)
     * @param ngrupo Índice del grupo (se usa para la multiplicidad del incremento)
     */
    public float valorSolar(int nsolares_grupo, int ngrupo){
        float valor_solar = (float) (Valor.SOLAR_BASE/nsolares_grupo * pow(Valor.INCREMENTO, ngrupo));
        return valor_solar;
    }


    /**Método para crear cadenas de espacios
     * @param n Número de espacios en blanco que se quieren
     */
    public String conEspacios(int n) {
        String cadena = ""; //Creamos una cadena vacía
        if(n>=0) {
            for(int m=0; m<n; m++) {
                cadena += " ";
            }
        }
        else {
            System.out.println("\nError en la función conEspacios: tiene que introducirse un número positivo de espacios.\n");
            //Nótese que igualmente devuelve una cadena vacía si se introduce un número negativo
        }
        return(cadena);
    }

    /**Método que devuelve las fichas de una casilla.
     * Una ficha en una casilla se identifica con "&@" donde @ es su caracter identificador (mayúscula entre A y Z).
     * Si hubiese varios jugadores en la misma casilla devuelve "&@@@..." donde cada @ es un identificador (máximo 6).
     * @param nombre_casilla Nombre de la casilla
     */
    public String fichas(String nombre_casilla) {//PASAMOS EL NOMBRE DE LA CASILLA
        //Variables que vamos a necesitar
        Casilla casilla = encontrar_casilla(nombre_casilla);    //Encontramos la casilla a partir del nombre
        int nj=casilla.getAvatares().size();    //Vemos cuántos jugadores hay en la casilla con .size()
        String fichas = "";

        if(nj==0) {
            fichas += conEspacios(Valor.NCHARS_CASILLA);   //No hay ninguna ficha en la casilla
        }
        else {
            fichas += "&";
            int i;
            for(i=0;i<nj;i++) {
                fichas += casilla.getAvatares().get(i);   //Vamos imprimiendo cada identificador después del &
            }
            fichas += conEspacios(Valor.NCHARS_CASILLA-i-1);   //Rellenamos el resto con espacios
        }

        return fichas;
    }

    /**Método que devuelve la mitad de arriba de las casillas (nombre con el formato adecuado para un nombre de casilla).
     * Formato: Nombre casilla + Espacios + Barra.
     * El número de espacios varía para que la cadena entera mida 8 caracteres (=Valor.NCHARS_CASILLA).
     * Si Nombre casilla ya mide 8 caracteres no se añaden espacios.
     * @param nombre_casilla Nombre de la casilla
     */
    public String formatoNombre(String nombre_casilla) {
        String nombreConFormato = new String();

        //Cuidado: los strings de casillas coloreadas miden más que el nombre por las cadenas para colorear
        int i=nombre_casilla.length();
        if(i<9) /*Casilla con nombre sin color*/ {
            nombreConFormato = nombre_casilla + conEspacios(Valor.NCHARS_CASILLA-nombre_casilla.length()) + Valor.BARRA;
        }
        else /*Casilla con nombre coloreado: 9 = longitud de "\u001B[30m" + "\u001B[0m" */ {
            nombreConFormato = nombre_casilla + conEspacios(Valor.NCHARS_CASILLA-nombre_casilla.length()+9) + Valor.BARRA;
        }

        return nombreConFormato;
    }

    /**Método que devuelve la mitad de abajo de las casillas (con fichas si las hubiese)
     * Formato: Nombre casilla + Espacios + Barra.
     * El número de espacios varía para que la cadena entera mida 9 caracteres (=Valor.NCHARS_CASILLA).
     * Si Nombre casilla + Barra ya mide 9 caracteres no se añaden espacios.
     * @param nombre_casilla Nombre de la casilla
     */
    public String formatoFichas(String nombre_casilla) {
        String nombreConFormato = new String();
        nombreConFormato = Valor.SUBRAYADO + fichas(nombre_casilla) + Valor.RESET + Valor.BARRA;
        return nombreConFormato;
    }

    /**Modificación del metodo toString() para imprimir el tablero.
     * Versión 3: como hay que imprimir todas las fichas que hay en una casilla
     * vamos a hacer que cada casilla tenga dos líneas: una para el nombre y otra
     * para imprimir las fichas si las hubiese (ahora en vez de una fila de líneas
     * para delimitar casillas va la segunda línea de la casilla subrayada
     */
    @Override
    public String toString() {
        String tabla = new String();

        //Variables auxiliares
        int i;
        //Buenas noticias para Adrián: quité el switch (T.T)
        //Ahora que las casillas ocupan 2 líneas no hay tantos casos iguales como para hacer un switch
        //Sólo las líneas del medio se construyen de manera parecida (uso bucle FOR para ellas)

        //Imprimimos la PRIMERA LÍNEA: el borde superior
        tabla += " ";
        for(i=0;i<Valor.NCASILLAS_POR_FILA;i++) {
            tabla += Valor.CASILLA_VACIA + " ";
        }
        tabla += "\n";

        //Imprimimos la SEGUNDA LÍNEA: parte superior de la primera fila de casillas
        tabla += Valor.BARRA;   //Borde izquierdo (no equivale a una casilla)
        //Lado norte entero
        for (i=20; i<30; i++) {
            tabla += formatoNombre(getCasilla(i).getNombre());
        }
        tabla += formatoNombre(getCasilla(30).getNombre()) + "\n";   //Primera casilla del lado este

        //Imprimimos la TERCERA LÍNEA: parte inferior de la primera fila de casillas
        tabla += Valor.BARRA;
        for (i=20; i<30; i++) {
            tabla += formatoFichas(getCasilla(i).getNombre());   //Lado norte entero
        }
        tabla += formatoFichas(getCasilla(30).getNombre()) + "\n";   //Primera casilla del lado este

        //De la LÍNEA Nº4 hasta la LÍNEA Nº20 se van imprimiendo con el siguiente FOR
        //Sobre el índice correspondiente de la casilla que toca imprimir en cada lado:
        //En la izquierda (ladoOeste) para la línea 3 sería el índice 9, la 5 el 8, etc.
        //En la derecha (ladoEste) para la línea 3 sería el índice 1, la 5 el 2, etc.
        //Se podría cambiar el (i-1)/2 por (int)(i/2)
        for(i=3;i<20;i++) {
            if (i%2!=0) /**LÍNEAS IMPARES: parte superior de la fila de casillas correspondientes*/ {

                tabla += Valor.BARRA;
                tabla += formatoNombre(posiciones.get(1).get(10-(i-1)/2).getNombre());  //Casilla de la izquierda

                //Añadimos los espacios del medio (se resta 1 porque hay un borde antes de la siguiente casilla)
                int nesp = (Valor.NCASILLAS_POR_FILA-2)*(Valor.NCHARS_CASILLA+1)-1;   //Pa que se vea un poco mejor
                tabla += conEspacios(nesp);

                //Barra + Casilla de la derecha
                tabla += Valor.BARRA + formatoNombre(posiciones.get(3).get((i-1)/2).getNombre()) + "\n";

            }
            else /**LÍNEAS PARES: parte inferior de la fila de casillas correspondientes*/ {

                tabla += Valor.BARRA;
                tabla += formatoFichas(posiciones.get(1).get(11-i/2).getNombre());  //Casilla de la izquierda

                //Añadimos los espacios del medio (se resta 1 porque hay un borde antes de la siguiente casilla)
                int nesp = (Valor.NCASILLAS_POR_FILA-2)*(Valor.NCHARS_CASILLA+1)-1;   //Pa que se vea un poco mejor
                tabla += conEspacios(nesp);

                //Barra + Casilla de la derecha
                tabla += Valor.BARRA + formatoFichas(posiciones.get(3).get(i/2-1).getNombre()) + "\n";

            }
        }

        //Imprimimos la LÍNEA 21: parte inferior de la penúltima fila de casillas
        tabla += Valor.BARRA;
        tabla += formatoFichas(getCasilla(11).getNombre());  //Casilla de la izquierda
        //Añadimos los espacios del medio: EN ESTE CASO LOS ESPACIO VAN SUBRAYADOS PARA HACER DE BORDE SUPERIOR
        for(i=0;i<Valor.NCASILLAS_POR_FILA-3;i++) {
            tabla += Valor.CASILLA_VACIA + " ";
        }
        tabla += Valor.CASILLA_VACIA + Valor.BARRA;   //El espacio más a la derecha va con barra
        //Barra + Casilla de la derecha
        tabla += formatoFichas(getCasilla(39).getNombre()) + "\n";

        //Imprimimos la LÍNEA 22: parte superior de última fila de casillas
        tabla += Valor.BARRA;
        tabla += formatoNombre(getCasilla(10).getNombre());   //Primera casilla del lado oeste
        //Lado Sur entero (nótese que está al revés)
        for (i=9; i>=0; i--) {
            tabla += formatoNombre(getCasilla(i).getNombre());
        }
        tabla += "\n";

        //Imprimimos la LÍNEA 23: parte inferior de la última fila de casillas
        tabla += Valor.BARRA;
        tabla += formatoFichas(getCasilla(10).getNombre());   //Primera casilla del lado oeste
        //Lado Sur entero (nótese que está al revés)
        for (i=9; i>=0; i--) {
            tabla += formatoFichas(getCasilla(i).getNombre());
        }
        tabla += "\n";

        return tabla;
    }



    //METODOS ÚTILES DEL TABLERO

    /**Método usado para buscar la casilla con el nombre pasado como argumento.
     * @param nombre Nombre de la casilla
     */
    public Casilla encontrar_casilla(String nombre){
        int i;
        for(i=0; i<40; i++){
            if(getCasilla(i).getNombre().equals(nombre)){
                return getCasilla(i);
            }

        }
        return null;
    }

    public Casilla getCasilla(int posicion){
        if(posicion<40 && posicion>-1){
            return this.posiciones.get(posicion/10).get(posicion%10);
        }
        else return null;
    }
    public ArrayList<ArrayList<Casilla>> getPosiciones() {
        return posiciones;
    }
}
