import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
	 * @param trainingData
	 * @param trainingLabels
	 * @param validationData
	 * @param validationLabels
	 */
	public void train(List<Image> trainingData, List<Integer> trainingLabels, List<Image> validationData,
			List<Image> validationLabels) {
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
		trainAndTune(trainingData, trainingLabels, validationData, validationLabels, kgrid);
	}

	public void trainAndTune(List<Image> trainingData, List<Integer> trainingLabels, List<Image> validationData,
			List<Image> validationLabels, List<Double> kgrid) {

		//Get P(Y): prior distribution over labels
		List<Double> p_y = getPriorDistribution(trainingLabels);
		
		//PRINT OUT PRIOR DISTRIBUTION 
		for(Double i:p_y){
			System.out.println(i);
		}
	}

	
	private List<Double> getPriorDistribution(List<Integer> trainingLabels) {
		List<Double> result = new ArrayList<Double>(legalLabels.size());
		for(int i = 0;i<trainingLabels.size(); ++i)
		{
			result.set(trainingLabels.get(i), result.get(trainingLabels.get(i))+1.0);
		}
		for(int i =0; i<result.size();++i){
			result.set(i, result.get(i)/trainingLabels.size());
		}
		return result;
	}

}
