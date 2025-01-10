package isilimageprocessing.Dialogues;

import CImage.CImageNG;
import CImage.Exceptions.CImageNGException;
import CImage.Observers.JLabelBeanCImage;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class JDialogAfficheMatriceDouble extends JDialog {
	private double Max;
	private double Min;
	private double blanc;
	private double noir;
	private final int D = 512;
	private final double[][] matrice;
	private final int M;
	private final int N;
	private CImageNG image;
	private final int[][] matrice_int;


	public JDialogAfficheMatriceDouble(Frame parent, boolean modal, double[][] m, String titre) {
		super(parent, modal);
		setContentPane(contentPane);
		setModal(true);

		initComponents();
		setTitle(titre);

		matrice = m;
		M = matrice.length;
		N = matrice[0].length;
		matrice_int = new int[M][N];
		try {
			image = new CImageNG(M,N,0);
		}
		catch (CImageNGException ex) { System.out.println("Erreur CImageNG : " + ex.getMessage()); }

		JLabelBeanCImage observer = new JLabelBeanCImage(image);
		ScrollPane1.setViewportView(observer);

		SliderNoir.setMaximum(D);
		SliderNoir.setValue(0);
		SliderBlanc.setMaximum(D);
		SliderBlanc.setValue(D);

		// ***** Recherche du Min *****
		Min = matrice[0][0];
		for(int i=0 ; i<M ; i++) {
			for(int j=0 ; j<N ; j++) {
				if (matrice[i][j] < Min) Min = matrice[i][j];
			}
		}
		LabelValeurMin.setText(String.valueOf(Min));
		noir = Min;
		TextFieldNoir.setText(String.valueOf(noir));

		// ***** Recherche du Max *****
		Max = matrice[0][0];
		for(int i=0 ; i<M ; i++) {
			for (int j = 0; j < N; j++) {
				if (matrice[i][j] > Max)
					Max = matrice[i][j];
			}
		}
		LabelValeurMax.setText(String.valueOf(Max));
		blanc = Max;
		TextFieldBlanc.setText(String.valueOf(blanc));

		MiseAJourCImage();
		pack();

	}

	private void initComponents() {

		setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);


		SliderBlanc.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mouseReleased(java.awt.event.MouseEvent evt) {
				SliderBlancMouseReleased();
			}
		});
		SliderNoir.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mouseReleased(java.awt.event.MouseEvent evt) {
				SliderNoirMouseReleased();
			}
		});
		SliderBlanc.addChangeListener(this::SliderBlancStateChanged);
		SliderNoir.addChangeListener(this::SliderNoirStateChanged);
		TextFieldBlanc.addActionListener(this::TextFieldBlancActionPerformed);
		TextFieldNoir.addActionListener(this::jTextFieldNoirActionPerformed);
		Button1.addActionListener(this::Button1ActionPerformed);
	}

	private void MiseAJourCImage() {
		for(int i=0 ; i<M ; i++) {
			for (int j = 0; j < N; j++) {
				if (matrice[i][j] >= blanc) {
					matrice_int[i][j] = 255;
				} else {
					if (matrice[i][j] <= noir) {
						matrice_int[i][j] = 0;
					} else {
						int val = (int) ((matrice[i][j] - noir) / (blanc - noir) * 255 + 0.5);
						if (val > 255)
							val = 255;
						if (val < 0)
							val = 0;
						matrice_int[i][j] = val;
					}
				}
			}
		}
		try {
			image.setMatrice(matrice_int);
		} catch (CImageNGException ex) {System.out.println("Erreur CImageNG : " + ex.getMessage());}
	}

	// <editor-fold defaultstate="collapsed" desc="Action Performed">
	private void Button1ActionPerformed(java.awt.event.ActionEvent evt) {
		JFileChooser choix = new JFileChooser();
		File fichier;

		choix.setCurrentDirectory(new File ("."));
		if (choix.showOpenDialog(null) == JFileChooser.APPROVE_OPTION)
		{
			fichier = choix.getSelectedFile();
			if (fichier != null)
			{
				try
				{
					image.enregistreFormatPNG(fichier);
				}
				catch (IOException ex)
				{
					System.err.println("Erreur I/O : " + ex.getMessage());
				}
			}
		}
	}
	private void jTextFieldNoirActionPerformed(java.awt.event.ActionEvent evt) {
		double val = Double.parseDouble(TextFieldNoir.getText());
		if (val < Min) {
			SliderNoir.setValue(0);
			return;
		}
		if (val >= blanc) {
			SliderNoir.setValue(SliderBlanc.getValue()-1);
			return;
		}
		int s = (int)((double)D*(val-Min)/(Max-Min)+0.5);
		SliderNoir.setValue(s);

	}
	private void TextFieldBlancActionPerformed(java.awt.event.ActionEvent evt) {
		double val = Double.parseDouble(TextFieldBlanc.getText());
		if (val > Max) {
			SliderBlanc.setValue(D);
			return;
		}
		if (val <= noir) {
			SliderBlanc.setValue(SliderNoir.getValue()+1);
			return;
		}
		int s = (int)((double)D*(val-Min)/(Max-Min)+0.5);
		SliderBlanc.setValue(s);
	}
	private void SliderNoirMouseReleased() {
		if (SliderNoir.getValue() >= SliderBlanc.getValue())
			SliderNoir.setValue(SliderBlanc.getValue()-1);
	}
	private void SliderBlancMouseReleased() {
		if (SliderBlanc.getValue() <= SliderNoir.getValue())
			SliderBlanc.setValue(SliderNoir.getValue()+1);
	}
	private void SliderBlancStateChanged(javax.swing.event.ChangeEvent evt) {
		int sNoir = SliderNoir.getValue();
		int sBlanc = SliderBlanc.getValue();

		if (sBlanc > sNoir) {
			blanc = Min + (Max-Min)*(double)sBlanc/(double)D;
			TextFieldBlanc.setText(String.valueOf(blanc));
		}

		MiseAJourCImage();
	}
	private void SliderNoirStateChanged(javax.swing.event.ChangeEvent evt) {
		int sNoir = SliderNoir.getValue();
		int sBlanc = SliderBlanc.getValue();

		if (sBlanc > sNoir) {
			noir = Min + (Max-Min)*(double)sNoir/(double)D;
			TextFieldNoir.setText(String.valueOf(noir));
		}

		MiseAJourCImage();
	}
	// </editor-fold>

	public static void main(String[] args) {
		JDialogAfficheMatriceDouble dialog = new JDialogAfficheMatriceDouble(new javax.swing.JFrame(), true,null,null);
		dialog.pack();
		dialog.setVisible(true);
		System.exit(0);
	}


	private JPanel contentPane;
	private JSlider SliderNoir;
	private JSlider SliderBlanc;
	private JTextField TextFieldBlanc;
	private JTextField TextFieldNoir;
	private JButton Button1;
	private JScrollPane ScrollPane1;
	private JLabel LabelValeurMin;
	private JLabel LabelValeurMax;

}
