package monopoly.interfaces;

import excepciones.accionNoValida.*;
import excepciones.noExisteObjeto.*;
import partida.*;

public interface Comandos {
    void acabarPartida();
    void declararBancarrota(Jugador jugador) throws NoExisteJugadorException;
    void verTablero();
    void infoJugadorTurno();
    void estadisticasGenerales();
    void salirCarcel() throws SalirCarcelException;
    void ayuda();
    void edificar(String tipoEdificio) throws NoExisteEdificioException;
    void listarVenta();
    void listarJugadores();
    void listarAvatares();
    void listarEdificios(String filtro);
    void listarTratosJugadorActual();
    void lanzarDados(int dado1, int dado2);
    void acabarTurno() throws AcabarTurnoException;
    void cambiarModo() throws CambiarModoException;
    void descCasilla(String casilla) throws NoExisteCasillaException;
    void comprar(String propiedad)throws NoExisteCasillaException;
    void aceptarTrato(String trato) throws NoExisteTratoException;
    void hipotecar(String propiedad) throws HipotecarException,NoExisteCasillaException;
    void deshipotecar(String propiedad) throws DeshipotecarException,NoExisteCasillaException;
    void estadisticasJugador(String jugador) throws NoExisteJugadorException;
    void descJugador(String[] nombreCompleto) throws NoExisteJugadorException;
    void descAvatar(String avatar) throws NoExisteAvatarException;
    void proponerTrato(String comando)throws NoExisteCasillaException, NoExisteJugadorException;
    void venderEdificios(String propiedad, String tipoEdificio, int cantidad) throws  NoExisteEdificioException, NoExisteCasillaException;
    void crearJugador(String nombre, String avatar);
}
