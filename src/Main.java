import java.awt.*;
import java.util.LinkedList;
import java.util.Random;

public class Main {

    private static int GRIDSIZE = 16;
    private static int GROUPSIZE = 10;
    private static int NUMBER_OF_CHILDREN = 5;

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



        Chromosome[] firstGroup = new Chromosome[GROUPSIZE];
        Chromosome currentMember;
        F.setTitle("generation 1");
        for (int i = 0; i < GROUPSIZE; i++){
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



        Selector selector = new TournamentSelection(NUMBER_OF_CHILDREN);
        selector.setElitism(false);
        Chromosome[] Parents = selector.produceSelection(firstGroup);

        F.setTitle("parents");
        System.out.println("----- new selection -----");
        for(Chromosome c : Parents ){
            System.out.println(c.SCORE);
            for(NodeLocation curr : c.PATH){
                F.colour(curr.GRID_X, curr.GRID_Y, c.PATH_COLOUR);
            }
            Thread.sleep(300);
            F.update(F.getGraphics());
        }

        System.out.println("----- children -----");
        for(int i = 0; i < Parents.length; i+=2) {
//            for(NodeLocation n : Parents[i].PATH){
//            System.out.print(n.getName() + " | ");
//            }
//            System.out.println("\n____________");
//            for(NodeLocation n : Parents[i+1].PATH){
//                System.out.print(n.getName() + " | ");
//            }
//            System.out.println("\n");
            F.setTitle("children of " + i + " and " + (i+1));
            for (Chromosome c : crossPollinate(Parents[i], Parents[i+1], NUMBER_OF_CHILDREN)) {
                System.out.println(c.SCORE);
                for (NodeLocation curr : c.PATH) {
                    F.colour(curr.GRID_X, curr.GRID_Y, c.PATH_COLOUR);
                    Thread.sleep(20);
                }
                Thread.sleep(500);
                F.update(F.getGraphics());
            }
        }
        
    }

    private static Chromosome[] crossPollinate(Chromosome par1, Chromosome par2, int number_of_children) {
        Chromosome[] CHILDREN = new Chromosome[number_of_children];
        LinkedList[] PARENT = {par1.PATH, par2.PATH};
        int picker = 0;
        for(int i = 0; i < number_of_children; i++){
            LinkedList<NodeLocation> child = new LinkedList<>();
            LinkedList<NodeLocation> swapLocations = getSwapLocations(par1, par2);
            LinkedList<NodeLocation> currentParent = PARENT[0];
            //test begin
//            for(NodeLocation N: swapLocations){
//                if(par1.PATH.contains(N))
//                    System.out.println("Success for 1: " + N.getName());
//                else
//                    System.out.println("Failure for 1");
//
//                if(par2.PATH.contains(N))
//                    System.out.println("Success for 2");
//                else
//                    System.out.println("Failure for 2");
//            }
            //test end

            //...
//            NodeLocation selectionMarker = swapLocations.remove();
            int selectionStart = 0;
            while(!swapLocations.isEmpty()){
                int selectionEnd = currentParent.indexOf(swapLocations.remove());
                int currentLocation = selectionStart;
                while(currentLocation < selectionEnd){
                    child.add(currentParent.get(currentLocation++));
                }
                currentParent = PARENT[picker++%2];
                selectionStart = selectionEnd;
            }

            //...
            if(child.isEmpty())
                System.out.println("Child is empty apparently");

            NodeLocation closestPoint;
            if(child.contains(par1.CLOSEST_POINT) && child.contains(par2.CLOSEST_POINT)){
                closestPoint = max(par1.CLOSEST_POINT, par2.CLOSEST_POINT);
            }
            else if (child.contains(par1.CLOSEST_POINT)){
                closestPoint = par1.CLOSEST_POINT;
            }
            else {
                closestPoint = par2.CLOSEST_POINT;
            }
            CHILDREN[i] = new Chromosome(child, GRIDSIZE, mixColour(par1, par2), closestPoint);
        }
        return CHILDREN;
    }

    static Color mixColour(Chromosome par1, Chromosome par2){
        Random R = new Random();
        int offsetR = R.nextInt(40) - 20;
        int offsetG = R.nextInt(20) - 20;
        int offsetB = R.nextInt(20) - 20;
        return new Color(Math.abs((par1.PATH_COLOUR.getRed() + par2.PATH_COLOUR.getRed())/2 + offsetR)%255 + 1,
                Math.abs((par1.PATH_COLOUR.getGreen() + par2.PATH_COLOUR.getGreen())/2 + offsetG)%255 + 1,
                Math.abs((par1.PATH_COLOUR.getBlue() + par2.PATH_COLOUR.getBlue())/2 + offsetB)%255 + 1);
    }

    private static LinkedList<NodeLocation> getSwapLocations(Chromosome parent1, Chromosome parent2){
        LinkedList<NodeLocation> swapLocations = new LinkedList<>();
        Random R = new Random();
        for(NodeLocation N : parent1.PATH){
            if(parent2.PATH.contains(N) && R.nextInt(2) == 0 && !swapLocations.contains(N)){
                swapLocations.add(N);
            }
        }
        return swapLocations;
    }

    private static NodeLocation max(NodeLocation location1, NodeLocation location2){
        if(location1.ROW + location1.COLUMN >= location2.ROW + location2.COLUMN){
            return location1;
        }
        else{
            return location2;
        }

    }
}
