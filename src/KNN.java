import java.util.List;

/**
 * Created by abhi on 12/12/16.
 */
public class KNN {

    private int[] legalLabels; //legal face or digit values (0,1 for face) (0....9 for digits)
    private int k;

    KNN(int[] legalLabels, int k){
        this.legalLabels = legalLabels;
        this.k = k;
    }

    void train(List<Feature> trainingData, List<Integer> trainingLabels){

    }
}
