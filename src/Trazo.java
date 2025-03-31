import java.awt.Graphics;
import java.awt.Color;
import java.io.*;

public class Trazo implements Serializable {

    private String tipo;
    private int x1, y1, x2, y2;
    private Color color;

    public Trazo(String tipo, int x1, int y1, int x2, int y2, Color color) {
        this.tipo = tipo;
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
        this.color = color;
    }

    public boolean contienePunto(int px, int py) {
        int x1 = Math.min(this.x1, this.x2);
        int y1 = Math.min(this.y1, this.y2);
        int x2 = Math.max(this.x1, this.x2);
        int y2 = Math.max(this.y1, this.y2);

        return px >= x1 && px <= x2 && py >= y1 && py <= y2;
    }

    public void dibujar(Graphics g) {
        g.setColor(this.color);
        switch (tipo) {
            case "Linea":
                g.drawLine(x1, y1, x2, y2);
                break;
            case "Rectangulo":
                g.drawRect(x1, y1, Math.abs(x2 - x1), Math.abs(y2 - y1));
                break;
            case "Ovalo":
                g.drawOval(x1, y1, Math.abs(x2 - x1), Math.abs(y2 - y1));
                break;
        }
    }

    public void trazoColor(Color nuevoColor) {
        this.color = nuevoColor;
    }
    
    public String getTipo() {
        return tipo;
    }

    public int getX1() {
        return x1;
    }

    public int getY1() {
        return y1;
    }

    public int getX2() {
        return x2;
    }

    public int getY2() {
        return y2;
    }

    public Color getColor() {
        return color;
    }

    public String toString() {
        return tipo + "," + x1 + "," + y1 + "," + x2 + "," + y2 + "," + color.getRGB();
    }
}
