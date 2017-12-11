package cmm.compiler;

import cmm.model.Symbol;
import cmm.model.TreeNode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;

public class Semantic {
    HashMap<String, Symbol> gobalSymbolTable; //here we store the symbol table of global variable
    Stack<HashMap<String, Symbol>> symbolTables;//here we store symbol tables of local variable
    //if we come into a new scope, push stack, if we come out, pop stack
    int errorNum;


    public Semantic() {
        gobalSymbolTable = new HashMap<>();
        symbolTables = new Stack<>();
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

    }

    private static void visitAssignInDecl(TreeNode node){

    }

    private static void visitType(TreeNode node){

    }

    private static ArrayList<String> visitArray(TreeNode node){
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

    private static void visitVarList(TreeNode node){

    }

    private static void visitIdentList(TreeNode node){

    }

    private static void visitIfStmt(TreeNode node){

    }

    private static void visitElseStmt(TreeNode node){

    }

    private static void visitWhileStmt(TreeNode node){
        TreeNode childWhile = node.getChild(0);
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

    }

    private static void visitAssignStmt(TreeNode node){

    }

    private static void visitValue(TreeNode node){

    }

    private static void visitConst(TreeNode node){

    }

    private static void visitExpr(TreeNode node){

    }



    private static void  visitStmtBlock(TreeNode node) {

    }

}
