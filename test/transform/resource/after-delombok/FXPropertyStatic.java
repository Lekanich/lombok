

class FXPropertyStatic {

	private static javafx.beans.property.BooleanProperty field;

	@java.lang.SuppressWarnings("all")
	@javax.annotation.Generated("lombok")
	public static javafx.beans.property.BooleanProperty fieldProperty() {
		return FXPropertyStatic.field;
	}

	@java.lang.SuppressWarnings("all")
	@javax.annotation.Generated("lombok")
	public static void setField(final java.lang.Boolean field) {
		FXPropertyStatic.field.setValue(field);
	}

	@java.lang.SuppressWarnings("all")
	@javax.annotation.Generated("lombok")
	public static java.lang.Boolean isField() {
		return field.getValue();
	}
}