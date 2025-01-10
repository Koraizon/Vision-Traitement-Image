package ImageProcessing.Seuillage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Seuillage {
	public static int[][] seuillageSimple(int[][] image, int seuil){
		int height = image.length;
		int width = image[0].length;
		int[][] result = new int[height][width];

		for (int h = 0; h < height; h++){
			for(int w = 0; w < width; w++){
				if(image[h][w] > seuil){
					result[h][w] = 255;
				} else {
					result[h][w] = 0;
				}
			}
		}
		return result;
	}

	public static int[][] seuillageDouble(int[][] image,int seuil1, int seuil2){
		int height = image.length;
		int width = image[0].length;
		int[][] result = new int[height][width];

		for (int h = 0; h < height; h++){
			for(int w = 0; w < width; w++){
				if(image[h][w] > seuil2){
					result[h][w] = 255;
				} else if(image[h][w] <= seuil2 && image[h][w] > seuil1)
					result[h][w] = 128;
				else {
					result[h][w] = 0;
				}
			}
		}
		return result;
	}

	public static int[][] seuillageAutomatique(int[][] image){
		int height = image.length;
		int width = image[0].length;
		int seuil = average(image);
//		int seuil = median(image);
		boolean sameSeuil = false;

		do {
			List<Integer> g1 = new ArrayList<>();
			List<Integer> g2 = new ArrayList<>();
			for (int[] ints : image) {
				for (int w = 0; w < width; w++) {
					if (ints[w] > seuil) {
						g1.add(ints[w]);
					} else {
						g2.add(ints[w]);
					}
				}
			}
			double averageG1 = average(g1);
			double averageG2 = average(g2);
			int newSeuil = (int) ((averageG1 + averageG2) / 2);
			if(newSeuil == seuil){
				sameSeuil = true;
			} else {
				seuil = newSeuil;
			}
		} while (!sameSeuil);
		return seuillageSimple(image, seuil);
	}

	private static int average(int[][] image){
		int height = image.length;
		int width = image[0].length;
		int sum = 0;
		for (int[] ints : image) {
			for (int w = 0; w < width; w++) {
				sum += ints[w];
			}
		}
		return sum / (height * width);
	}

	private static int average(List<Integer> g){
		int sum = 0;
		for(int i : g){
			sum += i;
		}
		return sum/g.size();
	}

	private static int median(int[][] image) {
		int[] allValues = new int[image.length*image.length];
		int index = 0;
		for (int[] ints : image) {
			for (int anInt : ints) {
				allValues[index] = anInt;
				index++;
			}
		}

		// Trier les valeurs des voisins
		Arrays.sort(allValues);

		return allValues[index / 2];
	}
}
