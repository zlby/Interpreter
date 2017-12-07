package cmm;

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
                res.add(child.getValue());
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

    private static void visitExprTemp(TreeNode node){

    }

}
