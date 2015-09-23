package ar.uba.fi.talker.dao;

import ar.uba.fi.talker.dto.TalkerDTO;

/**
 * @author astamato
 * 
 */
public class ImageDAO extends TalkerDTO {

	private long idCategory;

	public ImageDAO() {
	}

	public ImageDAO(int id, long idCategory, String path, String name) {
		super();
		this.setId(id);
		this.setName(name);
		this.idCategory = idCategory;
		this.setPath(path);
	}

	public long getIdCategory() {
		return idCategory;
	}

	public void setIdCategory(long idCategory) {
		this.idCategory = idCategory;
	}

}
