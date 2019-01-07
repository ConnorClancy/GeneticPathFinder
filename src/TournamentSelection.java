import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class TournamentSelection implements Selector{

    private boolean ELITES_ENABLED;
    private Chromosome[] ELITES;

    @Override
    public Chromosome[] produceSelection(Chromosome[] currentGen) {
        ArrayList<Chromosome> selection = new ArrayList<>();

        if(ELITES_ENABLED){
            //find top 5% of genes and save them separately, rounded to nearest int;
            int eliteCount = (currentGen.length * 5)/100;
            if(eliteCount == 0)
                eliteCount = 1;//if sample size is too small guarantee keeping one elite
            System.out.println("elites: " + eliteCount);
            ELITES = new Chromosome[eliteCount];
            ArrayList<Chromosome> tempList = new ArrayList<>();
            Collections.addAll(tempList, currentGen);
            Collections.sort(tempList);
            for(int i = 0; i < ELITES.length; i++){
                ELITES[i] = tempList.get(i);
            }
            System.out.println("--- elites ---");
            for(Chromosome c : ELITES){
                System.out.println(c.SCORE);
            }
            /*
            Need to remove elites from currentGen so that they do not appear multiple times in output.
             */
        }

        Random R = new Random();
        Chromosome choice, leftover = new Chromosome();
        float selectionThreshold;
        float t;
        int populationSize = currentGen.length;

        //Check for odd population size, if so cut the last member out and pit it against a random member later on
        if(populationSize % 2 == 1) {
            populationSize--;
            leftover = currentGen[populationSize];
        }

        for(int i = 0; i < populationSize; i+=2){
            selectionThreshold = currentGen[i].SCORE / (currentGen[i].SCORE + currentGen[i+1].SCORE + 0.0f);
            System.out.println("1: " + currentGen[i].SCORE + "\t|| 2: " + currentGen[i+1].SCORE + "\t----> threshold: " + selectionThreshold);
            if((t = R.nextFloat()) >= selectionThreshold)
                choice = currentGen[i];
            else
                choice = currentGen[i+1];
            System.out.println("choice = " + t);
            selection.add(choice);
        }

        //If population was odd, find random winning candidate and apply tournament algorithm to the new pair
        if(populationSize != currentGen.length) {
            System.out.println("----odd number of population----");
            Chromosome opponent = selection.get(R.nextInt(selection.size()));
            selectionThreshold = opponent.SCORE / (opponent.SCORE + leftover.SCORE + 0.0f);
            System.out.println("1: " + opponent.SCORE + "\t|| 2: " + leftover.SCORE + "\t----> threshold: " + selectionThreshold);
            if((t = R.nextFloat()) < selectionThreshold){
                selection.remove(opponent);
                selection.add(leftover);
            }
            System.out.println("choice = " + t);
        }

        Collections.shuffle(selection);//shuffle to reduce coupling of groups through generations
        return selection.toArray(new Chromosome[currentGen.length/2]);
    }

    @Override
    public void setElitism(boolean selection) {
        ELITES_ENABLED = selection;
    }
}
