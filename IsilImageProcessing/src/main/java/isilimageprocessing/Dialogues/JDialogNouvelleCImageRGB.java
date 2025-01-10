package isilimageprocessing.Dialogues;

import CImage.CImageRGB;
import CImage.Exceptions.CImageRGBException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class JDialogNouvelleCImageRGB extends JDialog {
	private Color couleur;
	private CImageRGB cImageRGB;
	private JPanel contentPanel;
	private JButton buttonOK;
	private JButton buttonCancel;
	private JTextField TfLargeur;
	private JTextField TfHauteur;
	private JPanel PanelPrintColor;
	private JButton ButtonChoisir;

	public JDialogNouvelleCImageRGB(java.awt.Frame parent, boolean modal) {
		super(parent, modal);
		couleur = new Color(255,255,255);
		cImageRGB = null;

		setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
		setTitle("Choix du niveau de gris");
		setResizable(false);
		contentPanel.setBackground(couleur);
		TfLargeur.setText("256");
		TfHauteur.setText("256");

		setContentPane(contentPanel);
		getRootPane().setDefaultButton(buttonOK);
		pack();

		buttonOK.addActionListener(e -> onOK());

		buttonCancel.addActionListener(e -> onCancel());
		ButtonChoisir.addActionListener(this::onChoisir);

		// call onCancel() when cross is clicked
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				onCancel();
			}
		});
	}

	private void onOK() {
		try {
			int largeur = Integer.parseInt(TfLargeur.getText());
			int hauteur = Integer.parseInt(TfHauteur.getText());
			cImageRGB = new CImageRGB(largeur,hauteur,couleur);
			setVisible(false);
			dispose();
		}
		catch (CImageRGBException ex) {
			JOptionPane.showMessageDialog(this,ex.getMessage(),"Erreur RGB !!!",JOptionPane.ERROR_MESSAGE);
		}
		catch(NumberFormatException e) {
			JOptionPane.showMessageDialog(this,"Hauteur et Largeur doivent etre entiers !","Erreur RGB !!!",JOptionPane.ERROR_MESSAGE);
		}
	}

	private void onCancel() {
		// add your code here if necessary
		dispose();
	}

	private void onChoisir(java.awt.event.ActionEvent evt) {
		Color newC = JColorChooser.showDialog(this,"Couleur de fond",couleur);
		if (newC != null) couleur = newC;
		PanelPrintColor.setBackground(couleur);
	}

	public CImageRGB getCImageRGB() {
		return cImageRGB;
	}

	public static void main(String[] args) {
		JDialogNouvelleCImageRGB dialog = new JDialogNouvelleCImageRGB(new javax.swing.JFrame(), true);
		dialog.pack();
		dialog.setVisible(true);
		System.exit(0);
	}
}
