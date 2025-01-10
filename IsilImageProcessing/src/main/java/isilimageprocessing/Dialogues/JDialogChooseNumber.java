package isilimageprocessing.Dialogues;

import javax.swing.*;

public class JDialogChooseNumber extends JDialog {
	private JPanel contentPane;
	private JButton buttonOK;
	private JTextField TextField;
	private JLabel Label;
	private String number;

	public JDialogChooseNumber(java.awt.Frame parent, boolean modal, String fieldName) {
		super(parent, modal);
		Label.setText(fieldName);
		setContentPane(contentPane);
		getRootPane().setDefaultButton(buttonOK);
		setTitle("Choisissez un nombre");
		setContentPane(contentPane);
		pack();


		buttonOK.addActionListener(e -> onOK());
	}

	public int showDialog() {
		setVisible(true);
		return Integer.parseInt(number);
	}
	public double showDialogDouble() {
		setVisible(true);
		return Double.parseDouble(number);
	}

	private void onOK() {
		number = TextField.getText();
		setVisible(false);
		dispose();
	}

}
