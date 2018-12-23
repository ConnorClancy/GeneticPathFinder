import javax.swing.*;
import java.awt.*;

public class Field extends JFrame {

    int SIZE;
    private NodeLocation[][] GRID;


    public Field(int size, NodeLocation[][] grid){
        SIZE = size;
        GRID = grid;
        setBounds(0,0,600,600);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setVisible(true);
    }

    /*
    Draws grid which acts as the field the algorithm works on
     */
    public void paint(Graphics g){
        super.paint(g);
        Graphics2D gr = (Graphics2D)g;
        for(int i = 0; i < SIZE; i++){
            for(int j = 0; j < SIZE; j++){
                gr.draw(new Rectangle(GRID[i][j].GRID_X, GRID[i][j].GRID_Y, 25, 25));
            }
        }
    }

    /*
    Fills an individual square on the grid with a set colour.
     */
    public void colour(int x, int y, Color c){
        System.out.println("X: " + x + " Y: " + y);
        Graphics gr = getContentPane().getGraphics();
        gr.setColor(c);
        gr.fillRect(x+1,y-21, 23, 23);
    }

}
