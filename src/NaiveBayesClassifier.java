import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;

import com.sun.javafx.geom.Vec2d;

public class NaiveBayesClassifier {

	List<Integer> legalLabels;
	String type;
	// k to be used for smoothing, set to 1 initially
	double k;

	// this flag decides whether k will have a default value or not
	boolean automaticTuning;

	/**
	 * Constructor to create a Naive Bayes Classifier
	 * 
	 * @param legalLabels
	 *            list of legal labels for this classifier
	 */
	public NaiveBayesClassifier(List<Integer> legalLabels) {
		this.legalLabels = legalLabels;
		this.type = "naivebayes";
		this.k = 1.0;
		this.automaticTuning = false;
	}

	/**
	 * Allows the smoothing constant to be set
	 * 
	 * @param k
	 *            the new smoothing constant
	 */
	public void setK(double k) {
		this.k = k;
	}

	/**
	 * Trains the data
	 * 
	 * @param trainingFeatures
	 * @param trainingLabels
	 * @param validationFeatures
	 * @param validationLabels
	 */
	public void train(List<Feature> trainingFeatures, List<Integer> trainingLabels, List<Feature> validationFeatures,
			List<Integer> validationLabels) {
		List<Double> kgrid = new ArrayList<Double>();
		
		if(automaticTuning){
			kgrid.add(0.001);
			kgrid.add(0.05);
			kgrid.add(0.1);
			kgrid.add(0.5);
			kgrid.add(1.0);
			kgrid.add(5.0);
			kgrid.add(10.0);
			kgrid.add(20.0);
			kgrid.add(50.0);
		}
		else{
			kgrid.add(k);
		}
		trainAndTune(trainingFeatures, trainingLabels, validationFeatures, validationLabels, kgrid);
	}

	public void trainAndTune(List<Feature> trainingFeatures, List<Integer> trainingLabels, List<Feature> validationFeatures,
			List<Integer> validationLabels, List<Double> kgrid) {

		//Get P(Y): prior distribution over labels
		List<Double> p_y = getPriorDistribution(trainingLabels);
		
		//Get distribution of features given labels for each label
		Vector<Vector<Vector<Double>>> p_F_given_y = getDistributionOfFeaturesGivenLabel(trainingFeatures, trainingLabels);
		
		//PRINT OUT PRIOR DISTRIBUTION & FEATURES 
		for(Double i:p_y){
			System.out.println(i);
		}
		for(Vector<Vector<Double>> grid: p_F_given_y){
			for(Vector<Double> row: grid){
				for(Double d :row){
					System.out.print(d.doubleValue()+ "     ");
				}
				System.out.println();
			}
			System.out.println();
			System.out.println();
			System.out.println();
			System.out.println();
		}
	}

	
	private List<Double> getPriorDistribution(List<Integer> trainingLabels) {
		List<Double> result = new ArrayList<Double>();
		
		for(int i=0;i<legalLabels.size();++i){
			result.add(0.0);
		}
		
		for(int i = 0;i<trainingLabels.size(); ++i)
		{
			result.set(trainingLabels.get(i), result.get(trainingLabels.get(i))+1.0);
		}
		for(int i =0; i<result.size();++i){
			result.set(i, result.get(i)/trainingLabels.size());
		}
		return result;
	}
	
	private Vector<Vector<Vector<Double>>> getDistributionOfFeaturesGivenLabel(List<Feature> features, List<Integer> labels){
		Vector<Vector<Vector<Double>>> result = new Vector<Vector<Vector<Double>>>();
		for (int labelvalue : legalLabels) {
			Vector<Vector<Double>> grid = new Vector<Vector<Double>>();
			//for each feature we find the probability it is 1 given the label
			for (int i = 0; i < features.get(0).getHeight(); ++i) {
				Vector<Double> row = new Vector<Double>();
				for (int j = 0; j < features.get(0).getWidth(); ++j) {
					int c_f_i_y = 0, c_f_prime_i_y = 0;
					//distribution of feature[i][j]
					for (int k = 0; k < features.size(); ++k) {

						if (labels.get(k) != labelvalue)
							continue;
						c_f_prime_i_y++;
						Feature Fi = features.get(k);
						if (Fi.getFeature(i, j) == 1) {
							c_f_i_y++;
						}
					}
					row.add(c_f_i_y * 1.0 / c_f_prime_i_y);
				}
				grid.addElement(row);
			}
			result.add(grid);
		}
		return result;
	}

}
