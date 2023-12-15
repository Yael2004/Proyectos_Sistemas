import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

import javax.swing.SwingUtilities;
import javax.swing.JTextArea;

public class ColaProcesos {
    private Queue<Proceso> colaProcesos;
    private int contP = 1;
    private Random random;
    private JTextArea AreaTexto;
    private JTextArea AreaProcesosCreacion;

    public ColaProcesos(JTextArea AreaTexto, JTextArea AreaProcesosCreacion) {
        colaProcesos = new LinkedList<>();
        random = new Random();
        this.AreaTexto = AreaTexto;
        this.AreaProcesosCreacion = AreaProcesosCreacion;
    }

    public synchronized void generarProcesoAleatorio() {
        int tiempoEjecucion = random.nextInt(10) + 1;
        int memoriaRequerida = random.nextInt(2000) + 1;

        Proceso nuevoProceso = new Proceso("P" + (contP), tiempoEjecucion, memoriaRequerida);
        colaProcesos.offer(nuevoProceso);
        SwingUtilities.invokeLater(() -> {
        AreaProcesosCreacion.append("Nuevo proceso generado: " + nuevoProceso.nombre + " con tiempo " + nuevoProceso.tiempoEjecucion +
                " y memoria " + nuevoProceso.memoriaRequerida + " MB \n");
        });

        contP++;
    }

    public synchronized Proceso ejecutarProceso() {
        return colaProcesos.poll();
    }

    public synchronized boolean isEmpty() {
        return colaProcesos.isEmpty();
    }
}
