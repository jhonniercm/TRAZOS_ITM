import java.io.*;
import java.util.ArrayList;

public class SaveTrazos implements Serializable {
    private Nodo cabeza;

    private class Nodo implements Serializable {
        Trazo dato;
        Nodo siguiente;

        public Nodo(Trazo dato) {
            this.dato = dato;
            this.siguiente = null;
        }
    }
    public void eliminar(Trazo t) {
        if (cabeza == null) return;
    
        // Si el primer nodo es el que queremos eliminar
        if (cabeza.dato.equals(t)) {
            cabeza = cabeza.siguiente;
            return;
        }
    
        Nodo temp = cabeza;
        while (temp.siguiente != null) {
            if (temp.siguiente.dato.equals(t)) {
                temp.siguiente = temp.siguiente.siguiente; // Saltar el nodo a eliminar
                return;
            }
            temp = temp.siguiente;
        }
    }
    
    public void agregar(Trazo t) {
        Nodo nuevo = new Nodo(t);
        if (cabeza == null) {
            cabeza = nuevo;
        } else {
            Nodo temp = cabeza;
            while (temp.siguiente != null) {
                temp = temp.siguiente;
            }
            temp.siguiente = nuevo;
        }
    }

    public void recorrer(java.awt.Graphics g) {
        Nodo temp = cabeza;
        while (temp != null) {
            temp.dato.dibujar(g);
            temp = temp.siguiente;
        }
    }

    public void limpiar() {
        cabeza = null;
    }

    public Trazo buscarTrazo(int x, int y) {
        Nodo temp = cabeza;
        while (temp != null) {
            if (temp.dato.contienePunto(x, y)) {
                return temp.dato; 
            }
            temp = temp.siguiente;
        }
        return null;
    }

    public void guardarEnArchivo(String nombreArchivo) {
        if (cabeza == null) {
            System.out.println("No hay trazos para guardar.");
            return;
        }

        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(nombreArchivo))) {
            out.writeObject(toArrayList());
            System.out.println("Dibujo guardado correctamente en: " + nombreArchivo);
        } catch (IOException e) {
            System.out.println("Error al guardar el archivo.");
            e.printStackTrace();
        }
    }

    public void cargarDesdeArchivo(String nombreArchivo) {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(nombreArchivo))) {
            ArrayList<Trazo> trazosCargados = (ArrayList<Trazo>) in.readObject();
            limpiar();  // Limpiamos antes de cargar los nuevos trazos
            fromArrayList(trazosCargados);
            System.out.println("Dibujo cargado correctamente desde: " + nombreArchivo);
        } catch (IOException e) {
            System.out.println("Error al cargar el archivo.");
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            System.out.println("Error: El archivo no contiene datos válidos.");
            e.printStackTrace();
        }
    }

    private ArrayList<Trazo> toArrayList() {
        ArrayList<Trazo> lista = new ArrayList<>();
        Nodo temp = cabeza;
        while (temp != null) {
            lista.add(temp.dato);
            temp = temp.siguiente;
        }
        return lista;
    }

    private void fromArrayList(ArrayList<Trazo> lista) {
        cabeza = null;
        for (Trazo t : lista) {
            agregar(t);
        }
    }
}
