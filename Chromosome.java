import java.awt.*;
import java.util.LinkedList;
import java.util.Random;

public class Chromosome {

    Color PATH_COLOUR;
    int SCORE;
    LinkedList<NodeLocation> PATH = new LinkedList<>();

    //used for initial population space
    public Chromosome(){
        Random r = new Random();
        PATH_COLOUR = new Color(r.nextFloat(), r.nextFloat(), r.nextFloat());
    }

    //used once cross-pollination has occurred to set the colour of the path to be a mixture of the parent's colours
    public Chromosome(Color colour){
        PATH_COLOUR = colour;
    }

    private void fitnessCalc(){
        SCORE = 10;
    }

    public void setPath(LinkedList<NodeLocation> L){
        PATH = L;
        fitnessCalc();
    }

    /*
    Creates paths which make up the initial population.
    Begins at location 0,0 and selects one of its 3 neighbours
    Randomly selects neighbour of new current location until
        A) Hits target location, or
        B) Hits itself and cannot continue creating new paths
     */
    public void createPath(NodeLocation[][] fieldMatrix){
        PATH.addFirst(fieldMatrix[0][0]);
        NodeLocation curr = PATH.getFirst();
        NodeLocation next;
        String[] neighbours;
        while(curr.COLUMN != fieldMatrix.length-1 || curr.ROW != fieldMatrix.length-1){
            neighbours = curr.traversableNeighbours.keySet().toArray(new String[0]);
            Random r = new Random();
            int selection = r.nextInt(neighbours.length);

            next = curr.traversableNeighbours.get(neighbours[selection]);
            int stuckCount = 0;
            while(PATH.contains(next)){
                next = curr.traversableNeighbours.get(neighbours[(selection++)%neighbours.length]);
                stuckCount++;
                if(stuckCount >= neighbours.length){
                    SCORE = 100;
                    return;
                }
            }
            curr = next;
            PATH.add(curr);
        }
        fitnessCalc();
    }

}
