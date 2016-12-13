import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Vector;

public class NaiveBayesClassifier {

	List<Integer> legalLabels;
	String type;
	List<Double> p_y;
	Vector<Vector<Vector<Double>>> p_F_given_y;
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

		if (automaticTuning) {
			kgrid.add(0.001);
			kgrid.add(0.05);
			kgrid.add(0.1);
			kgrid.add(0.5);
			kgrid.add(1.0);
			kgrid.add(5.0);
			kgrid.add(10.0);
			kgrid.add(20.0);
			kgrid.add(50.0);
		} else {
			kgrid.add(k);
		}
		trainAndTune(trainingFeatures, trainingLabels, validationFeatures, validationLabels, kgrid);
	}

	public void trainAndTune(List<Feature> trainingFeatures, List<Integer> trainingLabels,
			List<Feature> validationFeatures, List<Integer> validationLabels, List<Double> kgrid) {

		// Get P(Y): prior distribution over labels
		p_y = getPriorDistribution(trainingLabels);

		// Get distribution of features given labels for each label
		p_F_given_y = getDistributionOfFeaturesGivenLabel(trainingFeatures, trainingLabels);

		// PRINT OUT PRIOR DISTRIBUTION & FEATURES
//		for (Double i : p_y) {
//			System.out.println(i);
//		}
//		for (Vector<Vector<Double>> grid : p_F_given_y) {
//			for (Vector<Double> row : grid) {
//				for (Double d : row) {
//					System.out.print(d.doubleValue() + "     ");
//				}
//				System.out.println();
//			}
//			System.out.println();
//			System.out.println();
//			System.out.println();
//			System.out.println();
//		}
	}
	
	public List<Integer> classify(List<Feature> data){
        List<Integer> guesses = new ArrayList<>();
        for(Feature datum : data) {
        	int answer = legalLabels.stream().max(Comparator.comparing(y->calculateLogJointProbabilities(datum, y))).get();
            guesses.add(answer);
        }
        return guesses;
    }

	
	private List<Double> getPriorDistribution(List<Integer> trainingLabels) {
		List<Double> result = new ArrayList<Double>();

		for (int i = 0; i < legalLabels.size(); ++i) {
			result.add(0.0);
		}

		for (int i = 0; i < trainingLabels.size(); ++i) {
			result.set(trainingLabels.get(i), result.get(trainingLabels.get(i)) + 1.0);
		}
		for (int i = 0; i < result.size(); ++i) {
			result.set(i, result.get(i) / trainingLabels.size());
		}
		return result;
	}

	private Vector<Vector<Vector<Double>>> getDistributionOfFeaturesGivenLabel(List<Feature> features,
			List<Integer> labels/*, double smoothing*/) {
		Vector<Vector<Vector<Double>>> result = new Vector<Vector<Vector<Double>>>();
		for (int labelvalue : legalLabels) {
			Vector<Vector<Double>> grid = new Vector<Vector<Double>>();
			// for each feature we find the probability it is 1 given the label
			for (int i = 0; i < features.get(0).getHeight(); ++i) {
				Vector<Double> row = new Vector<Double>();
				for (int j = 0; j < features.get(0).getWidth(); ++j) {
					int c_f_i_y = 0, c_f_prime_i_y = 0;
					// distribution of feature[i][j]
					for (int k = 0; k < features.size(); ++k) {

						if (labels.get(k) != labelvalue)
							continue;
						c_f_prime_i_y++;
						Feature Fi = features.get(k);
						if (Fi.getFeature(i, j) == 1) {
							c_f_i_y++;
						}
//						********* SMOOTHING NOT NECESSARY, REMOVED **************
//						 c_f_prime_i_y += smoothing; 
					}
					row.add((c_f_i_y/* + smoothing*/) * 1.0 / c_f_prime_i_y);
				}
				grid.addElement(row);
			}
			result.add(grid);
		}
		return result;
	}

	private Double calculateLogJointProbabilities(Feature feature, int y) {
			double a = Math.log(p_y.get(y));
			double b = 0.0;
			for (int i = 0; i < p_F_given_y.get(y).size(); ++i) {
				for (int j = 0; j<p_F_given_y.get(y).get(i).size(); ++j) {
					if(feature.getFeature(i, j)==1)
						b += Math.log(p_F_given_y.get(y).get(i).get(j));
					else
						b += Math.log(1 - p_F_given_y.get(y).get(i).get(j));
				}
			}
		return (a+b);
	}
	
	
//	****************** ARCHIVED CODE **************************************** 
//	private double getMostProbableLabel(Feature image, int label){
//		double result = 1.0;
//		for(int i = 0; i<image.getHeight(); ++i){
//			for(int j=0; j<image.getWidth(); ++j){
//				int f_i = image.getFeature(i, j);
//				if(f_i==1)
//					result *= p_F_given_y.elementAt(label).elementAt(i).elementAt(j);
//				else
//					result *= (1 - p_F_given_y.elementAt(label).elementAt(i).elementAt(j));
//			}
//		}
//		return result;
//	}
//	
//	private List<Double> calculateLogJointProbabilities(int label) {
//		List<Double> result = new ArrayList<Double>();
//		for (int y = 0; y < legalLabels.size(); y++) {
//			double a = Math.log(p_y.get(y));
//			double b = 0.0;
//			for (int i = 0; i < p_F_given_y.get(y).size(); ++i) {
//				for (Double d : p_F_given_y.get(y).get(i)) {
//					b += Math.log(d);
//				}
//			}
//			result.add(a + b);
//		}
//		return result;
//	}

}
