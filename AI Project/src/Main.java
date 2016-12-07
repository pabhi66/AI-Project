import com.sun.scenario.effect.ImageData;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {


    /**
     * Main class that runs the program
     * @param args takes no arguments
     */
    public static void main(String[] args){
        //*************************************************
        //Ask user for training percentage
        //this percentage will be used to train the classifier
        //*************************************************
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter (1-100)% of data points that you want reserve for training ");
        System.out.println("==========================================================");
        System.out.print("Enter %: ");
        int percent = scanner.nextInt();
        while(percent <= 0 || percent > 100){
            System.out.println("Illegal input. Please enter correct value from 1 - 100");
            System.out.print("Enter %: ");
            percent = scanner.nextInt();
        }

        //*************************************************
        //Ask user, which classifier he/she wants to use
        //*************************************************
        System.out.println("==========================================================");
        System.out.println("Please choose a Classifier (enter 1,2,or 3)");
        System.out.println("1) Perceptron Classifier");
        System.out.println("2) NaiveBayes Classifier");
        System.out.println("3) custom Classifier");
        System.out.print("Enter Classifier#: ");
        int classifier = scanner.nextInt();
        while(classifier < 0 || classifier > 3){
            System.out.println("Illegal selection. Please enter 1,2,or 3");
            System.out.print("Enter Classifier#: ");
            classifier = scanner.nextInt();
        }

        //*************************************************
        //Set the training percentage for digit and face, we have 5000 digits and 500 images to train in the given data
        //*************************************************
        int trainingDigitPercent = (int) ((double) percent /100 * 5000);
        int trainingFacePercent = (int) ((double) percent /100 * 500);


        //*************************************************
        //Run the classifier based on user input
        //*************************************************
        runClassifiers(classifier, trainingDigitPercent, trainingFacePercent);
    }

    /**
     * Read data from the files and Run the classifier based on user input
     * @param classifier classifier
     * @param trainingDigitPercent training percentage of digits
     * @param trainingFacePercent training percentage of faces
     */
    private static void runClassifiers(int classifier, int trainingDigitPercent, int trainingFacePercent){

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
            run________Digit();
            run________Image();
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
        List<Integer> testLabelsDigit = loadLabels("data/digitdata/testlabels", 1000);
        List<Integer> trainingLabelsDigit = loadLabels("data/digitdata/traininglabels", trainingDigit);
        List<Integer> validationLabelsDigit = loadLabels("data/digitdata/validationlabels", 1000);

        //*************************************************
        //Read face data and labels from the files
        //*************************************************
        List<Image> testDataImage = loadData("data/facedata/facedatatest", 150, 60, 70);
        List<Image> trainingDataImage = loadData("data/facedata/facedatatrain", trainingFace, 60, 70);
        List<Image> validationDataImage = loadData("data/facedata/facedatavalidation", 301, 60, 70);
        List<Integer> testLabelsImage = loadLabels("data/facedata/facedatatestlabels", 150);
        List<Integer> trainingLabelsImage = loadLabels("data/facedata/facedatatrainlabels", trainingFace);
        List<Integer> validationLabelsImage = loadLabels("data/facedata/facedatavalidationlabels", 301);

        //*************************************************
        //Extract digit features of the digits
        //*************************************************
        toFeatures(trainingDataDigit, 0);
        toFeatures(validationDataDigit, 0);
        toFeatures(testDataDigit, 0);
        int[] legalDigits = new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9};


        //*************************************************
        //Extract face features of the faces
        //*************************************************
//        toFeatures(trainingDataImage, 1);
//        toFeatures(validationDataImage, 1);
//        toFeatures(testDataImage, 1);
        int[] legalImages = new int[]{0, 1};
    }

    private static void runPerceptronClassifierDigit(){

    }

    private static void runPerceptronClassifierImage(){

    }

    private static void runNaiveBayesClassifierDigit(){

    }

    private static void runNaiveBayesClassifierImage(){

    }

    private static void run________Digit(){

    }
    private static void run________Image(){

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
                char[][] img = image.getPixels();
                for (char[] anImg : img) {
                    for (int l = 0; l < img[0].length; l++) {
                        System.out.print(anImg[l]);
                    }
                    System.out.println("");
                }
                System.out.println("================");

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
    private static List<Feature> toFeatures(List<Image> data, int type) {
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
