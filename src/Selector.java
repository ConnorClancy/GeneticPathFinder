public interface Selector {

    Chromosome[] produceSelection(Chromosome[] currentGen);
    void setElitism(boolean selection);
}
