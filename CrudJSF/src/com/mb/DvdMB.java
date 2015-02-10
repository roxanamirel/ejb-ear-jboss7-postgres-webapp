package com.mb;

import java.util.List;
import java.util.Properties;

import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import com.facade.DVDFacade;
import com.facade.UserFacade;
import com.model.DVD;
import com.model.User;

@ManagedBean(name = "dvdMB")
@RequestScoped
public class DvdMB {

	@EJB
	private DVDFacade dvdFacade;
	private static final String CREATE_DVD = "createDVD";
	private static final String DELETE_DVD = "deleteDVD";
	private static final String UPDATE_DOG = "updateDVD";
	private static final String BUY_DVD = "buyDVD";
	private static final String LIST_ALL_DVDS = "listAllDVDS";
	private static final String STAY_IN_THE_SAME_PAGE = null;
	@EJB
	private UserFacade userFacade;
	private DVD dvd;

	public DVD getDVD() {
		if (dvd == null) {
			dvd = new DVD();
		}
		return dvd;
	}

	public void setDVD(DVD dvd) {
		this.dvd = dvd;
	}

	public List<DVD> getAllDVDs() {
		return dvdFacade.findAll();
	}

	public String updateDVDStart() {
		return UPDATE_DOG;
	}

	public String updateDVDEnd() {
		try {
			dvdFacade.update(dvd);
		} catch (EJBException e) {
			sendErrorMessageToUser("Error. Check if the price is above 0 or call the adm");
			return STAY_IN_THE_SAME_PAGE;
		}

		sendInfoMessageToUser("Operation Complete: Update");
		return LIST_ALL_DVDS;
	}

	public String deleteDVDStart() {
		return DELETE_DVD;
	}

	public String deleteDVDEnd() {
		try {
			dvdFacade.delete(dvd);
		} catch (EJBException e) {
			sendErrorMessageToUser("Error. Call the ADM");
			return STAY_IN_THE_SAME_PAGE;
		}

		sendInfoMessageToUser("Operation Complete: Delete");

		return LIST_ALL_DVDS;
	}

	public String buyDVDStart() {
		return BUY_DVD;
	}

	public String buyDVDEnd() {
		User user;
		try {
			ExternalContext context = FacesContext.getCurrentInstance()
					.getExternalContext();
			String userEmail = context.getUserPrincipal().getName();
			user = userFacade.findUserByEmail(userEmail);
			this.getDVD().setUser(user);
			dvdFacade.update(dvd);
		} catch (EJBException e) {
			sendErrorMessageToUser("Error. Call the ADM");
			return STAY_IN_THE_SAME_PAGE;
		}
		double sum = 0;
		List<DVD> dvds = dvdFacade.findAll();
		for (DVD d : dvds) {
			if (d.getUser() != null && d.getUser().equals(user)) {
				sum = sum + d.getPrice();
			}

		}
		sendInfoMessageToUser("Operation Complete: Bought DVD. To pay: " + sum);

		return LIST_ALL_DVDS;
	}

	public String createDVDStart() {
		return CREATE_DVD;
	}

	public String createDVDEnd() throws JMSException, NamingException {
		InitialContext ic = null;
		ic = new InitialContext();
		Connection conn = null;
		Session session = null;
		MessageProducer producer = null;
		MessageConsumer consumer = null;
		// Creating connection factory
		ConnectionFactory cnf = (ConnectionFactory) ic
				.lookup("/ConnectionFactory");
		// Creating Connection
		conn = cnf.createConnection();
		conn.start();
		// Creating Session
		session = conn.createSession(false, Session.AUTO_ACKNOWLEDGE);
		Destination destination = (Destination) ic.lookup("queue/MyQueue");
		// Creating producer
		producer = session.createProducer(destination);
		// Creating Messages
		TextMessage msg = session.createTextMessage("Message");
		msg.setText("Pretech Message");
		// Sending messages
		producer.send(msg);
		sendInfoMessageToUser(msg.getText());
		consumer = session.createConsumer(destination);
		List<User> users = userFacade.findAll();
		for (User u : users) {
			sendMail(u.getEmail(), dvd, consumer, msg);
		}
		producer.close();
		session.close();
		conn.stop();

		try {
			dvdFacade.save(dvd);
		} catch (EJBException e) {
			sendErrorMessageToUser("Error. Check if the price is above 0 or call the adm");

			return STAY_IN_THE_SAME_PAGE;
		}

		sendInfoMessageToUser("Operation Complete: Create");

		return LIST_ALL_DVDS;
	}

	private void sendMail(String email, DVD dvd, MessageConsumer consumer,
			TextMessage message) throws JMSException {
		Properties props = new Properties();
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.socketFactory.port", "465");
		props.put("mail.smtp.socketFactory.class",
				"javax.net.ssl.SSLSocketFactory");
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.port", "465");

		while (consumer.receive(1)!=null) {
			 Message m = consumer.receive(1);
			if (m != null) {
				if (m instanceof TextMessage) {
					message = (TextMessage) m;
					System.out.println("Reading message: " + message.getText());
				} else {
					break;
				}
			}

		}
		javax.mail.Session session = javax.mail.Session.getDefaultInstance(
				props, new javax.mail.Authenticator() {
					protected PasswordAuthentication getPasswordAuthentication() {
						return new PasswordAuthentication(
								"rroxanaioana@gmail.com", "Loxyme12");
					}
				});

		try {

			javax.mail.Message messagee = new MimeMessage(session);
			messagee.setFrom(new InternetAddress("rroxanaioana@gmail.com"));
			messagee.setRecipients(javax.mail.Message.RecipientType.TO,
					InternetAddress.parse(email));
			messagee.setSubject("New stuff in your DVD Collection");
			messagee.setText("Dear Subscriber,"
					+ "\n\n Don't miss our new offer! The DVD: "
					+ dvd.getName() + " is in our store."
					+ "You can buy it with only " + dvd.getPrice() + " USD. This is a "
					+ message.getText());

			Transport.send(messagee);

			System.out.println("Done");

		} catch (MessagingException e) {
			throw new RuntimeException(e);
		}

	}

	public String listAllDVDs() {
		return LIST_ALL_DVDS;
	}

	private void sendInfoMessageToUser(String message) {
		FacesContext context = getContext();
		context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
				message, message));
	}

	private void sendErrorMessageToUser(String message) {
		FacesContext context = getContext();
		context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
				message, message));
	}

	private FacesContext getContext() {
		FacesContext context = FacesContext.getCurrentInstance();
		return context;
	}
}
