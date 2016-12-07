/**
 * Created by abhi on 12/6/16.
 */
public class Feature {
    //feature matrix
    private int[][] feature;

    //*************************************************
    //Feature Constructor
    //create new feature given number of rows and column
    //*************************************************
    public Feature(int row, int col) {
        feature = new int[row][col];
    }

    //*************************************************
    //Set feature value
    //*************************************************
    public void setFeature(int x, int y, int val) {
        feature[x][y] = val;
    }

    //*************************************************
    //Return specific feature
    //*************************************************
    public int getFeature(int x, int y) {
        return feature[x][y];
    }

    //*************************************************
    //Return width of the feature object
    //*************************************************
    public int getWidth() {
        return feature[0].length;
    }

    //*************************************************
    //Return height of the feature object
    //*************************************************
    public int getHeight() {
        return feature.length;
    }

}
