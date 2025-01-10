
package ImageProcessing.Contours;

import ImageProcessing.Lineaire.FiltrageLineaireLocal;

public class ContoursLineaire {
    
    public static int[][] gradientPrewitt(int[][] image, int dir) {
        
        double[][] masque;
        
        if(dir == 1) {
            masque = new double[][] {{1d/3d, 0, (-1d)/3}, {1d/3d, 0, (-1d)/3}, {1d/3d, 0, (-1d)/3}};
        }
        else {
            masque = new double[][] {{1d/3d, 1d/3d, 1d/3d}, {0, 0, 0}, {-1d/3d, -1d/3d, -1d/3d}};
        }

        return filtrageLineaire(image, masque);
    }
    
    public static int[][] gradientSobel(int[][] image,int dir) {
        
        double[][] masque;
        if(dir == 1) {
            masque = new double[][] {{1d/4d, 0, -1d/4d}, {2d/4d, 0, -2d/4d}, {1d/4d, 0, -1d/4d}};
        }
        else {
            masque = new double[][] {{1d/4d, 2d/4d, 1d/4d}, {0, 0, 0}, {-1d/4d, -2d/4d, -1d/4d}};
        }

        return filtrageLineaire(image, masque);
    }
    
    public static int[][] laplacien4(int[][] image) {
        
        double[][] masque = new double[][] {{0, 1, 0}, {1, -4, 1}, {0, 1, 0}};

        return filtrageLineaire(image, masque);
    }
    
    public static int[][] laplacien8(int[][] image) {
        
        double[][] masque = new double[][] {{1, 1, 1}, {1, -8, 1}, {1, 1, 1}};
        
        return filtrageLineaire(image, masque);
    }

    public static int[][] filtrageLineaire(int[][] image, double[][] masque) {
        int[][] imageAfter = FiltrageLineaireLocal.filtreMasqueConvolution(image, masque);

        for (int i = 0; i < imageAfter.length; i++) {
            for (int j = 0; j < imageAfter[i].length; j++) {
                imageAfter[i][j] = Math.min(Math.max(imageAfter[i][j], 0), 255);
            }
        }

        return imageAfter;
    }
}
