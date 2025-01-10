package isilimageprocessing.Dialogues;

import javax.swing.*;

public class JDialogChoose2numbers extends JDialog {
	private JPanel contentPane;
	private JButton buttonOK;
	private JTextField textFieldNumber1;
	private JTextField textFieldNumber2;
	private JLabel textField1;
	private JLabel textField2;
	private final String[] objects;

	public JDialogChoose2numbers(java.awt.Frame parent, boolean modal, String nbr1, String nbr2) {
		super(parent, modal);
		setContentPane(contentPane);
		getRootPane().setDefaultButton(buttonOK);
		setTitle("Choisissez un nombre");
		setContentPane(contentPane);
		textField1.setText(nbr1);
		textField2.setText(nbr2);
		pack();
		objects = new String[2];

		buttonOK.addActionListener(e -> onOK());

	}
	public String[] showDialog() {
		setVisible(true);
		return objects;
	}

	private void onOK() {
		objects[0] = textFieldNumber1.getText();
		objects[1] = textFieldNumber2.getText();
		setVisible(false);
		dispose();
	}
}
