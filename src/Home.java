import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.TextField;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;

public class Home extends JFrame {

	private JPanel contentPane;
	static JComboBox<RadioPlay> comboBox;
	Player player;
	static String s = "";
	Thread th;
	JButton btnOuvir;
	public static ArrayList<RadioPlay> radios;
	JTextArea textArea_1;
	public static boolean save = false;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {

		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Home frame = new Home();
					frame.setVisible(true);
					frame.addWindowListener(new WindowAdapter() {
						@Override
						public void windowClosing(WindowEvent e) {
							if (save)
								write();

							e.getWindow().dispose();
						}
					});
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public Home() {
		setIconImage(Toolkit.getDefaultToolkit().getImage(
				Home.class.getResource("/icon/icon.ico")));
		comboBox = new JComboBox();
		comboBox.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if (th != null) {

					if (th.getState().toString() == "RUNNABLE"
							|| th.getState().toString() == "TIMED_WAITING") {

						th.stop();
						btnOuvir.setEnabled(true);
						textArea_1.setText("Stoped, click Play!");
					}
				}
			}
		});

		textArea_1 = new JTextArea();
		loadRadios();
		/*
		 * for (RadioPlay ra : radios) { System.out.println(ra);
		 * 
		 * }
		 */

		// sort();
		comboBox.removeAllItems();
		for (RadioPlay ra : radios) {
			// System.err.println(ra);
			comboBox.addItem(ra);

		}

		setResizable(false);
		setTitle("Web Radio Player");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 320, 172);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		// JComboBox<String> comboBox = new JComboBox();
		comboBox.setBounds(6, 26, 298, 26);
		contentPane.add(comboBox);

		JLabel lblStreamsDisponiveis = new JLabel("Streams disponiveis");
		lblStreamsDisponiveis.setBounds(6, 6, 142, 16);
		contentPane.add(lblStreamsDisponiveis);

		btnOuvir = new JButton("Play");
		// th.stop();
		btnOuvir.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				RadioPlay kk = (RadioPlay) comboBox.getSelectedItem();
				s = kk.getUrl();

				th = new Thread() {
					public void run() {
						play();

					}
				};

				th.start();
				if (th.getState().toString() == "RUNNABLE") {
					btnOuvir.setEnabled(false);
				}

				RadioPlay r = (RadioPlay) comboBox.getSelectedItem();
				textArea_1.setText(" A tocar " + r.getNome());

			}
		});
		btnOuvir.setBounds(235, 63, 70, 30);
		contentPane.add(btnOuvir);

		JButton btnNewButton = new JButton("Stop");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				th.stop();
				if (th.getState().toString() != "RUNNABLE") {
					btnOuvir.setEnabled(true);
				}

				textArea_1.setText(" Selecione uma Rádio");
			}
		});
		btnNewButton.setBounds(160, 63, 70, 30);
		contentPane.add(btnNewButton);

		JButton btnNova = new JButton("Nova");
		btnNova.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				TextField textField1 = new TextField();
				TextField textField2 = new TextField();
				Object[] inputFields = { "Nome", textField1, "URL", textField2 };
				int option = JOptionPane.showConfirmDialog(null, inputFields,
						"Adicionar Nova Rádio", JOptionPane.OK_CANCEL_OPTION);

				if (option == JOptionPane.OK_OPTION) {
					if (textField1.getText().equals("")
							|| textField2.getText().equals("")) {
						textArea_1.setText("Valores inválidos!");
					} else {
						radios.add(new RadioPlay(textField1.getText(),
								textField2.getText()));
						// System.out.println("daos:"+textField1.getText()
						// +"\n"+textField1.getText());
						save = true;
						comboBox.removeAllItems();
						for (RadioPlay radio : radios) {
							comboBox.addItem(radio);

						}
					}
				} else {
					// System.out.println("sdfsa canceled");
				}

			}
		});
		btnNova.setBounds(85, 63, 70, 30);
		contentPane.add(btnNova);

		textArea_1.setForeground(new Color(0, 255, 0));
		textArea_1.setFont(new Font("Georgia", Font.PLAIN, 13));
		textArea_1.setEditable(false);
		textArea_1.setBackground(Color.BLACK);
		textArea_1.setBounds(6, 104, 298, 33);
		contentPane.add(textArea_1);

		JButton btnNewButton_1 = new JButton("Apagar");
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				Object[] options = { "Sim", "Nop!" };
				int n = JOptionPane.showOptionDialog(null,
						"De certeza que queres apagar essa rádio?",
						"Apagar Rádio?", JOptionPane.YES_NO_OPTION,
						JOptionPane.WARNING_MESSAGE, null, options, options[1]);
				System.err.println(n);

				if (n == 0) {
					RadioPlay rad = (RadioPlay) comboBox.getSelectedItem();

					if (radios.remove(rad)) {
						save = true;

					}
					comboBox.removeAllItems();

					for (RadioPlay radioPlay : radios) {

						comboBox.addItem(radioPlay);

					}

				}

			}
		});
		btnNewButton_1.setBounds(5, 63, 75, 30);
		contentPane.add(btnNewButton_1);
		comboBox.removeAllItems();

		for (RadioPlay r : radios) {

			comboBox.addItem(r);

		}
	}

	public void play() {
		try {
			playRadioStream();
		} catch (IOException e) {
			e.printStackTrace();

			textArea_1.setText(e.toString());
		} catch (JavaLayerException e) {
			e.printStackTrace();

			textArea_1.setText(e.toString());
		}
	}

	private static void playRadioStream() throws IOException,
			JavaLayerException {

		URLConnection urlConnection = new URL(s).openConnection();

		urlConnection.connect();

		// Playing
		Player player = new Player(urlConnection.getInputStream());
		player.play();

	}

	public void loadRadios() {
		radios = new ArrayList<RadioPlay>();

		try {

			File fXmlFile = new File("data.xml");
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(fXmlFile);

			// optional, but recommended
			// read this -
			// http://stackoverflow.com/questions/13786607/normalization-in-dom-parsing-with-java-how-does-it-work
			doc.getDocumentElement().normalize();

			NodeList nList = doc.getElementsByTagName("radio");

			for (int temp = 0; temp < nList.getLength(); temp++) {

				Node nNode = nList.item(temp);

				if (nNode.getNodeType() == Node.ELEMENT_NODE) {

					Element eElement = (Element) nNode;

					radios.add(new RadioPlay(eElement
							.getElementsByTagName("nome").item(0)
							.getTextContent(), eElement
							.getElementsByTagName("url").item(0)
							.getTextContent()));

				}
			}
		} catch (FileNotFoundException e) {
			textArea_1.setText("Adicione uma rádio!");

		} catch (Exception e) {
			e.printStackTrace();
			textArea_1.setText(e.toString());
		}
	}

	public static void write() {

		try {

			DocumentBuilderFactory docFactory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

			// root elements
			Document doc = docBuilder.newDocument();
			Element rootElement = doc.createElement("radios");
			doc.appendChild(rootElement);

			for (RadioPlay r : radios) {

				Element staff = doc.createElement("radio");
				rootElement.appendChild(staff);

				Element firstname = doc.createElement("nome");
				firstname.appendChild(doc.createTextNode(r.getNome()));
				staff.appendChild(firstname);

				// lastname elements
				Element lastname = doc.createElement("url");
				lastname.appendChild(doc.createTextNode(r.getUrl()));
				staff.appendChild(lastname);

			}

			// write the content into xml file
			TransformerFactory transformerFactory = TransformerFactory
					.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(new File("data.xml"));

			transformer.transform(source, result);

			// System.out.println("File saved!");
			save = false;

		} catch (ParserConfigurationException pce) {
			pce.printStackTrace();
		} catch (TransformerException tfe) {
			tfe.printStackTrace();
		}
	}

	public void sort() {
		for (int j = 0; j < radios.size(); j++)

		{
			System.out.println("j" + j + "- " + radios.get(j).getNome());
			for (int i = j + 1; i < radios.size(); i++) {
				System.out.println(i + "- " + radios.get(i).getNome());
				// System.out.println(radios.get(j).getNome());
				if (radios.get(i).getNome().compareTo(radios.get(j).getNome()) > 0) {
					RadioPlay temp = radios.get(j);
					radios.set(j, radios.get(i));
					radios.set(i, temp);

				}
			}

		}
	}
}