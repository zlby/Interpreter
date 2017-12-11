package cmm.model;

public class ExprItem {
    Double value;
    boolean isDouble;

    public ExprItem(Double value, boolean isDouble) {
        this.value = value;
        this.isDouble = isDouble;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    public boolean isDouble() {
        return isDouble;
    }

    public void setIsDouble(boolean isDouble) {
        this.isDouble = isDouble;
    }
}
