
package ImageProcessing.NonLineaire;

public class MorphoElementaire {

	public static int[][] erosion(int [][] image, int tailleMasque) {
		int min;
		int posHorizontale, posVerticale;
		int[][] imageRes = new int[image.length][image[0].length];

		int rayonMasque = tailleMasque / 2;

		System.out.println("taille : " + image.length);

		for(int i = 0 ; i < image.length ; i++) {
			for(int j = 0 ; j < image[i].length ; j++) {
				min = 256;

				for(int u = -rayonMasque ; u <= rayonMasque ; u++) {
					for(int v = -rayonMasque ; v <= rayonMasque ; v++) {
						posHorizontale = i + u;
						posVerticale = j + v;


						if(posHorizontale >= 0 && posHorizontale < image.length && posVerticale >= 0 && posVerticale < image[i].length)
							min = Math.min(min, image[posHorizontale][posVerticale]);

					}
				}
				imageRes[i][j] = min;
			}
		}

		return imageRes;
	}

	public static int[][] dilatation(int [][] image, int tailleMasque) {
		int max;
		int posHorizontale, posVerticale;
		int[][] imageRes = new int[image.length][image[0].length];
		int rayonMasque = tailleMasque / 2;

		for(int i = 0 ; i < image.length ; i++) {
			for(int j = 0 ; j < image[i].length ; j++) {
				max = 0;

				for(int u = -rayonMasque ; u <= rayonMasque ; u++) {
					for(int v = -rayonMasque ; v <= rayonMasque ; v++) {
						posHorizontale = i + u;
						posVerticale = j + v;

						if(posHorizontale >= 0 && posHorizontale < image.length && posVerticale >= 0 && posVerticale < image[i].length)
							max = Math.max(max, image[posHorizontale][posVerticale]);

					}
				}
				imageRes[i][j] = max;
			}
		}

		return imageRes;
	}

	public static int[][] ouverture(int [][] image, int tailleMasque) {
		image = erosion(image, tailleMasque);
		image = dilatation(image, tailleMasque);

		return image;
	}

	public static int[][] fermeture(int [][] image, int tailleMasque) {
		image = dilatation(image, tailleMasque);
		image = erosion(image, tailleMasque);

		return image;
	}
}
