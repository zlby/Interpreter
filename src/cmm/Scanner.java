package cmm;

import java.io.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class Scanner {
    /**
     * read codes to be compiled from file
     * @param filename:String
     * @return string format of code
     * @throws FileNotFoundException
     */
    public static String readInput(String filename) throws FileNotFoundException{
        File file = new File(filename);
        FileInputStream fileName = new FileInputStream(file);
        ArrayList<String> strarr = new ArrayList<String>();
        BufferedReader br = new BufferedReader(new InputStreamReader(fileName));
        String temp = "";
        try {
            for(;;)
            {
                temp = br.readLine();
                if (temp == null)
                    break;
                strarr.add(temp);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        String finalstr = "";
        for (String str : strarr){
            finalstr = String.format("%s%s\n", finalstr, str);
        }

        return finalstr;
    }

    /**
     * do lexical analysis for input string
     * @param input:String
     * @return token list
     */
    public static Queue<Token> inputAnalyse(String input){
        Queue<Token> tokens = new LinkedList<>();
        char[] chars = input.toCharArray();
        int line = 1;
        int pos = 0;
        for (int index = 0; index < input.length(); index++){
            StringBuilder strbuffer;
            if (chars[index] == '\n'){
                line++;
                pos = 0;
            }
            else if (chars[index] == ' '
                    || chars[index] == '\t'
                    || chars[index] == '\r'
                    || chars[index] == '\f'){
                pos++;
            }
            else if (Lexer.isAdd(String.valueOf(chars[index])) != -1){
                tokens.add(new Token("+", String.valueOf(chars[index]), line, pos)); //add
                pos++;
            }
            else if (Lexer.isMinus(String.valueOf(chars[index])) != -1){
                tokens.add(new Token("-", String.valueOf(chars[index]), line, pos)); //minus
                pos++;
            }
            else if (Lexer.isMul(String.valueOf(chars[index])) != -1){
                tokens.add(new Token("*", String.valueOf(chars[index]), line, pos)); //multiply
                pos++;
            }
            else if (Lexer.isMod(String.valueOf(chars[index])) != -1){
                tokens.add(new Token("%", String.valueOf(chars[index]), line, pos)); //mod
                pos++;
            }
            else if (Lexer.isColon(String.valueOf(chars[index])) != -1){
                tokens.add(new Token(":", String.valueOf(chars[index]), line, pos)); //colon
                pos++;
            }
            else if (Lexer.isComma(String.valueOf(chars[index])) != -1){
                tokens.add(new Token(",", String.valueOf(chars[index]), line, pos)); //comma
                pos++;
            }
            else if (Lexer.isSemi(String.valueOf(chars[index])) != -1){
                tokens.add(new Token(";", String.valueOf(chars[index]), line, pos)); //semicolon
                pos++;
            }
            else if (Lexer.isEqual(String.valueOf(chars[index])) != -1 && Lexer.isEqual(String.valueOf(chars[index+1])) != -1){ //is equal
                tokens.add(new Token("==", "==", line, pos));
                index++;
                pos+=2;
            }
            else if (Lexer.isNot(String.valueOf(chars[index])) != -1 && Lexer.isEqual(String.valueOf(chars[index+1])) != -1){ //not equal
                tokens.add(new Token("!=", "!=", line, pos));
                index++;
                pos+=2;
            }
            else if (Lexer.isEqual(String.valueOf(chars[index])) != -1){
                tokens.add(new Token("=", String.valueOf(chars[index]), line, pos)); //equal
                pos++;
            }
            else if (Lexer.isLeftBrace(String.valueOf(chars[index])) != -1){
                tokens.add(new Token("{", String.valueOf(chars[index]), line, pos)); //left brace
                pos++;
            }
            else if (Lexer.isRightBrace(String.valueOf(chars[index])) != -1){
                tokens.add(new Token("}", String.valueOf(chars[index]), line, pos)); //right brace
                pos++;
            }
            else if (Lexer.isLeftBracket(String.valueOf(chars[index])) != -1){
                tokens.add(new Token("[", String.valueOf(chars[index]), line, pos)); //left bracket
                pos++;
            }
            else if (Lexer.isRightBracket(String.valueOf(chars[index])) != -1){
                tokens.add(new Token("]", String.valueOf(chars[index]), line, pos)); //right bracket
                pos++;
            }
            else if (Lexer.isLeftParent(String.valueOf(chars[index])) != -1){
                tokens.add(new Token("(", String.valueOf(chars[index]), line, pos)); //left parent
                pos++;
            }
            else if (Lexer.isRightParent(String.valueOf(chars[index])) != -1){
                tokens.add(new Token(")", String.valueOf(chars[index]), line, pos)); //right parent
                pos++;
            }
            else if (chars[index] == '<' && chars[index+1] == '='){
                tokens.add(new Token("<=", "<=", line, pos)); //less or equal
                index++;
                pos+=2;
            }
            else if (chars[index] == '>' && chars[index+1] == '='){
                tokens.add(new Token(">=", ">=", line, pos)); //greater or equal
                index++;
                pos+=2;
            }
            else if (chars[index] == '<'){
                tokens.add(new Token("<", "<", line, pos)); //less
                pos++;
            }
            else if (chars[index] == '>'){
                tokens.add(new Token(">", ">", line, pos)); //greater
                pos++;
            }
            else if (Lexer.isDiv(String.valueOf(chars[index])) == 0){
                if (chars[index+1] != '*' && chars[index+1] != '/'){
                    tokens.add(new Token("/", String.valueOf(chars[index]), line, pos)); //divide
                    pos++;
                    continue;
                }
                int beginline = line;
                int beginpos = pos;
                strbuffer = new StringBuilder();
                for(int i = index; i < chars.length;i++){
                    strbuffer.append(String.valueOf(chars[i]));
                    if (Lexer.isComment(strbuffer.toString()) == 0){
                        strbuffer.deleteCharAt(strbuffer.length()-1);
                        tokens.add(new Token("line comment", strbuffer.toString(), beginline, beginpos));
                        line++;
                        pos = 0;
                        index = i;
                        break;
                    }
                    if (Lexer.isComment(strbuffer.toString()) == 1){
                        tokens.add(new Token("block comment", strbuffer.toString().replaceAll("\n", " "), beginline, beginpos));
                        pos++;
                        index = i;
                        break;
                    }
                    if (chars[i] == '\n'){
                        line++;
                        pos = 0;
                    }else {
                        pos++;
                    }
                    if (i == (chars.length - 1)){
                        tokens.add(new Token("wrong", strbuffer.toString(), beginline, beginpos));
                        return tokens;
                    }
                }
            }
            else{
                int beginpos = pos;
                strbuffer = new StringBuilder();
                strbuffer.append(String.valueOf(chars[index]));
                for (int i = index + 1; i < input.length(); i++){
                    if (!nextTokenMark(chars[i])){
                        strbuffer.append(String.valueOf(chars[i]));
                        pos++;
                    }
                    else {
                        pos++;
                        index = i-1;
                        break;
                    }
                }
                if (Lexer.isInt(strbuffer.toString()) == 0 || strbuffer.toString().equals("1")){
                    tokens.add(new Token("intconst", strbuffer.toString(), line, beginpos)); //decimal int
                }
                else if (Lexer.isInt(strbuffer.toString()) == 1){
                    tokens.add(new Token("intconst", strbuffer.toString(), line, beginpos)); //binary int
                }
                else if (Lexer.isInt(strbuffer.toString()) == 2){
                    tokens.add(new Token("intconst", strbuffer.toString(), line, beginpos)); //hexadecimal int
                }
                else if (Lexer.isDouble(strbuffer.toString()) == 0){
                    tokens.add(new Token("doubleconst", strbuffer.toString(), line, beginpos)); //normal double
                }
                else if (Lexer.isDouble(strbuffer.toString()) == 1){
                    tokens.add(new Token("doubleconst", strbuffer.toString(), line, beginpos)); //scientific double
                }
                else if (Lexer.isBreak(strbuffer.toString()) == 0){
                    tokens.add(new Token("break", strbuffer.toString(), line, beginpos));
                }
                else if (Lexer.isElse(strbuffer.toString()) == 0){
                    tokens.add(new Token("else", strbuffer.toString(), line, beginpos));
                }
                else if (Lexer.isFalse(strbuffer.toString()) == 0){
                    tokens.add(new Token("false", strbuffer.toString(), line, beginpos));
                }
                else if (Lexer.isIf(strbuffer.toString()) == 0){
                    tokens.add(new Token("if", strbuffer.toString(), line, beginpos));
                }
                else if (Lexer.isRead(strbuffer.toString()) == 0){
                    tokens.add(new Token("read", strbuffer.toString(), line, beginpos));
                }
                else if (Lexer.isTrue(strbuffer.toString()) == 0){
                    tokens.add(new Token("true", strbuffer.toString(), line, beginpos));
                }
                else if (Lexer.isTypeDouble(strbuffer.toString()) == 0){
                    tokens.add(new Token("double", strbuffer.toString(), line, beginpos)); //type(double)
                }
                else if (Lexer.isTypeInt(strbuffer.toString()) == 0){
                    tokens.add(new Token("int", strbuffer.toString(), line, beginpos)); //type(int)
                }
                else if (Lexer.isWhile(strbuffer.toString()) == 0){
                    tokens.add(new Token("while", strbuffer.toString(), line, beginpos));
                }
                else if (Lexer.isWrite(strbuffer.toString()) == 0){
                    tokens.add(new Token("write", strbuffer.toString(), line, beginpos));
                }
                else if (Lexer.isIdent(strbuffer.toString()) == 0){
                    tokens.add(new Token("ident", strbuffer.toString(), line, beginpos)); //identifier
                }
                else {
                    tokens.add(new Token("wrong", strbuffer.toString(), line, beginpos));
                }
            }
        }
        return tokens;
    }

    /**
     * Testing whether a token can be
     * @param mark:char
     * @return if it is a mark of next token
     */
    public static boolean nextTokenMark(char mark){
        switch (mark){
            case ',':
                break;
            case ';':
                break;
            case ' ':
                break;
            case '+':
                break;
            case '-':
                break;
            case '*':
                break;
            case '/':
                break;
            case '%':
                break;
            case '=':
                break;
            case '(':
                break;
            case ')':
                break;
            case '[':
                break;
            case ']':
                break;
            case '{':
                break;
            case '}':
                break;
            case '<':
                break;
            case '>':
                break;
            case '\n':
                break;
            default:
                return false;
        }
        return true;
    }

    /**
     * generate all the output
     * @param filename
     */
    public static void scan(String filename){
        String input = "";
        try {
            input = readInput(filename);
            Queue<Token> tokens = inputAnalyse(input);
            while (tokens.peek() != null){
                System.out.println(tokens.poll().toString());
            }
        }catch (FileNotFoundException e){
            System.err.println("Cannot find file");
        }
    }
}
