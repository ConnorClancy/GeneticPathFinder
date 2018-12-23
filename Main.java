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

        /*
        Test to create 10 totally random paths through the field. Something similar
        will be used later as the first generation.
         */
        Chromosome firstGroup;

        for (int i = 0; i < 10; i++){
            firstGroup = new Chromosome();
            firstGroup.createPath(fieldMatrix);
            for(NodeLocation curr : firstGroup.PATH){
                F.colour(curr.GRID_X, curr.GRID_Y, firstGroup.PATH_COLOUR);
                System.out.println(curr.COLUMN + " , " + curr.ROW);
                Thread.sleep(50);
            }
            F.update(F.getGraphics());
        }
    }
}
