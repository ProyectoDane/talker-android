package ar.uba.fi.talker.dao;

import ar.uba.fi.talker.dto.TalkerDTO;

public class ConversationDAO extends TalkerDTO {

	private String pathSnapshot;
	
	public ConversationDAO() {
	}

	public ConversationDAO(long id, String path, String name, String pathSnapshot) {
		super();
		this.setId(id);
		this.setName(name);
		this.setPath(path);
		this.pathSnapshot = pathSnapshot;
	}
	
	public String getPathSnapshot() {
		return pathSnapshot;
	}
	public void setPathSnapshot(String pathSnapshot) {
		this.pathSnapshot = pathSnapshot;
	}
}
