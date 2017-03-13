class AccessorsFluent {
	@lombok.experimental.FXProperty @lombok.experimental.Accessors(fluent=true)
	private javafx.beans.property.StringProperty fieldName = new javafx.beans.property.SimpleStringProperty("");
}

class AccessorsChain {
	@lombok.experimental.FXProperty @lombok.experimental.Accessors(chain=true)
	private javafx.beans.property.StringProperty fieldName = new javafx.beans.property.SimpleStringProperty("");
}

@lombok.experimental.Accessors(prefix="f")
class AccessorsPrefix {
	@lombok.experimental.FXProperty private javafx.beans.property.StringProperty fieldName;
	@lombok.experimental.FXProperty private javafx.beans.property.StringProperty fActualField;
}

@lombok.experimental.Accessors(prefix={"f", ""})
class AccessorsPrefix2 {
	@lombok.experimental.FXProperty private javafx.beans.property.StringProperty fieldName;
	@lombok.experimental.FXProperty private javafx.beans.property.StringProperty fActualField;
}

class AccessorsFluentGenerics<T extends Number> {
	@lombok.experimental.FXProperty @lombok.experimental.Accessors(fluent=true) private javafx.beans.property.StringProperty name;
}

class AccessorsFluentNoChaining {
	@lombok.experimental.FXProperty @lombok.experimental.Accessors(fluent=true,chain=false) private javafx.beans.property.StringProperty name;
}

class AccessorsFluentStatic<T extends Number> {
	@lombok.experimental.FXProperty @lombok.experimental.Accessors(fluent=true) private static javafx.beans.property.StringProperty name;
}