package core;

public class Size {
	private String value;
	private String unit;
	
	/**
	 * Size information of recipe.
	 * 
	 * @param value Size
	 * @param unit Unit of size
	 */
	public Size(String value, String unit) {
		this.setValue(value);
		this.setUnit(unit);
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

}
