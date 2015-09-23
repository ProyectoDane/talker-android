package ar.uba.fi.talker.component;

import android.content.Context;

public class Contact extends Image {
	
	private String name;
	private String phone;
	
	public Contact(Context arg0) {
		super(arg0);
		// TODO Auto-generated constructor stub
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	
}
