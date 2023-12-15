import javax.swing.*;
import java.awt.*;
import java.util.concurrent.Semaphore;

class SimuladorFCFS extends JFrame {
    private ColaProcesos colaProcesos;
    private JTextArea AreaEjecutandose;
    private JTextArea AreaProcesosCreacion;
    private JTextArea AreaProcesosFinalizados;
    private JTextArea AreaExcluidos;
    private Semaphore mutex = new Semaphore(1);
    private int memoriaTotal = 1024;

    public SimuladorFCFS() {
        super("Simulador FCFS");

        JLabel labelProcesosCreacion = new JLabel("Procesos en Creacion:");
        JLabel labelEjecutandose = new JLabel("Procesos Ejecutandose:");
        JLabel labelProcesosFinalizados = new JLabel("Procesos Finalizados:");
        JLabel labelExcluidos = new JLabel("Procesos Excluidos por Memoria:");

        AreaEjecutandose = new JTextArea(10, 30);
        AreaEjecutandose.setEditable(false);
        AreaEjecutandose.setBackground(Color.BLACK);
        AreaEjecutandose.setForeground(Color.GREEN);

        AreaProcesosCreacion = new JTextArea(10, 30);
        AreaProcesosCreacion.setEditable(false);
        AreaProcesosCreacion.setBackground(Color.BLACK);
        AreaProcesosCreacion.setForeground(Color.BLUE);

        AreaProcesosFinalizados = new JTextArea(10, 30);
        AreaProcesosFinalizados.setEditable(false);
        AreaProcesosFinalizados.setBackground(Color.BLACK);
        AreaProcesosFinalizados.setForeground(Color.MAGENTA);

        AreaExcluidos = new JTextArea(10, 30);
        AreaExcluidos.setEditable(false);
        AreaExcluidos.setBackground(Color.BLACK);
        AreaExcluidos.setForeground(Color.RED);

        colaProcesos = new ColaProcesos(AreaProcesosFinalizados, AreaProcesosCreacion);

        JPanel panelProcesosCreacion = new JPanel(new BorderLayout());
        JPanel panelEjecutandose = new JPanel(new BorderLayout());
        JPanel panelProcesosFinalizados = new JPanel(new BorderLayout());
        JPanel panelExcluidos = new JPanel(new BorderLayout());

        panelProcesosCreacion.add(labelProcesosCreacion, BorderLayout.NORTH);
        panelProcesosCreacion.add(new JScrollPane(AreaProcesosCreacion), BorderLayout.CENTER);

        panelEjecutandose.add(labelEjecutandose, BorderLayout.NORTH);
        panelEjecutandose.add(new JScrollPane(AreaEjecutandose), BorderLayout.CENTER);

        panelProcesosFinalizados.add(labelProcesosFinalizados, BorderLayout.NORTH);
        panelProcesosFinalizados.add(new JScrollPane(AreaProcesosFinalizados), BorderLayout.CENTER);

        panelExcluidos.add(labelExcluidos, BorderLayout.NORTH);
        panelExcluidos.add(new JScrollPane(AreaExcluidos), BorderLayout.CENTER);

        setLayout(new GridLayout(2, 2));
        add(panelProcesosCreacion);
        add(panelEjecutandose);
        add(panelProcesosFinalizados);
        add(panelExcluidos);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 700); 
        setLocationRelativeTo(null);
        setVisible(true);

        Thread generadorProcesos = new Thread(() -> {
            while (true) {
                colaProcesos.generarProcesoAleatorio();
                try {
                    Thread.sleep(3000);  // Espera 3 segundos
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        Thread ejecutorProcesos = new Thread(() -> {
            while (true){
                if (!colaProcesos.isEmpty()) {
                    Proceso procesoActual = colaProcesos.ejecutarProceso();
                    if (memoriaTotal < procesoActual.memoriaRequerida){
                        AreaExcluidos.append("Proceso " + procesoActual.nombre + " con tiempo " + procesoActual.tiempoEjecucion +
                        " y memoria " + procesoActual.memoriaRequerida + " MB\n");
                    }else {
                        AreaEjecutandose.append("Ejecutando proceso " + procesoActual.nombre +
                                " con tiempo " + procesoActual.tiempoEjecucion +
                                " y memoria " + procesoActual.memoriaRequerida + " MB\n");
                        new Thread(() -> {
                            try {
                                mutex.acquire();
                                Thread.sleep(procesoActual.tiempoEjecucion * 1000);
                                AreaProcesosFinalizados.append("Proceso " + procesoActual.nombre + " completado.\n");
                            } catch (InterruptedException e) {
                                    e.printStackTrace();
                            }
                                mutex.release();
                        }).start();
                    }
                } 
            }    
        });
    

        generadorProcesos.start();
        ejecutorProcesos.start();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new SimuladorFCFS());
    }
}
