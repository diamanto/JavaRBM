package rbm;



import java.util.Random;

import picture_processing.Color;
import picture_processing.Picture;
import picture_processing.Utils;

public class RBMUtils {

	public static Random randi = new Random();

	public static void initializeHiddenBias(double[] hiddenBias,
			double initialBiasScaleFactor) {
		// 0
	}

	public static void initializeWeights(double[][] weights) {
		for (int i = 0; i < weights.length; i++) {
			for (int j = 0; j < weights[i].length; j++) {
				// nextGaussian return value with mean 0.0
				// and standard deviation 1.0, as we would like
				// it to be 0.01, we multiply with 0.01
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
			double[][] recon, double rate) {
		for (int i = 0; i < data.length; i++) {
			for (int j = 0; j < data[i].length; j++) {
				weights[i][j] += (data[i][j] - recon[i][j]) * rate;
			}
		}
	}

	public static void printVisible(double[] visible) {

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

	public static double getP(double[] lesson) {

		double p = 0;		
		for (double i : lesson) {
			if (i == 1) p++;
		}
		p = p / lesson.length;
		return p;
	}

	public static void setVBias(double[] visibleBias, double[] visibleB,
			int count) {
		for (int i = 0; i < visibleBias.length; i++) {
			visibleBias[i] = visibleB[count];
		}

	}

	public static void printMovies(double[][] weights) {
		String[] movies = {"Bias        ", "Harry Potter", "Avatar      ",
				"LOTR 3      ", "Gladiator   ", "Titanic      ", "Glitter     "};

		StringBuilder kaki = new StringBuilder();
		for (int i = 0; i < weights.length; i++) {

			kaki.append(movies[i] + "\t\t");
			for (int j = 0; j < weights[i].length; j++) {
				kaki.append(weights[i][j] + "\t");
			}
			kaki.append("\n");
		}

		System.out.println(kaki.toString());

		for (int i = 0; i < weights.length; i++) {
			System.out.print(movies[i] + "\t\t");
			for (int j = 0; j < weights[i].length; j++) {
				System.out.print((weights[i][j] > 0) + "\t");
			}
			System.out.println();
		}

	}

	public static void toPicture(double[] vis) {
		Picture image = Utils.createPicture(16, 16);
		Color szin;
		int count = 0;
		for (int h = 0; h < 16; h++) {
			for (int w = 0; w < 16; w++) {
				szin = vis[count] == 1 ? new Color(0, 0, 0) : new Color(255, 255, 255);
				image.setPixel(w, h, szin);
				count++;
			}
		}

		Utils.savePicture(image, "images/result.png");
	}

	public static Picture featuresToPicture(double[][] weights, double[] visible, double[] hidden) {
		Picture image = Utils.createPicture(5 * 17, 5 * 17);		
		
		double maxW = 0;
		double minW = 0;
		double dif;
		
		for (int j = 0; j < hidden.length; j++) {
			for (int i = 0; i < visible.length; i++) {
				if (weights[i][j] > maxW) maxW = weights[i][j];
				if (weights[i][j] < minW) minW = weights[i][j];
			}
		}
		
		dif = maxW - minW;
		
		int numHid = 0;
		for (int h = 0; h < image.getHeight(); h = h + 17) {
			for (int w = 0; w < image.getWidth(); w = w + 17) {
				drawPicture(weights, visible, hidden, numHid, h, w, dif, minW, image);
				numHid++;
			}
		}
		Picture big = Utils.createPicture(image.getWidth()*3, image.getHeight()*3);
		for (int i = 0; i < image.getWidth()*3; i++) {
			for (int j = 0; j < image.getHeight()*3; j++) {
				big.setPixel(i, j, image.getPixel(i/3, j/3));
			}
		}
		return big;
		
	}

	private static void drawPicture(double[][] weights, double[] visible,
			double[] hidden, int numHid, int h, int w, double dif, double minW, Picture image) {
		double shade;
		int numVis = 0;
		for (int j = h; j < h + 16; j++) {
			for (int i = w; i < w + 16; i++) {
				shade = (weights[numVis][numHid] - minW) / dif;;
				shade *= 255;
				shade -= 255;
				int s = (int) shade;
				image.setPixel(i, j, new Color(s, s, s));
				
				numVis++;
			}
		}
		
	}

	public static Picture getVisible(double[] visible) {
		Picture img = Utils.createPicture(16, 16);
		int count = 0;
		Color szin;
		for (int i = 0; i < 16; i++) {
			for (int j = 0; j < 16; j++) {
				szin = visible[count] == 1 ? new Color(0, 0, 0) : new Color(255, 255, 255);
				img.setPixel(j, i, szin);
				count++;
			}
		}
		 
		Picture img2 = Utils.createPicture(128, 128);
		for (int i = 0; i < 128; i++) {
			for (int j = 0; j < 128; j++) {
				img2.setPixel(i, j, img.getPixel(i/8, j/8));
			}
		}
		
		return img2;
	}

	public static void setValues(double[][] oldOne, double[][] newOne) {
		for (int i = 0; i < oldOne.length; i++) {
			for (int j = 0; j < oldOne[0].length; j++) {
				oldOne[i][j] = newOne[i][j];
			}
		}
		
	}

}
