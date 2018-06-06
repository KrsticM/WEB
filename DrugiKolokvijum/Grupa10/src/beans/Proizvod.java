package beans;

public class Proizvod {
	private static int count = 0;
	private int id;
	private String proizvodjac;
	private String tip;
	private String model;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	private String cena;

	public Proizvod(String proizvodjac, String tip, String model, String cena) {
		super();
		this.proizvodjac = proizvodjac;
		this.tip = tip;
		this.model = model;
		this.cena = cena;
		this.id = count++;
	}
	public String getProizvodjac() {
		return proizvodjac;
	}
	public void setProizvodjac(String proizvodjac) {
		this.proizvodjac = proizvodjac;
	}
	public String getTip() {
		return tip;
	}
	public void setTip(String tip) {
		this.tip = tip;
	}
	public String getModel() {
		return model;
	}
	public void setModel(String model) {
		this.model = model;
	}
	public String getCena() {
		return cena;
	}
	public void setCena(String cena) {
		this.cena = cena;
	}	
}
