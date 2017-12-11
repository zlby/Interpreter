package cmm.model;

import java.util.ArrayList;

public class Symbol {
    private String type;
    private String vname;
    private int lineNo;
    private int position;
    //if the variable is an array, we should consider more
    private int arrayLength;    //arrayLength: 0 means not array, m (m>=1) means array of length m
    private ArrayList<String> value;

    public Symbol(String type, String vname, int lineNo, int position, int arrayLength) {
        this.type = type;
        this.vname = vname;
        this.lineNo = lineNo;
        this.position = position;
        this.arrayLength = arrayLength;
        if (arrayLength == 0)
            value = new ArrayList<>(1);
        else
            value = new ArrayList<>(arrayLength);
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getVname() {
        return vname;
    }

    public void setVname(String vname) {
        this.vname = vname;
    }

    public int getLineNo() {
        return lineNo;
    }

    public void setLineNo(int lineNo) {
        this.lineNo = lineNo;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public int getArrayLength() {
        return arrayLength;
    }

    public void setArrayLength(int isArray) {
        this.arrayLength = isArray;
    }

    public ArrayList<String> getArrayValue() {
        return value;
    }

    private String getCommonValue(){
        return value.get(0);
    }

    public void setValue(ArrayList<String> value) {
        this.value = value;
    }

    public void initValue(String value){
        if (this.value.size() != 0)
            this.value.clear();
        this.value.add(value);
    }

    public void addArrayValue(String value){
        this.value.add(value);
    }

    public void setArrayValue(String value, int index){
        this.value.set(index, value);
    }
}
