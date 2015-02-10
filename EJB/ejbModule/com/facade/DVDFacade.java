package com.facade;

import java.util.List;

import javax.ejb.Local;

import com.model.DVD;

@Local
public interface DVDFacade {

	public abstract void save(DVD d);

	public abstract DVD update(DVD d);
	
	public abstract void delete(DVD d);

	public abstract DVD find(int entityID);

	public abstract List<DVD> findAll();
}