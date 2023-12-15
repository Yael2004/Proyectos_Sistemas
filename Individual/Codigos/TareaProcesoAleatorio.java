class TareaProcesoAleatorio {
    private ColaProcesos simulador;

    public TareaProcesoAleatorio(ColaProcesos simulador) {
        this.simulador = simulador;
    }

    public void ejecutar() {
        simulador.generarProcesoAleatorio();
    }
}
