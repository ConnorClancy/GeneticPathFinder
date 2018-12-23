import java.util.HashMap;

public class NodeLocation{

    private boolean IS_OBSTACLE;
    int GRID_X;
    int GRID_Y;
    int COLUMN;
    int ROW;
    HashMap<String, NodeLocation> traversableNeighbours = new HashMap<>();

    public NodeLocation(boolean ob, int col, int row){
        IS_OBSTACLE = ob;
        COLUMN = col;
        ROW = row;
    }

    public void setPhysicalPosition(int x, int y){
        GRID_X = x;
        GRID_Y = y;
    }

    public String getName(){
        return "[" + COLUMN + ", " + ROW + "]";
    }

    public void addNeighbour(String direction, NodeLocation neighbour){
        traversableNeighbours.put(direction, neighbour);
    }

    public boolean hasNeighbour(String direction){
        return traversableNeighbours.containsKey(direction);
    }

}
