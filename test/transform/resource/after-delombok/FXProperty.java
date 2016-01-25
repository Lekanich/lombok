import javafx.beans.property.BooleanProperty;
import javafx.beans.property.BooleanPropertyBase;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ListProperty;
import javafx.beans.property.MapProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;

class FXProperty {
	BooleanProperty isNone;
	BooleanPropertyBase isPrivate;
	ObjectProperty<Boolean> isPackage;
	ListProperty<Boolean> booleansList;
	MapProperty<Boolean, Integer> mapPublic;
	StringProperty noneString;
	IntegerProperty privateInteger;
	SimpleIntegerProperty packageInteger;
	ReadOnlyBooleanProperty protectedReadOnlyBoolean;

	FXProperty() {
	}

	private BooleanProperty isNoneProperty() {
		return this.isNone;
	}

	private void setIsNone(Boolean isNone) {
		this.isNone.setValue(isNone);
	}

	private Boolean isNone() {
		return this.isNone.getValue();
	}

	private BooleanPropertyBase isPrivateProperty() {
		return this.isPrivate;
	}

	private void setIsPrivate(Boolean isPrivate) {
		this.isPrivate.setValue(isPrivate);
	}

	private Boolean isPrivate() {
		return this.isPrivate.getValue();
	}

	ObjectProperty<Boolean> isPackageProperty() {
		return this.isPackage;
	}

	void setIsPackage(Boolean isPackage) {
		this.isPackage.setValue(isPackage);
	}

	Boolean isPackage() {
		return (Boolean)this.isPackage.getValue();
	}

	protected ListProperty<Boolean> booleansListProperty() {
		return this.booleansList;
	}

	protected void setBooleansList(ObservableList<Boolean> booleansList) {
		this.booleansList.setValue(booleansList);
	}

	protected ObservableList<Boolean> getBooleansList() {
		return this.booleansList.getValue();
	}

	public MapProperty<Boolean, Integer> mapPublicProperty() {
		return this.mapPublic;
	}

	public void setMapPublic(ObservableMap<Boolean, Integer> mapPublic) {
		this.mapPublic.setValue(mapPublic);
	}

	public ObservableMap<Boolean, Integer> getMapPublic() {
		return this.mapPublic.getValue();
	}

	private StringProperty noneStringProperty() {
		return this.noneString;
	}

	private void setNoneString(String noneString) {
		this.noneString.setValue(noneString);
	}

	private String getNoneString() {
		return this.noneString.getValue();
	}

	private IntegerProperty privateIntegerProperty() {
		return this.privateInteger;
	}

	private void setPrivateInteger(Integer privateInteger) {
		this.privateInteger.setValue(privateInteger);
	}

	private Integer getPrivateInteger() {
		return this.privateInteger.getValue();
	}

	SimpleIntegerProperty packageIntegerProperty() {
		return this.packageInteger;
	}

	void setPackageInteger(Integer packageInteger) {
		this.packageInteger.setValue(packageInteger);
	}

	Integer getPackageInteger() {
		return this.packageInteger.getValue();
	}

	protected ReadOnlyBooleanProperty protectedReadOnlyBooleanProperty() {
		return this.protectedReadOnlyBoolean;
	}

	protected Boolean isProtectedReadOnlyBoolean() {
		return this.protectedReadOnlyBoolean.getValue();
	}
}