
package ImageProcessing.Lineaire;

import ImageProcessing.Complexe.Complexe;
import ImageProcessing.Complexe.MatriceComplexe;
import ImageProcessing.Fourier.Fourier;

public class FiltrageLineaireGlobal {
    public static int[][] filtrePasseBasIdeal(int[][] image, int frequenceCoupure) {
        double radicant;
        int centerY = image.length / 2;
        int centerX = image[0].length / 2;
        int distanceX, distanceY;
        double[][] imageDouble = new double[image.length][image[0].length];

        int FreqCoupureCarree = frequenceCoupure * frequenceCoupure;

        for(int i = 0 ; i < image.length ; i++) {
            for (int j = 0; j < image[0].length; j++) {
                imageDouble[i][j] = image[i][j];
            }
        }
        
        MatriceComplexe fourierImage = Fourier.Fourier2D(imageDouble);
        fourierImage = Fourier.decroise(fourierImage);


        for(int y = 0 ; y < image.length ; y++) {
            for(int x = 0 ; x < image[y].length ; x++) {
                distanceX = Math.abs(x - centerX);
                distanceY = Math.abs(y - centerY);
                radicant = distanceX*distanceX + distanceY*distanceY;
                
                if(radicant > FreqCoupureCarree) {
                    fourierImage.set(y, x, 0, 0);
                }
            }
        }
        
        fourierImage = Fourier.decroise(fourierImage);
        fourierImage = Fourier.InverseFourier2D(fourierImage);

        int[][] imageFiltered = new int[image.length][image[0].length];
        
        for(int i = 0 ; i < image.length ; i++){
            for(int j = 0 ; j < image[0].length ; j++){
                int tmp = (int)(fourierImage.get(i, j).getPartieReelle());
                imageFiltered[i][j] = Math.max(0, Math.min(255, tmp));
            }
        }
        
        return imageFiltered;
    }
    
    public static int[][] filtrePasseHautIdeal(int[][] image, int frequenceCoupure) {
        double radicant;
        int centerY = image.length / 2;
        int centerX = image[0].length / 2;
        int distanceX, distanceY;
        double[][] imageDouble = new double[image.length][image[0].length];

        int FreqCoupureCarree = frequenceCoupure * frequenceCoupure;

        for(int i = 0 ; i < image.length ; i++) {
            for (int j = 0; j < image[0].length; j++) {
                imageDouble[i][j] = image[i][j];
            }
        }

        MatriceComplexe fourierImage = Fourier.Fourier2D(imageDouble);
        fourierImage = Fourier.decroise(fourierImage);


        for(int y = 0 ; y < image.length ; y++) {
            for(int x = 0 ; x < image[y].length ; x++) {
                distanceX = Math.abs(x - centerX);
                distanceY = Math.abs(y - centerY);
                radicant = distanceX*distanceX + distanceY*distanceY;

                if(radicant < FreqCoupureCarree) {
                    fourierImage.set(y, x, 0, 0);
                }
            }
        }

        fourierImage = Fourier.decroise(fourierImage);
        fourierImage = Fourier.InverseFourier2D(fourierImage);

        int[][] imageFiltered = new int[image.length][image[0].length];

        for(int i = 0 ; i < image.length ; i++){
            for(int j = 0 ; j < image[0].length ; j++){
                int tmp = (int)(fourierImage.get(i, j).getPartieReelle()) + 128;
                imageFiltered[i][j] = Math.max(0, Math.min(255, tmp));
            }
        }

        return imageFiltered;

    }
    
    public static int[][] filtrePasseBasButterworth(int[][] image, int frequenceCoupure, int ordre) {
        
        double mult;
        int centreHorizontal = image.length/2;
        int centreVertical = image[0].length/2;
        double[][] imageDouble = new double[image.length][image[0].length];
        
        for(int i = 0 ; i < image.length ; i++)
            for(int j = 0 ; j < image[0].length ; j++)
                imageDouble[i][j] = (image[i][j]);
        
        MatriceComplexe fourierImage = Fourier.Fourier2D(imageDouble);
        fourierImage = Fourier.decroise(fourierImage);
        
        for(int i = 0 ; i < image.length ; i++) {
            for(int j = 0 ; j < image[0].length ; j++) {
                mult = (i - centreHorizontal)*(i - centreHorizontal) + (j - centreVertical)*(j - centreVertical);
                mult = Math.sqrt(mult);
                mult /= frequenceCoupure;
                mult = Math.pow(mult, 2 * ordre);
                mult = 1 / (1 + mult);
                
                fourierImage.get(i, j).multiplie(new Complexe(mult, 0));
            }
        }
        
        fourierImage = Fourier.decroise(fourierImage);
        fourierImage = Fourier.InverseFourier2D(fourierImage);
        
        for(int i = 0 ; i < image.length ; i++){
            for(int j = 0 ; j < image[0].length ; j++){
                int tmp = (int)(fourierImage.get(i, j).getPartieReelle());
                image[i][j] = Math.max(0, Math.min(255, tmp));
            }
        }
        
        return image;
    }
    
    public static int[][] filtrePasseHautButterworth(int[][] image, int frequenceCoupure, int ordre) {
        double mult;
        int centreHorizontal = image.length/2;
        int centreVertical = image[0].length/2;
        double[][] imageDouble = new double[image.length][image[0].length];
        
        for(int i = 0 ; i < image.length ; i++)
            for(int j = 0 ; j < image[0].length ; j++)
                imageDouble[i][j] = (image[i][j]);
        
        MatriceComplexe fourierImage = Fourier.Fourier2D(imageDouble);
        fourierImage = Fourier.decroise(fourierImage);
        
        for(int i = 0 ; i < image.length ; i++) {
            for(int j = 0 ; j < image[0].length ; j++) {
                
                mult = (i - centreHorizontal)*(i - centreHorizontal) + (j - centreVertical)*(j - centreVertical);
                mult = Math.sqrt(mult);
                mult = frequenceCoupure / mult;
                mult = Math.pow(mult, 2 * ordre);
                mult = 1 / (1 + mult);
                
                fourierImage.get(i, j).multiplie(new Complexe(mult, 0));
            }
        }
        
        fourierImage = Fourier.decroise(fourierImage);
        fourierImage = Fourier.InverseFourier2D(fourierImage);
        
        for(int i = 0 ; i < image.length ; i++){
            for(int j = 0 ; j < image[0].length ; j++){
                int tmp = (int)(fourierImage.get(i, j).getPartieReelle()) + 128;
                image[i][j] = Math.max(0, Math.min(255, tmp));
            }
        }
        
        return image;
    }
}
