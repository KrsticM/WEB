package dao;

import java.util.ArrayList;
import java.util.List;

import beans.Proizvod;

public class ProizvodiDAO {
	private List<Proizvod> proizvodi = new ArrayList<Proizvod>();

	public List<Proizvod> getProizvodi() {
		return proizvodi;
	}

	public void setProizvodi(List<Proizvod> proizvodi) {
		this.proizvodi = proizvodi;
	}

	public List<Proizvod> izbaciPunjace() {
		List<Proizvod> retVal = new ArrayList<Proizvod>();

		for(int i=proizvodi.size()-1; i>= 0; i--)
		{
			if (!proizvodi.get(i).getTip().equals("Telefon"))
			{
				retVal.add(proizvodi.get(i));
				proizvodi.remove(i);
			}				
		}

		return retVal;
	}

	public void removeProizvod(String id) {
		int idInt = Integer.parseInt(id);
		for(int i=proizvodi.size()-1; i>=0; i--)
		{
			if(proizvodi.get(i).getId() == idInt)
			{
				proizvodi.remove(i);
			}
		}
			
			
	}

}
