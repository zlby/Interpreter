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
    private static HashMap<String, Symbol> gobalSymbolTable; //here we store the symbol table of global variable
    private static Stack<HashMap<String, Symbol>> symbolTables;//here we store symbol tables of local variable
    //if we come into a new scope, push stack, if we come out, pop stack
    int errorNum;


    public Semantic() {
        gobalSymbolTable = new HashMap<>();
        symbolTables = new Stack<>();
        symbolTables.push(gobalSymbolTable);
    }

    public static void semanticAnalyze(TreeNode parsingTree){



    }

    private static void visitStmt(TreeNode node){
        switch (node.getChild(0).getName()){
            case "VarDecl":
                visitVarDecl(node.getChild(0));
            case "IfStmt":
                visitIfStmt(node.getChild(0));
            case "WhileStmt":
                visitWhileStmt(node.getChild(0));
            case "BreakStmt":
                visitBreakStmt(node.getChild(0));
            case "AssignStmt":
                visitAssignStmt(node.getChild(0));
            case "ReadStmt":
                visitReadStmt(node.getChild(0));
            case "WriteStmt":
                visitWriteStmt(node.getChild(0));
            case "StmtBlock":
                visitStmtBlock(node.getChild(0));
        }
    }

    private static void visitStmts(TreeNode node){

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
                Symbol sbl = topMap.get(variable);
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
                    sbl.setArrayValue(String.valueOf(ep.getValue()), 0);
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
        ArrayList<TreeNode> childs = node.getChilds();
        ArrayList<String> res = new ArrayList<>();
        if (childs.size() == 1)
            return null;
        else {
            for (TreeNode child : childs)
                res.add(child.getName());
        }
        return null;
    }

    private static void visitArrayTemp(TreeNode node){

    }

    private static ArrayList<Token> visitVarList(TreeNode node){
        return null;
    }

    private static void visitIdentList(TreeNode node){

    }

    private static void visitIfStmt(TreeNode node){

    }

    private static void visitElseStmt(TreeNode node){

    }

    private static void visitWhileStmt(TreeNode node){
        TreeNode childExpr = node.getChild(1);
        TreeNode childStmt = node.getChild(2);

        while (!childExpr.getToken().getValue().equals("0")){
            visitStmt(childStmt);
        }
    }

    private static void visitBreakStmt(TreeNode node){

    }

    private static void visitReadStmt(TreeNode node){

    }

    private static void visitReadTemp(TreeNode node){

    }

    private static void visitWriteStmt(TreeNode node){
        System.out.println(node.getChild(2).getToken().getValue());
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

    private static ExprItem visitExpr(TreeNode node){
        return null;
    }



    private static void  visitStmtBlock(TreeNode node) {

    }



}
