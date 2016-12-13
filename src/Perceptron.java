import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * Run Perceptron algorithm on faces and digits
 */
class Perceptron {
    private int[] legalLabels; //legal face or digit values (0,1 for face) (0....9 for digits)
    private double[] weights; //legal label weight/value
    private double[][][] imageWeight; //height, width, and legal labels for each image

    /**
     * perceptron constructor
     * @param legalLabels legal labels
     */
    Perceptron(int[] legalLabels){
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
    private void setWeights(int width, int length, int height){
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
        //initialize length, width, and height
        int length = legalLabels.length;
        int width = trainingData.get(0).getWidth();
        int height = trainingData.get(0).getHeight();

        //set the length, width, and height for all images
        setWeights(width, length, height);

        //maximum iterations to do in perceptron, can change it to do more
        int maxIterations = 20;

        //do iterations and learn the images
        for(int i = 0; i < maxIterations; i++) {
            for(int j = 0; j < trainingData.size(); j++) {

                //learn from training data
                calculateScores(trainingData.get(j));

                //get prediction
                int prediction = findHeightWeightFeatures(weights);

                //get the true value
                int truth = trainingLabels.get(j);

                //if prediction is not true then adjust truth and prediction data of an pixel
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
     * Classifies each image as the label that most closely matches the prototype vector
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
