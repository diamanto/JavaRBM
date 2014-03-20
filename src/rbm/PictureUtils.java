package rbm;


public class PictureUtils {


	public static double[] convertTo256(int[] pixels) {

		int wh = (int) Math.sqrt(pixels.length);
		double[][] dd = new double[wh][wh];
		int count = 0;
		for (int i = 0; i < wh; i++) {
			for (int j = 0; j < wh; j++) {
				dd[i][j] = pixels[count];
				count++;
			}
		}
		count = 0;
		double[][] dd16 = new double[16][16];

		for (int i = 0; i < wh; i++) {
			for (int j = 0; j < wh; j++) {
				if (dd[i][j] == 1) {
					dd16[i/8][j/8] = 1;
				}
			}
		}

		double[] result = new double[256];
		for (int i = 0; i < 16; i++) {
			for (int j = 0; j < 16; j++) {
				result[16 * i + j] = dd16[i][j];
			}
		}
		
		for (int i = 0; i < pixels.length; i++) {
			//result[i] = pixels[i];
		}
		return result;
	}

}
