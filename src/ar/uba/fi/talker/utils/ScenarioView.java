package ar.uba.fi.talker.utils;

import ar.uba.fi.talker.dao.ScenarioDAO;

public class ScenarioView {
	
	private int id;
	private int idCode;
	private String path;
	private String name;
	
	public ScenarioView(ScenarioDAO scenarioDAO) {
		this.id = scenarioDAO.getId();
		this.idCode = scenarioDAO.getIdCode();
		this.path = scenarioDAO.getPath();
		this.name = scenarioDAO.getName();
	}

	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}

	public int getIdCode() {
		return idCode;
	}

	public void setIdCode(int idCode) {
		this.idCode = idCode;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}