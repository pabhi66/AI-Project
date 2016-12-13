import com.sun.scenario.effect.ImageData;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Main {


    private static int[] legalDigits = null;
    private static int[] legalImages = null;
    private static long start;
    private static long end;
    private static List<Feature> trainingFeaturesDigit = null;
    private static List<Feature> validationFeaturesDigit = null;
    private static List<Feature> testFeaturesDigit = null;

    private static List<Feature> trainingFeaturesFace = null;
    private static List<Feature> validationFeaturesFace = null;
    private static List<Feature> testFeaturesFace = null;

    private static List<Integer> testLabelsDigit = null;
    private static List<Integer> trainingLabelsDigit = null;
    private static List<Integer> validationLabelsDigit = null;

    private static List<Integer> testLabelsImage = null;
    private static List<Integer> trainingLabelsImage = null;
    private static List<Integer> validationLabelsImage = null;


    /**
     * Main class that runs the program
     * @param args takes no arguments
     */
    public static void main(String[] args){

        Scanner scanner = new Scanner(System.in);

        //*************************************************
        //Ask user, which classifier he/she wants to use
        //*************************************************
        System.out.println("Please choose a Classifier (enter 1,2,or 3)");
        System.out.println("1) Perceptron Classifier");
        System.out.println("2) Naive Bayes Classifier");
        System.out.println("3) KNN Classifier");
        System.out.print("Enter Classifier#: ");
        int classifier = scanner.nextInt();
        while(classifier < 0 || classifier > 3){
            System.out.println("Illegal selection. Please enter 1,2,or 3");
            System.out.print("Enter Classifier#: ");
            classifier = scanner.nextInt();
        }
        int k=-1;
        if(classifier == 3){
            System.out.println("Please enter (1-100)% of neighbors to use");
            k = scanner.nextInt();
            while(k <= 0 || k > 100){
                System.out.println("Illegal entry. Please enter from 1-100");
                k = scanner.nextInt();
            }
        }


        //*************************************************
        //Ask user for training percentage
        //this percentage will be used to train the classifier
        //*************************************************
        System.out.println("==========================================================");
        System.out.println("Enter (1-100)% of data points that you want reserve for training ");
        System.out.print("Enter %: ");
        int percent = scanner.nextInt();
        while(percent <= 0 || percent > 100){
            System.out.println("Illegal input. Please enter correct value from 1 - 100");
            System.out.print("Enter %: ");
            percent = scanner.nextInt();
        }

        //*************************************************
        //Set the training percentage for digit and face, we have 5000 digits and 500 images to train in the given data
        //*************************************************
        int trainingDigitPercent = (int) ((double) percent /100 * 5000);
        int trainingFacePercent = (int) ((double) percent /100 * 500);


        //*************************************************
        //Run the classifier based on user input
        //*************************************************
        runClassifiers(classifier, trainingDigitPercent, trainingFacePercent,k);
    }

    /**
     * Read data from the files and Run the classifier based on user input
     * @param classifier classifier
     * @param trainingDigitPercent training percentage of digits
     * @param trainingFacePercent training percentage of faces
     */
    private static void runClassifiers(int classifier, int trainingDigitPercent, int trainingFacePercent, int k){

        //*************************************************
        //Read all the data from the files and extract the features
        //*************************************************
        readData(trainingDigitPercent, trainingFacePercent);

        //*************************************************
        //Run Perceptron Classifier
        //*************************************************
        if(classifier == 1){
            runPerceptronClassifierDigit();
            runPerceptronClassifierImage();
        }
        //*************************************************
        //Run NaiveBayes Classifier
        //*************************************************
        else if(classifier == 2){
            runNaiveBayesClassifierDigit();
            runNaiveBayesClassifierImage();
        }
        //*************************************************
        //Run __________ Classifier
        //*************************************************
        else if(classifier == 3){
            runKNNDigit(k);
            runKNNImage(k);
        }


    }

    /**
     * read data from the files
     * @param trainingDigit training digit percentage
     * @param trainingFace training faces percentage
     */
    private static void readData(int trainingDigit, int trainingFace){
        //*************************************************
        //Read digit data and labels from the files
        //*************************************************
        List<Image> testDataDigit = loadData("data/digitdata/testimages", 1000, 28, 28);
        List<Image> trainingDataDigit = loadData("data/digitdata/trainingimages", trainingDigit, 28, 28);
        List<Image> validationDataDigit = loadData("data/digitdata/validationimages", 1000, 28, 28);
        testLabelsDigit = loadLabels("data/digitdata/testlabels", 1000);
        trainingLabelsDigit = loadLabels("data/digitdata/traininglabels", trainingDigit);
        validationLabelsDigit = loadLabels("data/digitdata/validationlabels", 1000);

        //*************************************************
        //Read face data and labels from the files
        //*************************************************
        List<Image> testDataImage = loadData("data/facedata/facedatatest", 150, 60, 70);
        List<Image> trainingDataImage = loadData("data/facedata/facedatatrain", trainingFace, 60, 70);
        List<Image> validationDataImage = loadData("data/facedata/facedatavalidation", 301, 60, 70);
        testLabelsImage = loadLabels("data/facedata/facedatatestlabels", 150);
        trainingLabelsImage = loadLabels("data/facedata/facedatatrainlabels", trainingFace);
        validationLabelsImage = loadLabels("data/facedata/facedatavalidationlabels", 301);

        //*************************************************
        //Extract digit features of the digits
        //*************************************************
        trainingFeaturesDigit = getFeatures(trainingDataDigit, 0);
        validationFeaturesDigit = getFeatures(validationDataDigit, 0);
        testFeaturesDigit = getFeatures(testDataDigit, 0);
        legalDigits = new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9};


        //*************************************************
        //Extract face features of the faces
        //*************************************************
        trainingFeaturesFace = getFeatures(trainingDataImage, 1);
        validationFeaturesFace = getFeatures(validationDataImage, 1);
        testFeaturesFace = getFeatures(testDataImage, 1);
        legalImages = new int[]{0, 1};
    }

    private static void runPerceptronClassifierDigit(){
        System.out.println("======================================");
        System.out.println("Running Perceptron Classifier on Digits");
        Perceptron perceptron = new Perceptron(legalDigits);
        start = System.currentTimeMillis();
        perceptron.train(trainingFeaturesDigit,trainingLabelsDigit);
        end = System.currentTimeMillis();
        List<Integer> checkResult = perceptron.classify(testFeaturesDigit);


        int errors = 0;
        int totalImages = testLabelsDigit.size();

        for(int i = 0; i < checkResult.size(); i++){
            int result = checkResult.get(i);
            if(result != testLabelsDigit.get(i))
                errors++;
        }

        System.out.println("**RESULT OF PERCEPTRON CLASSIFIER ON DIGITS**");
        System.out.println("Error rate: " + ((double) errors/testLabelsDigit.size()) + " " );
        System.out.println("Accuracy: " + ((double) (testLabelsDigit.size() - errors) / testLabelsDigit.size() ));
        System.out.println("Number of Errors: " + errors + " out of " + testLabelsDigit.size());
        System.out.println("Total training time: " + (end - start) + "ms");


    }

    private static void runPerceptronClassifierImage(){
        System.out.println("======================================");
        System.out.println("Running Perceptron Classifier on Faces");
        Perceptron perceptron = new Perceptron(legalImages);
        start = System.currentTimeMillis();
        perceptron.train(trainingFeaturesFace,trainingLabelsImage);
        end = System.currentTimeMillis();
        List<Integer> checkResult = perceptron.classify(testFeaturesFace);


        int errors = 0;
        for(int i = 0; i < checkResult.size(); i++){
            int result = checkResult.get(i);
            if(result != testLabelsImage.get(i))
                errors++;
        }

        System.out.println("**RESULT OF PERCEPTRON CLASSIFIER ON Images**");
        System.out.println("Error rate: " + ((double) errors/testLabelsImage.size()) + " " );
        System.out.println("Accuracy: " + ((double) (testLabelsImage.size() - errors) / testLabelsImage.size() ));
        System.out.println("Number of Errors: " + errors + " out of " + testLabelsImage.size());
        System.out.println("Total training time: " + (end - start) + "ms");
    }

    private static void runNaiveBayesClassifierImage(){
    	System.out.println("======================================");
        System.out.println("Running Naive Bayes Classifier on Faces");
        NaiveBayesClassifier bayes = new  NaiveBayesClassifier(IntStream.of(legalImages).boxed().collect(Collectors.toList()));
        start = System.currentTimeMillis();
        bayes.train(trainingFeaturesFace, trainingLabelsImage, validationFeaturesFace, validationLabelsImage);
        end = System.currentTimeMillis();
        List<Integer> checkResult = bayes.classify(testFeaturesFace);


        int errors = 0;
        int totalImages = testLabelsImage.size();

        for(int i = 0; i < checkResult.size(); i++){
            int result = checkResult.get(i);
            if(result != testLabelsImage.get(i))
                errors++;
        }

        System.out.println("**RESULT OF BAYES CLASSIFIER ON Images**");
        System.out.println("Error rate: " + ((double) errors/testLabelsImage.size()) + " " );
        System.out.println("Accuracy: " + ((double) (testLabelsImage.size() - errors) / testLabelsImage.size() ));
        System.out.println("Number of Errors: " + errors + " out of " + testLabelsImage.size());
        System.out.println("Total training time: " + (end - start) + "ms");
    }

    private static void runNaiveBayesClassifierDigit(){
    	System.out.println("======================================");
        System.out.println("Running Naive Bayes Classifier on Digits");
        NaiveBayesClassifier bayes = new  NaiveBayesClassifier(IntStream.of(legalDigits).boxed().collect(Collectors.toList()));
        start = System.currentTimeMillis();
        bayes.train(trainingFeaturesDigit, trainingLabelsDigit, validationFeaturesDigit, validationLabelsDigit);
        end = System.currentTimeMillis();
        List<Integer> checkResult = bayes.classify(testFeaturesDigit);


        int errors = 0;
        int totalImages = testLabelsDigit.size();

        for(int i = 0; i < checkResult.size(); i++){
            int result = checkResult.get(i);
            if(result != testLabelsDigit.get(i))
                errors++;
        }

        System.out.println("**RESULT OF BAYES CLASSIFIER ON Digits**");
        System.out.println("Error rate: " + ((double) errors/testLabelsDigit.size()) + " " );
        System.out.println("Accuracy: " + ((double) (testLabelsDigit.size() - errors) / testLabelsDigit.size() ));
        System.out.println("Number of Errors: " + errors + " out of " + testLabelsDigit.size());
        System.out.println("Total training time: " + (end - start) + "ms");

    }

    private static void runKNNDigit(int k){

    }
    private static void runKNNImage(int k){

    }

    /**
     * read the image data from the files
     * @param fileName file name
     * @param training number of training data
     * @param width width of an image or digit
     * @param height height of an image or digit
     * @return list of data in images or digit
     */
    private static List<Image> loadData(String fileName, int training, int width, int height){
        List<Image> imageData = new ArrayList<>();
        char[][] rawData;

        try {
            File file = new File(fileName);
            BufferedReader reader = new BufferedReader(new FileReader(file));

            String line = reader.readLine();
            for(int i = 0; i < training && line != null; i++) {
                if(line.isEmpty()){
                    continue;
                }
                rawData = new char[height][width];
                for(int j = 0; j < height; j++) {
                    char[] array = line.toCharArray();
                    rawData[j] = array;
                    line = reader.readLine();
                }
                Image image = new Image(rawData, width, height);
                imageData.add(image);

                //*************************************************
                //Printing the image to see everything is printed correctly
                //*************************************************
//                char[][] img = image.getPixels();
//                for (char[] anImg : img) {
//                    for (int l = 0; l < img[0].length; l++) {
//                        System.out.print(anImg[l]);
//                    }
//                    System.out.println("");
//                }
//                System.out.println("================");

            }
            reader.close();
        } catch(IOException e) {
            System.out.println("No File of the given name found!");
            System.exit(-1);
        }

        return imageData;
    }

    /**
     * read the labels from the files
     * @param fileName file name
     * @param training number of training data
     * @return list of actual result
     */
    private static List<Integer> loadLabels(String fileName, int training){
        List<Integer> result = new ArrayList<>();

        BufferedReader reader;

        try {
            File file = new File(fileName);
            reader = new BufferedReader(new FileReader(file));
            String line = reader.readLine();
            while (line != null && training > 0) {
                if (Integer.parseInt(line) >= 0 && Integer.parseInt(line) <= 9) {
                    result.add(Integer.parseInt(line));
                }
                line = reader.readLine();
                training--;
            }
            reader.close();
        }
        catch(IOException e) {
            System.out.println("No File of the given name found!");
            System.exit(-1);
        }
        return result;
    }

    /**
     * return list of features from the images
     * @param data list of images data
     * @param type data type: 1=face, 0=digit
     * @return list of features extracted from the images
     */
    // convert the original Image type data to Feature that uses in the classifiers
    private static List<Feature> getFeatures(List<Image> data, int type) {
        List<Feature> features = new ArrayList<>();
        for(Image image : data) {
            if(type == 0) {
                features.add(image.getFeatureDigit());
            } else if(type == 1) {
                features.add(image.getFeatureFace());
            }
        }
        return features;
    }

}
