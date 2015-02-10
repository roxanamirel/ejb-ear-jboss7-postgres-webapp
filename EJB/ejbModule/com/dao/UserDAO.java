package com.dao;

import java.util.HashMap;
import java.util.Map;

import javax.ejb.Stateless;

import com.model.User;

@Stateless
public class UserDAO extends GenericDAO<User> {

    public UserDAO() {
	super(User.class);
    }

    public User findUserByEmail(String email) {
	Map<String, Object> parameters = new HashMap<String, Object>();
	parameters.put("email", email);

	return super.findOneResult(User.FIND_BY_EMAIL, parameters);
    }
}