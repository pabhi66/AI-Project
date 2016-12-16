import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import static java.util.stream.Collectors.toList;

/**
 * Created by abhi on 12/12/16.
 */
public class KNN {

    private int[] legalLabels; //legal face or digit values (0,1 for face) (0....9 for digits)
    private int k; //k is the number of neighbors that the algorithm will study
    List<Node> world;

    KNN(int[] legalLabels, int k){
        this.legalLabels = legalLabels;
        this.k = k;
        world = new ArrayList<Node>();
    }

    void train(List<Feature> trainingData, List<Integer> trainingLabels){
    	//in training, it simply builds a world with nodes
    	for(int i = 0; i<trainingData.size(); ++i)
    	{
    		world.add(new Node(trainingLabels.get(i), trainingData.get(i)));
    	}
    	k= world.size()*k/100;
    }
    
    public List<Integer> classify(List<Feature> data){
        List<Integer> guesses = new ArrayList<Integer>();
        for(Feature datum : data) {
        	List<Node> neighbors = getNeighbors(datum, world);
        	int answer = getMostProbableLabel(neighbors);
            guesses.add(answer);
        }
        return guesses;
    }

	private int getMostProbableLabel(List<Node> neighbors) {
		int[] values = new int[legalLabels.length];
		for(Node n : neighbors){
			values[n.label]++;
		}
		int label_freq = values[0];
		int label = 0;
		for(int i = 0 ; i < values.length; ++i){
			if(values[i]>=label_freq){
				label_freq=values[i];
				label =i;
				
			}
		}
		return label;
	}

	private List<Node> getNeighbors(Feature datum, List<Node> world2) {
		List<Node> result = new ArrayList<Node>();
		
			for(Node n: world2){
				n.rank=Node.getHemmingDistance(datum, n);
			}
		
		result = world2.stream().sorted((a, b) -> Integer.compare(a.rank, b.rank)).limit(k).collect(toList());
		return result;
	}
}
