package modelo;

public class Serie {
	
	/************** Atributos **************/
	private String IdSerie;
    private String nombre;
    private String foto;
    private int tiempo; 

    
    /************** Constructores **************/
    public Serie() {
    	
    }

    public Serie(String pIdSerie, String pNombre, String pFoto, int pTiempo) {
        this.IdSerie = pIdSerie;
        this.nombre = pNombre;
        this.foto = pFoto;
        this.tiempo = pTiempo;
    }

    /************** Getters y Setters **************/
	public String getIdSerie() {
		return IdSerie;
	}

	public void setIdSerie(String idSerie) {
		IdSerie = idSerie;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getFoto() {
		return foto;
	}

	public void setFoto(String foto) {
		this.foto = foto;
	}

	public int getTiempo() {
		return tiempo;
	}

	public void setTiempo(int tiempo) {
		this.tiempo = tiempo;
	}
    
    
}
