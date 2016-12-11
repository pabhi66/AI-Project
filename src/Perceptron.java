import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * Run Perceptron algorithm on faces and digits
 */
class Perceptron {
    private int maxIterations; //number of iterations perceptron does
    private int[] legalLabels; //legal face or digit values (0,1 for face) (0....9 for digits)
    private double[] weights; //legal lable weight
    private double[][][] imageWeight; //height, width, and legale labels for each weight

    /**
     * perceptron constructor
     * @param maxIterations number of iterations Default = 20
     * @param legalLabels legal labels
     */
    Perceptron(int maxIterations, int[] legalLabels){
        this.maxIterations = maxIterations;
        this.legalLabels = legalLabels;
        setWeights();
    }

    /**
     * set weights for legal labels
     */
    private void setWeights(){
        weights = new double[legalLabels.length];
    }

    /**
     * set height, width, and length of an image
     * @param width width
     * @param length length
     * @param height height
     */
    private void initWeights(int width, int length, int height){
        imageWeight = new double[height][width][length];
        Random random = new Random();
        for(int y = 0; y < length; y++) {
            for(int i = 0; i < height; i++) {
                for(int j = 0; j < width; j++) {
                    imageWeight[i][j][y] = random.nextDouble();
                }
            }
        }
    }

    /**
     * train the faces or digits given train data
     * @param trainingData train data
     * @param trainingLabels train labels
     */
    void train(List<Feature> trainingData, List<Integer> trainingLabels){
        int height = trainingData.get(0).getHeight();
        int width = trainingData.get(0).getWidth();
        int length = legalLabels.length;
        initWeights(width, length, height);
        for(int i = 0; i < maxIterations; i++) {
            for(int j = 0; j < trainingData.size(); j++) {
                calculateScores(trainingData.get(j));
                int prediction = findHeightWeightFeatures(weights);
                int truth = trainingLabels.get(j);
                if(prediction != truth) {
                    for(int h = 0; h < height; h++) {
                        for(int w = 0; w < width; w++) {
                            imageWeight[h][w][truth] += trainingData.get(j).getFeature(h, w);
                            imageWeight[h][w][prediction] -= trainingData.get(j).getFeature(h, w);
                        }
                    }
                }
            }
        }
    }

    /**
     * Classifies each datum as the label that most closely matches the prototype vector
     * for that label.  See the project description for details.
     */
    List<Integer> classify(List<Feature> data){
        List<Integer> guesses = new ArrayList<>();
        for(Feature datum : data) {
            calculateScores(datum);
            guesses.add(findHeightWeightFeatures(weights));
        }
        return guesses;
    }

    /**
     * calculate the accuracy of the feature
     * @param feature feature of an image
     */
    private void calculateScores(Feature feature) {
        Arrays.fill(weights, 0);
        for(int y = 0; y < legalLabels.length; y++) {
            for(int i = 0; i < feature.getHeight(); i++) {
                for(int j = 0; j < feature.getWidth(); j++) {
                    weights[y] += imageWeight[i][j][y] * feature.getFeature(i, j);
                }
            }
        }
    }

    /**
     * Returns a list of the 100 features with the greatest weight for some label
     */
    private int findHeightWeightFeatures(double[] array){
        int maxIndex = 0;
        for(int i = 1; i < array.length; i++) {
            if(array[i] > array[maxIndex]) {
                maxIndex = i;
            }
        }
        return maxIndex;
    }
}
