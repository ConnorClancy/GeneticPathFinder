public class Main {
    public static void main(String[] args) throws Exception{

        int GRIDSIZE = 16;
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

        F.setTitle("parents");
        System.out.println("----- new selection -----");
        for(Chromosome c : newGen ){
            System.out.println(c.SCORE);
            for(NodeLocation curr : c.PATH){
                F.colour(curr.GRID_X, curr.GRID_Y, c.PATH_COLOUR);
            }
            Thread.sleep(300);
            F.update(F.getGraphics());
        }

    }

}
