package monopoly.interfaces;

import excepciones.NoExisteExcepcion.*;
import excepciones.ComandoImposibleException.*;
import partida.*;

public interface Comandos {
    void acabarPartida();
    void declararBancarrota(Jugador jugador) throws JugadorNoEncontrado;
    void verTablero();
    void infoJugadorTurno();
    void estadisticasGenerales();
    void salirCarcel() throws SalirCarcelImposibleExcepcion;
    void ayuda();
    void edificar(String tipoEdificio) throws EdificioNoEncontrado;
    void listarVenta();
    void listarJugadores();
    void listarAvatares();
    void listarEdificios(String filtro);
    void listarTratosJugadorActual();
    void lanzarDados(int dado1, int dado2);
    void acabarTurno() throws AcabarTurnoImposibleExcepcion;
    void cambiarModo() throws CambiarModoImposibleExcepcion;
    void descCasilla(String casilla) throws CasillaNoEncontrada;
    void comprar(String propiedad)throws CasillaNoEncontrada;
    void aceptarTrato(String trato) throws TratoNoEncontrado;
    void hipotecar(String propiedad) throws HipotecarImposibleExcepcion,CasillaNoEncontrada;
    void deshipotecar(String propiedad) throws DeshipotecarImposibleExcepcion,CasillaNoEncontrada;
    void estadisticasJugador(String jugador) throws JugadorNoEncontrado;
    void descJugador(String[] nombreCompleto) throws JugadorNoEncontrado;
    void descAvatar(String avatar) throws AvatarNoEncontrado;
    void proponerTrato(String comando)throws CasillaNoEncontrada, JugadorNoEncontrado;
    void venderEdificios(String propiedad, String tipoEdificio, int cantidad) throws  EdificioNoEncontrado, CasillaNoEncontrada;
    void crearJugador(String nombre, String avatar);
}
