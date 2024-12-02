package monopoly.interfaces;

import partida.*;

import monopoly.casillas.*;;


public interface Comandos {
    void acabarPartida();
    void declararBancarrota(Jugador jugador);
    void verTablero();
    void infoJugadorTurno();
    void estadisticasGenerales();
    void salirCarcel();
    void ayuda();
    void edificar(String tipoEdificio);
    void listarVenta();
    void listarJugadores();
    void listarAvatares();
    void listarEdificios(String filtro);
    void listarTratosJugadorActual();
    void lanzarDados(int dado1, int dado2);
    void acabarTurno();
    void cambiarModo();
    void descCasilla(String casilla);
    void comprar(String propiedad);
    void aceptarTrato(String trato);
    void hipotecar(String propiedad);
    void deshipotecar(String propiedad);
    void estadisticasJugador(String jugador);
    void descJugador(String[] nombreCompleto);
    void descAvatar(String avatar);
    void proponerTrato(String comando);
    void venderEdificios(String propiedad, String tipoEdificio, int cantidad);
}
