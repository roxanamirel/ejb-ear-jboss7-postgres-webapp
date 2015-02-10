package com.facade;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import com.dao.DVDDAO;
import com.model.DVD;

@Stateless
public class DVDFacadeImpl implements DVDFacade {
	
	@EJB
	private DVDDAO dvdDAO;
	
	@Override
	public void save(DVD dvd) {
		isDVDWithAllData(dvd);
		
		dvdDAO.save(dvd);
	}

	@Override
	public DVD update(DVD dvd) {
		isDVDWithAllData(dvd);
		
		return dvdDAO.update(dvd);
	}
	
	@Override
	public void delete(DVD dvd) {
		dvdDAO.delete(dvd);
	}

	@Override
	public DVD find(int entityID) {
		return dvdDAO.find(entityID);
	}

	@Override
	public List<DVD> findAll() {
		return dvdDAO.findAll();
	}
	
	private void isDVDWithAllData(DVD dvd){
		boolean hasError = false;
		
		if(dvd == null){
			hasError = true;
		}
		
		if (dvd.getName() == null || "".equals(dvd.getName().trim())){
			hasError = true;
		}
		
		if(dvd.getPrice() <= 0){
			hasError = true;
		}
		
		if (hasError){
			throw new IllegalArgumentException("The dvd is missing data. Check the name and price, they should have value.");
		}
	}
}