package ar.uba.fi.talker.utils;

import ar.uba.fi.talker.dto.TalkerDTO;

public class GridItems {

	private long id;
	private TalkerDTO elementGridView;

	public GridItems(long id, TalkerDTO elementGridView) {
		this.id = id;
		this.elementGridView = elementGridView;
	}
	
	public TalkerDTO getElementGridView() {
		return elementGridView;
	}
	
	public long getId() {
		return id;
	}
}
