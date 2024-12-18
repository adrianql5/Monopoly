package partida;

public class Estadisticas {

    // Atributos
    private float impuestosYTasasPagados;
    private float pagoDeAlquileres;
    private float cobroDeAlquileres;
    private float dineroSalidaRecaudado;
    private float dineroRecaudadoBote;
    private int vecesEnLaCarcel;
    private int vecesTirado;

    // Constructor
    public Estadisticas() {
        this.impuestosYTasasPagados = 0;
        this.pagoDeAlquileres = 0;
        this.cobroDeAlquileres = 0;
        this.dineroSalidaRecaudado = 0;;
        this.dineroRecaudadoBote = 0;
        this.vecesEnLaCarcel = 0;
        this.vecesTirado = 0;

    }

    // Getters y Setters para cada atributo

    public float getImpuestosYTasasPagados() {
        return impuestosYTasasPagados;
    }

    public void sumarImpuestosYTasasPagados(float impuestosPagados) {
        this.impuestosYTasasPagados += impuestosPagados;
    }

    public float getPagoDeAlquileres() {
        return pagoDeAlquileres;
    }

    public void sumarPagoDeAlquileres(float pagoDeAlquileres) {
        this.pagoDeAlquileres += pagoDeAlquileres;
    }

    public float getCobroDeAlquileres() {
        return cobroDeAlquileres;
    }

    public void sumarCobroDeAlquileres(float cobroDeAlquileres) {
        this.cobroDeAlquileres += cobroDeAlquileres;
    }

    public float getDineroSalidaRecaudado() {
        return dineroSalidaRecaudado;
    }

    public void sumarDineroSalidaRecaudado(float dineroSalidaRecaudado) {
        this.dineroSalidaRecaudado += dineroSalidaRecaudado;
    }

    public float getDineroRecaudadoBote() {
        return dineroRecaudadoBote;
    }

    public void sumarDineroRecaudadoBote(float dineroRecaudadoBote) {
        this.dineroRecaudadoBote += dineroRecaudadoBote;
    }

    public int getVecesEnLaCarcel() {
        return vecesEnLaCarcel;
    }

    public void sumarVecesEnLaCarcel(int vecesEnLaCarcel) {
        this.vecesEnLaCarcel += vecesEnLaCarcel;
    }

    public int getVecesTirado() {
        return vecesTirado;
    }

    public void sumarVecesTirado(int vecesTirado) {
        this.vecesTirado += vecesTirado;
    }







}
