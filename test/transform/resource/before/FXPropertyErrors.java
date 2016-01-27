//skip compare content

class FXPropertyWithGetter {
	@lombok.Getter
	@lombok.experimental.FXProperty
	private javafx.beans.property.BooleanProperty field;
}

class FXPropertyOnPrimitive {
	@lombok.experimental.FXProperty
	private int field;
}

class FXPropertyOnNonPropertyClass {
	@lombok.experimental.FXProperty
	private Integer field;
}