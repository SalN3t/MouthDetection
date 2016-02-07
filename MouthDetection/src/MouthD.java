import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FilterReader;
import java.io.IOException;
import java.sql.Time;
import java.util.ArrayList;
import java.util.LinkedList;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;
import org.opencv.videoio.VideoCapture;


public class MouthD {

	static int count ;
	MakePhoneCall call ;
	static boolean gotResponed = false ;
	static ArrayList<String> phoneNumbersArr ; 
	Time systemTime, startTime,endTime;
	
	
	public MouthD(){
		count =0;
		call = new MakePhoneCall();
		gotResponed = false;
		//systemTime = new Time(System.currentTimeMillis());
		endTime = startTime = systemTime;
		
		phoneNumbersArr = initializePhoneNumbers(); 
		
	}
	private ArrayList<String> initializePhoneNumbers(){
		ArrayList<String> tmp = new ArrayList<String>();
		// First place hold the number the second holds type={call,text}
		//TODO FileReader from txt File
		FileReader file_to_read;
		try {
			file_to_read = new FileReader("phoneNumbers.txt");
			BufferedReader br = new BufferedReader(file_to_read);
			
			String line;

			while((line = br.readLine())!= null){
			tmp.add(line);
			}
			
			br.close();
			
			
		} catch (IOException e) {
			
			System.out.println("Can't Read The Phone Numbers File");
		}
		
		return tmp;
		
	}
	
	//Helper method to call after ADDING a number
	private void updatePhoneListAfterADD(){
		phoneNumbersArr = initializePhoneNumbers();
		
	}
	
	//Helper method to call after REMOVING a number
	private void updatePhoneListAfterREMOVE(){
		try {
			
		String filename= "phoneNumbers.txt";
	    FileWriter fw = new FileWriter(filename); //the true will append the new data
	    
	    for(int i =0; i < phoneNumbersArr.size(); ++i){
	    	
				fw.write(phoneNumbersArr.get(i));
			
	    }
	    fw.close();
	    
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("Can't UpdateAfterREMOVE");
		}
	}
	
	/**
	 * 
	 * @param phoneNumber
	 * @param type
	 */
	public void addNewPhoneNumber(String phoneNumber, String type){

		
		for(int i=0; i<phoneNumbersArr.size(); ++i){
			if(phoneNumbersArr.get(i).equals(phoneNumber)){
				if(phoneNumbersArr.get(i+1) != null && phoneNumbersArr.get(i+1).equals(type)){
					
						 new IOException("The number with the same type already exists!");
					
				}
			}
		}
		
		try
		{
		    String filename= "phoneNumbers.txt";
		    FileWriter fw = new FileWriter(filename,true); //the true will append the new data
		    fw.write(phoneNumber+"\n");//appends the string to the file
		    fw.write(type+"\n");
		    updatePhoneListAfterADD();		// Update the current phone numbers list
		    fw.close();
		}
		catch(IOException ioe)
		{
		    System.err.println("IOException In AddingNewNumber: " + ioe.getMessage());
		}
		
	}
	
	/**
	 * 
	 * @param phoneNumber
	 * @param type
	 */
	public void removePhoneNumber(String phoneNumber, String type){
		boolean itIsThere = false;
		for(int i=0;i< phoneNumbersArr.size();++i){
			if(phoneNumbersArr.get(i).equals(phoneNumber)){
				if(phoneNumbersArr.get(i+1) != null && phoneNumbersArr.get(i+1).equals(type)){
					phoneNumbersArr.remove(i);
					phoneNumbersArr.remove(i+1);
					itIsThere = true;
					updatePhoneListAfterREMOVE();
				}
			}
		}
		if(itIsThere == false) new IOException(" The Number is not in the list!!");
	}
	

	
	public void setGotResponse(boolean gotIt){
		this.gotResponed = gotIt;
		if(gotIt == true)
			try {
				Thread.sleep(10000); // Sleep for 10 minute
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				System.out.println("Exception at setGotResponse method");
			} 
	}
	
	public boolean getResponseStatus(){
		return gotResponed;
	}
	
	public ArrayList<String> viewPhoneNumberArr(){
		return phoneNumbersArr;
	}
	public int getCountDown(){
		return count;
	}
	/////////////////////////////////////////////////
	
	/**
	 * Mat Yawn Detection
	 * @param fr
	 * @return
	 */
	public Mat detectMouth(Mat fr){

		
		//updateSystemTime(); // Setting The Times Start
		
		
		//********************************
	    CascadeClassifier faceDetector = new CascadeClassifier("E:/Programs EXE Files/Programming Applications/OpenCV/opencv3-0/build/etc/haarcascades/haarcascade_frontalface_alt.xml");
		CascadeClassifier mouthDetector = new CascadeClassifier("E:/Programs EXE Files/Programming Applications/OpenCV/opencv2-4-11/build/share/OpenCV/haarcascades/haarcascade_mcs_mouth.xml");

	     
	     MatOfRect faceDetections = new MatOfRect();
	     MatOfRect mouthDetections = new MatOfRect();
	     
	     
		//********************************
		// ** START-------------

		faceDetector.detectMultiScale(fr, faceDetections);
		
		Mat mouthFrame = new Mat();
		
		// **** Make Rectangle
		
		int k=0;
		for (Rect rect : faceDetections.toArray()) {
            Imgproc.rectangle(fr, new Point(rect.x, rect.y), new Point(rect.x + rect.width , rect.y + rect.height), new Scalar(255, 0, 0));

            //if(rect.x < 70)System.out.println("Head is: ...Right...");
           // if(rect.x > 360 )System.out.println("Head is: ...Left...");
            
            mouthFrame =  fr.submat(faceDetections.toArray()[k]);
            ++k;
         }
		if((mouthFrame.height()/5)+15  >0 && (mouthFrame.width()/2) +50 > 0 && (mouthFrame.height()/2) -15 > 0 && (mouthFrame.width()/2)  -50 >0){
			// Increase or Decrease the Frame (i.e. the sensitivity of yawn capturing);
			mouthFrame = mouthFrame.submat(new Rect((mouthFrame.height()/5)+15  ,(mouthFrame.width()/2) +57, (mouthFrame.height()/2) -15, (mouthFrame.width()/2)  -57));
			mouthDetector.detectMultiScale(mouthFrame, mouthDetections);
		}
	
		
		
		if( !(mouthDetections.toArray().length > 0)){
			++ count;
			System.out.println("1 count: "+count);
			if(count > 8){
				
				//System.out.println("Stop Yawning");
				System.out.println("Making Phone Call!");
				count =0;
				call.makeCall();
			}
		}else{
			count =0;
			System.out.println("2 count: "+count);
		}
		
		
		
		for(int i =0 ; i < mouthDetections.toArray().length; ++i){
			Imgproc.rectangle(mouthFrame, new Point(mouthDetections.toArray()[i].x, mouthDetections.toArray()[i].y), new Point(mouthDetections.toArray()[i].x + mouthDetections.toArray()[i].width , mouthDetections.toArray()[i].y + mouthDetections.toArray()[i].height), new Scalar(255, 0, 0),5);			
		}
		
		
		return fr;

		// ** END--------------
		
		
	}
	
	public static void main(String[] args) throws InterruptedException{
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);  
		
		MouthD md = new MouthD();
		
		VideoCapture vcap = new VideoCapture(0);
		
		Mat det = new Mat();
		
		while(true){
			vcap.read(det);
			 det = md.detectMouth(det);
			// Imgcodecs.imwrite("faceWmouth.jpg",det);
			 //Thread.sleep(2000);
		}
	}
}
