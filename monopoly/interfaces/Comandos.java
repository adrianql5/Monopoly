package monopoly.interfaces;

import partida.*;

import monopoly.casillas.*;;


//faltan muchos comandos pero me da pereza histórica
public interface Comandos {
    void listarVenta();
    void listarJugadores();
    void listarAvatares();
    void listarTratosJugadorActual();
    
    void verTablero();

    void jugadorActual();

    void salirCarcel();
    
    void estadisticasGenerales();

    void acabarTurno();
    void cambiarModo();
    
    void listarEdificios(String color);
    void descAvatar(String id);
    void descJugador(String nombre);
    void descCasilla(String nombre);
    
    
    void declararBancarrota(Jugador jugador);
    void estadisticasJugador(String jugador);
    
    void aceptarTrato(String id);
    
    void comprar(String nombre);
    void edificar(String tipo);
    
}
