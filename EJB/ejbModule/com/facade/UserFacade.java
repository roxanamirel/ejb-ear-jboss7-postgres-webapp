package com.facade;

import java.util.List;

import javax.ejb.Local;



import com.model.User;

@Local
public interface UserFacade {
	public User findUserByEmail(String email);
	public List<User>findAll();
	public void save(User user);

}