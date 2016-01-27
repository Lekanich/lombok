

class AccessorsFluent {

	private javafx.beans.property.StringProperty fieldName = new javafx.beans.property.SimpleStringProperty("");

	@java.lang.SuppressWarnings("all")
	@javax.annotation.Generated("lombok")
	public javafx.beans.property.StringProperty fieldNameProperty() {
		return this.fieldName;
	}

	@java.lang.SuppressWarnings("all")
	@javax.annotation.Generated("lombok")
	public AccessorsFluent fieldName(final java.lang.String fieldName) {
		this.fieldName.setValue(fieldName);
		return this;
	}

	@java.lang.SuppressWarnings("all")
	@javax.annotation.Generated("lombok")
	public java.lang.String fieldName() {
		return fieldName.getValue();
	}
}
class AccessorsChain {

	private javafx.beans.property.StringProperty fieldName = new javafx.beans.property.SimpleStringProperty("");

	@java.lang.SuppressWarnings("all")
	@javax.annotation.Generated("lombok")
	public javafx.beans.property.StringProperty fieldNameProperty() {
		return this.fieldName;
	}

	@java.lang.SuppressWarnings("all")
	@javax.annotation.Generated("lombok")
	public AccessorsChain setFieldName(final java.lang.String fieldName) {
		this.fieldName.setValue(fieldName);
		return this;
	}

	@java.lang.SuppressWarnings("all")
	@javax.annotation.Generated("lombok")
	public java.lang.String getFieldName() {
		return fieldName.getValue();
	}
}
class AccessorsPrefix {

	private javafx.beans.property.StringProperty fieldName;
	private javafx.beans.property.StringProperty fActualField;

	@java.lang.SuppressWarnings("all")
	@javax.annotation.Generated("lombok")
	public javafx.beans.property.StringProperty actualFieldProperty() {
		return this.fActualField;
	}

	@java.lang.SuppressWarnings("all")
	@javax.annotation.Generated("lombok")
	public void setActualField(final java.lang.String actualField) {
		this.fActualField.setValue(actualField);
	}

	@java.lang.SuppressWarnings("all")
	@javax.annotation.Generated("lombok")
	public java.lang.String getActualField() {
		return fActualField.getValue();
	}
}
class AccessorsPrefix2 {

	private javafx.beans.property.StringProperty fieldName;
	private javafx.beans.property.StringProperty fActualField;

	@java.lang.SuppressWarnings("all")
	@javax.annotation.Generated("lombok")
	public javafx.beans.property.StringProperty fieldNameProperty() {
		return this.fieldName;
	}

	@java.lang.SuppressWarnings("all")
	@javax.annotation.Generated("lombok")
	public void setFieldName(final java.lang.String fieldName) {
		this.fieldName.setValue(fieldName);
	}

	@java.lang.SuppressWarnings("all")
	@javax.annotation.Generated("lombok")
	public java.lang.String getFieldName() {
		return fieldName.getValue();
	}

	@java.lang.SuppressWarnings("all")
	@javax.annotation.Generated("lombok")
	public javafx.beans.property.StringProperty actualFieldProperty() {
		return this.fActualField;
	}

	@java.lang.SuppressWarnings("all")
	@javax.annotation.Generated("lombok")
	public void setActualField(final java.lang.String actualField) {
		this.fActualField.setValue(actualField);
	}

	@java.lang.SuppressWarnings("all")
	@javax.annotation.Generated("lombok")
	public java.lang.String getActualField() {
		return fActualField.getValue();
	}
}
class AccessorsFluentGenerics<T extends Number> {

	private javafx.beans.property.StringProperty name;

	@java.lang.SuppressWarnings("all")
	@javax.annotation.Generated("lombok")
	public javafx.beans.property.StringProperty nameProperty() {
		return this.name;
	}

	@java.lang.SuppressWarnings("all")
	@javax.annotation.Generated("lombok")
	public AccessorsFluentGenerics<T> name(final java.lang.String name) {
		this.name.setValue(name);
		return this;
	}

	@java.lang.SuppressWarnings("all")
	@javax.annotation.Generated("lombok")
	public java.lang.String name() {
		return name.getValue();
	}
}
class AccessorsFluentNoChaining {

	private javafx.beans.property.StringProperty name;

	@java.lang.SuppressWarnings("all")
	@javax.annotation.Generated("lombok")
	public javafx.beans.property.StringProperty nameProperty() {
		return this.name;
	}

	@java.lang.SuppressWarnings("all")
	@javax.annotation.Generated("lombok")
	public void name(final java.lang.String name) {
		this.name.setValue(name);
	}

	@java.lang.SuppressWarnings("all")
	@javax.annotation.Generated("lombok")
	public java.lang.String name() {
		return name.getValue();
	}
}
class AccessorsFluentStatic<T extends Number> {

	private static javafx.beans.property.StringProperty name;

	@java.lang.SuppressWarnings("all")
	@javax.annotation.Generated("lombok")
	public static javafx.beans.property.StringProperty nameProperty() {
		return AccessorsFluentStatic.name;
	}

	@java.lang.SuppressWarnings("all")
	@javax.annotation.Generated("lombok")
	public static void name(final java.lang.String name) {
		AccessorsFluentStatic.name.setValue(name);
	}

	@java.lang.SuppressWarnings("all")
	@javax.annotation.Generated("lombok")
	public static java.lang.String name() {
		return name.getValue();
	}
}