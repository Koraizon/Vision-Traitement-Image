
package ImageProcessing.NonLineaire;

import java.util.Arrays;


public class MorphoComplexe {

    public static int[][] dilatationGeodesique(int[][] image, int[][] masqueGeodesique, int nbIter) {
        if (nbIter < 1) {
            System.err.println("erreur : nbrIter doit etre superieur a 0");
            return null;
        }

        int[][] finalImage = MorphoElementaire.dilatation(image, 3);
        finalImage = intersectionBlanc(finalImage, masqueGeodesique);
        for (int i = 1; i < nbIter; i++) {
            finalImage = MorphoElementaire.dilatation(finalImage, 3);
            finalImage = intersectionBlanc(finalImage, masqueGeodesique);
        }
        return finalImage;
    }
    public static int[][] reconstructionGeodesique(int[][] image, int[][] masqueGeodesique) {
        int[][] imageSortie = image;
        int[][] lastImage;
        int cpt = 0;
        do {
            cpt++;
            lastImage = imageSortie;
            imageSortie = dilatationGeodesique(imageSortie, masqueGeodesique, 1);

        } while (!compareImage(lastImage, imageSortie));

        System.out.println("nombre d'iterations : " + cpt);
        return imageSortie;
    }
    public static int[][] filtreMedian(int[][] image, int tailleMasque) {
        if (tailleMasque % 2 == 0) {
            System.err.println("veuillez entrer une taille de masque impaire !");
            return null;
        }
        int hauteur = image.length;
        int largeur = image[0].length;
        int tailleMasqueMoitie = tailleMasque / 2;

        int[][] imageFiltree = new int[hauteur][largeur];

        for (int i = 0; i < hauteur; i++) {
            for (int j = 0; j < largeur; j++) {
                int[] voisins = new int[tailleMasque * tailleMasque + 1];
                int index = 0;

                // Parcourir la zone du masque autour du pixel (i, j)
                for (int k = -tailleMasqueMoitie; k <= tailleMasqueMoitie; k++) {
                    for (int l = -tailleMasqueMoitie; l <= tailleMasqueMoitie; l++) {
                        int voisinI = i + k;
                        int voisinJ = j + l;

                        // Vérifier si le voisin est dans les limites de l'image
                        if (voisinI >= 0 && voisinI < hauteur && voisinJ >= 0 && voisinJ < largeur) {
                            voisins[index] = image[voisinI][voisinJ];
                            index++;
                        }
                    }
                }

                // Trier les valeurs des voisins
                Arrays.sort(voisins);

                // Sélectionner la médiane
                int mediane = voisins[index / 2];

                // Stocker la valeur filtrée dans l'image filtrée
                imageFiltree[i][j] = mediane;
            }
        }

        return imageFiltree;
    }


    private static int[][] intersectionBlanc(int[][] dilatee, int[][] limite) {

        if(dilatee.length != limite.length || dilatee[0].length != limite[0].length) {
            System.err.println("erreur : Taille des images differentes");
            return null;
        }

        for(int i = 0 ; i < dilatee.length ; i++) {
            for(int j = 0 ; j < dilatee[i].length ; j++) {
                if(dilatee[i][j] != limite[i][j]) {
                    dilatee[i][j] = 0;
                }
            }
        }

        return dilatee;
    }
    private static boolean compareImage(int[][] image, int[][] image2) {
        for (int i = 0; i < image.length; i++) {
            for (int j = 0; j < image[i].length; j++) {
                if (image[i][j] != image2[i][j]) {
                    return false;
                }
            }
        }
        return true;
    }
}
