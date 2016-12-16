public class Node{
	int label;
	Feature features;
	int rank;
	public Node(int label, Feature features){
		this.features = features;
		this.label = label;
		rank = 9999999;
	}
	public static int getHemmingDistance(Feature a, Node b){
		int distance = 0;
		for(int i = 0; i < a.getHeight(); ++i){
			for(int j = 0; j < a.getWidth(); ++j){
				if(a.getFeature(i, j)!= b.features.getFeature(i, j)){
					distance++;
				}
			}
		}
		return distance;
	}
}
