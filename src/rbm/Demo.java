package rbm;

public class Demo {

	public static void main(String[] args) {
		
		// visible, hidden, learning rate
		RBM rbm = new RBM(6, 2, 0.01);
		double[][] trainingData = {{1, 1, 1, 0, 0, 0}, 
									{1, 0, 1, 0, 0, 0},
									{1, 1, 1, 0, 0, 0}, 
									{0, 0, 1, 1, 1, 0}, 
									{0, 0, 1, 1, 0, 0}, 
									{0, 0, 1, 1, 1, 0}};

		rbm.train(trainingData, 5000);	
		rbm.printMovies();
	}

}
