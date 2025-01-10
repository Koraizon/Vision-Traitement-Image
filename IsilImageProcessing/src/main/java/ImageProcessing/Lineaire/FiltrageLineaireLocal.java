package ImageProcessing.Lineaire;

public class FiltrageLineaireLocal {
    public static int[][] filtreMasqueConvolution(int[][] image, double [][] masque) {
        if (masque.length % 2 != 1) throw new RuntimeException("erreur : le masque doit etre impair");
        if (masque.length != masque[0].length) throw new RuntimeException("erreur : le masque doit etre une matrice carree");

        if (masque[0][0] > 1.0 / (masque.length * masque.length))
            System.err.println("attention ! l'image finale pourrait contenir des pixels avec des valeurs superieures a 255");

        int lengthY = image.length;
        int lengthX = image[0].length;
        double[][] imageAfterFilter = new double[lengthY][lengthX];
        int[][] imageInt = new int[lengthY][lengthX];

        int pixelBorder = masque.length/2;

        for (int y = pixelBorder ; y < lengthY - pixelBorder; y++) {        //parcourir l'image
            for (int x = pixelBorder; x < lengthX - pixelBorder; x++) {
                imageAfterFilter[y][x] = 0;
                for (int i = 0; i < masque.length; i++) {       //parcourir les pixels de la taille du masque pour obtenir le pixel [y][x]
                    for (int j = 0; j < masque[i].length; j++) {
                        imageAfterFilter[y][x] += image[y-pixelBorder+i][x-pixelBorder+j]*masque[i][j];
                    }
                }
                imageInt[y][x] = (int) imageAfterFilter[y][x];
            }

        }

        int pixelToAdd;
        int xToTake;
        int yToTake;
        for (int y = 0; y < lengthY; y++) {        //parcourir l'image
            for (int x = 0; x < lengthX; x++) {
                imageAfterFilter[y][x] = 0;
                for (int i = 0; i < masque.length; i++) {      //parcourir les pixels de la taille du masque pour obtenir le pixel [y][x]
                    for (int j = 0; j < masque[i].length; j++) {
                        yToTake = y-pixelBorder+i;
                        xToTake = x-pixelBorder+j;
                        if (yToTake < 0 || xToTake < 0 || yToTake >= lengthY || xToTake >= lengthX)    //gerer les contours en remplacant les pixels inexistants par 0
                            pixelToAdd = 0;
                        else
                            pixelToAdd = image[yToTake][xToTake];

                        imageAfterFilter[y][x] += pixelToAdd*masque[i][j];
                    }
                }
                imageInt[y][x] = (int) imageAfterFilter[y][x];
            }
        }

        return imageInt;

    }

    public static int[][] filtreMoyenneur(int[][] image, int tailleMasque) {
        if (tailleMasque % 2 != 1) throw new RuntimeException("erreur : le masque doit etre impair");

        double[][] masque = new double[tailleMasque][tailleMasque];

        for (int i = 0; i < tailleMasque; i++) {
            for (int j = 0; j < tailleMasque; j++) {
                masque[i][j] = 1.0 / (tailleMasque * tailleMasque);
            }
        }

        return filtreMasqueConvolution(image, masque);
    }
}
