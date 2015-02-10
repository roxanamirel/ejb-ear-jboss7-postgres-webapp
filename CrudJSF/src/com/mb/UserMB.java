package com.mb;

import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

import com.facade.UserFacade;
import com.model.User;

@SessionScoped
@ManagedBean(name = "userMB")
public class UserMB {
	private User user;

	@EJB
	private UserFacade userFacade;
	private static final String REGISTER_USER = "register";
	private static final String LOGIN = "login";

	@PermitAll
	public User getUser() {
		if (user == null) {
			ExternalContext context = FacesContext.getCurrentInstance()
					.getExternalContext();
			if( context.getUserPrincipal()!=null){
			String userEmail = context.getUserPrincipal().getName();
			user = userFacade.findUserByEmail(userEmail);
			}
			else{
				user = new User();
			}
		}

		return user;
	}
	@PermitAll
	public String registerStart() {
		
		return REGISTER_USER;

	}
	@PermitAll
	public String cancel() {
		return LOGIN;

	}
	@PermitAll
	public String registerEnd() {
		try {
			
			user.setRole("USER");
			userFacade.save(user);
		} catch (EJBException e) {
			return REGISTER_USER;
		}
		return LOGIN;

	}

	public boolean isUserAdmin() {
		return getRequest().isUserInRole("ADMIN");
	}

	public String logOut() {
		getRequest().getSession().invalidate();
		return "logout";
	}

	private HttpServletRequest getRequest() {
		return (HttpServletRequest) FacesContext.getCurrentInstance()
				.getExternalContext().getRequest();
	}
}
