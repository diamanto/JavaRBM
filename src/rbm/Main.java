package rbm;

import picture_processing.Color;
import picture_processing.Picture;
import picture_processing.Utils;



public class Main {

	public static void main(String[] args) {

		TrainingSet ts = new TrainingSet();
		double[][] trainingSet = ts.getTrainingSet();
		double[][] proba = new double[1][256];
		Picture image = Utils.loadPicture("images/33.png");

		int w = 16;
		int h = 16;
		Color szin = null;
		for (int i = 0; i < w; i++) {
			for (int j = 0; j < h; j++) {
				szin = image.getPixel(j, i); 
				proba[0][16 * i + j] = szin.getBlue() > 100 ? 0 : 1;
			}
		}

		RBM rbm = new RBM(256, 25, 0.01);

		rbm.train(trainingSet, 5000);
		rbm.printVisible();
		System.out.println("\n\n" + rbm.getEnergy());
		rbm.train(proba, 1);
		System.out.println("\n\n" + rbm.getEnergy());


	}
}
