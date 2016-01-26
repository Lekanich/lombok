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
	@lombok.experimental.FXProperty(lombok.AccessLevel.PROTECTED)
	CustomPropety<String> customProperty;
	@lombok.experimental.FXProperty(lombok.AccessLevel.PROTECTED)
	CustomCollectionProperty<String> customCollectionProperty;
}

class CustomPropety<T> extends javafx.beans.property.SimpleObjectProperty<T> {}
class CustomCollectionProperty<C> extends javafx.beans.property.SimpleListProperty<C> {}