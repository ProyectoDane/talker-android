package ar.uba.fi.talker.dao;

import android.os.Parcel;
import android.os.Parcelable;
import ar.uba.fi.talker.dto.TalkerDTO;

public class ContactDAO extends TalkerDTO implements Parcelable {

	private long imageId;
	private String phone;
	private String address;

	public ContactDAO() {
		super();
	}
	
	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeLong(getId());
		dest.writeLong(getImageId());
		dest.writeString(getName());
		dest.writeString(getAddress());
		dest.writeString(getPath());
		dest.writeString(getPhone());
	}
	
	public static final Creator<ContactDAO> CREATOR = new Creator<ContactDAO>() {

		@Override
		public ContactDAO createFromParcel(Parcel source) {
			ContactDAO dao = new ContactDAO();
			dao.setId(source.readLong());
			dao.setImageId(source.readLong());
			dao.setName(source.readString());
			dao.setAddress(source.readString());
			dao.setPath(source.readString());
			dao.setPhone(source.readString());
			return dao;
		}

		@Override
		public ContactDAO[] newArray(int size) {
			return null;
		}
	};
	
	public ContactDAO(int id, long imageId, String phone, String address) {
		super();
		this.setId(id);
		this.imageId = imageId;
		this.phone = phone;
		this.address = address;
	}

	public long getImageId() {
		return imageId;
	}

	public void setImageId(long imageId) {
		this.imageId = imageId;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

}
