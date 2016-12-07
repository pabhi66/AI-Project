/**
 * Created by abhi on 12/6/16.
 */
public class Image {
    private final int WIDTH;
    private final int HEIGHT;
    private char[][] image;

    //*************************************************
    //Image constructor
    //creates a new image from given height or width, and data
    //*************************************************
    public Image(char[][]data, int width, int height) {
        WIDTH = width;
        HEIGHT = height;
        if(data == null) {
            this.image = new char[height][width];
        } else {
            this.image = data;
        }
    }

    //*************************************************
    //Return the pixel from the image
    //*************************************************
    public char getPixel(int row, int column) {
        return image[row][column];
    }

    //*************************************************
    //returns the entire image with all pixels
    //*************************************************
    public char[][] getPixels() {
        return image;
    }

    //*************************************************
    //Get features of the digits
    //*************************************************
    public Feature getFeatureDigit() {
        Feature feature = new Feature(HEIGHT, WIDTH);
        for(int i = 0; i < HEIGHT; i++) {
            for(int j = 0; j < WIDTH; j++) {
                if(getPixel(i,j) == '+') {
                    feature.setFeature(i, j, 1);
                } else if(getPixel(i,j) == '#'){
                    feature.setFeature(i, j, 2);
                } else {
                    feature.setFeature(i, j, 0);
                }
            }
        }
        return feature;
    }

    //*************************************************
    //Get features of the faces
    //*************************************************
    public Feature getFeatureFace() {
        Feature feature = new Feature(HEIGHT, WIDTH);
        for(int i = 0; i < HEIGHT; i++) {
            for(int j = 0; j < WIDTH; j++) {
                if(getPixel(i,j) == '#') {
                    feature.setFeature(i, j, 1);
                } else {
                    feature.setFeature(i, j, 0);
                }
            }
        }
        return feature;
    }

}

