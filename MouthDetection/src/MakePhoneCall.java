import java.util.LinkedHashMap;

import com.plivo.helper.api.client.RestAPI;
import com.plivo.helper.api.response.call.Call;
import com.plivo.helper.exception.PlivoException;


public class MakePhoneCall {
       String authId = "Your AUTH_ID";
        String authToken = "Your AUTH_TOKEN";
         String pilvoPhoneNumber = "Your Pilvo phone Number";

	public void makeCall(){

 
        RestAPI restAPI = new RestAPI(authId, authToken, "v1");

		LinkedHashMap<String, String> params = new LinkedHashMap<String, String>();
		LinkedHashMap<String, String> paramstext = new LinkedHashMap<String, String>();
		
		
		for(int i=0; i< MouthD.phoneNumbersArr.size(); ++i){
			if(MouthD.phoneNumbersArr.get(i).equals("call")){
				restAPI = new RestAPI("authId", "authToken", "v1");
				
				if(MouthD.phoneNumbersArr.get(i-1) == null)throw new IllegalStateException("There is no phone Number to make a call!");
				params.put("from", pilvoPhoneNumber);
			    params.put("to", MouthD.phoneNumbersArr.get(i-1));
			    params.put("answer_url", "http://server/url/answer.xml");
			    
			    Call response;
			    
			    try {
			        response = restAPI.makeCall(params);
			        Thread.sleep(30000);
			        //restAPI.hangupAllCalls();
			        
			        //restAPI.sendMessage(paramstxt); 
			        System.out.println("1: "+response.apiId);
			        System.out.println(response.serverCode);
			        
			    } 
			    catch (PlivoException | InterruptedException e) {
			        System.out.println("1: "+e.getMessage());
			    }
			    
			}
		
		System.out.println("IN BETWEEEEEN");
			

			System.out.println(MouthD.phoneNumbersArr.get(i) );
			if(MouthD.phoneNumbersArr.get(i).equals("text") && MouthD.gotResponed == false ){
				System.out.println("##");
				restAPI = new RestAPI("authId", "authToken", "v1");
				if(MouthD.phoneNumbersArr.get(i-1) == null)throw new IllegalStateException("There is no phone Number to send a text!");
				paramstext.put("src", pilvoPhoneNumber);
			    paramstext.put("dst", MouthD.phoneNumbersArr.get(i-1));
			    paramstext.put("text","No Body Answer My Call!");
			    
			    try {
			        restAPI.sendMessage(paramstext); 
			        
			    } 
			    catch (PlivoException e) {
			        System.out.println("2: "+e.getMessage());
			    }
			    

			}
		
		
		}
		MouthD.gotResponed = false;		// Reset Got Response;
	}
	
	
	
	public static void main(String[] args){
		MakePhoneCall mpc= new MakePhoneCall();
		
		mpc.makeCall();
		
	}
}
