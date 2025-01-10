
package ImageProcessing.Contours;

public class ContoursNonLineaire {
    
    public static int[][] gradientErosion(int[][] image) {
        int height = image.length;
        int width = image[0].length;
        int[][] result = new int[height][width];

        for (int h = 0; h < height; h++){
            for(int w = 0; w < width; w++){
                int minValue = Integer.MAX_VALUE;
                for (int i = h - 1; i <= h + 1; i++){
                    for(int j = w - 1; j <= w + 1; j++){
                        if(i >= 0 && i < height && j >= 0 && j < width){
                            minValue = Math.min(minValue, image[i][j]);
                        }
                    }
                }
                result[h][w] = image[h][w] - minValue;
            }
        }
        return result;
    }
    
    public static int[][] gradientDilatation(int[][] image) {
        int height = image.length;
        int width = image[0].length;
        int[][] result = new int[height][width];

        for (int h = 0; h < height; h++){
            for(int w = 0; w < width; w++){
                int maxValue = Integer.MIN_VALUE;
                for (int i = h - 1; i <= h + 1; i++){
                    for(int j = w - 1; j <= w + 1; j++){
                        if(i >= 0 && i < height && j >= 0 && j < width){
                            maxValue = Math.max(maxValue, image[i][j]);
                        }
                    }
                }
                result[h][w] = maxValue - image[h][w];
            }
        }
        return result;
    }
    
    public static int[][] gradientBeucher(int[][] image) {
        int height = image.length;
        int width = image[0].length;
        int[][] result = new int[height][width];
        int[][] gradientErosion = gradientErosion(image);
        int[][] gradientDilatation = gradientDilatation(image);
        for (int h = 0; h < height; h++) {
            for (int w = 0; w < width; w++) {
                result[h][w] = gradientErosion[h][w] + gradientDilatation[h][w];
            }
        }
        return result;
    }
    
    public static int[][] laplacienNonLineaire(int[][] image) {
        int height = image.length;
        int width = image[0].length;
        int[][] result = new int[height][width];
        int[][] gradientErosion = gradientErosion(image);
        int[][] gradientDilatation = gradientDilatation(image);
        for (int h = 0; h < height; h++) {
            for (int w = 0; w < width; w++) {
                result[h][w] = Math.abs(gradientDilatation[h][w] - gradientErosion[h][w]);
            }
        }
        return result;
    }
}
