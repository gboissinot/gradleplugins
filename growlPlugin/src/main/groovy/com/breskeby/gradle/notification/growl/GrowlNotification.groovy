package com.breskeby.gradle.notification.growl;


import org.gradle.api.tasks.TaskAction
import info.growl.Growl;
import info.growl.GrowlException;
import info.growl.GrowlUtils;

import com.breskeby.gradle.notification.AbstractNotificationTask;
import com.breskeby.gradle.notification.NotifyConfiguration

public class GrowlNotification extends AbstractNotificationTask{
    
    private void loadNativeGrowlLib(){
    	File f = null;
    	 try{
    		f = new File("libgrowl.jnilib");
    	    InputStream inputStream = getClass().getResourceAsStream("/libgrowl.jnilib");
    	    
    	    f.append(inputStream)
    	    inputStream.close();
    	 }
    	 catch (IOException e){
    	    	
    	 }
    	 if(f!=null){
    		 f.deleteOnExit();
    	 }	
	}
	

	@TaskAction
	public void sendNotification(String message) {
		loadNativeGrowlLib()
		Growl growl = GrowlUtils.getGrowlInstance("Gradle");
		growl.addNotification("Gradle Notification!", true);
		try {
			growl.register();
			growl.sendNotification("Gradle Notification!", "Gradle Build", message);
		} catch (GrowlException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
