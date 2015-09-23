package ar.uba.fi.talker.dto;


/**
 * 
 * @author sergio
 */
public class TalkerDTO {

	private long id;
	private String name;
	private String path;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getPath() {
		return path;
	}
	
	public void setPath(String path) {
		this.path = path;
	}
}
