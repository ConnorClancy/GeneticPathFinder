import java.awt.*;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.Random;

public class Main {

    private static int GRIDSIZE = 16;

    public static void main(String[] args) throws Exception{

        NodeLocation[][] fieldMatrix = new NodeLocation[GRIDSIZE][GRIDSIZE];

        System.out.println(fieldMatrix.length);
        /*
        Initialisation of Field of which the algorithm works, creating nodes and situating them and their surroundings
         */
        for(int i = 0; i < GRIDSIZE; i++){
            for(int j = 0; j < GRIDSIZE; j++){
                fieldMatrix[i][j] = new NodeLocation(false, i, j);
                fieldMatrix[i][j].setPhysicalPosition(100 + (i * 25), 100 + (j * 25));
            }
        }
        Field F = new Field(GRIDSIZE, fieldMatrix);

        /*
        Find neighbours of each node which are available to travel to
         */
        String[] directions = { "NorthWest","West","SouthWest",
                                "North","South","NorthEast",
                                "East","SouthEast"};
        int dirCount;
        for(int i = 0; i < GRIDSIZE; i++){
            for(int j = 0; j < GRIDSIZE; j++){
                dirCount = 0;
                for(int x = -1; x <= 1; x++){
                    for(int y = -1; y <= 1; y++){
                        if(x==0 && y==0) {
                            continue;
                        }
                        if ((i + x >= 0 && i + x < F.SIZE) && (j + y >= 0 && j + y < F.SIZE))
                            fieldMatrix[i][j].addNeighbour(directions[dirCount], fieldMatrix[i + x][j + y]);
                        dirCount++;
                    }
                }
            }
        }

        Chromosome[] firstGroup = new Chromosome[15];
        Chromosome currentMember;
        F.setTitle("generation 1");
        for (int i = 0; i < 15; i++){
            currentMember = new Chromosome();
            currentMember.createPath(fieldMatrix);
            for(NodeLocation curr : currentMember.PATH){
                F.colour(curr.GRID_X, curr.GRID_Y, currentMember.PATH_COLOUR);
//                System.out.println(curr.COLUMN + " , " + curr.ROW);
//                Thread.sleep(50);
            }
            System.out.println(i+1 + ": " + currentMember.SCORE);
            firstGroup[i] = currentMember;
            Thread.sleep(300);
            F.update(F.getGraphics());
        }

        Selector selector = new TournamentSelection();
        selector.setElitism(false);
        Chromosome[] newGen = selector.produceSelection(firstGroup);

//        F.setTitle("parents");
//        System.out.println("----- new selection -----");
//        for(Chromosome c : newGen ){
//            System.out.println(c.SCORE);
//            for(NodeLocation curr : c.PATH){
//                F.colour(curr.GRID_X, curr.GRID_Y, c.PATH_COLOUR);
//            }
//            Thread.sleep(300);
//            F.update(F.getGraphics());
//        }

        for(NodeLocation n : newGen[0].PATH){
            System.out.print(n.getName() + " | ");
        }
        System.out.println("\n____________");
        for(NodeLocation n : newGen[1].PATH){
            System.out.print(n.getName() + " | ");
        }
        crossPollinate(newGen[0], newGen[1], 2);
    }

    static Chromosome[] crossPollinate(Chromosome par1, Chromosome par2, int number_of_children) {
        /*
        for number_of_children times
            create path P
            currentPath = par1.PATH
            crossPath = par2.PATH
            while(currentPath has next) do
                P.add(currentPath.getNext)
                if( currentPath intersects otherPath )
                    50/50 chance swap(currentPath, otherPath)
            end while
            children_array[i] = new Chromosome(P)
        end for
        return children_array
         */
        Chromosome[] children = new Chromosome[number_of_children];
        Random R = new Random();
        int choice;
        LinkedList<NodeLocation> currentPath = par1.PATH;
        LinkedList<NodeLocation> crossPath = par2.PATH;
        LinkedList<NodeLocation> tempPath;
        ListIterator<NodeLocation> tempIterator;
        for (int i = 0; i < number_of_children; i++) {
            ListIterator<NodeLocation> currentIterator = par1.PATH.listIterator();
            ListIterator<NodeLocation> crossIterator = par2.PATH.listIterator();
            LinkedList<NodeLocation> P = new LinkedList<>();
            NodeLocation curr, closest_point_marker = new NodeLocation(false, 0,0);
            int closest = 100;
            while(currentIterator.hasNext()){
                curr = currentIterator.next();
                System.out.println(curr.COLUMN + " , " + curr.ROW);
                P.add(curr);

                int currScore = Math.abs(curr.COLUMN - GRIDSIZE) + Math.abs(curr.ROW - GRIDSIZE);
                if(currScore <= closest){
                    closest = currScore;
                    closest_point_marker = curr;
                }
                if (crossPath.contains(curr)) {
                    int index = crossPath.indexOf(curr);
                    choice = R.nextInt(2);
                    System.out.println("Potential Switch");
                    if (choice == 0) {
                        System.out.println("actual Switch");
                        tempPath = crossPath;
                        crossPath = currentPath;
                        currentPath = tempPath;

                        while(index >= crossIterator.nextIndex()){
                            crossIterator.next();
                        }
                        while(index < crossIterator.nextIndex()){
                            crossIterator.previous();
                        }
                        crossIterator.next();
                        tempIterator = crossIterator;
                        crossIterator = currentIterator;
                        currentIterator = tempIterator;
                    }
                }
            }
            for(NodeLocation n : P){
                System.out.print(n.getName()+ " | ");
            }
            Chromosome child = new Chromosome(P, GRIDSIZE, Color.RED, closest_point_marker);
            children[i] = child;
        }
        return children;
    }
}
