package rbm;

import picture_processing.Color;
import picture_processing.Picture;
import picture_processing.Utils;


public class Main {

	public static void main(String[] args) {

		TrainingSet ts = new TrainingSet();
		double[][] trainingSet = ts.getTrainingSet();

		RBM rbm = new RBM(256, 25, 0.01);

		rbm.train(trainingSet, 100);
		rbm.printVisible();
		rbm.featuresToPicture();
		rbm.getEnergy();


	}
}
