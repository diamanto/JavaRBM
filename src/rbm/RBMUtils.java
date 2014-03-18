package rbm;

import java.util.Random;

public class RBMUtils {

	public static Random randi = new Random();

	public static void initializeVisibleBias(double[] visibleBias,
			double n) {
		for (int i = 0; i < visibleBias.length; i++) {
			visibleBias[i] = randi.nextGaussian() * n;
		}

	}

	public static void initializeHiddenBias(double[] hiddenBias,
			double initialBiasScaleFactor) {
		// 0
	}

	public static void initializeWeights(double[][] weights, double n) {
		for (int i = 0; i < weights.length; i++) {
			for (int j = 0; j < weights[i].length; j++) {
				weights[i][j] = randi.nextGaussian() * 0.01;
			}
		}
	}

	public static double sigmoid(double t) {
		return 1 / (1 + Math.exp(-t));
	}

	public static double activationFunction(double x) {
		if (Math.random() < sigmoid(x)) {
			return 1;
		} else {
			return 0;
		}
	}

	public static void setValues(double[] old, double[] newOne) {
		for (int i = 0; i < old.length; i++) {
			old[i] = newOne[i];
		}
	}



	public static double[] getValues(double[] d, 
			double[] bias) {
		double[] vals = new double[d.length];
		for (int i = 0; i < d.length; i++) {
			vals[i] = activationFunction(d[i] + bias[i]);
		}
		return vals;
	}


	public static void update0(double[] visible, double[] hidden,
			double[] visibleBias, double[] hiddenBias, double[][] weights) {
		for (int j = 0; j < hidden.length; j++) {
			for (int i = 0; i < visible.length; i++) {
				hidden[j] += visible[i] * weights[i][j];
			}
			hidden[j] += hiddenBias[j];
			hidden[j] = activationFunction(hidden[j]);
		}	

	}

	public static void update1(double[] visible, double[] hidden,
			double[] visibleBias, double[] hiddenBias, double[][] weights) {
		for (int j = 0; j < hidden.length; j++) {
			for (int i = 0; i < visible.length; i++) {
				hidden[j] += visible[i] * weights[i][j];
			}
			hidden[j] += hiddenBias[j];
			hidden[j] = sigmoid(hidden[j]);			
		}	
	}

	public static void reconstruct(double[] visible, double[] hidden,
			double[] visibleBias, double[] hiddenBias, double[][] weights) {
		for (int i = 0; i < visible.length; i++) {
			for (int j = 0; j < hidden.length; j++) {
				visible[i] += hidden[j] * weights[i][j];
			}
			visible[i] += visibleBias[i];
			visible[i] = activationFunction(visible[i]);			
		}	

	}

	public static double[][] computeData(double[] visible, double[] hidden,
			double[] visibleBias, double[] hiddenBias) {
		double[][] result = new double[visible.length][hidden.length];
		for (int i = 0; i < visible.length; i++) {
			for (int j = 0; j < hidden.length; j++) {
				result[i][j] = visible[i] * hidden[j];
			}
		}
		return result;		
	}

	public static double[][] computeRecon(double[] visible, double[] hidden,
			double[] visibleBias, double[] hiddenBias) {
		double[][] result = new double[visible.length][hidden.length];
		for (int i = 0; i < visible.length; i++) {
			for (int j = 0; j < hidden.length; j++) {
				result[i][j] = visible[i] * hidden[j];
			}
		}
		return result;		
	}

	public static void updateWeights(double[][] weights, double[][] data,
			double[][] recon, double[] visible, double[] hidden) {
		for (int i = 0; i < visible.length; i++) {
			for (int j = 0; j < hidden.length; j++) {
				weights[i][j] += (data[i][j] - recon[i][j]) * 0.01;
			}
		}


	}

	public static void printVisible(double[] visible) {
		//Picture image = new Picture("images/result.png");
		for (int i = 0; i < visible.length; i++) {
			if (i % 16 == 0) {
				System.out.println();
			}
			System.out.print((int) visible[i]);
		}

	}

	public static void randomHidden(double[] hidden) {
		for (int i = 0; i < hidden.length; i++) {
			hidden[i] = randi.nextInt(2);
		}
	}

	public static double getEnergy(double[] visible, double[] visibleBias, 
			double[] hidden, double[] hiddenBias, double[][] weights) {

		double energy = 0;

		for (int i = 0; i < visible.length; i++) {
			energy -= visible[i] * visibleBias[i]; 
		}

		for (int j = 0; j < hidden.length; j++) {
			energy -= hidden[j] * hiddenBias[j]; 
		}

		for (int i = 0; i < visible.length; i++) {
			for (int j = 0; j < hidden.length; j++) {
				energy -= visible[i] * hidden[j] * weights[i][j]; 
			}
		}

		return energy;
	}

}
