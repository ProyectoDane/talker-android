package ar.uba.fi.talker.utils;

import ar.uba.fi.talker.dao.ConversationDAO;

public class GridConversationItems {

	private int id;
	private ConversationDAO conversationView;

	public GridConversationItems(int id, ConversationDAO conversationView) {
		this.id = id;
		this.conversationView = conversationView;
	}

	public ConversationDAO getConversationDAO() {
		return conversationView;
	}

	public int getId() {
		return id;
	}
}
