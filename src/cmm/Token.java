package cmm;

public class Token {
    private String flag;
    private String value;
    private int lineNo;
    private int position;

    public Token(String flag, String value, int lineNo, int position) {
        this.flag = flag;
        this.value = value;
        this.lineNo = lineNo;
        this.position = position;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
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

    @Override
    public String toString() {
        return "Token{" +
                "flag='" + flag + '\'' +
                ", value='" + value + '\'' +
                ", line=" + lineNo +
                ", position=" + position +
                '}';
    }
}
