package ar.uba.fi.talker.dao;

import ar.uba.fi.talker.dto.TalkerDTO;

/**
 * @author earias
 *
 */
public class ScenarioDAO extends TalkerDTO {

	public ScenarioDAO() {
	}

	public ScenarioDAO(long keyId, String path, String name) {
		super();
		setId(keyId);
		this.setName(name);
		this.setPath(path);
	}

}
