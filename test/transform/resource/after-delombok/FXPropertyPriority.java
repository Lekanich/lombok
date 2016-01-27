

class FXPropertyWithData {
	private javafx.beans.property.BooleanProperty propertyField;
	private int noPropertyField;

	@java.lang.SuppressWarnings("all")
	@javax.annotation.Generated("lombok")
	public javafx.beans.property.BooleanProperty propertyFieldProperty() {
		return this.propertyField;
	}

	@java.lang.SuppressWarnings("all")
	@javax.annotation.Generated("lombok")
	public void setPropertyField(final java.lang.Boolean propertyField) {
		this.propertyField.setValue(propertyField);
	}

	@java.lang.SuppressWarnings("all")
	@javax.annotation.Generated("lombok")
	public java.lang.Boolean isPropertyField() {
		return propertyField.getValue();
	}

	@java.lang.SuppressWarnings("all")
	@javax.annotation.Generated("lombok")
	public FXPropertyWithData() {
	}

	@java.lang.SuppressWarnings("all")
	@javax.annotation.Generated("lombok")
	public javafx.beans.property.BooleanProperty getPropertyField() {
		return this.propertyField;
	}

	@java.lang.SuppressWarnings("all")
	@javax.annotation.Generated("lombok")
	public int getNoPropertyField() {
		return this.noPropertyField;
	}

	@java.lang.SuppressWarnings("all")
	@javax.annotation.Generated("lombok")
	public void setNoPropertyField(final int noPropertyField) {
		this.noPropertyField = noPropertyField;
	}

	@java.lang.Override
	@java.lang.SuppressWarnings("all")
	@javax.annotation.Generated("lombok")
	public boolean equals(final java.lang.Object o) {
		if (o == this) return true;
		if (!(o instanceof FXPropertyWithData)) return false;
		final FXPropertyWithData other = (FXPropertyWithData)o;
		if (!other.canEqual((java.lang.Object)this)) return false;
		final java.lang.Object this$propertyField = this.getPropertyField();
		final java.lang.Object other$propertyField = other.getPropertyField();
		if (this$propertyField == null ? other$propertyField != null : !this$propertyField.equals(other$propertyField)) return false;
		if (this.getNoPropertyField() != other.getNoPropertyField()) return false;
		return true;
	}

	@java.lang.SuppressWarnings("all")
	@javax.annotation.Generated("lombok")
	protected boolean canEqual(final java.lang.Object other) {
		return other instanceof FXPropertyWithData;
	}

	@java.lang.Override
	@java.lang.SuppressWarnings("all")
	@javax.annotation.Generated("lombok")
	public int hashCode() {
		final int PRIME = 59;
		int result = 1;
		final java.lang.Object $propertyField = this.getPropertyField();
		result = result * PRIME + ($propertyField == null ? 43 : $propertyField.hashCode());
		result = result * PRIME + this.getNoPropertyField();
		return result;
	}

	@java.lang.Override
	@java.lang.SuppressWarnings("all")
	@javax.annotation.Generated("lombok")
	public java.lang.String toString() {
		return "FXPropertyWithData(propertyField=" + this.getPropertyField() + ", noPropertyField=" + this.getNoPropertyField() + ")";
	}
}
class FXPropertyWithGetter {

	private javafx.beans.property.BooleanProperty propertyField;
	private int noPropertyField;

	@java.lang.SuppressWarnings("all")
	@javax.annotation.Generated("lombok")
	public javafx.beans.property.BooleanProperty propertyFieldProperty() {
		return this.propertyField;
	}

	@java.lang.SuppressWarnings("all")
	@javax.annotation.Generated("lombok")
	public void setPropertyField(final java.lang.Boolean propertyField) {
		this.propertyField.setValue(propertyField);
	}

	@java.lang.SuppressWarnings("all")
	@javax.annotation.Generated("lombok")
	public java.lang.Boolean isPropertyField() {
		return propertyField.getValue();
	}

	@java.lang.SuppressWarnings("all")
	@javax.annotation.Generated("lombok")
	public javafx.beans.property.BooleanProperty getPropertyField() {
		return this.propertyField;
	}

	@java.lang.SuppressWarnings("all")
	@javax.annotation.Generated("lombok")
	public int getNoPropertyField() {
		return this.noPropertyField;
	}
}
class FXPropertyWithSetter {

	private javafx.beans.property.BooleanProperty propertyField;
	private int noPropertyField;

	@java.lang.SuppressWarnings("all")
	@javax.annotation.Generated("lombok")
	public javafx.beans.property.BooleanProperty propertyFieldProperty() {
		return this.propertyField;
	}

	@java.lang.SuppressWarnings("all")
	@javax.annotation.Generated("lombok")
	public void setPropertyField(final java.lang.Boolean propertyField) {
		this.propertyField.setValue(propertyField);
	}

	@java.lang.SuppressWarnings("all")
	@javax.annotation.Generated("lombok")
	public java.lang.Boolean isPropertyField() {
		return propertyField.getValue();
	}

	@java.lang.SuppressWarnings("all")
	@javax.annotation.Generated("lombok")
	public void setNoPropertyField(final int noPropertyField) {
		this.noPropertyField = noPropertyField;
	}
}