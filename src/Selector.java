public interface Selector {

    Chromosome[] produceSelection(Chromosome[] currentGen) throws ParentChildRatioException;
    void setElitism(boolean selection);
}
