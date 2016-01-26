

class FXProperty {

	javafx.beans.property.BooleanProperty isNone;
	javafx.beans.property.BooleanPropertyBase isPrivate;
	javafx.beans.property.ObjectProperty<Boolean> isPackage;
	javafx.beans.property.ListProperty<Boolean> booleansList;
	javafx.beans.property.MapProperty<Boolean, Integer> mapPublic;
	javafx.beans.property.StringProperty noneString;
	javafx.beans.property.IntegerProperty privateInteger;
	javafx.beans.property.SimpleIntegerProperty packageInteger;
	javafx.beans.property.ReadOnlyBooleanProperty protectedReadOnlyBoolean;

	@java.lang.SuppressWarnings("all")
	@javax.annotation.Generated("lombok")
	private javafx.beans.property.BooleanProperty isNoneProperty() {
		return this.isNone;
	}

	@java.lang.SuppressWarnings("all")
	@javax.annotation.Generated("lombok")
	private void setIsNone(final java.lang.Boolean isNone) {
		this.isNone.setValue(isNone);
	}

	@java.lang.SuppressWarnings("all")
	@javax.annotation.Generated("lombok")
	private java.lang.Boolean isNone() {
		return isNone.getValue();
	}

	@java.lang.SuppressWarnings("all")
	@javax.annotation.Generated("lombok")
	private javafx.beans.property.BooleanPropertyBase isPrivateProperty() {
		return this.isPrivate;
	}

	@java.lang.SuppressWarnings("all")
	@javax.annotation.Generated("lombok")
	private void setIsPrivate(final java.lang.Boolean isPrivate) {
		this.isPrivate.setValue(isPrivate);
	}

	@java.lang.SuppressWarnings("all")
	@javax.annotation.Generated("lombok")
	private java.lang.Boolean isPrivate() {
		return isPrivate.getValue();
	}

	@java.lang.SuppressWarnings("all")
	@javax.annotation.Generated("lombok")
	javafx.beans.property.ObjectProperty<Boolean> isPackageProperty() {
		return this.isPackage;
	}

	@java.lang.SuppressWarnings("all")
	@javax.annotation.Generated("lombok")
	void setIsPackage(final java.lang.Boolean isPackage) {
		this.isPackage.setValue(isPackage);
	}

	@java.lang.SuppressWarnings("all")
	@javax.annotation.Generated("lombok")
	java.lang.Boolean isPackage() {
		return isPackage.getValue();
	}

	@java.lang.SuppressWarnings("all")
	@javax.annotation.Generated("lombok")
	protected javafx.beans.property.ListProperty<Boolean> booleansListProperty() {
		return this.booleansList;
	}

	@java.lang.SuppressWarnings("all")
	@javax.annotation.Generated("lombok")
	protected void setBooleansList(final javafx.collections.ObservableList<java.lang.Boolean> booleansList) {
		this.booleansList.setValue(booleansList);
	}

	@java.lang.SuppressWarnings("all")
	@javax.annotation.Generated("lombok")
	protected javafx.collections.ObservableList<java.lang.Boolean> getBooleansList() {
		return booleansList.getValue();
	}

	@java.lang.SuppressWarnings("all")
	@javax.annotation.Generated("lombok")
	public javafx.beans.property.MapProperty<Boolean, Integer> mapPublicProperty() {
		return this.mapPublic;
	}

	@java.lang.SuppressWarnings("all")
	@javax.annotation.Generated("lombok")
	public void setMapPublic(final javafx.collections.ObservableMap<java.lang.Boolean, java.lang.Integer> mapPublic) {
		this.mapPublic.setValue(mapPublic);
	}

	@java.lang.SuppressWarnings("all")
	@javax.annotation.Generated("lombok")
	public javafx.collections.ObservableMap<java.lang.Boolean, java.lang.Integer> getMapPublic() {
		return mapPublic.getValue();
	}

	@java.lang.SuppressWarnings("all")
	@javax.annotation.Generated("lombok")
	private javafx.beans.property.StringProperty noneStringProperty() {
		return this.noneString;
	}

	@java.lang.SuppressWarnings("all")
	@javax.annotation.Generated("lombok")
	private void setNoneString(final java.lang.String noneString) {
		this.noneString.setValue(noneString);
	}

	@java.lang.SuppressWarnings("all")
	@javax.annotation.Generated("lombok")
	private java.lang.String getNoneString() {
		return noneString.getValue();
	}

	@java.lang.SuppressWarnings("all")
	@javax.annotation.Generated("lombok")
	private javafx.beans.property.IntegerProperty privateIntegerProperty() {
		return this.privateInteger;
	}

	@java.lang.SuppressWarnings("all")
	@javax.annotation.Generated("lombok")
	private void setPrivateInteger(final java.lang.Integer privateInteger) {
		this.privateInteger.setValue(privateInteger);
	}

	@java.lang.SuppressWarnings("all")
	@javax.annotation.Generated("lombok")
	private java.lang.Integer getPrivateInteger() {
		return privateInteger.getValue();
	}

	@java.lang.SuppressWarnings("all")
	@javax.annotation.Generated("lombok")
	javafx.beans.property.SimpleIntegerProperty packageIntegerProperty() {
		return this.packageInteger;
	}

	@java.lang.SuppressWarnings("all")
	@javax.annotation.Generated("lombok")
	void setPackageInteger(final java.lang.Integer packageInteger) {
		this.packageInteger.setValue(packageInteger);
	}

	@java.lang.SuppressWarnings("all")
	@javax.annotation.Generated("lombok")
	java.lang.Integer getPackageInteger() {
		return packageInteger.getValue();
	}

	@java.lang.SuppressWarnings("all")
	@javax.annotation.Generated("lombok")
	protected javafx.beans.property.ReadOnlyBooleanProperty protectedReadOnlyBooleanProperty() {
		return this.protectedReadOnlyBoolean;
	}

	@java.lang.SuppressWarnings("all")
	@javax.annotation.Generated("lombok")
	protected java.lang.Boolean isProtectedReadOnlyBoolean() {
		return protectedReadOnlyBoolean.getValue();
	}
}