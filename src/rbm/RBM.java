package rbm;

public class RBM {

	private static final double INITIAL_WEIGHT_SCALE_FACTOR = 0.1;
	//private static final double INITIAL_BIAS_SCALE_FACTOR = 0.1;

	private double[] visibleBias;
	private double[] hiddenBias;

	private double[] visible;
	private double[] hidden;

	private double[][] weights;

	public RBM(int numVisible, int numHidden) {

		visibleBias = new double[numVisible];
		visible = new double[numVisible];
		hiddenBias = new double[numHidden];
		hidden = new double[numHidden];
		weights = new double[numVisible][numHidden];

		RBMUtils.initializeWeights(weights, INITIAL_WEIGHT_SCALE_FACTOR);
		
		//Utils.initializeVisibleBias(visibleBias, INITIAL_BIAS_SCALE_FACTOR);
		
		
		// Set the hidden biases to 0
		//Utils.initializeHiddenBias(hiddenBias, INITIAL_BIAS_SCALE_FACTOR);

	}

	public void train(double[][] trainingSet, double learningRate, int numEpochs) {

		for (int index = 0; index < numEpochs; index++) {
			
			for (double[] lesson : trainingSet) {

				RBMUtils.setValues(visible, lesson);
				
				RBMUtils.update0(visible, hidden, visibleBias, hiddenBias, weights);
				
				double[][] data = RBMUtils.computeData(visible, hidden, visibleBias, hiddenBias);
				
				RBMUtils.reconstruct(visible, hidden, visibleBias, hiddenBias, weights);
				
				RBMUtils.update1(visible, hidden, visibleBias, hiddenBias, weights);
				
				double[][] recon = RBMUtils.computeRecon(visible, hidden, visibleBias, hiddenBias);
				
				RBMUtils.updateWeights(weights, data, recon, visible, hidden);
				
			}
		}

	}

	public void getResult() {
	
		RBMUtils.printVisible(visible);
		
	}

	public void randomHidden() {
		RBMUtils.randomHidden(hidden);
		RBMUtils.reconstruct(visible, hidden, visibleBias, hiddenBias, weights);
		getResult();
	}

	public void getEnergy() {
		double e = RBMUtils.getEnergy(visible, visibleBias, hidden, hiddenBias, weights);
		System.out.println("\n\n" + e);
	}
}


