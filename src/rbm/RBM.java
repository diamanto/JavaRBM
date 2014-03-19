package rbm;

public class RBM {

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

		RBMUtils.initializeWeights(weights);

	}

	public void train(double[][] trainingSet, double learningRate, int numEpochs) {
		
		double[] visibleB = new double[trainingSet.length];
		
		for (int i = 0; i < trainingSet.length; i++) {
			double p = RBMUtils.getP(trainingSet[i]);
			double x = p / (1 - p);
			visibleB[i] = Math.log(x);
		}

		for (int index = 0; index < numEpochs; index++) {
			
			for (int count = 0; count < trainingSet.length; count++) {

				RBMUtils.setValues(visible, trainingSet[count]);
				RBMUtils.setVBias(visibleBias, visibleB, count);
				
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


