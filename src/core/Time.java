package core;

public class Time {
	private String value;
	private String unit;
	
	/**
	 * Preparation time information of recipe.
	 * 
	 * @param value Preparation time
	 * @param unit Unit of preparation time
	 */
	public Time(String value, String unit) {
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
