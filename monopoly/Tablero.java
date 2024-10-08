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
        ladoSur.add(new Casilla("Solar1","Solar",1,1000,banca));
        ladoSur.add(new Casilla("Caja","Caja de comunidad",2,banca));
        ladoSur.add(new Casilla("Solar2","Solar",3,1000,banca));
        ladoSur.add(new Casilla("Imp1",4,500,banca));
        ladoSur.add(new Casilla("Trans1","Transporte",5,2000,banca));
        ladoSur.add(new Casilla("Solar3","Solar",6,1000,banca));
        ladoSur.add(new Casilla("Suerte","Suerte",7,banca));
        ladoSur.add(new Casilla("Solar4","Solar",8,1000,banca));
        ladoSur.add(new Casilla("Solar5","Solar",9,1000,banca));

        posiciones.add(ladoSur);

        this.grupos.put("WHITE",new Grupo(getCasilla(1), getCasilla(3),"WHITE"));
        getCasilla(1).setGrupo(grupos.get("WHITE"));
        getCasilla(3).setGrupo(grupos.get("WHITE"));

        this.grupos.put("CYAN",new Grupo(getCasilla(6),getCasilla(8),getCasilla(9),"CYAN"));
        getCasilla(6).setGrupo(grupos.get("CYAN"));
        getCasilla(8).setGrupo(grupos.get("CYAN"));
        getCasilla(9).setGrupo(grupos.get("CYAN"));
    }

    //Método que inserta casillas del lado oeste.
    private void insertarLadoOeste() {
        ArrayList<Casilla> ladoOeste = new ArrayList<Casilla>();
        ladoOeste.add(new Casilla("Carcel","Especial",10,banca));
        ladoOeste.add(new Casilla("Solar6","Solar",11,1000,banca));

        ladoOeste.add(new Casilla("Serv1","Servicios",12,2500,banca));
        ladoOeste.add(new Casilla("Solar7","Solar",13,1000,banca));
        ladoOeste.add(new Casilla("Solar8","Solar",14,1000,banca));
        ladoOeste.add(new Casilla("Trans2","Transporte",15,2000,banca));
        ladoOeste.add(new Casilla("Solar9","Solar",16,1000,banca));
        ladoOeste.add(new Casilla("Caja","Caja de comunidad",17,banca));
        ladoOeste.add(new Casilla("Solar10","Solar",18,1000,banca));
        ladoOeste.add(new Casilla("Solar11","Solar",19,banca));

        posiciones.add(ladoOeste);

        this.grupos.put("BLUE",new Grupo(getCasilla(11), getCasilla(13),getCasilla(14),"BLUE"));
        getCasilla(11).setGrupo(grupos.get("BLUE"));
        getCasilla(13).setGrupo(grupos.get("BLUE"));
        getCasilla(14).setGrupo(grupos.get("BLUE"));

        this.grupos.put("YELLOW",new Grupo(getCasilla(16), getCasilla(18),getCasilla(19),"YELLOW"));
        getCasilla(16).setGrupo(grupos.get("YELLOW"));
        getCasilla(18).setGrupo(grupos.get("YELLOW"));
        getCasilla(19).setGrupo(grupos.get("YELLOW"));
    }

    //Método para insertar las casillas del lado norte.
    private void insertarLadoNorte() {
        ArrayList<Casilla> ladoNorte = new ArrayList<Casilla>();
        ladoNorte.add(new Casilla("Parking","Especial",20,banca));
        ladoNorte.add(new Casilla("Solar12","Solar",21,1000,banca));
        ladoNorte.add(new Casilla("Suerte","Suerte",22,banca));
        ladoNorte.add(new Casilla("Solar13","Solar",23,1000,banca));
        ladoNorte.add(new Casilla("Solar14","Solar",24,1000,banca));
        ladoNorte.add(new Casilla("Trans3","Transporte",25,2000,banca));
        ladoNorte.add(new Casilla("Solar15","Solar",26,1000,banca));
        ladoNorte.add(new Casilla("Solar16","Solar",27,1000,banca));
        ladoNorte.add(new Casilla("Serv2","Servicios",28,2500,banca));
        ladoNorte.add(new Casilla("Solar17","Solar",29,1000,banca));

        posiciones.add(ladoNorte);

        this.grupos.put("BLACK",new Grupo(getCasilla(21), getCasilla(23),getCasilla(24),"BLACK"));
        getCasilla(21).setGrupo(grupos.get("BLACK"));
        getCasilla(23).setGrupo(grupos.get("BLACK"));
        getCasilla(24).setGrupo(grupos.get("BLACK"));
        this.grupos.put("GREEN",new Grupo(getCasilla(26), getCasilla(27),getCasilla(29),"GREEN"));
        getCasilla(26).setGrupo(grupos.get("GREEN"));
        getCasilla(27).setGrupo(grupos.get("GREEN"));
        getCasilla(29).setGrupo(grupos.get("GREEN"));
    }


    //Método que inserta las casillas del lado este.
    private void insertarLadoEste() {
        ArrayList<Casilla> ladoEste = new ArrayList<Casilla>();
        ladoEste.add(new Casilla("IrCarcel","Especial",30,banca));
        ladoEste.add(new Casilla("Solar18","Solar",31,1000,banca));
        ladoEste.add(new Casilla("Serv19","Servicios",32,2500,banca));
        ladoEste.add(new Casilla("Caja","Caja de comunidad",33,banca));
        ladoEste.add(new Casilla("Solar20","Solar",34,1000,banca));
        ladoEste.add(new Casilla("Trans4","Transporte",35,2000,banca));
        ladoEste.add(new Casilla("Suerte","Suerte",36,banca));
        ladoEste.add(new Casilla("Solar21","Solar",37,1000,banca));
        ladoEste.add(new Casilla("Imp2",38,500,banca));
        ladoEste.add(new Casilla("Solar22","Solar",39,1000,banca));


        posiciones.add(ladoEste);
        this.grupos.put("RED",new Grupo(getCasilla(31), getCasilla(32),getCasilla(34),"RED"));
        getCasilla(31).setGrupo(grupos.get("RED"));
        getCasilla(32).setGrupo(grupos.get("RED"));
        getCasilla(34).setGrupo(grupos.get("RED"));

        this.grupos.put("PURPLE",new Grupo(getCasilla(37), getCasilla(39),"PURPLE"));
        getCasilla(37).setGrupo(grupos.get("PURPLE"));
        getCasilla(39).setGrupo(grupos.get("PURPLE"));
        
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

    /**Método que devuelve el nombre de una casilla coloreado si así le corresponde
     * @param casilla Casilla cuyo nombre hay que colorear (se presupone que es un Solar)
     */
    public String colorearNombre(Casilla casilla) {
        String nombreColoreado = new String();

        //Escogemos el color en función del grupo al que pertenece la casilla
        
        switch (casilla.getGrupo().getColorGrupo()) {
            case "BLACK":
                nombreColoreado = Valor.BLACK;
                break;
            case "RED":
                nombreColoreado = Valor.RED;
                break;
            case "GREEN":
                nombreColoreado = Valor.GREEN;
                break;
            case "YELLOW":
                nombreColoreado = Valor.YELLOW;
                break;
            case "BLUE":
                nombreColoreado = Valor.BLUE;
                break;
            case "PURPLE":
                nombreColoreado = Valor.PURPLE;
                break;
            case "CYAN":
                nombreColoreado = Valor.CYAN;
                break;
            case "WHITE":
                nombreColoreado = Valor.WHITE;
                break;
            default:
                System.out.println(casilla.getNombre() + " no es una casilla de tipo Solar.\n");
                nombreColoreado = "";
                break;
        }

        nombreColoreado += casilla.getNombre() + Valor.RESET;

        return nombreColoreado;
    }

    /**Método que devuelve las fichas de una casilla.
     * Una ficha en una casilla se identifica con "&@" donde @ es su caracter identificador (mayúscula entre A y Z).
     * Si hubiese varios jugadores en la misma casilla devuelve "&@@@..." donde cada @ es un identificador (máximo 6).
     * @param casilla Nombre de la casilla
     */
    public String fichas(Casilla casilla) {//PASAMOS EL NOMBRE DE LA CASILLA
        //Variables que vamos a necesitar
        int nj=casilla.getAvatares().size();    //Vemos cuántos jugadores hay en la casilla con .size()
        String fichas = "";

        if(nj==0) {
            fichas += conEspacios(Valor.NCHARS_CASILLA);   //No hay ninguna ficha en la casilla
        }
        else {
            fichas += "&";
            int i;
            for(i=0;i<nj;i++) {
                fichas += casilla.getAvatares().get(i).getId();   //Vamos imprimiendo cada identificador después del &
            }
            fichas += conEspacios(Valor.NCHARS_CASILLA-i-1);   //Rellenamos el resto con espacios
        }

        return fichas;
    }

    /**Método que devuelve la mitad de arriba de las casillas (nombre con el formato adecuado para un nombre de casilla).
     * Formato: Nombre casilla + Espacios + Barra.
     * El número de espacios varía para que la cadena entera mida 8 caracteres (=Valor.NCHARS_CASILLA).
     * Si Nombre casilla ya mide 8 caracteres no se añaden espacios.
     * @param casilla Nombre de la casilla
     */
    public String formatoNombre(Casilla casilla) {
        String nombreConFormato = new String();

        if(casilla.getTipo().equals("Solar")) {
            nombreConFormato = colorearNombre(casilla) + conEspacios(Valor.NCHARS_CASILLA-casilla.getNombre().length()) + Valor.BARRA;
        }
        else {
            nombreConFormato = casilla.getNombre() + conEspacios(Valor.NCHARS_CASILLA-casilla.getNombre().length()) + Valor.BARRA;
        }

        return nombreConFormato;
    }

    /**Método que devuelve la mitad de abajo de las casillas (con fichas si las hubiese)
     * Formato: Nombre casilla + Espacios + Barra.
     * El número de espacios varía para que la cadena entera mida 9 caracteres (=Valor.NCHARS_CASILLA).
     * Si Nombre casilla + Barra ya mide 9 caracteres no se añaden espacios.
     * @param casilla Nombre de la casilla
     */
    public String formatoFichas(Casilla casilla) {
        String nombreConFormato = new String();
        nombreConFormato = Valor.SUBRAYADO + fichas(casilla) + Valor.RESET + Valor.BARRA;
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
            tabla += formatoNombre(getCasilla(i));
        }
        tabla += formatoNombre(getCasilla(30)) + "\n";   //Primera casilla del lado este

        //Imprimimos la TERCERA LÍNEA: parte inferior de la primera fila de casillas
        tabla += Valor.BARRA;
        for (i=20; i<30; i++) {
            tabla += formatoFichas(getCasilla(i));   //Lado norte entero
        }
        tabla += formatoFichas(getCasilla(30)) + "\n";   //Primera casilla del lado este

        //De la LÍNEA Nº4 hasta la LÍNEA Nº20 se van imprimiendo con el siguiente FOR
        //Sobre el índice correspondiente de la casilla que toca imprimir en cada lado:
        //En la izquierda (ladoOeste) para la línea 3 sería el índice 9, la 5 el 8, etc.
        //En la derecha (ladoEste) para la línea 3 sería el índice 1, la 5 el 2, etc.
        //Se podría cambiar el (i-1)/2 por (int)(i/2)
        for(i=3;i<20;i++) {
            if (i%2!=0) /**LÍNEAS IMPARES: parte superior de la fila de casillas correspondientes*/ {

                tabla += Valor.BARRA;
                tabla += formatoNombre(getCasilla(20-(i-1)/2));  //Casilla de la izquierda

                //Añadimos los espacios del medio (se resta 1 porque hay un borde antes de la siguiente casilla)
                int nesp = (Valor.NCASILLAS_POR_FILA-2)*(Valor.NCHARS_CASILLA+1)-1;   //Pa que se vea un poco mejor
                tabla += conEspacios(nesp);

                //Barra + Casilla de la derecha
                tabla += Valor.BARRA + formatoNombre(getCasilla(30+(i-1)/2)) + "\n";

            }
            else /**LÍNEAS PARES: parte inferior de la fila de casillas correspondientes*/ {

                tabla += Valor.BARRA;
                tabla += formatoFichas(getCasilla(21-i/2));  //Casilla de la izquierda

                //Añadimos los espacios del medio (se resta 1 porque hay un borde antes de la siguiente casilla)
                int nesp = (Valor.NCASILLAS_POR_FILA-2)*(Valor.NCHARS_CASILLA+1)-1;   //Pa que se vea un poco mejor
                tabla += conEspacios(nesp);

                //Barra + Casilla de la derecha
                tabla += Valor.BARRA + formatoFichas(getCasilla(29+i/2)) + "\n";

            }
        }

        //Imprimimos la LÍNEA 21: parte inferior de la penúltima fila de casillas
        tabla += Valor.BARRA;
        tabla += formatoFichas(getCasilla(11));  //Casilla de la izquierda
        //Añadimos los espacios del medio: EN ESTE CASO LOS ESPACIO VAN SUBRAYADOS PARA HACER DE BORDE SUPERIOR
        for(i=0;i<Valor.NCASILLAS_POR_FILA-3;i++) {
            tabla += Valor.CASILLA_VACIA + " ";
        }
        tabla += Valor.CASILLA_VACIA + Valor.BARRA;   //El espacio más a la derecha va con barra
        //Barra + Casilla de la derecha
        tabla += formatoFichas(getCasilla(39)) + "\n";

        //Imprimimos la LÍNEA 22: parte superior de última fila de casillas
        tabla += Valor.BARRA;
        tabla += formatoNombre(getCasilla(10));   //Primera casilla del lado oeste
        //Lado Sur entero (nótese que está al revés)
        for (i=9; i>=0; i--) {
            tabla += formatoNombre(getCasilla(i));
        }
        tabla += "\n";

        //Imprimimos la LÍNEA 23: parte inferior de la última fila de casillas
        tabla += Valor.BARRA;
        tabla += formatoFichas(getCasilla(10));   //Primera casilla del lado oeste
        //Lado Sur entero (nótese que está al revés)
        for (i=9; i>=0; i--) {
            tabla += formatoFichas(getCasilla(i));
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
