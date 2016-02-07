import java.awt.EventQueue;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.videoio.VideoCapture;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class MouthDJFrame {

	static MouthD md;
	//
	static Mat holderMat;
	private JFrame frame;
	//JPanels
	static private MatPanel matPanel;
	private JPanel optionsPanel;
	private JPanel gotItPanel ;
	private JPanel countPanel;
	//JButtons
	JButton phoneNumbers_btn , gotIt_btn;
	//JLabel
	static JLabel countLabel ;
	/**
	 * Create the application.
	 */
	public MouthDJFrame() {
		frame = new JFrame();
		frame.setBounds(100, 100, 828, 720);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		matPanel = new MatPanel();
		optionsPanel = new JPanel();
		gotItPanel = new JPanel();
		countPanel = new JPanel();
		
		countLabel = new JLabel("Hello");
		
		//Button Initialize
		phoneNumbers_btn = new JButton("Phones Options");
		gotIt_btn = new JButton("Got it!");
		
		//adding to panel
		
			matPanel.setSize(holderMat.width()  , holderMat.height());
			matPanel.setimagewithMat(holderMat);
			
			optionsPanel.add(phoneNumbers_btn);
			gotItPanel.add(gotIt_btn);
			
			//countLabel.setText(String.valueOf(MouthD.count));
			countPanel.add(countLabel);
			
			eventHanlder();
				
		
		
		//adding to Frame
		frame.getContentPane().add(matPanel, BorderLayout.CENTER);
		frame.getContentPane().add(optionsPanel, BorderLayout.SOUTH);
		frame.getContentPane().add(gotItPanel, BorderLayout.WEST);
		frame.getContentPane().add(countPanel, BorderLayout.EAST);
		
		
	}

	
	
	private void eventHanlder() {
		// Buttons
		phoneNumbers_btn.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				phoneNumbers_btn.setText("Hello!!");
				frame.repaint();

			}
			
		});
		gotIt_btn.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				md.setGotResponse(true);
				System.out.println("Got Response!");
				frame.repaint();
				
			}
			
		});
		new Runnable(){

			@Override
			public void run() {
				while(true){
					countLabel.setText(String.valueOf(md.getCountDown()));		
					frame.repaint();
				}
				
			}
			
		};
	}

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);  
		md = new MouthD();
		Mat det = new Mat();
		
		VideoCapture vcap = new VideoCapture(0);
		vcap.read(det);
		MouthDJFrame.holderMat = md.detectMouth(det);
		
		MouthDJFrame window =  new MouthDJFrame();
			while(true){
				vcap.read(det);
				 MouthDJFrame.holderMat = md.detectMouth(det);
				 matPanel.setimagewithMat(holderMat);
				 countLabel.setText(String.valueOf(md.count));
				 window.frame.repaint();
			}
		
	}
	
	
	

}
