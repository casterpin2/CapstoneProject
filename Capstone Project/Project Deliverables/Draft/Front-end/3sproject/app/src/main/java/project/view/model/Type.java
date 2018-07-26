package project.view.model;

public class Type {
    private int typeID;
    private String typeName;
    private int numberOfRecord;
    private String typeImage;

    public Type() {}

    public Type(int typeID, String typeName, int numberOfRecord, String typeImage) {
        this.typeID = typeID;
        this.typeName = typeName;
        this.numberOfRecord = numberOfRecord;
        this.typeImage = typeImage;
    }

    public int getTypeID() {
        return typeID;
    }

    public void setTypeID(int typeID) {
        this.typeID = typeID;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public int getNumberOfRecord() {
        return numberOfRecord;
    }

    public void setNumberOfRecord(int numberOfRecord) {
        this.numberOfRecord = numberOfRecord;
    }

    public String getTypeImage() {
        return typeImage;
    }

    public void setTypeImage(String typeImage) {
        this.typeImage = typeImage;
    }
}
