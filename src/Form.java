import java.awt.Color;
import java.awt.TextArea;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

public class Form extends JFrame {

	private JPanel contentPane;
	private JTextField textField;
	private JTextField textField_1;
	private TextArea textArea;
	private Form z;
	private JButton btnCancelar;

	public Form() {
		z = this;
		textArea = new TextArea();
		setResizable(false);
		setTitle("Adinionar Radio Stream");
		setBounds(100, 100, 450, 179);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JLabel label = new JLabel("");
		label.setBounds(5, 5, 424, 0);
		contentPane.add(label);

		JLabel lblNewLabel = new JLabel("Nome");
		lblNewLabel.setBounds(25, 19, 46, 14);
		contentPane.add(lblNewLabel);

		textField = new JTextField();
		textField.setBounds(69, 16, 152, 20);
		contentPane.add(textField);
		textField.setColumns(10);

		JLabel lblUrl = new JLabel("URL");
		lblUrl.setBounds(25, 47, 46, 14);
		contentPane.add(lblUrl);

		textField_1 = new JTextField();
		textField_1.setBounds(69, 44, 355, 20);
		contentPane.add(textField_1);
		textField_1.setColumns(10);

		JButton btnAdicionar = new JButton("Adicionar");
		btnAdicionar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				if (textField.getText().equals("")
						|| textField_1.getText().equals("")) {
					textArea.setText(" Nome ou Url inválidos");
				} else {

					// String k =textField.getText()+"#"+textField_1.getText();

					Home.radios.add(new RadioPlay(textField.getText(),
							textField_1.getText()));
					Home.save = true;

					z.setVisible(false);
					Home.comboBox.removeAllItems();
					for (RadioPlay r : Home.radios) {
						Home.comboBox.addItem(r);
					}
					

				}

			}
		});
		btnAdicionar.setBounds(234, 75, 89, 23);
		contentPane.add(btnAdicionar);

		textArea.setBackground(Color.BLACK);
		textArea.setForeground(Color.GREEN);
		textArea.setEditable(false);
		textArea.setBounds(10, 104, 429, 44);
		contentPane.add(textArea);

		btnCancelar = new JButton("Cancelar");
		btnCancelar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				z.setVisible(false);
			}
		});
		btnCancelar.setBounds(333, 75, 89, 23);
		contentPane.add(btnCancelar);
	}

	public void escrever(String f, String linha) {
		try {
			FileWriter writer = new FileWriter(f, true);
			writer.write(linha);
			writer.write("\r\n"); // write new line
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
