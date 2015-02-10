package com.jms;

import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;


@MessageDriven(
		activationConfig = { @ActivationConfigProperty(
				propertyName = "destinationType", propertyValue = "javax.jms.Queue"),
				@ActivationConfigProperty(propertyName = "destination", propertyValue = "queue/Pretech"),
		        @ActivationConfigProperty(propertyName = "acknowledgeMode", propertyValue = "Auto-acknowledge"),
		        @ActivationConfigProperty(propertyName = "maxSession", propertyValue = "0") })

				
		
public class MDBean implements MessageListener {

  
    public MDBean() {
    }
	

    public void onMessage(Message message) {
    	if (message instanceof TextMessage) {
            TextMessage tm = (TextMessage) message;
            try {
                  System.out.println("Message received in listener "
                                + tm.getText());
            } catch (JMSException e) {
                  e.printStackTrace();
            }
     }        
    }

}
