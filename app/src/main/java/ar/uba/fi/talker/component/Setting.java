package ar.uba.fi.talker.component;

public class Setting {

	private String textColor;
	private float textSize;
	private String pencilColor;
	private float pencilSize;
	private float eraserSize;
	private Boolean isEnabledLabelImage;
	private Boolean isEnabledLabelContact;

	public float getPencilSize() {
		return pencilSize;
	}

	public void setPencilSize(float pencilSize) {
		this.pencilSize = pencilSize;
	}

	public String getPencilColor() {
		return pencilColor;
	}

	public void setPencilColor(String pencilColor) {
		this.pencilColor = pencilColor;
	}

	public Boolean getIsEnabledLabelImage() {
		return isEnabledLabelImage;
	}

	public void setIsEnabledLabelImage(Boolean isEnabledLabelImage) {
		this.isEnabledLabelImage = isEnabledLabelImage;
	}

	public Boolean getIsEnabledLabelContact() {
		return isEnabledLabelContact;
	}

	public void setIsEnabledLabelContact(Boolean isEnabledLabelContact) {
		this.isEnabledLabelContact = isEnabledLabelContact;
	}

	public String getTextColor() {
		return textColor;
	}

	public void setTextColor(String textColor) {
		this.textColor = textColor;
	}

	public float getEraserSize() {
		return eraserSize;
	}

	public void setEraserSize(float eraserSize) {
		this.eraserSize = eraserSize;
	}

	public float getTextSize() {
		return textSize;
	}

	public void setTextSize(float textSize) {
		this.textSize = textSize;
	}
	
}
