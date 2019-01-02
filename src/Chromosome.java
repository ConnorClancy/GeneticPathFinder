import java.awt.*;
import java.util.LinkedList;
import java.util.Random;

public class Chromosome {

    Color PATH_COLOUR;
    int SCORE;
    LinkedList<NodeLocation> PATH = new LinkedList<>();
    NodeLocation CLOSEST_POINT;

    //used for initial population space
    public Chromosome(){
        Random r = new Random();
        PATH_COLOUR = new Color(r.nextFloat(), r.nextFloat(), r.nextFloat());
    }

    //used once cross-pollination has occurred to set the colour of the path to be a mixture of the parent's colours
    public Chromosome(Color colour){
        PATH_COLOUR = colour;
    }

    /*
    Fitness is based off of length of the path, how close it ended to the goal and how close the path
    was at its closest point. Multiplication is used for the destination distance and closest point distance
    instead of addition in order to prioritise the goal state over length in the early stages and path
    length over closeness to goal in later stages (as that should reduce to 0 when all of the current generation
    reach the goal node and optimisation becomes more concerning).
     */
    private void fitnessCalc(int fieldSize){
        SCORE = PATH.size() +
                Math.abs(PATH.getLast().COLUMN - fieldSize) + Math.abs(PATH.getLast().ROW - fieldSize) *
                Math.abs(CLOSEST_POINT.COLUMN - fieldSize) + Math.abs(CLOSEST_POINT.ROW - fieldSize);
    }

    public void setPath(LinkedList<NodeLocation> L, int fieldSize){
        PATH = L;
        fitnessCalc(fieldSize);
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
        int temp;
        int closest = 100;

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
                    fitnessCalc(fieldMatrix.length);
                    return;
                }
            }
            curr = next;

            temp = Math.abs(curr.COLUMN - fieldMatrix.length) + Math.abs(curr.ROW - fieldMatrix.length);
            if(temp <= closest){
                closest = temp;
                CLOSEST_POINT = curr;
            }

            PATH.add(curr);
        }
        fitnessCalc(fieldMatrix.length);
    }

}