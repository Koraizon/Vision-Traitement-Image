package isilimageprocessing;

import CImage.CImage;
import CImage.Observers.Events.*;
import ImageProcessing.Complexe.MatriceComplexe;
import ImageProcessing.Contours.ContoursLineaire;
import ImageProcessing.Contours.ContoursNonLineaire;
import ImageProcessing.Fourier.Fourier;
import ImageProcessing.Lineaire.FiltrageLineaireGlobal;
import ImageProcessing.Lineaire.FiltrageLineaireLocal;
import ImageProcessing.NonLineaire.MorphoComplexe;
import ImageProcessing.NonLineaire.MorphoElementaire;
import ImageProcessing.Seuillage.Seuillage;
import isilimageprocessing.Dialogues.*;
import java.awt.*;
import javax.swing.*;

import CImage.CImageNG;
import CImage.CImageRGB;
import CImage.Exceptions.CImageNGException;
import CImage.Exceptions.CImageRGBException;
import CImage.Observers.JLabelBeanCImage;
import isilimageprocessing.Dialogues.JDialogNouvelleCImageNG;
import isilimageprocessing.Dialogues.JDialogNouvelleCImageRGB;

import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;


public class IsilImageProcessing extends javax.swing.JFrame implements ClicListener, SelectLigneListener,
		SelectRectListener, SelectRectFillListener, SelectCercleListener, SelectCercleFillListener {
	private CImageRGB imageRGB;
	private CImageNG imageNG;

	private final JLabelBeanCImage observerLeft;
	private final JLabelBeanCImage observerRight;
	private Color couleurPinceauRGB;
	private int couleurPinceauNG;
	private int[] courbeHisto;

	public IsilImageProcessing() {
		super();

		setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
		setTitle("TestCImage3");

		setContentPane(panel1);

		initComponents();

		imageRGB = null;
		imageNG = null;

		observerLeft = new JLabelBeanCImage();
		observerLeft.addClicListener(this);
		observerLeft.addSelectLigneListener(this);
		observerLeft.addSelectRectListener(this);
		observerLeft.addSelectRectFillListener(this);
		observerLeft.addSelectCercleListener(this);
		observerLeft.addSelectCercleFillListener(this);
		observerLeft.setMode(JLabelBeanCImage.INACTIF);
		ScrollPaneLeft.setViewportView(observerLeft);

		observerRight = new JLabelBeanCImage();
		observerRight.addClicListener(this);
		observerRight.addSelectLigneListener(this);
		observerRight.addSelectRectListener(this);
		observerRight.addSelectRectFillListener(this);
		observerRight.addSelectCercleListener(this);
		observerRight.addSelectCercleFillListener(this);
		observerRight.setMode(JLabelBeanCImage.INACTIF);
		ScrollPaneRight.setViewportView(observerRight);

		MenuDessiner.setEnabled(false);
		MenuFourier.setEnabled(false);
		MenuHistogramme.setEnabled(false);
		MenuFiltrageLineaire.setEnabled(false);
		MenuTraitementNonLineaire.setEnabled(false);
		MenuContours.setEnabled(false);
		MenuSeuillage.setEnabled(false);

		couleurPinceauRGB = Color.BLACK;
		couleurPinceauNG = 0;

		setVisible(true);
		pack();
		inverseButton.addActionListener(this::inverseButtonActionPerformed);
	}

	private void initComponents() {
		MenuItemImageNouvelleRGB.addActionListener(this::MenuItemImageNouvelleRGBActionPerformed);
		MenuItemImageNouvelleNG.addActionListener(this::MenuItemImageNouvelleNGActionPerformed);
		MenuItemImageOuvrirRGB.addActionListener(this::MenuItemImageOuvrirRGBActionPerformed);
		MenuItemImageOuvrirNG.addActionListener(this::MenuItemImageOuvrirNGActionPerformed);
		MenuItemImageEnregistrerSous.addActionListener(this::MenuItemImageEnregistrerSousActionPerformed);
		MenuItemImageQuitter.addActionListener(this::MenuItemImageQuitterActionPerformed);

		MenuItemDessinerCouleurPinceau.addActionListener(this::MenuItemDessinerCouleurPinceauActionPerformed);
		CheckBoxMenuItemDessinerPixel.addActionListener(this::CheckBoxMenuItemDessinerPixelActionPerformed);
		CheckBoxMenuItemDessinerLigne.addActionListener(this::CheckBoxMenuItemDessinerLigneActionPerformed);
		CheckBoxMenuItemDessinerRectangle.addActionListener(this::CheckBoxMenuItemDessinerRectangleActionPerformed);
		CheckBoxMenuItemDessinerRectanglePlein
				.addActionListener(this::CheckBoxMenuItemDessinerRectanglePleinActionPerformed);
		CheckBoxMenuItemDessinerCercle.addActionListener(this::CheckBoxMenuItemDessinerCercleActionPerformed);
		CheckBoxMenuItemDessinerCerclePlein.addActionListener(this::CheckBoxMenuItemDessinerCerclePleinActionPerformed);

		MenuItemFourierAfficherModule.addActionListener(this::MenuItemFourierAfficherModuleActionPerformed);
		MenuItemFourierAfficherPhase.addActionListener(this::MenuItemFourierAfficherPhaseActionPerformed);
		MenuItemFourierAfficherReelle.addActionListener(this::MenuItemFourierAfficherReelleActionPerformed);
		MenuItemFourierAfficherImaginaire.addActionListener(this::MenuItemFourierAfficherImaginaireActionPerformed);

		/*MenuItemHistogrammeAfficher.addActionListener(this::MenuItemHistogrammeAfficherActionPerformed);
		MenuItemHistogrammeRehaussement.addActionListener(this::MenuItemHistogrammeRehaussementActionPerformed);
		MenuItemHistogrammeCourbeSaturation.addActionListener(this::MenuItemHistogrammeCourbeSaturationActionPerformed);
		MenuItemHistogrammeCourbeGamma.addActionListener(this::MenuItemHistogrammeCourbeGammaActionPerformed);
		MenuItemHistogrammeCourbeNegatif.addActionListener(this::MenuItemHistogrammeCourbeNegatifActionPerformed);
		MenuItemHistogrammeCourbeEgalisation
				.addActionListener(this::MenuItemHistogrammeCourbeEgalisationActionPerformed);*/

		MenuFiltrageLineaireGlobalBasIdeal.addActionListener(this::MenuFiltrageLineaireGlobalBasIdealActionPerformed);
		MenuFiltrageLineaireGlobalHautIdeal.addActionListener(this::MenuFiltrageLineaireGlobalHautIdealActionPerformed);
		MenuFiltrageLineaireGlobalBasButter.addActionListener(this::MenuFiltrageLineaireGlobalBasButterActionPerformed);
		MenuFiltrageLineaireGlobalHautButter
				.addActionListener(this::MenuFiltrageLineaireGlobalHautButterActionPerformed);

		MenuFiltrageLineaireLocalConvolution
				.addActionListener(this::MenuFiltrageLineaireLocalConvolutionActionPerformed);
		MenuFiltrageLineaireLocalMoyenneur.addActionListener(this::MenuFiltrageLineaireLocalMoyenneurActionPerformed);

		MenuTraitementNonLineaireErosion.addActionListener(this::MenuTraitementNonLineaireErosionActionPerformed);
		MenuTraitementNonLineaireDilatation.addActionListener(this::MenuTraitementNonLineaireDilatationActionPerformed);
		MenuTraitementNonLineaireOuverture.addActionListener(this::MenuTraitementNonLineaireOuvertureActionPerformed);
		MenuTraitementNonLineaireFermeture.addActionListener(this::MenuTraitementNonLineaireFermetureActionPerformed);

		MenuTraitementNonLineaireDilatationGeodesique
				.addActionListener(this::MenuTraitementNonLineaireDilatationGeodesiqueActionPerformed);
		MenuTraitementNonLineaireReconstructionGeodesique
				.addActionListener(this::MenuTraitementNonLineaireReconstructionGeodesiqueActionPerformed);
		MenuTraitementNonLineaireFiltreMedian
				.addActionListener(this::MenuTraitementNonLineaireFiltreMedianActionPerformed);

		MenuContoursLineairePrewitt.addActionListener(this::MenuContoursLineairePrewittActionPerformed);
		MenuContoursLineaireSobel.addActionListener(this::MenuContoursLineaireSobelActionPerformed);
		MenuContoursLineaireLaplacien4.addActionListener(this::MenuContoursLineaireLaplacien4ActionPerformed);
		MenuContoursLineaireLaplacien8.addActionListener(this::MenuContoursLineaireLaplacien8ActionPerformed);

		MenuContoursNonLineaireErosion.addActionListener(this::MenuContoursNonLineaireErosionActionPerformed);
		MenuContoursNonLineaireDilatation.addActionListener(this::MenuContoursNonLineaireDilatationActionPerformed);
		MenuContoursNonLineaireBeucher.addActionListener(this::MenuContoursNonLineaireBeucherActionPerformed);
		MenuContoursNonLineaireLaplacien.addActionListener(this::MenuContoursNonLineaireLaplacienActionPerformed);

		MenuSeuillageSimple.addActionListener(this::MenuSeuillageSimpleActionPerformed);
		MenuSeuillageDouble.addActionListener(this::MenuSeuillageDoubleActionPerformed);
		MenuSeuillageAutomatique.addActionListener(this::MenuSeuillageAutomatiqueActionPerformed);

		MenuApplication1.addActionListener(this::MenuApplication1ActionPerformed);
		MenuApplication2A.addActionListener(this::MenuApplication2AActionPerformed);
		MenuApplication2B.addActionListener(this::MenuApplication2BActionPerformed);
		MenuApplication3.addActionListener(this::MenuApplication3ActionPerformed);
		MenuApplication4.addActionListener(this::MenuApplication4ActionPerformed);
		MenuApplication5.addActionListener(this::MenuApplication5ActionPerformed);
		MenuApplication6.addActionListener(this::MenuApplication6ActionPerformed);
		MenuApplication7.addActionListener(this::MenuApplication7ActionPerformed);

	}

	private void inverseButtonActionPerformed(ActionEvent ActionEvent) {
		if (observerRight.getCImage() == null && observerLeft.getCImage() == null)
			return;

		if (observerLeft.getCImage() != null && observerRight.getCImage() == null) {
			observerRight.setCImage(observerLeft.getCImage());
		}

		CImage imageRight = observerRight.getCImage();
		observerRight.setCImage(observerLeft.getCImage());
		observerLeft.setCImage(imageRight);

		if (imageRight instanceof CImageNG) {
			imageNG = (CImageNG) imageRight;
		} else if (imageRight instanceof CImageRGB) {
			imageRGB = (CImageRGB) imageRight;
		}

	}

	// <editor-fold defaultstate="collapsed" desc="Partie Image">
	private void activeMenusNG() {
		MenuDessiner.setEnabled(true);
		MenuFourier.setEnabled(true);
		MenuHistogramme.setEnabled(true);
		MenuFiltrageLineaire.setEnabled(true);
		MenuTraitementNonLineaire.setEnabled(true);
		MenuTraitementNonLineaireDilatationGeodesique.setEnabled(true);
		MenuTraitementNonLineaireReconstructionGeodesique.setEnabled(true);
		MenuContours.setEnabled(true);
		MenuSeuillage.setEnabled(true);
	}

	private void activeMenusRGB() {
		MenuDessiner.setEnabled(true);
		MenuFourier.setEnabled(false);
		MenuHistogramme.setEnabled(false);
		MenuFiltrageLineaire.setEnabled(true);
		MenuTraitementNonLineaire.setEnabled(true);
		MenuTraitementNonLineaireDilatationGeodesique.setEnabled(false);
		MenuTraitementNonLineaireReconstructionGeodesique.setEnabled(false);
		MenuContours.setEnabled(true);
		MenuSeuillage.setEnabled(false);
	}

	private void MenuItemImageNouvelleRGBActionPerformed(java.awt.event.ActionEvent evt) {
		JDialogNouvelleCImageRGB dialog = new JDialogNouvelleCImageRGB(this, true);
		dialog.setVisible(true);
		imageRGB = dialog.getCImageRGB();
		observerLeft.setCImage(imageRGB);
		imageNG = null;
		activeMenusRGB();
	}

	private void MenuItemImageNouvelleNGActionPerformed(java.awt.event.ActionEvent evt) {
		JDialogNouvelleCImageNG dialog = new JDialogNouvelleCImageNG(this, true);
		dialog.setVisible(true);
		imageNG = dialog.getCImageNG();
		observerLeft.setCImage(imageNG);
		imageRGB = null;
		activeMenusNG();
	}

	private void MenuItemImageOuvrirRGBActionPerformed(java.awt.event.ActionEvent evt) {
		JFileChooser choix = new JFileChooser();
		File fichier;

		choix.setCurrentDirectory(new File("."));
		if (choix.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
			fichier = choix.getSelectedFile();
			if (fichier != null) {
				try {
					imageRGB = new CImageRGB(fichier);
					observerLeft.setCImage(imageRGB);
					imageNG = null;
					activeMenusRGB();
				} catch (IOException ex) {
					System.err.println("Erreur I/O : " + ex.getMessage());
				}
			}
		}
	}

	private void MenuItemImageOuvrirNGActionPerformed(java.awt.event.ActionEvent evt) {
		JFileChooser choix = new JFileChooser();
		File fichier;

		choix.setCurrentDirectory(new File("."));
		if (choix.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
			fichier = choix.getSelectedFile();
			if (fichier != null) {
				try {
					imageNG = new CImageNG(fichier);
					observerLeft.setCImage(imageNG);
					imageRGB = null;
					activeMenusNG();
				} catch (IOException ex) {
					System.err.println("Erreur I/O : " + ex.getMessage());
				}
			}
		}
	}

	private void MenuItemImageEnregistrerSousActionPerformed(java.awt.event.ActionEvent evt) {
		JFileChooser choix = new JFileChooser();
		File fichier;

		choix.setCurrentDirectory(new File("."));
		if (choix.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
			fichier = choix.getSelectedFile();
			if (fichier != null) {
				try {
					if (imageRGB != null)
						imageRGB.enregistreFormatPNG(fichier);
					if (imageNG != null)
						imageNG.enregistreFormatPNG(fichier);
				} catch (IOException ex) {
					System.err.println("Erreur I/O : " + ex.getMessage());
				}
			}
		}
	}

	private void MenuItemImageQuitterActionPerformed(java.awt.event.ActionEvent evt) {
		System.exit(0);
	}

	// </editor-fold>
	// <editor-fold defaultstate="collapsed" desc="Partie Dessiner">
	private void MenuItemDessinerCouleurPinceauActionPerformed(java.awt.event.ActionEvent evt) {
		if (imageRGB != null) {
			Color newC = JColorChooser.showDialog(this, "Couleur du pinceau", couleurPinceauRGB);
			if (newC != null)
				couleurPinceauRGB = newC;
			observerLeft.setCouleurPinceau(couleurPinceauRGB);
		}

		if (imageNG != null) {
			JDialogChoixCouleurNG dialog = new JDialogChoixCouleurNG(this, true, couleurPinceauNG);
			dialog.setVisible(true);
			couleurPinceauNG = dialog.getCouleur();
		}
	}

	private void CheckBoxMenuItemDessinerPixelActionPerformed(java.awt.event.ActionEvent evt) {
		if (!CheckBoxMenuItemDessinerPixel.isSelected())
			observerLeft.setMode(JLabelBeanCImage.INACTIF);
		else {
			CheckBoxMenuItemDessinerPixel.setSelected(true);
			CheckBoxMenuItemDessinerLigne.setSelected(false);
			CheckBoxMenuItemDessinerRectangle.setSelected(false);
			CheckBoxMenuItemDessinerRectanglePlein.setSelected(false);
			CheckBoxMenuItemDessinerCercle.setSelected(false);
			CheckBoxMenuItemDessinerCerclePlein.setSelected(false);
			observerLeft.setMode(JLabelBeanCImage.CLIC);
		}
	}

	private void CheckBoxMenuItemDessinerLigneActionPerformed(java.awt.event.ActionEvent evt) {
		if (!CheckBoxMenuItemDessinerLigne.isSelected())
			observerLeft.setMode(JLabelBeanCImage.INACTIF);
		else {
			CheckBoxMenuItemDessinerPixel.setSelected(false);
			CheckBoxMenuItemDessinerLigne.setSelected(true);
			CheckBoxMenuItemDessinerRectangle.setSelected(false);
			CheckBoxMenuItemDessinerRectanglePlein.setSelected(false);
			CheckBoxMenuItemDessinerCercle.setSelected(false);
			CheckBoxMenuItemDessinerCerclePlein.setSelected(false);
			observerLeft.setMode(JLabelBeanCImage.SELECT_LIGNE);
		}
	}

	private void CheckBoxMenuItemDessinerRectangleActionPerformed(java.awt.event.ActionEvent evt) {
		if (!CheckBoxMenuItemDessinerRectangle.isSelected())
			observerLeft.setMode(JLabelBeanCImage.INACTIF);
		else {
			CheckBoxMenuItemDessinerPixel.setSelected(false);
			CheckBoxMenuItemDessinerLigne.setSelected(false);
			CheckBoxMenuItemDessinerRectangle.setSelected(true);
			CheckBoxMenuItemDessinerRectanglePlein.setSelected(false);
			CheckBoxMenuItemDessinerCercle.setSelected(false);
			CheckBoxMenuItemDessinerCerclePlein.setSelected(false);
			observerLeft.setMode(JLabelBeanCImage.SELECT_RECT);
		}
	}

	private void CheckBoxMenuItemDessinerRectanglePleinActionPerformed(java.awt.event.ActionEvent evt) {
		if (!CheckBoxMenuItemDessinerRectanglePlein.isSelected())
			observerLeft.setMode(JLabelBeanCImage.INACTIF);
		else {
			CheckBoxMenuItemDessinerPixel.setSelected(false);
			CheckBoxMenuItemDessinerLigne.setSelected(false);
			CheckBoxMenuItemDessinerRectangle.setSelected(false);
			CheckBoxMenuItemDessinerRectanglePlein.setSelected(true);
			CheckBoxMenuItemDessinerCercle.setSelected(false);
			CheckBoxMenuItemDessinerCerclePlein.setSelected(false);
			observerLeft.setMode(JLabelBeanCImage.SELECT_RECT_FILL);
		}
	}

	private void CheckBoxMenuItemDessinerCercleActionPerformed(java.awt.event.ActionEvent evt) {
		if (!CheckBoxMenuItemDessinerCercle.isSelected())
			observerLeft.setMode(JLabelBeanCImage.INACTIF);
		else {
			CheckBoxMenuItemDessinerPixel.setSelected(false);
			CheckBoxMenuItemDessinerLigne.setSelected(false);
			CheckBoxMenuItemDessinerRectangle.setSelected(false);
			CheckBoxMenuItemDessinerRectanglePlein.setSelected(false);
			CheckBoxMenuItemDessinerCercle.setSelected(true);
			CheckBoxMenuItemDessinerCerclePlein.setSelected(false);
			observerLeft.setMode(JLabelBeanCImage.SELECT_CERCLE);
		}
	}

	private void CheckBoxMenuItemDessinerCerclePleinActionPerformed(java.awt.event.ActionEvent evt) {
		if (!CheckBoxMenuItemDessinerCerclePlein.isSelected())
			observerLeft.setMode(JLabelBeanCImage.INACTIF);
		else {
			CheckBoxMenuItemDessinerPixel.setSelected(false);
			CheckBoxMenuItemDessinerLigne.setSelected(false);
			CheckBoxMenuItemDessinerRectangle.setSelected(false);
			CheckBoxMenuItemDessinerRectanglePlein.setSelected(false);
			CheckBoxMenuItemDessinerCercle.setSelected(false);
			CheckBoxMenuItemDessinerCerclePlein.setSelected(true);
			observerLeft.setMode(JLabelBeanCImage.SELECT_CERCLE_FILL);
		}
	}

	// </editor-fold>
	// <editor-fold defaultstate="collapsed" desc="Partie Fourier">
	private MatriceComplexe getMatriceComplexe() throws CImageNGException {
		int[][] f_int = imageNG.getMatrice();
		double[][] f = new double[imageNG.getLargeur()][imageNG.getHauteur()];
		for (int i = 0; i < imageNG.getLargeur(); i++)
			for (int j = 0; j < imageNG.getHauteur(); j++)
				f[i][j] = f_int[i][j];

		System.out.println("Debut Fourier");
		MatriceComplexe fourier = Fourier.Fourier2D(f);
		System.out.println("Fin Fourier");
		return Fourier.decroise(fourier);
	}

	private void MenuItemFourierAfficherModuleActionPerformed(java.awt.event.ActionEvent evt) {
		try {
			MatriceComplexe fourier = getMatriceComplexe();
			double[][] module = fourier.getModule();

			JDialogAfficheMatriceDouble dialog = new JDialogAfficheMatriceDouble(this, false, module,
					"Fourier : Affichage du module");
			dialog.setVisible(true);
		} catch (CImageNGException ex) {
			System.out.println("Erreur CImageNG : " + ex.getMessage());
		}
	}

	private void MenuItemFourierAfficherPhaseActionPerformed(java.awt.event.ActionEvent evt) {
		try {
			MatriceComplexe fourier = getMatriceComplexe();
			double[][] phase = fourier.getPhase();

			JDialogAfficheMatriceDouble dialog = new JDialogAfficheMatriceDouble(this, false, phase,
					"Fourier : Affichage de la phase");
			dialog.setVisible(true);
		} catch (CImageNGException ex) {
			System.out.println("Erreur CImageNG : " + ex.getMessage());
		}
	}

	private void MenuItemFourierAfficherReelleActionPerformed(java.awt.event.ActionEvent evt) {
		try {
			MatriceComplexe fourier = getMatriceComplexe();
			double[][] partieReelle = fourier.getPartieReelle();

			JDialogAfficheMatriceDouble dialog = new JDialogAfficheMatriceDouble(this, false, partieReelle,
					"Fourier : Affichage de la partie reelle");
			dialog.setVisible(true);
		} catch (CImageNGException ex) {
			System.out.println("Erreur CImageNG : " + ex.getMessage());
		}

	}

	private void MenuItemFourierAfficherImaginaireActionPerformed(java.awt.event.ActionEvent evt) {
		try {
			MatriceComplexe fourier = getMatriceComplexe();
			double[][] partieImaginaire = fourier.getPartieImaginaire();

			JDialogAfficheMatriceDouble dialog = new JDialogAfficheMatriceDouble(this, false, partieImaginaire,
					"Fourier : Affichage de la partie imaginaire");
			dialog.setVisible(true);
		} catch (CImageNGException ex) {
			System.out.println("Erreur CImageNG : " + ex.getMessage());
		}
	}

	// </editor-fold>
	// <editor-fold defaultstate="collapsed" desc="Partie Histogramme">
	/*private void MenuItemHistogrammeAfficherActionPerformed(java.awt.event.ActionEvent evt) {
		int[] histo;
		try {
			int[][] f_int = imageNG.getMatrice();
			histo = Histogramme.Histogramme256(f_int);
			new Histogramme(f_int, histo);

		} catch (CImageNGException ex) {
			System.out.println("Erreur CImageNG : " + ex.getMessage());
		}

	}

	private void MenuItemHistogrammeRehaussementActionPerformed(java.awt.event.ActionEvent evt) {
		if (courbeHisto == null) {
			System.err.println("creez une courbe avant");
			return;
		}
		try {
			int[][] f_int = imageNG.getMatrice();
			int[][] imageRehausse = Histogramme.rehaussement(f_int, courbeHisto);

			int[] histo = Histogramme.Histogramme256(f_int);
			new Histogramme(f_int, histo);

			observerRight.setCImage(new CImageNG(imageRehausse));

		} catch (CImageNGException ex) {
			System.out.println("Erreur CImageNG : " + ex.getMessage());
		}

	}

	private void MenuItemHistogrammeCourbeSaturationActionPerformed(java.awt.event.ActionEvent evt) {
		JDialogChoose2numbers dialogButter = new JDialogChoose2numbers(this, true, "Smin", "Smax");
		String[] values = dialogButter.showDialog();
		if (values == null)
			return;
		int Smin = Integer.parseInt(values[0]);
		int Smax = Integer.parseInt(values[1]);

		courbeHisto = Histogramme.creeCourbeTonaleLineaireSaturation(Smin, Smax);

	}

	private void MenuItemHistogrammeCourbeGammaActionPerformed(java.awt.event.ActionEvent evt) {
		JDialogChooseNumber Number = new JDialogChooseNumber(this, true, "Gamma : ");
		double gamma = Number.showDialogDouble();

		courbeHisto = Histogramme.creeCourbeTonaleGamma(gamma);

	}

	private void MenuItemHistogrammeCourbeNegatifActionPerformed(java.awt.event.ActionEvent evt) {
		courbeHisto = Histogramme.creeCourbeTonaleNegatif();

	}

	private void MenuItemHistogrammeCourbeEgalisationActionPerformed(java.awt.event.ActionEvent evt) {
		try {
			int[][] f_int = imageNG.getMatrice();
			courbeHisto = Histogramme.creeCourbeTonaleEgalisation(f_int);

		} catch (CImageNGException ex) {
			System.out.println("Erreur CImageNG : " + ex.getMessage());
		}

	}*/

	// </editor-fold>
	// <editor-fold defaultstate="collapsed" desc="Partie DessinerClics">
	public void ClicDetected(UnClicEvent e) {
		if (CheckBoxMenuItemDessinerPixel.isSelected()) {
			try {
				if (imageRGB != null)
					imageRGB.setPixel(e.getX(), e.getY(), couleurPinceauRGB);
				if (imageNG != null)
					imageNG.setPixel(e.getX(), e.getY(), couleurPinceauNG);
			} catch (CImageRGBException ex) {
				System.out.println("Erreur RGB : " + ex.getMessage());
			} catch (CImageNGException ex) {
				System.out.println("Erreur NG : " + ex.getMessage());
			}
		}
	}

	public void SelectLigneDetected(DeuxClicsEvent e) {
		if (CheckBoxMenuItemDessinerLigne.isSelected()) {
			try {
				if (imageRGB != null)
					imageRGB.DessineLigne(e.getX1(), e.getY1(), e.getX2(), e.getY2(), couleurPinceauRGB);
				if (imageNG != null)
					imageNG.DessineLigne(e.getX1(), e.getY1(), e.getX2(), e.getY2(), couleurPinceauNG);
			} catch (CImageRGBException ex) {
				System.out.println("Erreur RGB : " + ex.getMessage());
			} catch (CImageNGException ex) {
				System.out.println("Erreur NG : " + ex.getMessage());
			}
		}
	}

	public void SelectRectDetected(DeuxClicsEvent e) {
		if (CheckBoxMenuItemDessinerRectangle.isSelected()) {
			try {
				if (imageRGB != null)
					imageRGB.DessineRect(e.getX1(), e.getY1(), e.getX2(), e.getY2(), couleurPinceauRGB);
				if (imageNG != null)
					imageNG.DessineRect(e.getX1(), e.getY1(), e.getX2(), e.getY2(), couleurPinceauNG);
			} catch (CImageRGBException ex) {
				System.out.println("Erreur RGB : " + ex.getMessage());
			} catch (CImageNGException ex) {
				System.out.println("Erreur NG : " + ex.getMessage());
			}
		}
	}

	public void SelectRectFillDetected(DeuxClicsEvent e) {
		if (CheckBoxMenuItemDessinerRectanglePlein.isSelected()) {
			try {
				if (imageRGB != null)
					imageRGB.RemplitRect(e.getX1(), e.getY1(), e.getX2(), e.getY2(), couleurPinceauRGB);
				if (imageNG != null)
					imageNG.RemplitRect(e.getX1(), e.getY1(), e.getX2(), e.getY2(), couleurPinceauNG);
			} catch (CImageRGBException ex) {
				System.out.println("Erreur RGB : " + ex.getMessage());
			} catch (CImageNGException ex) {
				System.out.println("Erreur NG : " + ex.getMessage());
			}
		}
	}

	public void SelectCercleDetected(DeuxClicsEvent e) {
		if (CheckBoxMenuItemDessinerCercle.isSelected()) {
			try {
				if (imageRGB != null)
					imageRGB.DessineCercle(e.getX1(), e.getY1(), e.getX2(), e.getY2(), couleurPinceauRGB);
				if (imageNG != null)
					imageNG.DessineCercle(e.getX1(), e.getY1(), e.getX2(), e.getY2(), couleurPinceauNG);
			} catch (CImageRGBException ex) {
				System.out.println("Erreur RGB : " + ex.getMessage());
			} catch (CImageNGException ex) {
				System.out.println("Erreur NG : " + ex.getMessage());
			}
		}
	}

	public void SelectCercleFillDetected(DeuxClicsEvent e) {
		if (CheckBoxMenuItemDessinerCerclePlein.isSelected()) {
			try {
				if (imageRGB != null)
					imageRGB.RemplitCercle(e.getX1(), e.getY1(), e.getX2(), e.getY2(), couleurPinceauRGB);
				if (imageNG != null)
					imageNG.RemplitCercle(e.getX1(), e.getY1(), e.getX2(), e.getY2(), couleurPinceauNG);
			} catch (CImageRGBException ex) {
				System.out.println("Erreur RGB : " + ex.getMessage());
			} catch (CImageNGException ex) {
				System.out.println("Erreur NG : " + ex.getMessage());
			}
		}
	}
	// </editor-fold>

	// <editor-fold defaultstate="collapsed" desc="Filtrage Lineaire Global">
	public void MenuFiltrageLineaireGlobalBasIdealActionPerformed(java.awt.event.ActionEvent evt) {
		System.out.println("Filtrage global passe-bas idéal");
		JDialogChooseNumber number = new JDialogChooseNumber(this, true, "Taille du masque : ");
		int numberChoosen = number.showDialog();
		if (imageNG != null) {
			try {
				int[][] f_int = imageNG.getMatrice();

				int[][] resultatFiltrePasseBas = FiltrageLineaireGlobal.filtrePasseBasIdeal(f_int, numberChoosen);

				CImageNG imageapresFilter = new CImageNG(resultatFiltrePasseBas);

				observerRight.setCImage(imageapresFilter);

			} catch (CImageNGException ex) {
				System.out.println("Erreur CImageNG : " + ex.getMessage());
			}
		} else if (imageRGB != null) {
			try {
				int[][] imageR = new int[imageRGB.getLargeur()][imageRGB.getHauteur()];
				int[][] imageG = new int[imageRGB.getLargeur()][imageRGB.getHauteur()];
				int[][] imageB = new int[imageRGB.getLargeur()][imageRGB.getHauteur()];

				imageRGB.getMatricesRGB(imageR, imageG, imageB);

				int[][] imageRAfter = FiltrageLineaireGlobal.filtrePasseBasIdeal(imageR, numberChoosen);
				int[][] imageGAfter = FiltrageLineaireGlobal.filtrePasseBasIdeal(imageG, numberChoosen);
				int[][] imageBAfter = FiltrageLineaireGlobal.filtrePasseBasIdeal(imageB, numberChoosen);

				CImageRGB imageapresFilter = new CImageRGB(imageRAfter, imageGAfter, imageBAfter);

				observerRight.setCImage(imageapresFilter);
			} catch (CImageRGBException ex) {
				System.out.println("Erreur CImageNG : " + ex.getMessage());
			}
		}

	}

	public void MenuFiltrageLineaireGlobalHautIdealActionPerformed(java.awt.event.ActionEvent evt) {
		System.out.println("Filtrage global passe-haut idéal");
		JDialogChooseNumber number = new JDialogChooseNumber(this, true, "Taille du masque : ");
		int numberChoosen = number.showDialog();
		if (imageNG != null) {
			try {
				int[][] f_int = imageNG.getMatrice();

				int[][] resultatFiltrePasseBas = FiltrageLineaireGlobal.filtrePasseHautIdeal(f_int, numberChoosen);

				CImageNG imageapresFilter = new CImageNG(resultatFiltrePasseBas);

				observerRight.setCImage(imageapresFilter);

			} catch (CImageNGException ex) {
				System.out.println("Erreur CImageNG : " + ex.getMessage());
			}
		} else if (imageRGB != null) {
			try {
				int[][] imageR = new int[imageRGB.getLargeur()][imageRGB.getHauteur()];
				int[][] imageG = new int[imageRGB.getLargeur()][imageRGB.getHauteur()];
				int[][] imageB = new int[imageRGB.getLargeur()][imageRGB.getHauteur()];

				imageRGB.getMatricesRGB(imageR, imageG, imageB);

				int[][] imageRAfter = FiltrageLineaireGlobal.filtrePasseHautIdeal(imageR, numberChoosen);
				int[][] imageGAfter = FiltrageLineaireGlobal.filtrePasseHautIdeal(imageG, numberChoosen);
				int[][] imageBAfter = FiltrageLineaireGlobal.filtrePasseHautIdeal(imageB, numberChoosen);

				CImageRGB imageapresFilter = new CImageRGB(imageRAfter, imageGAfter, imageBAfter);

				observerRight.setCImage(imageapresFilter);
			} catch (CImageRGBException ex) {
				System.out.println("Erreur CImageRGB : " + ex.getMessage());
			}
		}

	}

	public void MenuFiltrageLineaireGlobalBasButterActionPerformed(java.awt.event.ActionEvent evt) {
		System.out.println("Filtrage global passe-bas Butterworth");
		JDialogChoose2numbers dialogButter = new JDialogChoose2numbers(this, true, "Frequence de coupure",
				"ordre du filtre");
		String[] values = dialogButter.showDialog();
		int FreqCoup = Integer.parseInt(values[0]);
		int ordreFiltre = Integer.parseInt(values[1]);
		if (imageNG != null) {
			try {
				int[][] f_int = imageNG.getMatrice();

				System.out.println("f : " + FreqCoup + " ordre : " + ordreFiltre);
				int[][] resultatFiltrePasseBas = FiltrageLineaireGlobal.filtrePasseBasButterworth(f_int, FreqCoup,
						ordreFiltre);
				System.out.println("test");
				CImageNG imageapresFilter = new CImageNG(resultatFiltrePasseBas);

				observerRight.setCImage(imageapresFilter);
			} catch (CImageNGException ex) {
				System.out.println("Erreur CImageNG : " + ex.getMessage());
			}
		} else if (imageRGB != null) {
			try {
				int[][] imageR = new int[imageRGB.getLargeur()][imageRGB.getHauteur()];
				int[][] imageG = new int[imageRGB.getLargeur()][imageRGB.getHauteur()];
				int[][] imageB = new int[imageRGB.getLargeur()][imageRGB.getHauteur()];

				imageRGB.getMatricesRGB(imageR, imageG, imageB);

				int[][] imageRAfter = FiltrageLineaireGlobal.filtrePasseBasButterworth(imageR, FreqCoup, ordreFiltre);
				int[][] imageGAfter = FiltrageLineaireGlobal.filtrePasseBasButterworth(imageG, FreqCoup, ordreFiltre);
				int[][] imageBAfter = FiltrageLineaireGlobal.filtrePasseBasButterworth(imageB, FreqCoup, ordreFiltre);

				CImageRGB imageapresFilter = new CImageRGB(imageRAfter, imageGAfter, imageBAfter);

				observerRight.setCImage(imageapresFilter);
			} catch (CImageRGBException ex) {
				System.out.println("Erreur CImageRGB : " + ex.getMessage());
			}
		}

	}

	public void MenuFiltrageLineaireGlobalHautButterActionPerformed(java.awt.event.ActionEvent evt) {
		System.out.println("Filtrage global passe-haut Butterworth");

		JDialogChoose2numbers dialogButter = new JDialogChoose2numbers(this, true, "Frequence de coupure",
				"ordre du filtre");
		String[] values = dialogButter.showDialog();
		int FreqCoup = Integer.parseInt(values[0]);
		int ordreFiltre = Integer.parseInt(values[1]);
		if (imageNG != null) {
			try {
				int[][] f_int = imageNG.getMatrice();

				int[][] resultatFiltrePasseBas = FiltrageLineaireGlobal.filtrePasseHautButterworth(f_int, FreqCoup,
						ordreFiltre);

				observerRight.setCImage(new CImageNG(resultatFiltrePasseBas));
			} catch (CImageNGException ex) {
				System.out.println("Erreur CImageNG : " + ex.getMessage());
			}
		} else if (imageRGB != null) {
			try {
				int[][] imageR = new int[imageRGB.getLargeur()][imageRGB.getHauteur()];
				int[][] imageG = new int[imageRGB.getLargeur()][imageRGB.getHauteur()];
				int[][] imageB = new int[imageRGB.getLargeur()][imageRGB.getHauteur()];

				imageRGB.getMatricesRGB(imageR, imageG, imageB);

				int[][] imageRAfter = FiltrageLineaireGlobal.filtrePasseHautButterworth(imageR, FreqCoup, ordreFiltre);
				int[][] imageGAfter = FiltrageLineaireGlobal.filtrePasseHautButterworth(imageG, FreqCoup, ordreFiltre);
				int[][] imageBAfter = FiltrageLineaireGlobal.filtrePasseHautButterworth(imageB, FreqCoup, ordreFiltre);

				observerRight.setCImage(new CImageRGB(imageRAfter, imageGAfter, imageBAfter));
			} catch (CImageRGBException ex) {
				System.out.println("Erreur CImageRGB : " + ex.getMessage());
			}
		}

	}

	// </editor-fold>
	// <editor-fold defaultstate="collapsed" desc="Filtrage Lineaire Local">
	public void MenuFiltrageLineaireLocalConvolutionActionPerformed(java.awt.event.ActionEvent evt) {
		System.out.println("Filtrage local avec masque de convolution");

		JDialogChoose2numbers dialogButter = new JDialogChoose2numbers(this, true, "Taille du masque",
				"Valeur dans le masque");
		String[] values = dialogButter.showDialog();
		int numberChoosen = Integer.parseInt(values[0]);
		double masqueValues = Double.parseDouble(values[1]);
		double[][] matConvo = new double[numberChoosen][numberChoosen];
		for (int i = 0; i < numberChoosen; i++) {
			for (int j = 0; j < numberChoosen; j++) {
				matConvo[i][j] = masqueValues;
			}
		}
		if (imageNG != null) {
			try {
				int[][] f_int = imageNG.getMatrice();
				int[][] resultatConvolution = FiltrageLineaireLocal.filtreMasqueConvolution(f_int, matConvo);
				observerRight.setCImage(new CImageNG(resultatConvolution));
			} catch (CImageNGException ex) {
				System.out.println("Erreur CImageNG : " + ex.getMessage());
			}
		} else if (imageRGB != null) {
			try {
				int[][] imageR = new int[imageRGB.getLargeur()][imageRGB.getHauteur()];
				int[][] imageG = new int[imageRGB.getLargeur()][imageRGB.getHauteur()];
				int[][] imageB = new int[imageRGB.getLargeur()][imageRGB.getHauteur()];

				imageRGB.getMatricesRGB(imageR, imageG, imageB);

				int[][] imageRAfter = FiltrageLineaireLocal.filtreMasqueConvolution(imageR, matConvo);
				int[][] imageGAfter = FiltrageLineaireLocal.filtreMasqueConvolution(imageG, matConvo);
				int[][] imageBAfter = FiltrageLineaireLocal.filtreMasqueConvolution(imageB, matConvo);

				observerRight.setCImage(new CImageRGB(imageRAfter, imageGAfter, imageBAfter));
			} catch (CImageRGBException ex) {
				ex.printStackTrace();
			}
		}

	}

	public void MenuFiltrageLineaireLocalMoyenneurActionPerformed(java.awt.event.ActionEvent evt) {
		System.out.println("Filtrage local avec filtre moyenneur");
		JDialogChooseNumber number = new JDialogChooseNumber(this, true, "Taille du masque : ");
		int numberChoosen = number.showDialog();

		if (imageNG != null) {
			try {
				int[][] f_int = imageNG.getMatrice();

				int[][] resultatFiltrePasseBas = FiltrageLineaireLocal.filtreMoyenneur(f_int, numberChoosen);

				observerRight.setCImage(new CImageNG(resultatFiltrePasseBas));
			} catch (CImageNGException ex) {
				System.out.println("Erreur CImageNG : " + ex.getMessage());
			}
		} else if (imageRGB != null) {
			try {
				int[][] imageR = new int[imageRGB.getLargeur()][imageRGB.getHauteur()];
				int[][] imageG = new int[imageRGB.getLargeur()][imageRGB.getHauteur()];
				int[][] imageB = new int[imageRGB.getLargeur()][imageRGB.getHauteur()];

				imageRGB.getMatricesRGB(imageR, imageG, imageB);

				int[][] imageRAfter = FiltrageLineaireLocal.filtreMoyenneur(imageR, numberChoosen);
				int[][] imageGAfter = FiltrageLineaireLocal.filtreMoyenneur(imageG, numberChoosen);
				int[][] imageBAfter = FiltrageLineaireLocal.filtreMoyenneur(imageB, numberChoosen);

				observerRight.setCImage(new CImageRGB(imageRAfter, imageGAfter, imageBAfter));
			} catch (CImageRGBException ex) {
				ex.printStackTrace();
			}
		}

	}

	// </editor-fold>
	// <editor-fold defaultstate="collapsed" desc="Filtrage non-lineaire">
	public void MenuTraitementNonLineaireErosionActionPerformed(java.awt.event.ActionEvent evt) {
		JDialogChooseNumber number = new JDialogChooseNumber(this, true, "Taille du masque : ");
		int numberChoosen = number.showDialog();

		if (imageNG != null) {
			try {
				int[][] f_int = imageNG.getMatrice();
				int[][] resultatErosion = MorphoElementaire.erosion(f_int, numberChoosen);
				observerRight.setCImage(new CImageNG(resultatErosion));
			} catch (CImageNGException ex) {
				System.out.println("Erreur CImageNG : " + ex.getMessage());
			}
		} else if (imageRGB != null) {
			try {
				int[][] imageR = new int[imageRGB.getLargeur()][imageRGB.getHauteur()];
				int[][] imageG = new int[imageRGB.getLargeur()][imageRGB.getHauteur()];
				int[][] imageB = new int[imageRGB.getLargeur()][imageRGB.getHauteur()];

				imageRGB.getMatricesRGB(imageR, imageG, imageB);

				int[][] imageRAfter = MorphoElementaire.erosion(imageR, numberChoosen);
				int[][] imageGAfter = MorphoElementaire.erosion(imageG, numberChoosen);
				int[][] imageBAfter = MorphoElementaire.erosion(imageB, numberChoosen);

				observerRight.setCImage(new CImageRGB(imageRAfter, imageGAfter, imageBAfter));
			} catch (CImageRGBException ex) {
				ex.printStackTrace();
			}
		}

	}

	public void MenuTraitementNonLineaireDilatationActionPerformed(java.awt.event.ActionEvent evt) {
		JDialogChooseNumber number = new JDialogChooseNumber(this, true, "Taille du masque : ");
		int numberChoosen = number.showDialog();

		if (imageNG != null) {
			try {
				int[][] f_int = imageNG.getMatrice();
				int[][] resultatErosion = MorphoElementaire.dilatation(f_int, numberChoosen);
				observerRight.setCImage(new CImageNG(resultatErosion));
			} catch (CImageNGException ex) {
				System.out.println("Erreur CImageNG : " + ex.getMessage());
			}
		} else if (imageRGB != null) {
			try {
				int[][] imageR = new int[imageRGB.getLargeur()][imageRGB.getHauteur()];
				int[][] imageG = new int[imageRGB.getLargeur()][imageRGB.getHauteur()];
				int[][] imageB = new int[imageRGB.getLargeur()][imageRGB.getHauteur()];

				imageRGB.getMatricesRGB(imageR, imageG, imageB);

				int[][] imageRAfter = MorphoElementaire.dilatation(imageR, numberChoosen);
				int[][] imageGAfter = MorphoElementaire.dilatation(imageG, numberChoosen);
				int[][] imageBAfter = MorphoElementaire.dilatation(imageB, numberChoosen);

				observerRight.setCImage(new CImageRGB(imageRAfter, imageGAfter, imageBAfter));
			} catch (CImageRGBException ex) {
				ex.printStackTrace();
			}
		}
	}

	public void MenuTraitementNonLineaireOuvertureActionPerformed(java.awt.event.ActionEvent evt) {
		JDialogChooseNumber number = new JDialogChooseNumber(this, true, "Taille du masque : ");
		int numberChoosen = number.showDialog();

		if (imageNG != null) {
			try {
				int[][] f_int = imageNG.getMatrice();
				int[][] resultatOuverture = MorphoElementaire.ouverture(f_int, numberChoosen);
				observerRight.setCImage(new CImageNG(resultatOuverture));
			} catch (CImageNGException ex) {
				System.out.println("Erreur CImageNG : " + ex.getMessage());
			}
		} else if (imageRGB != null) {
			try {
				int[][] imageR = new int[imageRGB.getLargeur()][imageRGB.getHauteur()];
				int[][] imageG = new int[imageRGB.getLargeur()][imageRGB.getHauteur()];
				int[][] imageB = new int[imageRGB.getLargeur()][imageRGB.getHauteur()];

				imageRGB.getMatricesRGB(imageR, imageG, imageB);

				int[][] imageRAfter = MorphoElementaire.ouverture(imageR, numberChoosen);
				int[][] imageGAfter = MorphoElementaire.ouverture(imageG, numberChoosen);
				int[][] imageBAfter = MorphoElementaire.ouverture(imageB, numberChoosen);

				observerRight.setCImage(new CImageRGB(imageRAfter, imageGAfter, imageBAfter));
			} catch (CImageRGBException ex) {
				ex.printStackTrace();
			}
		}
	}

	public void MenuTraitementNonLineaireFermetureActionPerformed(java.awt.event.ActionEvent evt) {
		JDialogChooseNumber number = new JDialogChooseNumber(this, true, "Taille du masque : ");
		int numberChoosen = number.showDialog();

		if (imageNG != null) {
			try {
				int[][] f_int = imageNG.getMatrice();
				int[][] resultatFermeture = MorphoElementaire.fermeture(f_int, numberChoosen);
				observerRight.setCImage(new CImageNG(resultatFermeture));
			} catch (CImageNGException ex) {
				System.out.println("Erreur CImageNG : " + ex.getMessage());
			}
		} else if (imageRGB != null) {
			try {
				int[][] imageR = new int[imageRGB.getLargeur()][imageRGB.getHauteur()];
				int[][] imageG = new int[imageRGB.getLargeur()][imageRGB.getHauteur()];
				int[][] imageB = new int[imageRGB.getLargeur()][imageRGB.getHauteur()];

				imageRGB.getMatricesRGB(imageR, imageG, imageB);

				int[][] imageRAfter = MorphoElementaire.fermeture(imageR, numberChoosen);
				int[][] imageGAfter = MorphoElementaire.fermeture(imageG, numberChoosen);
				int[][] imageBAfter = MorphoElementaire.fermeture(imageB, numberChoosen);

				observerRight.setCImage(new CImageRGB(imageRAfter, imageGAfter, imageBAfter));
			} catch (CImageRGBException ex) {
				ex.printStackTrace();
			}
		}
	}

	public void MenuTraitementNonLineaireDilatationGeodesiqueActionPerformed(java.awt.event.ActionEvent evt) {
		try {
			int[][] f_int = imageNG.getMatrice();

			JDialogChooseNumber number = new JDialogChooseNumber(this, true, "Nombre d'iteration : ");
			int nbrIter = number.showDialog();

			CImageNG imageDilate = (CImageNG) observerRight.getCImage();
			int[][] imageMat = imageDilate.getMatrice();

			int[][] resultatFermeture = MorphoComplexe.dilatationGeodesique(imageMat, f_int, nbrIter);

			CImageNG imageapresFilter = new CImageNG(resultatFermeture);
			System.out.println("apres");

			observerRight.setCImage(imageapresFilter);
		} catch (CImageNGException ex) {
			System.out.println("Erreur CImageNG : " + ex.getMessage());
		}
	}

	public void MenuTraitementNonLineaireReconstructionGeodesiqueActionPerformed(java.awt.event.ActionEvent evt) {
		try {
			int[][] f_int = imageNG.getMatrice();

			CImageNG imageDilate = (CImageNG) observerRight.getCImage();
			int[][] imageMat = imageDilate.getMatrice();

			int[][] resultatReconstruction = MorphoComplexe.reconstructionGeodesique(imageMat, f_int);

			CImageNG imageapresFilter = new CImageNG(resultatReconstruction);
			observerRight.setCImage(imageapresFilter);
		} catch (CImageNGException ex) {
			System.out.println("Erreur CImageNG : " + ex.getMessage());
		}
	}

	public void MenuTraitementNonLineaireFiltreMedianActionPerformed(java.awt.event.ActionEvent evt) {
		JDialogChooseNumber number = new JDialogChooseNumber(this, true, "Taille du masque : ");
		int maskSize = number.showDialog();
		if (imageNG != null) {
			try {
				int[][] f_int = imageNG.getMatrice();

				int[][] resultatFermeture = MorphoComplexe.filtreMedian(f_int, maskSize);

				observerRight.setCImage(new CImageNG(resultatFermeture));
			} catch (CImageNGException ex) {
				System.out.println("Erreur CImageNG : " + ex.getMessage());
			}
		} else if (imageRGB != null) {
			try {
				int[][] imageR = new int[imageRGB.getLargeur()][imageRGB.getHauteur()];
				int[][] imageG = new int[imageRGB.getLargeur()][imageRGB.getHauteur()];
				int[][] imageB = new int[imageRGB.getLargeur()][imageRGB.getHauteur()];

				imageRGB.getMatricesRGB(imageR, imageG, imageB);

				int[][] imageRAfter = MorphoComplexe.filtreMedian(imageR, maskSize);
				int[][] imageGAfter = MorphoComplexe.filtreMedian(imageG, maskSize);
				int[][] imageBAfter = MorphoComplexe.filtreMedian(imageB, maskSize);

				observerRight.setCImage(new CImageRGB(imageRAfter, imageGAfter, imageBAfter));
			} catch (CImageRGBException ex) {
				ex.printStackTrace();
			}
		}
	}

	// </editor-fold>
	// <editor-fold defaultstate="collapsed" desc="Contours lineaire">
	public void MenuContoursLineairePrewittActionPerformed(java.awt.event.ActionEvent evt) {
		JDialogChooseNumber number = new JDialogChooseNumber(this, true, "1 pour horizontal, 2 pour vertical");
		int horVer = number.showDialog();

		if (horVer != 1 && horVer != 2) {
			System.err.println("veuillez choisir 1 ou 2");
			return;
		}
		if (imageNG != null) {
			try {
				int[][] f_int = imageNG.getMatrice();
				int[][] imageAfter = ContoursLineaire.gradientPrewitt(f_int, horVer);
				observerRight.setCImage(new CImageNG(imageAfter));
			} catch (Exception e) {
				e.printStackTrace();
			}

		} else if (imageRGB != null) {
			try {
				int[][] imageR = new int[imageRGB.getLargeur()][imageRGB.getHauteur()];
				int[][] imageG = new int[imageRGB.getLargeur()][imageRGB.getHauteur()];
				int[][] imageB = new int[imageRGB.getLargeur()][imageRGB.getHauteur()];

				imageRGB.getMatricesRGB(imageR, imageG, imageB);

				int[][] imageRAfter = ContoursLineaire.gradientPrewitt(imageR, horVer);
				int[][] imageGAfter = ContoursLineaire.gradientPrewitt(imageG, horVer);
				int[][] imageBAfter = ContoursLineaire.gradientPrewitt(imageB, horVer);

				observerRight.setCImage(new CImageRGB(imageRAfter, imageGAfter, imageBAfter));
			} catch (CImageRGBException ex) {
				ex.printStackTrace();
			}
		}
	}

	public void MenuContoursLineaireSobelActionPerformed(java.awt.event.ActionEvent evt) {
		JDialogChooseNumber number = new JDialogChooseNumber(this, true, "1 pour horizontal, 2 pour vertical");
		int horVer = number.showDialog();

		if (horVer != 1 && horVer != 2) {
			System.err.println("veuillez choisir 1 ou 2");
			return;
		}
		if (imageNG != null) {
			try {
				int[][] f_int = imageNG.getMatrice();
				int[][] imageAfter = ContoursLineaire.gradientSobel(f_int, horVer);
				observerRight.setCImage(new CImageNG(imageAfter));
			} catch (Exception e) {
				e.printStackTrace();
			}

		} else if (imageRGB != null) {
			try {
				int[][] imageR = new int[imageRGB.getLargeur()][imageRGB.getHauteur()];
				int[][] imageG = new int[imageRGB.getLargeur()][imageRGB.getHauteur()];
				int[][] imageB = new int[imageRGB.getLargeur()][imageRGB.getHauteur()];

				imageRGB.getMatricesRGB(imageR, imageG, imageB);

				int[][] imageRAfter = ContoursLineaire.gradientSobel(imageR, horVer);
				int[][] imageGAfter = ContoursLineaire.gradientSobel(imageG, horVer);
				int[][] imageBAfter = ContoursLineaire.gradientSobel(imageB, horVer);

				observerRight.setCImage(new CImageRGB(imageRAfter, imageGAfter, imageBAfter));
			} catch (CImageRGBException ex) {
				ex.printStackTrace();
			}
		}
	}

	public void MenuContoursLineaireLaplacien4ActionPerformed(java.awt.event.ActionEvent evt) {
		if (imageNG != null) {
			try {
				int[][] f_int = imageNG.getMatrice();
				int[][] imageAfter = ContoursLineaire.laplacien4(f_int);
				observerRight.setCImage(new CImageNG(imageAfter));
			} catch (Exception e) {
				e.printStackTrace();
			}

		} else if (imageRGB != null) {
			try {
				int[][] imageR = new int[imageRGB.getLargeur()][imageRGB.getHauteur()];
				int[][] imageG = new int[imageRGB.getLargeur()][imageRGB.getHauteur()];
				int[][] imageB = new int[imageRGB.getLargeur()][imageRGB.getHauteur()];

				imageRGB.getMatricesRGB(imageR, imageG, imageB);

				int[][] imageRAfter = ContoursLineaire.laplacien4(imageR);
				int[][] imageGAfter = ContoursLineaire.laplacien4(imageG);
				int[][] imageBAfter = ContoursLineaire.laplacien4(imageB);

				observerRight.setCImage(new CImageRGB(imageRAfter, imageGAfter, imageBAfter));
			} catch (CImageRGBException ex) {
				ex.printStackTrace();
			}
		}
	}

	public void MenuContoursLineaireLaplacien8ActionPerformed(java.awt.event.ActionEvent evt) {
		if (imageNG != null) {
			try {
				int[][] f_int = imageNG.getMatrice();
				int[][] imageAfter = ContoursLineaire.laplacien8(f_int);
				observerRight.setCImage(new CImageNG(imageAfter));
			} catch (Exception e) {
				e.printStackTrace();
			}

		} else if (imageRGB != null) {
			try {
				int[][] imageR = new int[imageRGB.getLargeur()][imageRGB.getHauteur()];
				int[][] imageG = new int[imageRGB.getLargeur()][imageRGB.getHauteur()];
				int[][] imageB = new int[imageRGB.getLargeur()][imageRGB.getHauteur()];

				imageRGB.getMatricesRGB(imageR, imageG, imageB);

				int[][] imageRAfter = ContoursLineaire.laplacien8(imageR);
				int[][] imageGAfter = ContoursLineaire.laplacien8(imageG);
				int[][] imageBAfter = ContoursLineaire.laplacien8(imageB);

				observerRight.setCImage(new CImageRGB(imageRAfter, imageGAfter, imageBAfter));
			} catch (CImageRGBException ex) {
				ex.printStackTrace();
			}
		}
	}

	// </editor-fold>
	// <editor-fold defaultstate="collapsed" desc="Contours non lineaire">
	public void MenuContoursNonLineaireErosionActionPerformed(java.awt.event.ActionEvent evt) {
		if (imageNG != null) {
			try {
				int[][] f_int = imageNG.getMatrice();
				int[][] imageAfter = ContoursNonLineaire.gradientErosion(f_int);
				observerRight.setCImage(new CImageNG(imageAfter));
			} catch (Exception e) {
				e.printStackTrace();
			}

		} else if (imageRGB != null) {
			try {
				int[][] imageR = new int[imageRGB.getLargeur()][imageRGB.getHauteur()];
				int[][] imageG = new int[imageRGB.getLargeur()][imageRGB.getHauteur()];
				int[][] imageB = new int[imageRGB.getLargeur()][imageRGB.getHauteur()];

				imageRGB.getMatricesRGB(imageR, imageG, imageB);

				int[][] imageRAfter = ContoursNonLineaire.gradientErosion(imageR);
				int[][] imageGAfter = ContoursNonLineaire.gradientErosion(imageG);
				int[][] imageBAfter = ContoursNonLineaire.gradientErosion(imageB);

				observerRight.setCImage(new CImageRGB(imageRAfter, imageGAfter, imageBAfter));
			} catch (CImageRGBException ex) {
				ex.printStackTrace();
			}
		}
	}

	public void MenuContoursNonLineaireDilatationActionPerformed(java.awt.event.ActionEvent evt) {
		if (imageNG != null) {
			try {
				int[][] f_int = imageNG.getMatrice();
				int[][] imageAfter = ContoursNonLineaire.gradientDilatation(f_int);
				observerRight.setCImage(new CImageNG(imageAfter));
			} catch (Exception e) {
				e.printStackTrace();
			}

		} else if (imageRGB != null) {
			try {
				int[][] imageR = new int[imageRGB.getLargeur()][imageRGB.getHauteur()];
				int[][] imageG = new int[imageRGB.getLargeur()][imageRGB.getHauteur()];
				int[][] imageB = new int[imageRGB.getLargeur()][imageRGB.getHauteur()];

				imageRGB.getMatricesRGB(imageR, imageG, imageB);

				int[][] imageRAfter = ContoursNonLineaire.gradientDilatation(imageR);
				int[][] imageGAfter = ContoursNonLineaire.gradientDilatation(imageG);
				int[][] imageBAfter = ContoursNonLineaire.gradientDilatation(imageB);

				observerRight.setCImage(new CImageRGB(imageRAfter, imageGAfter, imageBAfter));
			} catch (CImageRGBException ex) {
				ex.printStackTrace();
			}
		}
	}

	public void MenuContoursNonLineaireBeucherActionPerformed(java.awt.event.ActionEvent evt) {
		if (imageNG != null) {
			try {
				int[][] f_int = imageNG.getMatrice();
				int[][] imageAfter = ContoursNonLineaire.gradientBeucher(f_int);
				observerRight.setCImage(new CImageNG(imageAfter));
			} catch (Exception e) {
				e.printStackTrace();
			}

		} else if (imageRGB != null) {
			try {
				int[][] imageR = new int[imageRGB.getLargeur()][imageRGB.getHauteur()];
				int[][] imageG = new int[imageRGB.getLargeur()][imageRGB.getHauteur()];
				int[][] imageB = new int[imageRGB.getLargeur()][imageRGB.getHauteur()];

				imageRGB.getMatricesRGB(imageR, imageG, imageB);

				int[][] imageRAfter = ContoursNonLineaire.gradientBeucher(imageR);
				int[][] imageGAfter = ContoursNonLineaire.gradientBeucher(imageG);
				int[][] imageBAfter = ContoursNonLineaire.gradientBeucher(imageB);

				observerRight.setCImage(new CImageRGB(imageRAfter, imageGAfter, imageBAfter));
			} catch (CImageRGBException ex) {
				ex.printStackTrace();
			}
		}
	}

	public void MenuContoursNonLineaireLaplacienActionPerformed(java.awt.event.ActionEvent evt) {
		if (imageNG != null) {
			try {
				int[][] f_int = imageNG.getMatrice();
				int[][] imageAfter = ContoursNonLineaire.laplacienNonLineaire(f_int);
				observerRight.setCImage(new CImageNG(imageAfter));
			} catch (Exception e) {
				e.printStackTrace();
			}

		} else if (imageRGB != null) {
			try {
				int[][] imageR = new int[imageRGB.getLargeur()][imageRGB.getHauteur()];
				int[][] imageG = new int[imageRGB.getLargeur()][imageRGB.getHauteur()];
				int[][] imageB = new int[imageRGB.getLargeur()][imageRGB.getHauteur()];

				imageRGB.getMatricesRGB(imageR, imageG, imageB);

				int[][] imageRAfter = ContoursNonLineaire.laplacienNonLineaire(imageR);
				int[][] imageGAfter = ContoursNonLineaire.laplacienNonLineaire(imageG);
				int[][] imageBAfter = ContoursNonLineaire.laplacienNonLineaire(imageB);

				observerRight.setCImage(new CImageRGB(imageRAfter, imageGAfter, imageBAfter));
			} catch (CImageRGBException ex) {
				ex.printStackTrace();
			}
		}
	}

	// </editor-fold>
	// <editor-fold defaultstate="collapsed" desc="Seuillage">
	public void MenuSeuillageSimpleActionPerformed(java.awt.event.ActionEvent evt) {
		try {
			JDialogChooseNumber number = new JDialogChooseNumber(this, true, "Seuil : ");
			int Seuil = number.showDialog();

			int[][] f_int = imageNG.getMatrice();
			int[][] imageAfter = Seuillage.seuillageSimple(f_int, Seuil);

			CImageNG imageapresFilter = new CImageNG(imageAfter);
			observerRight.setCImage(imageapresFilter);
		} catch (CImageNGException e) {
			throw new RuntimeException(e);
		}

	}

	public void MenuSeuillageDoubleActionPerformed(java.awt.event.ActionEvent evt) {
		try {
			JDialogChoose2numbers args = new JDialogChoose2numbers(this, true, "Seuil1 : ", "Seuil2 : ");
			String[] Seuils = args.showDialog();

			int seuil1 = Integer.parseInt(Seuils[0]);
			int seuil2 = Integer.parseInt(Seuils[1]);

			int[][] f_int = imageNG.getMatrice();
			int[][] imageAfter = Seuillage.seuillageDouble(f_int, seuil1, seuil2);

			CImageNG imageapresFilter = new CImageNG(imageAfter);
			observerRight.setCImage(imageapresFilter);
		} catch (CImageNGException e) {
			throw new RuntimeException(e);
		}

	}

	public void MenuSeuillageAutomatiqueActionPerformed(java.awt.event.ActionEvent evt) {
		try {
			int[][] f_int = imageNG.getMatrice();
			int[][] imageAfter = Seuillage.seuillageAutomatique(f_int);

			CImageNG imageapresFilter = new CImageNG(imageAfter);
			observerRight.setCImage(imageapresFilter);
		} catch (CImageNGException e) {
			throw new RuntimeException(e);
		}

	}
	// </editor-fold>

	// <editor-fold defaultstate="collapsed" desc="Application">
	public void MenuApplication1ActionPerformed(java.awt.event.ActionEvent evt) {
		File fichier = new File("./ImagesEtape5/lenaBruit.png");
		try {
			imageRGB = new CImageRGB(fichier);
			imageNG = null;
			observerLeft.setCImage(imageRGB);
			activeMenusRGB();

			int[][] imageR = new int[imageRGB.getLargeur()][imageRGB.getHauteur()];
			int[][] imageG = new int[imageRGB.getLargeur()][imageRGB.getHauteur()];
			int[][] imageB = new int[imageRGB.getLargeur()][imageRGB.getHauteur()];

			imageRGB.getMatricesRGB(imageR, imageG, imageB);

			/*
			 * imageR = FiltrageLineaireGlobal.filtrePasseBasButterworth(imageR, 100, 2);
			 * imageG = FiltrageLineaireGlobal.filtrePasseBasButterworth(imageG, 100, 2);
			 * imageB = FiltrageLineaireGlobal.filtrePasseBasButterworth(imageB, 100, 2);
			 */

			imageR = MorphoComplexe.filtreMedian(imageR, 5);
			imageG = MorphoComplexe.filtreMedian(imageG, 5);
			imageB = MorphoComplexe.filtreMedian(imageB, 5);

			observerRight.setCImage(new CImageRGB(imageR, imageG, imageB));
		} catch (IOException | CImageRGBException ex) {
			System.err.println("Erreur I/O : " + ex.getMessage());
		}
	}

	public void MenuApplication2AActionPerformed(java.awt.event.ActionEvent evt) {
		System.out.println("2A");
		File fichier = new File("./ImagesEtape5/lenaAEgaliser.jpg");
		try {
			imageRGB = new CImageRGB(fichier);
			imageNG = null;
			observerLeft.setCImage(imageRGB);
			activeMenusRGB();

			int[][] imageR = new int[imageRGB.getLargeur()][imageRGB.getHauteur()];
			int[][] imageG = new int[imageRGB.getLargeur()][imageRGB.getHauteur()];
			int[][] imageB = new int[imageRGB.getLargeur()][imageRGB.getHauteur()];

			imageRGB.getMatricesRGB(imageR, imageG, imageB);

			/*imageR = Histogramme.rehaussement(imageR, Histogramme.creeCourbeTonaleEgalisation(imageR));
			imageG = Histogramme.rehaussement(imageG, Histogramme.creeCourbeTonaleEgalisation(imageG));
			imageB = Histogramme.rehaussement(imageB, Histogramme.creeCourbeTonaleEgalisation(imageB));*/

			observerRight.setCImage(new CImageRGB(imageR, imageG, imageB));
		} catch (IOException | CImageRGBException ex) {
			System.err.println("Erreur I/O : " + ex.getMessage());
		}
	}

	public void MenuApplication2BActionPerformed(java.awt.event.ActionEvent evt) {
		System.out.println("2B");
		File fichier = new File("./ImagesEtape5/lenaAEgaliser.jpg");
		try {
			imageRGB = new CImageRGB(fichier);
			imageNG = imageRGB.getCImageNG();
			observerLeft.setCImage(imageRGB);
			activeMenusRGB();

			int[][] imageR = new int[imageRGB.getLargeur()][imageRGB.getHauteur()];
			int[][] imageG = new int[imageRGB.getLargeur()][imageRGB.getHauteur()];
			int[][] imageB = new int[imageRGB.getLargeur()][imageRGB.getHauteur()];

			imageRGB.getMatricesRGB(imageR, imageG, imageB);

			int[][] f_int = imageNG.getMatrice();
			/*int[] courbeTonaleEgalisation = Histogramme.creeCourbeTonaleEgalisation(f_int);

			imageR = Histogramme.rehaussement(imageR, courbeTonaleEgalisation);
			imageG = Histogramme.rehaussement(imageG, courbeTonaleEgalisation);
			imageB = Histogramme.rehaussement(imageB, courbeTonaleEgalisation);*/

			observerRight.setCImage(new CImageRGB(imageR, imageG, imageB));
		} catch (IOException | CImageRGBException | CImageNGException ex) {
			System.err.println("Erreur I/O : " + ex.getMessage());
		}
	}

	public void MenuApplication3ActionPerformed(java.awt.event.ActionEvent evt) {
		File fichier = new File("./ImagesEtape5/petitsPois.png");
		try {
			imageRGB = new CImageRGB(fichier);
			observerLeft.setCImage(imageRGB);
			imageNG = imageRGB.getCImageNG();
			activeMenusNG();

			int[][] imageR = new int[imageRGB.getLargeur()][imageRGB.getHauteur()];
			int[][] imageG = new int[imageRGB.getLargeur()][imageRGB.getHauteur()];
			int[][] imageB = new int[imageRGB.getLargeur()][imageRGB.getHauteur()];

			imageRGB.getMatricesRGB(imageR, imageG, imageB);

			/*imageR = Histogramme.rehaussement(imageR, Histogramme.creeCourbeTonaleNegatif());
			imageG = Histogramme.rehaussement(imageG, Histogramme.creeCourbeTonaleNegatif());
			imageB = Histogramme.rehaussement(imageB, Histogramme.creeCourbeTonaleNegatif());*/

			imageR = Seuillage.seuillageSimple(imageR, 128);
			imageG = Seuillage.seuillageSimple(imageG, 128);
			imageB = Seuillage.seuillageSimple(imageB, 128);

			int[][] imageRErode = MorphoElementaire.erosion(imageR, 4);
			int[][] imageGErode = MorphoElementaire.erosion(imageG, 4);
			int[][] imageBErode = MorphoElementaire.erosion(imageB, 4);

			int[][] imageRFinal = MorphoComplexe.reconstructionGeodesique(imageRErode, imageR);
			int[][] imageBFinal = MorphoComplexe.reconstructionGeodesique(imageBErode, imageB);

			observerRight.setCImage(new CImageNG(imageRFinal));
			observerLeft.setCImage(new CImageNG(imageBFinal));

//			imageB = MorphoElementaire.fermeture(imageB, 5);
//			imageR = MorphoElementaire.fermeture(imageR, 5);
//			imageG = MorphoElementaire.fermeture(imageG, 4);
//
//			imageB = MorphoElementaire.dilatation(imageB, 5);
//			imageR = MorphoElementaire.dilatation(imageR, 5);
//			imageG = MorphoElementaire.fermeture(imageG, 4);
//
//			imageB = Seuillage.seuillageSimple(imageB, 128);
//			imageR = Seuillage.seuillageSimple(imageR, 128);
//
//
//			Histogramme.creeCourbeTonaleNegatif(Histogramme.Histogramme256(imageB));
//
//			imageB = MorphoComplexe.reconstructionGeodesique(imageB, imageNG.getMatrice());
//			imageR = MorphoComplexe.reconstructionGeodesique(imageR, imageNG.getMatrice());

//			observerLeft.setCImage(new CImageNG(imageR));
//			observerRight.setCImage(new CImageRGB(imageR, imageG, imageB));

//			observerRight.setCImage(new CImageRGB(imageR, imageG, imageB));

		} catch (Exception ex) {
			System.err.println("Erreur I/O : " + ex.getMessage());
		}
	}

	public void MenuApplication4ActionPerformed(java.awt.event.ActionEvent evt) {
		File fichier = new File("./ImagesEtape5/balanes.png");
		try {
			imageNG = new CImageNG(fichier);
			observerLeft.setCImage(imageNG);
			imageRGB = null;
			activeMenusNG();

			int[][] f_int = imageNG.getMatrice();
			int[][] base = Seuillage.seuillageSimple(f_int, 126);
			int[][] erode = MorphoElementaire.erosion(base, 11);

			int[][] grandesBalanes = MorphoComplexe.reconstructionGeodesique(erode, base);

			int[][] petitesBalanes = MorphoElementaire.ouverture(soustraction(base, grandesBalanes), 3);

			observerLeft.setCImage(new CImageNG(grandesBalanes));
			observerRight.setCImage(new CImageNG(petitesBalanes));

		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}

	public void MenuApplication5ActionPerformed(java.awt.event.ActionEvent evt) {
		File fichier = new File("./ImagesEtape5/tools.png");
		try {
			imageNG = new CImageNG(fichier);
			observerLeft.setCImage(imageNG);
			imageRGB = null;
			activeMenusNG();

			int[][] image = imageNG.getMatrice();

			int[][] withoutTools = MorphoElementaire.erosion(image, 25);

			image = soustraction(image, withoutTools);
			image = Seuillage.seuillageSimple(image, 50);

//			int n = 4;
//			int lenX = image.length/n;
//			int lenY = image[0].length/n;
//			int[][][] imageDecomp = new int[n*n][lenX][lenY];
//
//			for (int i = 0; i < n; i++) {
//				for (int j = 0; j < n; j++) {
//					for (int y = 0; y < lenY; y++) {
//						System.arraycopy(image[i * lenY + y], j * lenY, imageDecomp[i * n + j][y], 0, lenX);
//					}
//				}
//			}
//
//			for (int i = 0; i < n*n; i++) {
//				imageDecomp[i] = Seuillage.seuillageSimple(imageDecomp[i], Histogramme.luminance(imageDecomp[i]) + 23);
//			}
//
//
//
//			for (int i = 0; i < n; i++) {
//				for (int j = 0; j < n; j++) {
//					for (int y = 0; y < lenY; y++) {
//						System.arraycopy(imageDecomp[i * n + j][y], 0, image[i * lenY + y], j * lenY, lenX);
//					}
//				}
//			}
			observerRight.setCImage(new CImageNG(image));
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public void MenuApplication6ActionPerformed(java.awt.event.ActionEvent evt) {

	}

	public void MenuApplication7ActionPerformed(java.awt.event.ActionEvent evt) {
		File fichier = new File("./ImagesEtape5/Tartines.jpg");
		try {
			imageRGB = new CImageRGB(fichier);
			imageNG = null;
			observerLeft.setCImage(imageRGB);
			activeMenusRGB();

			int[][] imageR = new int[imageRGB.getLargeur()][imageRGB.getHauteur()];
			int[][] imageG = new int[imageRGB.getLargeur()][imageRGB.getHauteur()];
			int[][] imageB = new int[imageRGB.getLargeur()][imageRGB.getHauteur()];

			imageRGB.getMatricesRGB(imageR, imageG, imageB);

			int[][] contourR = ContoursNonLineaire.laplacienNonLineaire(imageR);
			int[][] contourG = ContoursNonLineaire.laplacienNonLineaire(imageG);
			int[][] contourB = ContoursNonLineaire.laplacienNonLineaire(imageB);

			int[][] contour = addition(contourB, addition(contourR, contourG));

			imageG = addition(imageG, contour);

			observerRight.setCImage(new CImageRGB(imageR, imageG, imageB));

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	// </editor-fold>

	public static void main(String[] args) {
		java.awt.EventQueue.invokeLater(IsilImageProcessing::new);
	}

	public static int[][] soustraction(int[][] image, int[][] moins) {

		int[][] res = new int[image.length][image[0].length];

		for (int i = 0; i < image.length; i++) {
			for (int j = 0; j < image[0].length; j++) {
				res[i][j] = Math.min(image[i][j] - moins[i][j], 255);
//				if (image[i][j] == 0 || image[i][j] == 255 && moins[i][j] == 255){
//					res[i][j] = 0;
//					continue;
//				}
//
//				res[i][j] = 255;
			}
		}

		return res;
	}

	public static int[][] addition(int[][] image, int[][] plus) {
		int[][] res = new int[image.length][image[0].length];

		for (int i = 0; i < image.length; i++) {
			for (int j = 0; j < image[0].length; j++) {
				res[i][j] = Math.min(image[i][j] + plus[i][j], 255);
			}
		}

		return res;
	}

	// <editor-fold defaultstate="collapsed" desc="Variables for graphic interface">
	private JPanel panel1;
	private JScrollPane ScrollPaneLeft;
	private JScrollPane ScrollPaneRight;

	private JMenu MenuImage;
	private JMenu MenuImageNouvelle;
	private JMenuItem MenuItemImageNouvelleRGB;
	private JMenuItem MenuItemImageNouvelleNG;
	private JMenu MenuImageOuvrir;
	private JMenuItem MenuItemImageOuvrirRGB;
	private JMenuItem MenuItemImageOuvrirNG;
	private JMenuItem MenuItemImageEnregistrerSous;
	private JMenuItem MenuItemImageQuitter;

	private JMenu MenuDessiner;
	private JMenuItem MenuItemDessinerCouleurPinceau;
	private JCheckBoxMenuItem CheckBoxMenuItemDessinerPixel;
	private JCheckBoxMenuItem CheckBoxMenuItemDessinerLigne;
	private JCheckBoxMenuItem CheckBoxMenuItemDessinerRectangle;
	private JCheckBoxMenuItem CheckBoxMenuItemDessinerRectanglePlein;
	private JCheckBoxMenuItem CheckBoxMenuItemDessinerCercle;
	private JCheckBoxMenuItem CheckBoxMenuItemDessinerCerclePlein;

	private JMenu MenuFourier;
	private JMenu MenuFourrierAfficher;
	private JMenuItem MenuItemFourierAfficherModule;
	private JMenuItem MenuItemFourierAfficherPhase;
	private JMenuItem MenuItemFourierAfficherReelle;
	private JMenuItem MenuItemFourierAfficherImaginaire;

	private JMenu MenuHistogramme;
	private JMenuItem MenuItemHistogrammeAfficher;
	private JMenuItem MenuItemHistogrammeRehaussement;
	private JMenuItem MenuItemHistogrammeCourbeSaturation;
	private JMenuItem MenuItemHistogrammeCourbeGamma;
	private JMenuItem MenuItemHistogrammeCourbeNegatif;
	private JMenuItem MenuItemHistogrammeCourbeEgalisation;

	private JMenu MenuFiltrageLineaire;
	private JMenu MenuFiltrageLineaireGlobal;
	private JMenuItem MenuFiltrageLineaireGlobalBasIdeal;
	private JMenuItem MenuFiltrageLineaireGlobalHautIdeal;
	private JMenuItem MenuFiltrageLineaireGlobalBasButter;
	private JMenuItem MenuFiltrageLineaireGlobalHautButter;

	private JMenu MenuFiltrageLineaireLocal;
	private JMenuItem MenuFiltrageLineaireLocalConvolution;
	private JMenuItem MenuFiltrageLineaireLocalMoyenneur;

	private JMenu MenuTraitementNonLineaire;
	private JMenu MenuTraitementNonLineaireElementaire;
	private JMenuItem MenuTraitementNonLineaireErosion;
	private JMenuItem MenuTraitementNonLineaireDilatation;
	private JMenuItem MenuTraitementNonLineaireOuverture;
	private JMenuItem MenuTraitementNonLineaireFermeture;
	private JMenu MenuTraitementNonLineaireComplexe;
	private JMenuItem MenuTraitementNonLineaireDilatationGeodesique;
	private JMenuItem MenuTraitementNonLineaireReconstructionGeodesique;
	private JMenuItem MenuTraitementNonLineaireFiltreMedian;

	private JMenu MenuContours;
	private JMenu MenuContoursLineaire;
	private JMenuItem MenuContoursLineairePrewitt;
	private JMenuItem MenuContoursLineaireSobel;
	private JMenuItem MenuContoursLineaireLaplacien4;
	private JMenuItem MenuContoursLineaireLaplacien8;

	private JMenu MenuContoursNonLineaire;
	private JMenuItem MenuContoursNonLineaireErosion;
	private JMenuItem MenuContoursNonLineaireDilatation;
	private JMenuItem MenuContoursNonLineaireBeucher;
	private JMenuItem MenuContoursNonLineaireLaplacien;

	private JMenu MenuSeuillage;
	private JMenuItem MenuSeuillageSimple;
	private JMenuItem MenuSeuillageDouble;
	private JMenuItem MenuSeuillageAutomatique;

	private JMenu MenuApplication;
	private JMenuItem MenuApplication1;
	private JMenu MenuApplication2;
	private JMenuItem MenuApplication2A;
	private JMenuItem MenuApplication2B;
	private JMenuItem MenuApplication3;
	private JMenuItem MenuApplication4;
	private JMenuItem MenuApplication5;
	private JMenuItem MenuApplication6;
	private JMenuItem MenuApplication7;

	private JButton inverseButton;
	// </editor-fold>
}
