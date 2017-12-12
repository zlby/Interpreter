package cmm.compiler;

import cmm.model.ExprItem;
import cmm.model.Symbol;
import cmm.model.Token;
import cmm.model.TreeNode;

import java.text.Format;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;

public class Semantic {
    private static HashMap<String, Symbol> gobalSymbolTable = new HashMap<>(); //here we store the symbol table of global variable
    private static Stack<HashMap<String, Symbol>> symbolTables = new Stack<>();//here we store symbol tables of local variable
    private static String userInput = null;
    //if we come into a new scope, push stack, if we come out, pop stack
    int errorNum;


//    public Semantic() {
//        gobalSymbolTable = new HashMap<>();
//        symbolTables = new Stack<>();
//        symbolTables.push(gobalSymbolTable);
//    }

    public static void semanticAnalyze(String filename){
        gobalSymbolTable = new HashMap<>();
        symbolTables = new Stack<>();
        userInput = null;
        symbolTables.push(gobalSymbolTable);
        TreeNode rootNode = Parser.doParsing(filename);
        visitProgram(rootNode);


    }

    private static void visitStmt(TreeNode node){
        switch (node.getChild(0).getName()){
            case "VarDecl":
                visitVarDecl(node.getChild(0));
                break;
            case "IfStmt":
                visitIfStmt(node.getChild(0));
                break;
            case "WhileStmt":
                visitWhileStmt(node.getChild(0));
                break;
            case "BreakStmt":
                visitBreakStmt(node.getChild(0));
                break;
            case "AssignStmt":
                visitAssignStmt(node.getChild(0));
                break;
            case "ReadStmt":
                visitReadStmt(node.getChild(0));
                break;
            case "WriteStmt":
                visitWriteStmt(node.getChild(0));
                break;
            case "StmtBlock":
                symbolTables.push(new HashMap<String, Symbol>());
                visitStmtBlock(node.getChild(0));
                symbolTables.pop();
        }
    }

    private static void visitStmts(TreeNode node){
        if(node.getChild(0) != null){
            visitStmt(node.getChild(0));
            visitStmts(node.getChild(1));
        }else{
            return;
        }
    }

    private static void visitProgram(TreeNode node) {
        visitStmt(node.getChild(0));
        visitStmts(node.getChild(1));
    }

    private static void visitVarDecl(TreeNode node){
        TreeNode typeChild = node.getChild(0);
        TreeNode varListChild = node.getChild(1);
        TreeNode assignInDeclChild = node.getChild(2);
        HashMap<String, Symbol> topMap = symbolTables.peek();

        ArrayList<Token> variableList = visitVarList(varListChild);
        for (Token variable : variableList){
            if (topMap.containsKey(variable.getValue())) {
                System.err.println("the variable name: " + variable.getValue() + " already exist!\n" +
                        "Line: " + variable.getLineNo() + ", Pos: " + variable.getPosition());
                continue;
            }
            String typestr = visitType(typeChild);
            String typename = typestr.substring(0, typestr.indexOf("["));
            int arrlen = Integer.parseInt(typestr.substring(typestr.indexOf("[") + 1, typestr.indexOf("]")));
            Symbol sbl = new Symbol(typename, variable.getValue(), variable.getLineNo(), variable.getPosition(), arrlen);
            topMap.put(variable.getValue(), sbl);
        }

        if (!assignInDeclChild.getChild(0).getName().equals("null")){
            TreeNode exprChild = assignInDeclChild.getChild(1);
            ExprItem ep = visitExpr(exprChild);
            for (Token variable : variableList) {
                Symbol sbl = topMap.get(variable.getValue());
                if (sbl.getArrayLength() != 0){
                    System.err.println("Cannot declare and assign array at the same time.\n" +
                    "Line: " + variable.getLineNo() + ", Pos: " + variable.getPosition());
                    break;
                }
                else if (sbl.getType().equals("int") && ep.isDouble()){
                    System.err.println("Cannot assign double value to a variable of type int.\n" +
                    "Line: " + variable.getLineNo() + ", Pos: " + variable.getPosition());
                }
                else {
//                    sbl.setArrayValue(String.valueOf(ep.getValue()), 0);
                    sbl.addArrayValue(String.valueOf(ep.getValue()));
                }
            }
        }
    }


    private static String visitType(TreeNode node){
        Token typeToken = node.getChild(0).getToken();
        TreeNode arrayChild = node.getChild(1);

        if (visitArray(arrayChild) == null){
            return typeToken.getValue() + "[0]";
        }
        else {
            ArrayList<Token> arraytokens = visitArray(arrayChild);
            String typeString = typeToken.getValue();
            for (Token item : arraytokens)
                typeString += item.getValue();
            return typeString;
        }
    }

    private static ArrayList<Token> visitArray(TreeNode node){
        if(node.getChild(0).equals("[")) {
            return visitArrayTemp(node.getChild(1));
        }else{
            return null;
        }
    }

    private static ArrayList<Token> visitArrayTemp(TreeNode node){
        if(node.getChild(0).getToken().getFlag().equals("intconst")){
            ArrayList<Token> arr = new ArrayList<>();
            arr.add(node.getChild(1).getParent().getParent().getChild(0).getToken());
            arr.add(node.getChild(1).getToken());
            arr.add(node.getChild(2).getToken());
            return arr;
        }else if(visitExpr(node.getChild(0)).isDouble()){
            System.err.println("double is illegal in array");
            return null;
        }else{
            ArrayList<Token> arr = new ArrayList<>();
            Token token = new Token("intconst",String.valueOf(visitExpr(node.getChild(0)).getValue()),
                    node.getChild(0).getToken().getPosition(),node.getChild(0).getToken().getLineNo());
            arr.add(node.getChild(1).getParent().getParent().getChild(0).getToken());
            arr.add(token);
            arr.add(node.getChild(2).getToken());
            return arr;
        }
    }

    private static ArrayList<Token> visitVarList(TreeNode node){

        return visitIdentList(node.getChild(1));

    }

    private static ArrayList<Token> visitIdentList(TreeNode node){

        if(!(node.getChild(0).getName().equals("null"))){
            ArrayList<Token> arr = new ArrayList<>();
            ArrayList<Token> arr1 = new ArrayList<>();
            arr.add(node.getParent().getChild(0).getToken());
            arr.add(node.getChild(0).getToken());
            arr.add(node.getChild(1).getToken());
            arr1 = visitIdentList(node.getChild(2));
            if (arr1 != null)
                arr.addAll(arr1);
            return arr;
        }else if(node.getChild(0).getName().equals("null") && node.getParent().getChild(0).getToken().getFlag().equals("ident")){
            ArrayList<Token> arr = new ArrayList<>();
            arr.add(node.getParent().getChild(0).getToken());
            return arr;
        }else{
            return null;
        }
    }


    private static void visitIfStmt(TreeNode node){
        if(visitExpr(node.getChild(1)).getValue() != 0) {
            visitStmt(node.getChild(2));
        }else{
            visitElseStmt(node.getChild(3));
        }
    }

    private static void visitElseStmt(TreeNode node){
        if(node.getChilds().size() == 0){
            return;
        }else{
            visitStmt(node.getChild(1));
        }
    }

    private static void visitWhileStmt(TreeNode node){
        TreeNode childExpr = node.getChild(1);
        TreeNode childStmt = node.getChild(2);

        while (visitExpr(childExpr).getValue() == 0){
            visitStmt(childStmt);
        }
    }

    private static void visitBreakStmt(TreeNode node){

    }

    private static void visitReadStmt(TreeNode node){
        String value = new Semantic().readInput();
        Double doubleValue = 0.0;
        Integer intValue = 0;
        boolean isInt = false;
        try {
            doubleValue = Double.parseDouble(value);
        }catch (NumberFormatException e){
            System.err.println("Please enter a number!");
            return;
        }

        try {
            intValue = Integer.parseInt(value);
            isInt = true;
        }catch (NumberFormatException e){
            isInt = false;
        }

        String variableName = node.getChild(3).getToken().getValue();

        HashMap<String, Symbol> targetMap = null;

        for (int i = 0; i < symbolTables.size(); i++) {
            if (symbolTables.get(i).containsKey(variableName)){
                targetMap = symbolTables.get(i);
            }
        }
        if (targetMap == null){
            System.err.println("Error: " + variableName + " at line: " + node.getChild(3).getToken().getLineNo()
                    + ",  pos:"  +  node.getChild(3).getToken().getPosition() +
                    "\nYou should declare a variable before you use it.");
        }
        else {
            Symbol sbl = targetMap.get(variableName);
            if (sbl.getType().equals("int") && isInt == false){
                System.err.println("Error! Cannot assign a double value to a variable of type int.\n" +
                        "Line: " + node.getChild(3).getToken().getLineNo() + ", Pos: " + node.getChild(3).getToken().getPosition());
            }
            else if (sbl.getType().equals("int")) {
                sbl.setArrayValue(String.valueOf(intValue), 0);
            }
            else {
                sbl.setArrayValue(String.valueOf(doubleValue), 0);
            }
        }
    }

    /**
     * 设置用户输入
     *
     * @param input 输入的内容
     */
    public synchronized void setUserInput(String input) {
        userInput = input;
        notify();
    }

    /**
     * 读取用户输入
     *
     * @return 返回用户输入内容的字符串形式
     */
    public synchronized String readInput() {
        String result = null;
        try {
            while (userInput == null) {
                wait();
            }
        } catch (InterruptedException ie) {
            ie.printStackTrace();
        }
        result = userInput;
        userInput = null;
        return result;
    }


    private static void visitReadTemp(TreeNode node){

    }

    private static void visitWriteStmt(TreeNode node){
        ExprItem ei = visitExpr((node.getChild(2)));
        if (ei.isDouble())
            System.out.println(ei.getValue());
        else
            System.out.println(ei.getValue().intValue());
    }

    private static void visitAssignStmt(TreeNode node){
        TreeNode valueChild = node.getChild(0);
        TreeNode exprChild = node.getChild(2);

        String valueStr = visitValue(valueChild);

        if (!valueStr.contains("[")){
            String variableName = valueStr;

            HashMap<String, Symbol> targetMap = null;

            for (int i = 0; i < symbolTables.size(); i++) {
                if (symbolTables.get(i).containsKey(variableName)){
                    targetMap = symbolTables.get(i);
                }
            }
            if (targetMap == null){
                System.err.println("Error: " + variableName + " at line: " + valueChild.getChild(0).getToken().getLineNo()
                        + ",  pos:"  + valueChild.getChild(0).getToken().getPosition() +
                        "\nYou should declare a variable before you use it.");
            }
            else {
                Symbol sbl = targetMap.get(variableName);
                if (sbl.getType().equals("int") && visitExpr(exprChild).isDouble()){
                    System.err.println("Cannot assign a double type value to variable of type int.\n" + "Line: " +
                            node.getChild(1).getToken().getLineNo() + ", Pos: " + node.getChild(1).getToken().getPosition());
                }
                sbl.initValue(String.valueOf(visitExpr(exprChild).getValue()));
            }
        }
        else {
            String variableName = valueStr.substring(0, valueStr.indexOf("["));
            String indexstr = valueStr.substring(valueStr.indexOf("[") + 1, valueStr.indexOf("]"));
            int index = 0;
            HashMap<String, Symbol> targetMap = null;

            for (int i = 0; i < symbolTables.size(); i++) {
                if (symbolTables.get(i).containsKey(variableName)){
                    targetMap = symbolTables.get(i);
                }
            }
            if (targetMap == null){
                System.err.println("Error: " + variableName + " at line: " + valueChild.getChild(0).getToken().getLineNo()
                        + ",  pos:"  + valueChild.getChild(0).getToken().getPosition() +
                        "\nYou should declare a variable before you use it.");
            }
            else {
                try {
                    index = Integer.parseInt(indexstr);
                }catch (NumberFormatException e){
                    System.err.println("Number Format Exception");
                }
                Symbol sbl = targetMap.get(variableName);
                if (index >= sbl.getArrayLength() || index < 0){
                    System.err.println("Index out of range: " + valueChild.getChild(1).getChild(1).getChild(0).getToken().getValue() +
                    "\nat line: " + valueChild.getChild(1).getChild(1).getChild(0).getToken().getLineNo() + ", pos: " +
                            valueChild.getChild(1).getChild(1).getChild(0).getToken().getPosition());
                }
                else {
                    String type = sbl.getType();
                    if (type.equals("double")){
                        sbl.setArrayValue(String.valueOf(visitExpr(exprChild).getValue()), index);
                    }
                    else if (type.equals("int")){
                        if (visitExpr(exprChild).isDouble()){
                            System.err.println("Cannot assign a double type value to variable of type int.\n" + "Line: " +
                                    node.getChild(1).getToken().getLineNo() + ", Pos: " + node.getChild(1).getToken().getPosition());
                        }
                        else {
                            sbl.setArrayValue(String.valueOf(visitExpr(exprChild).getValue()), index);
                        }
                    }
                }
            }
        }
    }

    private static String visitValue(TreeNode node){
        Token identToken = node.getChild(0).getToken();
        TreeNode arrayChild = node.getChild(1);

        if (visitArray(arrayChild) == null)
            return identToken.getValue();
        else {
            ArrayList<Token> arrtokens = visitArray(arrayChild);
            String s = identToken.getValue();
            for (Token t : arrtokens){
                s += t.getValue();
            }
            return s;
        }
    }

    private static void visitConst(TreeNode node){

    }

//    private static ExprItem visitExpr(TreeNode node){
//        return null;
//    }

    private static ExprItem visitExpr(TreeNode node){

        //决定结果的类型
        boolean isDouble = false;
        //储存操作数
        Double[] operands = new Double[2];
        //初始化储存操作符类型的string
        String operator = null;
        double result = 0.0;//初始化结果
        if (node.getChild(0).getName().equals("(")) {//以"("开头
            visitExpr(node.getChild(1));
        } else {
            Token t = node.getChild(0).getToken();
            if (t != null) {
                operator = node.getChild(0).getToken().getValue();//第一个必定为操作符+或-
                if (operator.equals("+")) {
                    visitExpr(node.getChild(1));
                } else {
                    ExprItem exprItem = visitExpr(node.getChild(1));
                    result = -exprItem.getValue();
                }
            } else {
                TreeNode temp = node.getChild(0).getChild(0);
                String type = temp.getToken().getFlag();
                String content = temp.getToken().getValue();
                if (type.equals("intconst")) {
                    operands[0] = Double.parseDouble(content);
                } else if (type.equals("doubleconst")) {
                    operands[0] = Double.parseDouble(content);
                    isDouble = true;
                } else if (type.equals("ident")) {
                    String variableName = temp.getToken().getValue();
                    String variableValue = "";
                    HashMap<String, Symbol> targetMap = null;

                    for (int j = 0; j < symbolTables.size(); j++) {
                        if (symbolTables.get(j).containsKey(variableName)){
                            targetMap = symbolTables.get(j);
                        }
                    }
                    if (targetMap == null){
                        System.err.println("Error: " + variableName + " at line: " + temp.getToken().getLineNo()
                                + ",  pos:"  + temp.getToken().getPosition() +
                                "\nYou should declare a variable before you use it.");
                    }
                    else {
                        Symbol sbl = targetMap.get(variableName);
                        variableValue = sbl.getValue().get(0);
                    }
                    operands[0]=Double.parseDouble(variableValue);
                }else if (type.equals("true")) {//若只有一个结点，则可能为true或false
                    operands[0] = 0.0;
                } else if (type.equals("false")) {
                    operands[0] = -1.0;
                }

                if (node.getChild(1).getChild(0).getToken()==null) {
                    result = operands[0];
                } else {
                    TreeNode temp2 = node.getChild(1);
                    operator = temp2.getChild(0).getName();
                    ExprItem item2 = visitExpr(temp2.getChild(1));
                    operands[1] = item2.getValue();
                    double a1 = operands[0];
                    double a2 = operands[1];//将储存的操作数转化为double型
                    if (isDouble) {//若为double则直接计算
                        if (operator.equals("+")) {
                            result = a1 + a2;
                        } else if (operator.equals("-")) {
                            result = a1 - a2;
                        } else if (operator.equals("*")) {
                            result = a1 * a2;
                        } else if (operator.equals("/")) {
                            result = a1 / a2;
                        } else if (operator.equals("%")) {
                            result = a1 % a2;
                        } else if (operator.equals("<")) {
                            if (a1 < a2)
                                result = 0;//判定为true
                            else
                                result = -1;//判定为false
                        } else if (operator.equals("<=")) {
                            if (a1 <= a2)
                                result = 0;//判定为true
                            else
                                result = -1;//判定为false
                        } else if (operator.equals("==")) {
                            if (a1 == a2)
                                result = 0;//判定为true
                            else
                                result = -1;//判定为false
                        } else if (operator.equals(">")) {
                            if (a1 > a2)
                                result = 0;//判定为true
                            else
                                result = -1;//判定为false
                        } else if (operator.equals(">=")) {
                            if (a1 >= a2)
                                result = 0;//判定为true
                            else
                                result = -1;//判定为false
                        } else if (operator.equals("!=")) {
                            if (a1 != a2)
                                result = 0;//判定为true
                            else
                                result = -1;//判定为false
                        }
                    } else {//若为整数则转化为整数再计算
                        int b1 = (int) a1;
                        int b2 = (int) a2;
                        if (operator.equals("+")) {
                            result = b1 + b2;
                        } else if (operator.equals("-")) {
                            result = b1 - b2;
                        } else if (operator.equals("*")) {
                            result = b1 * b2;
                        } else if (operator.equals("/")) {
                            result = b1 / b2;
                        } else if (operator.equals("%")) {
                            result = b1 % b2;
                        } else if (operator.equals("<")) {
                            if (b1 < b2)
                                result = 0;//判定为true
                            else
                                result = -1;//判定为false
                        } else if (operator.equals("<=")) {
                            if (b1 <= b2)
                                result = 0;//判定为true
                            else
                                result = -1;//判定为false
                        } else if (operator.equals("==")) {
                            if (b1 == b2)
                                result = 0;//判定为true
                            else
                                result = -1;//判定为false
                        } else if (operator.equals(">")) {
                            if (b1 > b2)
                                result = 0;//判定为true
                            else
                                result = -1;//判定为false
                        } else if (operator.equals(">=")) {
                            if (b1 >= b2)
                                result = 0;//判定为true
                            else
                                result = -1;//判定为false
                        } else if (operator.equals("!=")) {
                            if (b1 != b2)
                                result = 0;//判定为true
                            else
                                result = -1;//判定为false
                        }
                    }

                }
            }
        }
        return new ExprItem(result, isDouble);


    }




    private static void  visitStmtBlock(TreeNode node) {
        if(node.getChild(0).getToken().getValue().equals("[")) {
            visitStmts(node.getChild(1));
        }else{
            return;
        }
    }



}
