package cmm;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Lexer{
    public static int isInt(String input){
        /*                                             state
        return: 0           decimal int                 1,2
                1           binary int                  4
                2           hexadecimal int             3

                -1          not int
         */

        int[][] t = new int[5][7];
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 7; j++) {
                t[i][j] = -1;
            }
        }

        t[0][0] = 1;
        t[0][1] = 2;
        t[1][2] = 1;
        t[2][3] = 3;
        t[2][4] = 4;
        t[3][5] = 3;
        t[4][6] = 4;

        int state = 0;

        Pattern c = Pattern.compile("[1-9]");
        Pattern d = Pattern.compile("[0-9]");
        Pattern e = Pattern.compile("[0-9]|[a-f]|[A-F]");
        Pattern f = Pattern.compile("[0-1]");

        char[] chars = input.toCharArray();
        int[] intinput = new int[input.length()];
        for (int i = 0; i < intinput.length; i++) {
            intinput[i] = -1;
        }

        for (int i = 0; i < chars.length; i++){
            Matcher mc = c.matcher(String.valueOf(chars[i]));
            Matcher md = d.matcher(String.valueOf(chars[i]));
            Matcher me = e.matcher(String.valueOf(chars[i]));
            Matcher mf = f.matcher(String.valueOf(chars[i]));

            if (chars[i] == 'x')
                intinput[i] = 3;
            else if (chars[i] == '0')
                intinput[i] = 1;
            else if (mf.matches())
                intinput[i] = 6;
            else if (mc.matches())
                intinput[i] = 0;
            else if (md.matches())
                intinput[i] = 2;
            else if (chars[i] == 'b')
                intinput[i] = 4;
            else if (me.matches())
                intinput[i] = 5;
        }

        try{
            for (int i = 0; i < chars.length; i++){
                if (intinput[i] == 1 && t[state][intinput[i]] == -1)
                    intinput[i] = 6;
                if (intinput[i] == 6 && t[state][intinput[i]] == -1)
                    intinput[i] = 2;
                if (intinput[i] == 0 && t[state][intinput[i]] == -1)
                    intinput[i] = 2;
                if (intinput[i] == 2 && t[state][intinput[i]] == -1)
                    intinput[i] = 5;
                if (intinput[i] == 4 && t[state][intinput[i]] == -1)
                    intinput[i] = 5;
                state = t[state][intinput[i]];
            }
        }catch (IndexOutOfBoundsException e1){
            return -1;
        }

        if (state==1 || state==2)
            return 0;
        if (state==4)
            return 1;
        if (state==3)
            return 2;

        return -1;
    }

    public static int isDouble(String input){
        /*
        return: 0           decimal double
                1           scientific double

                -1          not double
                -2          not double but int
         */
        if (Lexer.isInt(input) != -1){
            return -2;
        }


        /* Parsing Table:
                0 	1	2	3	4	5	6
                0	+	-	.	c	d	e
         ----------------------------
         0	|	1		2		3
         1	|				4			6
         2	|	1				3
         3	|				4		3	6
         4	|						5
         5	|						5	6
         6	|		7	7			8
         7	|						8
         8	|						8
         here: c = 1-9  d = 0-9  e = E|e
         accepting state: normal double:1,3,5
                          scientific double: 8
         */
        int[][] table = new int[9][7];
        for (int i = 0; i < 9; i++)
        {
            for (int j = 0; j < 6; j++)
            {
                table[i][j] = -1;
            }
        }
        table[0][0] = 1;
        table[0][2] = 2;
        table[0][4] = 3;
        table[1][3] = 4;
        table[1][6] = 6;
        table[2][0] = 1;
        table[2][4] = 3;
        table[3][3] = 4;
        table[3][5] = 3;
        table[3][6] = 6;
        table[4][5] = 5;
        table[5][5] = 5;
        table[5][6] = 6;
        table[6][1] = 7;
        table[6][2] = 7;
        table[6][5] = 8;
        table[7][5] = 8;
        table[8][5] = 8;



        int state = 0;

        Pattern c = Pattern.compile("[1-9]");
        Pattern d = Pattern.compile("[0-9]");


        char[] chars = input.toCharArray();
        int[] intinput = new int[input.length()];
        for (int i = 0; i < intinput.length; i++) {
            intinput[i] = -1;
        }

        for (int i = 0; i < chars.length; i++) {
            Matcher mc = c.matcher(String.valueOf(chars[i]));
            Matcher md = d.matcher(String.valueOf(chars[i]));
            if (chars[i] == '0')
                intinput[i] = 0;
            else if (chars[i] == '+')
                intinput[i] = 1;
            else if (chars[i] == '-')
                intinput[i] = 2;
            else if (chars[i] == '.')
                intinput[i] = 3;
            else if (chars[i] == 'e' || chars[i] == 'E')
                intinput[i] = 6;
            else if (mc.matches())
                intinput[i] = 4;
            else if (md.matches())
                intinput[i] = 5;
        }

        try {
            for (int i = 0; i < chars.length; i++) {
                if (intinput[i] == 0 && table[state][intinput[i]] == -1)
                    intinput[i] = 5;
                if (intinput[i] == 4 && table[state][intinput[i]] == -1)
                    intinput[i] = 5;
                state = table[state][intinput[i]];
            }
        } catch(IndexOutOfBoundsException e) {
            return -1;
        }


        if (state == 1 || state == 3 || state == 5)
            return 0;
        if (state == 8)
            return 1;

        return -1;
    }

    public static int isSpace(String input){
        /*
        return: 0           space
                -1          not space
         */
        if (input.equals(" ")){
            return 0;
        }else {
            return -1;
        }
    }

    public static int isIdent(String input){
        /*
        return: 0           is identifier
                -1          not identifier
         */
        Pattern begin = Pattern.compile("_|[A-Z]|[a-z]");
        Pattern next = Pattern.compile("_|[0-9]|[A-Z]|[a-z]");
        char[] chars = input.toCharArray();
        Matcher mbegin = begin.matcher(String.valueOf(chars[0]));
        if (!mbegin.matches())
            return -1;

        for (int i = 1; i < (chars.length-1); i++){
            Matcher m = next.matcher(String.valueOf(chars[i]));
            if (!m.matches())
                return -1;
        }


        return 0;
    }

    public static int isComment(String input){
        /*                                         state
        return: 0           is line comment         3
                1           is block comment        6

                -1          not comment
         */

        int[][] t = new int[7][6];
        for (int i = 0; i < 7; i++) {
            for (int j = 0; j < 6; j++) {
                t[i][j] = -1;
            }
        }

        t[0][0] = 1;
        t[1][0] = 2;
        t[1][3] = 4;
        t[2][1] = 2;
        t[2][2] = 3;
        t[4][3] = 5;
        t[4][5] = 4;
        t[5][0] = 6;
        t[5][4] = 4;

        int state = 0;

        char[] chars = input.toCharArray();
        int[] intinput = new int[input.length()];
        for (int i = 0; i < intinput.length; i++) {
            intinput[i] = -1;
        }

        for (int i = 0; i < chars.length; i++){
            if (chars[i] == '/')
                intinput[i] = 0;
            else if (chars[i] == '*')
                intinput[i] = 3;
            else if (chars[i] == '\n')
                intinput[i] = 2;
            else if (chars[i] != '*')
                intinput[i] = 99;//5
            else if (chars[i] != '/')
                intinput[i] = 99;//4
            else if (chars[i] != '\n')
                intinput[i] = 99;//1
        }

        try {
            for (int i = 0; i < chars.length; i++) {
                if (state == 2){
                    if (intinput[i] != 2)
                        intinput[i] = 1;
                }
                if (state == 4){
                    if (intinput[i] != 3)
                        intinput[i] = 5;
                }
                if (state == 5){
                    if (intinput[i] != 0)
                        intinput[i] = 4;
                }
                state = t[state][intinput[i]];
            }
        } catch(IndexOutOfBoundsException e) {
            return -1;
        }

        if (state == 3)
            return 0;
        if (state == 6)
            return 1;

        return -1;
    }

    public static int isTypeInt(String input){
        /*
        return: 0           is type int
                -1          not type int
         */
        if (input.equals("int")) {
            return 0;
        }else {
            return -1;
        }
    }

    public static int isTypeDouble(String input){
        /*
        return: 0           is type double
                -1          not type double
         */
        if (input.equals("double")) {
            return 0;
        }else {
            return -1;
        }
    }

    public static int isWhile(String input){
        /*
        return: 0           is while
                -1          not while
         */
        if (input.equals("while")){
            return 0;
        }else {
            return -1;
        }
    }

    public static int isRead(String input){
        /*
        return: 0           is read
                -1          not read
         */
        if (input.equals("read")) {
            return 0;
        }else {
            return -1;
        }
    }

    public static int isBreak(String input){
        /*
        return: 0           is break
                -1          not break
         */
        if (input.equals("break")){
            return 0;
        }else {
            return -1;
        }
    }

    public static int isWrite(String input){
        /*
        return: 0           is write
                -1          not write
         */
        if (input.equals("write")){
            return 0;
        }else {
            return -1;
        }
    }

    public static int isIf(String input){
        /*
        return: 0           is if
                -1          not if
         */
        if (input.equals("if")){
            return 0;
        }else {
            return -1;
        }
    }

    public static int isElse(String input){
        /*
        return: 0           is else
                -1          not else
         */
        if (input.equals("else")){
            return 0;
        }else {
            return -1;
        }
    }

    public static int isLeftParent(String s) {
        if (s.equals("("))
            return 0;
        return -1;
    }

    public static int isLeftBrace(String s) {
        if (s.equals("{"))
            return 0;
        return -1;
    }

    public static int isLeftBracket(String s) {
        if (s.equals("["))
            return 0;
        return -1;
    }

    public static int isComma(String s) {
        if (s.equals(","))
            return 0;
        return -1;
    }

    public static int isColon(String s) {
        if (s.equals(":"))
            return 0;
        return -1;
    }

    public static int isSemi(String s){
        if (s.equals(";"))
            return 0;
        return -1;
    }

    public static int isEqual(String s){
        if (s.equals("="))
            return 0;
        return -1;
    }

    public static int isRightBracket(String s) {
        if (s.equals("]"))
            return 0;
        return -1;
    }

    public static int isRightBrace(String s) {
        if (s.equals("}"))
            return 0;
        return -1;
    }

    public static int isRightParent(String s) {
        if (s.equals(")"))
            return 0;
        return -1;
    }

    public static int isTrue(String s) {
        if (s.equals("true"))
            return 0;
        return -1;
    }

    public static int isFalse(String s) {
        if (s.equals("false"))
            return 0;
        return -1;
    }

    public static int isAdd(String input){
        if (input.equals("+"))
            return 0;
        return -1;
    }

    public static int isMinus(String input){
        if (input.equals("-"))
            return 0;
        return -1;
    }

    public static int isMul(String input){
        if (input.equals("*"))
            return 0;
        return -1;
    }

    public static int isDiv(String input){
        if (input.equals("/"))
            return 0;
        return -1;
    }

    public static int isMod(String input){
        if (input.equals("%"))
            return 0;
        return -1;
    }

    public static int isNot(String input){
        if (input.equals("!"))
            return 0;
        return -1;
    }
}
