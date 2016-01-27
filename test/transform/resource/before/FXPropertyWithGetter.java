//skip compare content

class FXPropertyWithGetter {
	@lombok.Getter
	@lombok.experimental.FXProperty
	private javafx.beans.property.BooleanProperty field;
}