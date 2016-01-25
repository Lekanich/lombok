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

	private javafx.beans.property.BooleanProperty isNoneProperty() {
		return this.isNone;
	}

	private void setIsNone(Boolean isNone) {
		this.isNone.setValue(isNone);
	}

	private Boolean isNone() {
		return this.isNone.getValue();
	}

	private javafx.beans.property.BooleanPropertyBase isPrivateProperty() {
		return this.isPrivate;
	}

	private void setIsPrivate(Boolean isPrivate) {
		this.isPrivate.setValue(isPrivate);
	}

	private Boolean isPrivate() {
		return this.isPrivate.getValue();
	}

	javafx.beans.property.ObjectProperty<Boolean> isPackageProperty() {
		return this.isPackage;
	}

	void setIsPackage(Boolean isPackage) {
		this.isPackage.setValue(isPackage);
	}

	Boolean isPackage() {
		return (Boolean)this.isPackage.getValue();
	}

	protected javafx.beans.property.ListProperty<Boolean> booleansListProperty() {
		return this.booleansList;
	}

	protected void setBooleansList(javafx.collections.ObservableList<Boolean> booleansList) {
		this.booleansList.setValue(booleansList);
	}

	protected javafx.collections.ObservableList<Boolean> getBooleansList() {
		return this.booleansList.getValue();
	}

	public javafx.beans.property.MapProperty<Boolean, Integer> mapPublicProperty() {
		return this.mapPublic;
	}

	public void setMapPublic(javafx.collections.ObservableMap<Boolean, Integer> mapPublic) {
		this.mapPublic.setValue(mapPublic);
	}

	public javafx.collections.ObservableMap<Boolean, Integer> getMapPublic() {
		return this.mapPublic.getValue();
	}

	private javafx.beans.property.StringProperty noneStringProperty() {
		return this.noneString;
	}

	private void setNoneString(String noneString) {
		this.noneString.setValue(noneString);
	}

	private String getNoneString() {
		return this.noneString.getValue();
	}

	private javafx.beans.property.IntegerProperty privateIntegerProperty() {
		return this.privateInteger;
	}

	private void setPrivateInteger(Integer privateInteger) {
		this.privateInteger.setValue(privateInteger);
	}

	private Integer getPrivateInteger() {
		return this.privateInteger.getValue();
	}

	javafx.beans.property.SimpleIntegerProperty packageIntegerProperty() {
		return this.packageInteger;
	}

	void setPackageInteger(Integer packageInteger) {
		this.packageInteger.setValue(packageInteger);
	}

	Integer getPackageInteger() {
		return this.packageInteger.getValue();
	}

	protected javafx.beans.property.ReadOnlyBooleanProperty protectedReadOnlyBooleanProperty() {
		return this.protectedReadOnlyBoolean;
	}

	protected Boolean isProtectedReadOnlyBoolean() {
		return this.protectedReadOnlyBoolean.getValue();
	}
}