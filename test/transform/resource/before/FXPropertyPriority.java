@lombok.Data
class FXPropertyWithData {
	@lombok.experimental.FXProperty
	private javafx.beans.property.BooleanProperty propertyField;
	private int noPropertyField;
}

@lombok.Getter
class FXPropertyWithGetter {
	@lombok.experimental.FXProperty
	private javafx.beans.property.BooleanProperty propertyField;
	private int noPropertyField;
}

@lombok.Setter
class FXPropertyWithSetter {
	@lombok.experimental.FXProperty
	private javafx.beans.property.BooleanProperty propertyField;
	private int noPropertyField;
}