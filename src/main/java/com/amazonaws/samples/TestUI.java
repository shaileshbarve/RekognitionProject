package com.amazonaws.samples;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.awt.event.ActionEvent;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JTextPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.Box;
import javax.swing.SwingConstants;
import javax.swing.JTextArea;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JPanel;

public class TestUI {

	private static JFrame frmTest;
	public static JTextField textField;
	public static JTextArea txtrThisIsA;
	private JLabel lblOutput;
	private JButton btnNewButton;
	private JLabel lblImagePath;
	public static JTextField textStream;
	private JButton btnCompareImage;
	private JLabel lblTextImage;
	public static JTextField textField3;
	private JButton btnReadText;
	public static JTextField textField4;
	private JButton btnCreateCollection;
	private JButton btnDeleteCollection;
	private JButton btnListCollection;
	public static JTextField textField_Userid;
	public static JTextField textField_name;
	private JTextField textField_1;
	
//	JScrollPane scrolltxt = new JScrollPane(txtrThisIsA);

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					TestUI window = new TestUI();
					window.frmTest.setVisible(true);

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public TestUI() {
		initialize();
		
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmTest = new JFrame();
		frmTest.setTitle("Image Rekognition UI");
		frmTest.setBounds(100, 100, 760, 608);
		frmTest.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmTest.getContentPane().setLayout(null);
		
		
			
		RekognitionClass.InitializeAWSRekognitionClient();
		
		JButton btnDetect = new JButton("Detect Face");
		btnDetect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
	
					RekognitionClass.DetectFacesfromFile();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		btnDetect.setBounds(10, 69, 144, 23);
		frmTest.getContentPane().add(btnDetect);
		
		textField = new JTextField();
		textField.setText("C://Users//sbarve//Pictures//Camera Roll//shail1.jpg");
		textField.setBounds(10, 32, 364, 20);
		frmTest.getContentPane().add(textField);
		textField.setColumns(10);
		
		JLabel lblImage = new JLabel("Image Path:");
		lblImage.setBounds(10, 3, 98, 31);
		frmTest.getContentPane().add(lblImage);
		
		Box verticalBox = Box.createVerticalBox();
		verticalBox.setBounds(368, 290, 1, 1);
		frmTest.getContentPane().add(verticalBox);
		
		
		txtrThisIsA = new JTextArea();
		txtrThisIsA.setLineWrap(true);
		txtrThisIsA.setWrapStyleWord(true);
		txtrThisIsA.setBounds(384, 30, 350, 529);
		frmTest.getContentPane().add(txtrThisIsA);
		
//		//Scroll bar		
//		JScrollPane scroll = new JScrollPane (txtrThisIsA);
//	    scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
//	          scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
//	          
//		frame.add(scroll);
//		frame.setVisible(true);
//		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//		frame.getContentPane().add(scroll);
//		//Scroll bar	
		
		
		lblOutput = new JLabel("Output:");
		lblOutput.setBounds(384, 11, 49, 14);
		frmTest.getContentPane().add(lblOutput);
		
		btnNewButton = new JButton("Analyze Image");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					RekognitionClass.AnalyzeImagefromFile();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		btnNewButton.setBounds(155, 69, 144, 23);
		frmTest.getContentPane().add(btnNewButton);
		
		lblImagePath = new JLabel("Image Path2:");
		lblImagePath.setBounds(10, 121, 98, 31);
		frmTest.getContentPane().add(lblImagePath);
		
		textStream = new JTextField();
		textStream.setColumns(10);
		textStream.setBounds(190, 126, 179, 20);
		frmTest.getContentPane().add(textStream);
		
		btnCompareImage = new JButton("Compare Image");
		btnCompareImage.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					RekognitionClass.CompareImagesfromFile();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		btnCompareImage.setBounds(10, 179, 144, 23);
		frmTest.getContentPane().add(btnCompareImage);
		
		lblTextImage = new JLabel("Text Image:");
		lblTextImage.setBounds(10, 213, 98, 31);
		frmTest.getContentPane().add(lblTextImage);
		
		textField3 = new JTextField();
		textField3.setColumns(10);
		textField3.setBounds(10, 242, 364, 20);
		frmTest.getContentPane().add(textField3);
		
		btnReadText = new JButton("Read Text");
		btnReadText.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					RekognitionClass.ReadTextfromFile();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		btnReadText.setBounds(10, 271, 144, 23);
		frmTest.getContentPane().add(btnReadText);
		
		JLabel lblImagePath_1 = new JLabel("Image Path3:");
		lblImagePath_1.setBounds(10, 317, 98, 31);
		frmTest.getContentPane().add(lblImagePath_1);
		
		textField4 = new JTextField();
		textField4.setColumns(10);
		textField4.setBounds(10, 346, 364, 20);
		frmTest.getContentPane().add(textField4);
		
		JButton btnAddToCollection = new JButton("Add to Collection");
		btnAddToCollection.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					RekognitionClass.AddImageToCollectionandDB();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		btnAddToCollection.setBounds(10, 437, 144, 23);
		frmTest.getContentPane().add(btnAddToCollection);
		
		JButton btnSearchCollection = new JButton("Validate User!");
		btnSearchCollection.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
//					RekognitionClass.SearchImagesinCollection();
					RekognitionClass.SearchAllfacesinCollection();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		btnSearchCollection.setBounds(229, 312, 144, 23);
		frmTest.getContentPane().add(btnSearchCollection);
		
		btnCreateCollection = new JButton("Create Collection");
		btnCreateCollection.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					RekognitionClass.CreateCollection();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		btnCreateCollection.setBounds(10, 502, 144, 23);
		frmTest.getContentPane().add(btnCreateCollection);
		
		btnDeleteCollection = new JButton("Delete Collection");
		btnDeleteCollection.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					RekognitionClass.DeleteCollection();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		btnDeleteCollection.setBounds(155, 536, 144, 23);
		frmTest.getContentPane().add(btnDeleteCollection);
		
		btnListCollection = new JButton("List Collection");
		btnListCollection.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					RekognitionClass.ListCollection();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		btnListCollection.setBounds(10, 536, 144, 23);
		frmTest.getContentPane().add(btnListCollection);
		
		textField_Userid = new JTextField();
		textField_Userid.setColumns(10);
		textField_Userid.setBounds(61, 379, 133, 20);
		frmTest.getContentPane().add(textField_Userid);
		
		JLabel lblName = new JLabel("User Id:");
		lblName.setBounds(10, 377, 49, 31);
		frmTest.getContentPane().add(lblName);
		
		JLabel lblName_1 = new JLabel("Name:");
		lblName_1.setBounds(20, 400, 49, 31);
		frmTest.getContentPane().add(lblName_1);
		
		textField_name = new JTextField();
		textField_name.setText("shail");
		textField_name.setColumns(10);
		textField_name.setBounds(61, 406, 133, 20);
		frmTest.getContentPane().add(textField_name);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(732, 213, 2, 2);
		frmTest.getContentPane().add(scrollPane);
		
		JButton btnCapture = new JButton("Start Webcam");
		btnCapture.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				//capture call
				try {
					RekognitionClass.StartWebcam();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		btnCapture.setBounds(229, 447, 144, 23);
		frmTest.getContentPane().add(btnCapture);
		
		JButton btnTakePhoto = new JButton("Take Photo");
		btnTakePhoto.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				//capture call
				try {
					RekognitionClass.SnapPhoto();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		btnTakePhoto.setBounds(229, 481, 144, 23);
		frmTest.getContentPane().add(btnTakePhoto);
		
		JButton btnStopWebcam = new JButton("Start Stream");
		btnStopWebcam.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
				RekognitionClass.StartStream();
				}catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		btnStopWebcam.setBounds(229, 179, 144, 23);
		frmTest.getContentPane().add(btnStopWebcam);
		
		JButton btnDeleteStream = new JButton("Delete Stream");
		btnDeleteStream.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				try {
					RekognitionClass.DeleteStreamProcessor();
					}catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
			}
		});
		btnDeleteStream.setBounds(229, 213, 144, 23);
		frmTest.getContentPane().add(btnDeleteStream);
		
		textField_1 = new JTextField();
		textField_1.setColumns(10);
		textField_1.setBounds(0, 150, 369, 20);
		frmTest.getContentPane().add(textField_1);
	}
}
