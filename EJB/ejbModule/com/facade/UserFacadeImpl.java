package com.facade;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import com.dao.UserDAO;
import com.model.User;

@Stateless
public class UserFacadeImpl implements UserFacade {

	@EJB 
	private UserDAO userDAO;
	
	public User findUserByEmail(String email) {
		return userDAO.findUserByEmail(email);
	}

	public List<User> findAll() {
		return userDAO.findAll();
	}

	@Override
	public void save(User user) {
		userDAO.save(user);
	}


}