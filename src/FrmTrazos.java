import java.awt.*;
import java.awt.event.*;
import java.io.File;
import javax.swing.*;

public class FrmTrazos extends JFrame {

    private String[] tipoTrazo = new String[] { "Linea", "Rectangulo", "Ovalo" };
    JComboBox cmbTipoTrazo;
    JTextField txtInfo;
    JPanel pnlDibujo;
    int x, y;
    boolean trazando = false;

    private SaveTrazos listaTrazos = new SaveTrazos();
    private Trazo trazoSeleccionado = null;
    private boolean modoSeleccion = false;

    public FrmTrazos() {

        setSize(600, 500);
        setTitle("Editor de gráficas");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        JToolBar tbTrazos = new JToolBar();
        tbTrazos.setPreferredSize(new Dimension (600, 80));
        cmbTipoTrazo = new JComboBox<>(tipoTrazo);
        tbTrazos.add(cmbTipoTrazo);

        JButton btnEliminar = new JButton("Eliminar seleccionado"); 
        tbTrazos.add(btnEliminar);

        btnEliminar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (trazoSeleccionado != null) {
                    listaTrazos.eliminar(trazoSeleccionado);
                    trazoSeleccionado = null; // Deseleccionar después de eliminar
                    pnlDibujo.repaint();
                }
            }
        });

        // Agregare el boton que me permita borrar
        JButton btnLimpiar = crearBotonConIcono("imagenes/limpiar.png");
        tbTrazos.add(btnLimpiar);

        btnLimpiar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                listaTrazos.limpiar();
                pnlDibujo.repaint();
            }
        });

        JButton btnGuardar = crearBotonConIcono("imagenes/guardar.png");
        tbTrazos.add(btnGuardar);

        btnGuardar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setDialogTitle("Guardar Dibujo");
                int userSelection = fileChooser.showSaveDialog(null);

                if (userSelection == JFileChooser.APPROVE_OPTION) {
                    File archivo = fileChooser.getSelectedFile();
                    listaTrazos.guardarEnArchivo(archivo.getAbsolutePath());
                }
            }
        });

        JButton btnCargar = crearBotonConIcono("imagenes/cargar.png");
        tbTrazos.add(btnCargar);


        btnCargar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setDialogTitle("Cargar Dibujo");
                int userSelection = fileChooser.showOpenDialog(null);

                if (userSelection == JFileChooser.APPROVE_OPTION) {
                    File archivo = fileChooser.getSelectedFile();
                    listaTrazos.cargarDesdeArchivo(archivo.getAbsolutePath());
                    pnlDibujo.repaint();
                }
            }
        });

        JButton btnSeleccionar = crearBotonConIcono("imagenes/seleccionar.png");
        tbTrazos.add(btnSeleccionar);

        btnSeleccionar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                modoSeleccion = !modoSeleccion;
                if (modoSeleccion) {
                    txtInfo.setText("Modo selección activado");
                } else {
                    txtInfo.setText("Modo dibujo activado");
                    trazoSeleccionado = null;
                }
                pnlDibujo.repaint();
            }
        });


        
        DefaultComboBoxModel dcm = new DefaultComboBoxModel(tipoTrazo);
        cmbTipoTrazo.setModel(dcm);
        tbTrazos.add(cmbTipoTrazo);

        txtInfo = new JTextField();
        tbTrazos.add(txtInfo);

        pnlDibujo = new PanelDibujo();
        pnlDibujo.setBackground(Color.GRAY);

        getContentPane().add(tbTrazos, BorderLayout.NORTH);
        getContentPane().add(pnlDibujo, BorderLayout.CENTER);

        pnlDibujo.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent me) {
                if (modoSeleccion) {
                    Trazo seleccionado = listaTrazos.buscarTrazo(me.getX(), me.getY());

                    if (seleccionado != null) {

                        trazoSeleccionado = seleccionado;
                        trazoSeleccionado.trazoColor(Color.BLUE);
                        txtInfo.setText("Seleccionaste un trazo en: x=" + me.getX() + ", y=" + me.getY());
                    } else {
                        trazoSeleccionado = null;
                    }
                } else {
                    trazoSeleccionado = null;
                    dibujar(me.getX(), me.getY());
                }

                pnlDibujo.repaint();
            }
        });

        pnlDibujo.addMouseMotionListener(new MouseMotionAdapter() {
            public void mouseMoved(MouseEvent me) {
                dibujarTemporal(me.getX(), me.getY());
            }
        });

    }

    private void dibujar(int x, int y) {
        if (!trazando) {
            trazando = true;
            this.x = x;
            this.y = y;
            txtInfo.setText("Trazando desde x=" + this.x + ", y=" + this.y);
        } else {
            trazando = false;

            Trazo nuevoTrazo = new Trazo(
                    tipoTrazo[cmbTipoTrazo.getSelectedIndex()], this.x, this.y, x, y, Color.GREEN);
            listaTrazos.agregar(nuevoTrazo);

            // 🚀 No dibujamos aquí, solo pedimos que la pantalla se redibuje
            pnlDibujo.repaint();

            txtInfo.setText("");
        }
    }



    private JButton crearBotonConIcono(String ruta) {
        java.net.URL imgURL = getClass().getClassLoader().getResource(ruta);
        if (imgURL != null) {
            ImageIcon iconoOriginal = new ImageIcon(imgURL);
            Image img = iconoOriginal.getImage().getScaledInstance(48, 48, Image.SCALE_SMOOTH);
            return new JButton(new ImageIcon(img));
        } else {
            System.err.println("No se encontró la imagen: " + ruta);
            return new JButton("X");
        }
    }
    
    private void dibujarTemporal(int x, int y) {
        if (trazando) {
            Graphics g = pnlDibujo.getGraphics();
            g.setColor(Color.BLACK);
            // g.fillRect(0, 0, pnlDibujo.getWidth(), pnlDibujo.getHeight());
            pnlDibujo.repaint();

            g.setColor(Color.YELLOW);
            int ancho = Math.abs(this.x - x);
            int alto = Math.abs(this.y - y);
            int x1 = x < this.x ? x : this.x;
            int y1 = y < this.y ? y : this.y;
            switch (cmbTipoTrazo.getSelectedIndex()) {
                case 0:
                    g.drawLine(this.x, this.y, x, y);
                    break;
                case 1:
                    g.drawRect(x1, y1, ancho, alto);
                    break;
                case 2:
                    g.drawOval(x1, y1, ancho, alto);
                    break;
            }
        }
    }

    class PanelDibujo extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            listaTrazos.recorrer(g);

            if (trazoSeleccionado != null) {
                g.setColor(Color.BLUE);
                trazoSeleccionado.dibujar(g);
            }
        }
    }
}