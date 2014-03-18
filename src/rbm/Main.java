package rbm;

import picture_processing.Color;
import picture_processing.Picture;
import picture_processing.Utils;


public class Main {

	public static void main(String[] args) {

		double[][] trainingSet = new double[10][256];
		Picture[] images = new Picture[10];
		double[][] harmas = new double[1][256];
		double[][] xes = new double[1][256];
		double[][] hetes = new double[1][256];
		Picture harom = Utils.loadPicture("images/probB.png");
		Picture xPic = Utils.loadPicture("images/x.png");
		Picture hetPic = Utils.loadPicture("images/77.png");
		images[0] = Utils.loadPicture("images/1.png");
		images[1] = Utils.loadPicture("images/2.png");
		images[2] = Utils.loadPicture("images/3.png");
		images[3] = Utils.loadPicture("images/4.png");
		images[4] = Utils.loadPicture("images/5.png");
		images[5] = Utils.loadPicture("images/6.png");
		images[6] = Utils.loadPicture("images/7.png");
		images[7] = Utils.loadPicture("images/8.png");
		images[8] = Utils.loadPicture("images/9.png");
		images[9] = Utils.loadPicture("images/10.png");
		int w = 16;
		int h = 16;
		Color szin = null;
		for (int k = 0; k < images.length; k++) {
			for (int i = 0; i < w; i++) {
				for (int j = 0; j < h; j++) {
					szin = images[k].getPixel(j, i); 
					trainingSet[k][16 * i + j] = szin.getBlue() > 100 ? 0 : 1;

				}
			}
		}

		for (int i = 0; i < w; i++) {
			for (int j = 0; j < h; j++) {
				szin = harom.getPixel(j, i); 
				harmas[0][16 * i + j] = szin.getBlue() > 100 ? 0 : 1;	
				szin = xPic.getPixel(j, i);
				xes[0][16 * i + j] = szin.getBlue() > 100 ? 0 : 1;	
				szin = hetPic.getPixel(j, i);
				hetes[0][16 * i + j] = szin.getBlue() > 100 ? 0 : 1;	

			}
		}

		RBM rbm = new RBM(256, 20);

		rbm.train(trainingSet, 0.01, 5000);
		rbm.getResult();
		rbm.train(harmas, 0.01, 1);
		rbm.getResult();
		//rbm.randomHidden();

		rbm.getEnergy();


	}
}
