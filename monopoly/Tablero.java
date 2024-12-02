package monopoly;

import static java.lang.Math.pow;

import java.util.*;
import monopoly.casillas.propiedades.*;
import monopoly.casillas.acciones.*;
import monopoly.casillas.*;
import partida.*;


public class Tablero {
    private ArrayList<ArrayList<Casilla>> posiciones; //Posiciones del tablero: se define como un arraylist de arraylists de casillas (uno por cada lado del tablero).
    private HashMap<String, Grupo> grupos; //Grupos del tablero, almacenados como un HashMap con clave String (será el color del grupo).
    private Jugador banca; //Un jugador que será la banca.


    // =======================
    // SECCIÓN: CONSTRUCTORES
    // =======================

    //Constructor: únicamente le pasamos el jugador banca (que se creará desde el menú).
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
        ((Impuesto)getCasilla(4)).asignarParking((Especial)getCasilla(20));
        ((Impuesto)getCasilla(38)).asignarParking((Especial)getCasilla(20));
        ((Especial)getCasilla(30)).asignarCarcel((Especial)getCasilla(10));
    }

    /**Método auxiliar de generarCasillas() para insertar las casillas del lado sur.*/
    private void insertarLadoSur() {
        ArrayList<Casilla> ladoSur = new ArrayList<Casilla>();
        ladoSur.add(new Especial("Salida",0));
        ladoSur.add(new Solar("Solar1",1));
        ladoSur.add(new AccionCajaComunidad("Caja",2));
        ladoSur.add(new Solar("Solar2",3));
        ladoSur.add(new Impuesto("Imp1",4));
        ladoSur.add(new Transporte("Trans1",5));
        ladoSur.add(new Solar("Solar3",6));
        ladoSur.add(new AccionSuerte("Suerte",7));
        ladoSur.add(new Solar("Solar4",8));
        ladoSur.add(new Solar("Solar5",9));

        posiciones.add(ladoSur);

        this.grupos.put("WHITE",new Grupo(getSolar(1), getSolar(3),"WHITE"));
        getSolar(1).setGrupo(grupos.get("WHITE"));
        getSolar(3).setGrupo(grupos.get("WHITE"));

        this.grupos.put("CYAN",new Grupo(getSolar(6),getSolar(8),getSolar(9),"CYAN"));
        getSolar(6).setGrupo(grupos.get("CYAN"));
        getSolar(8).setGrupo(grupos.get("CYAN"));
        getSolar(9).setGrupo(grupos.get("CYAN"));
    }

    /**Método auxiliar de generarCasillas() para insertar las casillas del lado oeste.*/
    private void insertarLadoOeste() {
        ArrayList<Casilla> ladoOeste = new ArrayList<Casilla>();
        ladoOeste.add(new Especial("Carcel",10));
        ladoOeste.add(new Solar("Solar6",11));
        ladoOeste.add(new Servicio("Serv1",12));
        ladoOeste.add(new Solar("Solar7",13));
        ladoOeste.add(new Solar("Solar8",14));
        ladoOeste.add(new Transporte("Trans2",15));
        ladoOeste.add(new Solar("Solar9",16));
        ladoOeste.add(new AccionCajaComunidad("Caja",17));
        ladoOeste.add(new Solar("Solar10",18));
        ladoOeste.add(new Solar("Solar11",19));

        posiciones.add(ladoOeste);

        this.grupos.put("BLUE",new Grupo(getSolar(11), getSolar(13),getSolar(14),"BLUE"));
        getSolar(11).setGrupo(grupos.get("BLUE"));
        getSolar(13).setGrupo(grupos.get("BLUE"));
        getSolar(14).setGrupo(grupos.get("BLUE"));

        this.grupos.put("YELLOW",new Grupo(getSolar(16), getSolar(18),getSolar(19),"YELLOW"));
        getSolar(16).setGrupo(grupos.get("YELLOW"));
        getSolar(18).setGrupo(grupos.get("YELLOW"));
        getSolar(19).setGrupo(grupos.get("YELLOW"));
    }

    /**Método auxiliar de generarCasillas() para insertar las casillas del lado norte.*/
    private void insertarLadoNorte() {
        ArrayList<Casilla> ladoNorte = new ArrayList<Casilla>();
        ladoNorte.add(new Especial("Parking",20));
        ladoNorte.add(new Solar("Solar12",21));
        ladoNorte.add(new AccionSuerte("Suerte",22));
        ladoNorte.add(new Solar("Solar13",23));
        ladoNorte.add(new Solar("Solar14",24));
        ladoNorte.add(new Transporte("Trans3",25));
        ladoNorte.add(new Solar("Solar15",26));
        ladoNorte.add(new Solar("Solar16",27));
        ladoNorte.add(new Servicio("Serv2",28));
        ladoNorte.add(new Solar("Solar17",29));

        posiciones.add(ladoNorte);

        this.grupos.put("BLACK",new Grupo(getSolar(21), getSolar(23),getSolar(24),"BLACK"));
        getSolar(21).setGrupo(grupos.get("BLACK"));
        getSolar(23).setGrupo(grupos.get("BLACK"));
        getSolar(24).setGrupo(grupos.get("BLACK"));
        
        this.grupos.put("GREEN",new Grupo(getSolar(26), getSolar(27),getSolar(29),"GREEN"));
        getSolar(26).setGrupo(grupos.get("GREEN"));
        getSolar(27).setGrupo(grupos.get("GREEN"));
        getSolar(29).setGrupo(grupos.get("GREEN"));
    }

    /**Método auxiliar de generarCasillas() para insertar las casillas del lado este.*/
    private void insertarLadoEste() {
        ArrayList<Casilla> ladoEste = new ArrayList<Casilla>();
        ladoEste.add(new Especial("IrCarcel",30));
        ladoEste.add(new Solar("Solar18",31));
        ladoEste.add(new Solar("Solar19",32));
        ladoEste.add(new AccionCajaComunidad("Caja",33));
        ladoEste.add(new Solar("Solar20",34));
        ladoEste.add(new Transporte("Trans4",35));
        ladoEste.add(new AccionSuerte("Suerte",36));
        ladoEste.add(new Solar("Solar21",37));
        ladoEste.add(new Impuesto("Imp2",38));
        ladoEste.add(new Solar("Solar22",39));

        posiciones.add(ladoEste);

        this.grupos.put("RED",new Grupo(getSolar(31), getSolar(32),getSolar(34),"RED"));
        getSolar(31).setGrupo(grupos.get("RED"));
        getSolar(32).setGrupo(grupos.get("RED"));
        getSolar(34).setGrupo(grupos.get("RED"));

        this.grupos.put("PURPLE",new Grupo(getSolar(37), getSolar(39),"PURPLE"));
        getSolar(37).setGrupo(grupos.get("PURPLE"));
        getSolar(39).setGrupo(grupos.get("PURPLE"));

    }

    
    // =========================================
    // SECCIÓN: MÉTODOD DE ACCESO Y MANIPULACIÓN
    // =========================================
    public Casilla getCasilla(int posicion){
        if(posicion<40 && posicion>-1){
            return this.posiciones.get(posicion/10).get(posicion%10);
        }
        else return null;
    }


    public Solar getSolar(int posicion){
        if(posicion<40 && posicion>-1){
            Casilla aux =this.posiciones.get(posicion/10).get(posicion%10);
            if(aux instanceof Solar){
                return (Solar) aux;
            }
            else return null;
        }
        else return null;
    }

    public Propiedad getPropiedad(int posicion){
        if(posicion<40 && posicion>-1){
            Casilla aux =this.posiciones.get(posicion/10).get(posicion%10);
            if(aux instanceof Propiedad){
                return (Propiedad) aux;
            }
            else return null;
        }
        else return null;
    }

    /**Método usado para buscar la casilla con el nombre pasado como argumento.
     * @param nombre Nombre de la casilla
     */
    public Solar encontrar_solar(String nombre){
        int i;
        for(i=0; i<40; i++){
            if(getSolar(i)!=null){
                if(getSolar(i).getNombre().equals(nombre)){
                    return getSolar(i);
                }
            }

        }
        return null;
    }

    public Propiedad encontrar_propiedad(String nombre){
        int i;
        for(i=0; i<40; i++){
            if(getPropiedad(i)!=null){
                if(getPropiedad(i).getNombre().equals(nombre)){
                    return getPropiedad(i);
                }
            }

        }
        return null;
    }


    public Casilla encontrar_casilla(String nombre){
        int i;
        for(i=0; i<40; i++){
            if(getCasilla(i).getNombre().equals(nombre)){
                return getCasilla(i);
            }

        }
        return null;
    }


    /**Método para aumentar el coste de los solares que pertenecen a la banca cuando todos dan 4 vueltas sin comprar.*/
    public void aumentarCoste(Jugador banca) {
        // Itera sobre todas las posiciones del tablero
        for (int i = 0; i < getPosiciones().size(); i++) {
            Propiedad propiedad = getSolar(i);

            // Verifica si la casilla no tiene dueño (es decir, si su dueño es la banca)
            if (propiedad.getDuenho() == banca && propiedad instanceof Solar) {

                // Aplica el porcentaje de incremento al valor de la casilla
                float incremento = propiedad.getValor() * 0.05f;
                propiedad.sumarValor(incremento);
            }
        }
    }
    
    // =========================================
    // SECCIÓN: MÉTODO DE ACCESO Y MANIPULACIÓN
    // =========================================

    public ArrayList<ArrayList<Casilla>> getPosiciones() {
        return posiciones;
    }



    // ==============================
    // SECCIÓN: IMPRESIÓN DE TABLERO
    // ==============================

    /**Método para subrayar texto.
     * @param texto Texto que hay que subrayar
     */
    public String subrayar(String texto) {
        return Valor.SUBRAYADO + texto + Valor.RESET;
    }

    /**Método para crear cadenas de espacios.
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

    /**Método que devuelve el nombre de una casilla coloreado si así le corresponde.
     * @param casilla Casilla cuyo nombre hay que colorear (se presupone que es un Solar)
     */
    public String colorearNombre(Solar solar) {
        String nombreColoreado;

        //Escogemos el color en función del grupo al que pertenece la casilla
        switch (solar.getGrupo().getColorGrupo()) {
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
                System.out.println(solar.getNombre() + " tiene un color de grupo inválido.\n");
                nombreColoreado = "";
                break;
        }

        nombreColoreado += solar.getNombre() + Valor.RESET;

        return nombreColoreado;
    }

    /**Método que devuelve las fichas de una casilla.
     * Una ficha en una casilla se identifica con "&@" donde @ es su caracter identificador (mayúscula entre A y Z).
     * Si hubiese varios jugadores en la misma casilla devuelve "&@@@..." donde cada @ es un identificador (máximo 6).
     * @param casilla Nombre de la casilla
     */
    public String fichas(Casilla casilla) {
        //Variables que vamos a necesitar
        int nj=casilla.getAvatares().size();    //Vemos cuántos jugadores hay en la casilla con .size()
        String fichas = "";

        if(nj==0) {
            fichas += conEspacios(Valor.NCHARS_CASILLA);   //No hay ninguna ficha en la casilla
        }
        else {
            fichas += Valor.BOLD_STRING + "&";  //La negrita ya se resetea en formatoFichas junto con el subrayado
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
        String nombreConFormato;

        if(casilla instanceof Solar) {
            nombreConFormato = colorearNombre((Solar)casilla) + conEspacios(Valor.NCHARS_CASILLA-casilla.getNombre().length()) + Valor.BARRA;
        }
        else {
            nombreConFormato = casilla.getNombre() + conEspacios(Valor.NCHARS_CASILLA-casilla.getNombre().length()) + Valor.BARRA;
        }

        return nombreConFormato;
    }


    /**Método que devuelve la mitad de abajo de las casillas (con fichas si las hubiese)
     * Formato: Fichas si las hubiera + Espacios + Barra.
     * El número de espacios varía para que la cadena entera mida 8 caracteres (=Valor.NCHARS_CASILLA).
     * @param casilla Nombre de la casilla
     */
    public String formatoFichas(Casilla casilla) {
        String nombreConFormato;
        nombreConFormato = subrayar(fichas(casilla)) + Valor.BARRA;
        return nombreConFormato;
    }

    /**Modificación del metodo toString() para imprimir el tablero.
     * Versión 3: como hay que imprimir todas las fichas que hay en una casilla
     * vamos a hacer que cada casilla tenga dos líneas: una para el nombre y otra
     * para imprimir las fichas si las hubiese (ahora en vez de una fila de líneas
     * para delimitar casillas va la segunda línea de la casilla subrayada).
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
            tabla += subrayar(Valor.CASILLA_VACIA) + " ";
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
        //En la izquierda (ladoOeste) para la línea 3 sería el índice 19, la 5 el 18, etc.
        //En la derecha (ladoEste) para la línea 3 sería el índice 31, la 5 el 32, etc.
        for(i=3;i<20;i++) {
            if (i%2!=0) /**LÍNEAS IMPARES: parte superior de la fila de casillas correspondientes*/ {

                tabla += Valor.BARRA;
                tabla += formatoNombre(getCasilla(20-(i-1)/2));  //Casilla de la izquierda

                //PETADA HISTORICA
                //Si hubiese que imprimir algo de texto en el centro del tablero se imprime
                //Como partimos del índice 3 ajustamos para empezar en TABLERO[0]
                if(Texto.TABLERO[i-3]!=null) {

                    //Añadimos el texto (dejando una casilla de sangría)
                    tabla += conEspacios(Valor.NCHARS_CASILLA + 1) + Texto.TABLERO[i-3];
                    //Rellenamos el resto con espacios
                    int nesp = (Valor.NCASILLAS_POR_FILA - 3) * (Valor.NCHARS_CASILLA + 1) - 1 - Texto.TABLERO[i-3].length();
                    tabla += conEspacios(nesp);
                }
                //En este caso no hay nada que imprimir en la línea
                //Añadimos los espacios/texto del medio (se resta 1 porque hay un borde antes de la siguiente casilla)
                else {
                    int nesp = (Valor.NCASILLAS_POR_FILA - 2) * (Valor.NCHARS_CASILLA + 1) - 1;
                    tabla += conEspacios(nesp);
                }

                //Barra + Casilla de la derecha
                tabla += Valor.BARRA + formatoNombre(getCasilla(30+(i-1)/2)) + "\n";

            }
            else /**LÍNEAS PARES: parte inferior de la fila de casillas correspondientes*/ {

                tabla += Valor.BARRA;
                tabla += formatoFichas(getCasilla(21-i/2));  //Casilla de la izquierda

                //PETADA HISTORICA
                //Si hubiese que imprimir algo de texto en el centro del tablero se imprime
                if(Texto.TABLERO[i-3]!=null) {

                    //Añadimos el texto (dejando una casilla de sangría)
                    tabla += conEspacios(Valor.NCHARS_CASILLA + 1) + Texto.TABLERO[i-3];
                    //Rellenamos el resto con espacios
                    int nesp = (Valor.NCASILLAS_POR_FILA - 3) * (Valor.NCHARS_CASILLA + 1) - 1 - Texto.TABLERO[i-3].length();
                    tabla += conEspacios(nesp);
                }
                //En este caso no hay nada que imprimir en la línea
                //Añadimos los espacios/texto del medio (se resta 1 porque hay un borde antes de la siguiente casilla)
                else {
                    int nesp = (Valor.NCASILLAS_POR_FILA - 2) * (Valor.NCHARS_CASILLA + 1) - 1;
                    tabla += conEspacios(nesp);
                }

                //Barra + Casilla de la derecha
                tabla += Valor.BARRA + formatoFichas(getCasilla(29+i/2)) + "\n";

            }
        }

        //Imprimimos la LÍNEA 21: parte inferior de la penúltima fila de casillas
        tabla += Valor.BARRA;
        tabla += formatoFichas(getCasilla(11));  //Casilla de la izquierda
        //Añadimos los espacios del medio: EN ESTE CASO LOS ESPACIO VAN SUBRAYADOS PARA HACER DE BORDE SUPERIOR
        for(i=0;i<Valor.NCASILLAS_POR_FILA-3;i++) {
            tabla += subrayar(Valor.CASILLA_VACIA) + " ";
        }
        tabla += subrayar(Valor.CASILLA_VACIA) + Valor.BARRA;   //El espacio más a la derecha va con barra
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

}
