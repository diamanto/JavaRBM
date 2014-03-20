package rbm;



public class Main {

	public static void main(String[] args) {

		TrainingSet ts = new TrainingSet();
		double[][] trainingSet = ts.getTrainingSet();

		RBM rbm = new RBM(256, 25, 0.01);

		rbm.train(trainingSet, 2000);
		rbm.printVisible();
		rbm.featuresToPicture();
		rbm.getEnergy();


	}
}
