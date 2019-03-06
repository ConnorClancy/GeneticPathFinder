import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class TournamentSelection implements Selector {

    private boolean ELITES_ENABLED;
    //    private Chromosome[] ELITES;
    private int CHILD_AMOUNT;

    TournamentSelection(int number_of_children_per_pair) {
        CHILD_AMOUNT = number_of_children_per_pair;
    }

//    @Override
//    public Chromosome[] produceSelection(Chromosome[] currentGen) {
//        ArrayList<Chromosome> selection = new ArrayList<>();
//
//        Random R = new Random();
//        Chromosome choice;
//        ArrayList<Chromosome> leftovers = new ArrayList<>();
//        float selectionThreshold;
//        float t;
//        int populationSize = currentGen.length;
//
//        //Check for odd population size or if number of parents produced will be odd,
//        // if so cut the last member out and pit it against a random member later on
//        while(populationSize % 2 == 1 || (populationSize/2) % 2 == 1) {
//            populationSize--;
//            leftovers.add(currentGen[populationSize]);
//        }
//
//        for(int i = 0; i < populationSize; i+=2){
//            selectionThreshold = currentGen[i].SCORE / (currentGen[i].SCORE + currentGen[i+1].SCORE + 0.0f);
//            System.out.println("1: " + currentGen[i].SCORE + "\t|| 2: " + currentGen[i+1].SCORE + "\t----> threshold: " + selectionThreshold);
//            if((t = R.nextFloat()) >= selectionThreshold)
//                choice = currentGen[i];
//            else
//                choice = currentGen[i+1];
//            System.out.println("choice = " + t);
//            selection.add(choice);
//        }
//
//        //If population was odd, find random winning candidate and apply tournament algorithm to the new pair
//        if(populationSize != currentGen.length) {
//            //System.out.println("debug: pop-" + populationSize + " | genLen-" + currentGen.length);
//            System.out.println("----odd number of population----");
//            for(Chromosome leftover : leftovers) {
//                Chromosome opponent = selection.get(R.nextInt(selection.size()));
//                selectionThreshold = opponent.SCORE / (opponent.SCORE + leftover.SCORE + 0.0f);
//                System.out.println("1: " + opponent.SCORE + "\t|| 2: " + leftover.SCORE + "\t----> threshold: " + selectionThreshold);
//                if ((t = R.nextFloat()) < selectionThreshold) {
//                    selection.remove(opponent);
//                    selection.add(leftover);
//                }
//                System.out.println("choice = " + t);
//            }
//        }
//
//        Collections.shuffle(selection);//shuffle to reduce coupling of groups through generations
//        return selection.toArray(new Chromosome[selection.size()]);
//    }

    @Override
    public Chromosome[] produceSelection(Chromosome[] currentGen) throws ParentChildRatioException{

        ArrayList<Chromosome> selection = new ArrayList<>();
        Chromosome[] current_batch = new Chromosome[CHILD_AMOUNT];
        System.out.println("Batchlen: " + current_batch.length);
        int number_of_parents = currentGen.length / CHILD_AMOUNT ;
        System.out.println("child amount: " +CHILD_AMOUNT + "| gen_len: " + currentGen.length);
        System.out.println("num-parents: " + number_of_parents);
        if(number_of_parents % 2 != 0){
            throw new ParentChildRatioException(number_of_parents);
        }
        int count = 0;
        while(selection.size() < number_of_parents){
            for(int i = 0; i < CHILD_AMOUNT; i++){
                current_batch[i] = currentGen[count + i];
            }
            selection.add(tournament(current_batch));
            count = count + CHILD_AMOUNT;
        }
        return selection.toArray(new Chromosome[selection.size()]);
    }

    @Override
    public void setElitism(boolean selection) {
        ELITES_ENABLED = selection;
    }

    /*
    Take batch of children and tournament them against each other, return one to be a parent to the next generation.
    Reciprocal of score taken to make smaller scores more of a priority.
     */
    private Chromosome tournament(Chromosome[] batch) {
        double total_batch_score = 0;
        for (Chromosome c : batch) {
            total_batch_score += 1.0/c.SCORE;
            System.out.println(c.SCORE + " -> " + 1.0/c.SCORE);
        }
        System.out.println("batch score: " + total_batch_score);
        Random R = new Random();
        double choice = R.nextDouble() * total_batch_score;
        System.out.println("choice: " + choice);
        int count = 0;
        double cumulative_measure = 1.0/batch[0].SCORE;
        while (count < batch.length - 1) {
            System.out.println("current cumu: " + cumulative_measure);
            if (choice <= cumulative_measure)
                return batch[count];
            cumulative_measure += 1.0/batch[++count].SCORE;
        }
        return batch[batch.length - 1];
    }


}
