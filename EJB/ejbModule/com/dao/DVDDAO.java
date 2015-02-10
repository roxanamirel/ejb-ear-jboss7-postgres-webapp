package com.dao;

import javax.ejb.Stateless;

import com.model.DVD;

@Stateless
public class DVDDAO extends GenericDAO<DVD> {

    public DVDDAO() {
	super(DVD.class);
    }
    
    public void delete(DVD d) {
        super.delete(d.getId(), DVD.class);
    }
}