public class ParentChildRatioException extends Exception{

    ParentChildRatioException(int num){
        super("Ratio between number of parents and number of children must result in an even number of parents" +
                ", current number of parents: " + num);
    }
}
