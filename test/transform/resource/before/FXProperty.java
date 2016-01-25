class FXProperty {
	@lombok.experimental.FXProperty(lombok.AccessLevel.NONE)
	javafx.beans.property.BooleanProperty isNone;
	@lombok.experimental.FXProperty(lombok.AccessLevel.PRIVATE)
	javafx.beans.property.BooleanPropertyBase isPrivate;
	@lombok.experimental.FXProperty(lombok.AccessLevel.PACKAGE)
	javafx.beans.property.ObjectProperty<Boolean> isPackage;
	@lombok.experimental.FXProperty(lombok.AccessLevel.PROTECTED)
	javafx.beans.property.ListProperty<Boolean> booleansList;
	@lombok.experimental.FXProperty(lombok.AccessLevel.PUBLIC)
	javafx.beans.property.MapProperty<Boolean, Integer> mapPublic;
	@lombok.experimental.FXProperty(lombok.AccessLevel.NONE)
	javafx.beans.property.StringProperty noneString;
	@lombok.experimental.FXProperty(lombok.AccessLevel.PRIVATE)
	javafx.beans.property.IntegerProperty privateInteger;
	@lombok.experimental.FXProperty(lombok.AccessLevel.PACKAGE)
	javafx.beans.property.SimpleIntegerProperty packageInteger;
	@lombok.experimental.FXProperty(lombok.AccessLevel.PROTECTED)
	javafx.beans.property.ReadOnlyBooleanProperty protectedReadOnlyBoolean;
}