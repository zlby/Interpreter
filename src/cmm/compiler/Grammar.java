package cmm.compiler;

import java.util.ArrayList;
import java.util.HashMap;

public class Grammar {

    public static HashMap<String, ArrayList<ArrayList<String>>> getGrammar(){
        /*adding element into grammars*/
        HashMap<String, ArrayList<ArrayList<String>>> grammar = new HashMap<>();

        ArrayList<ArrayList<String>> all = new ArrayList<>();
        ArrayList<String> one = new ArrayList<>();

        all = new ArrayList<>();
        one = new ArrayList<>();
        one.add("Stmt");
        one.add("Stmts");
        all.add(one);
        grammar.put("Program", all);

        all = new ArrayList<>();
        one = new ArrayList<>();
        one.add("Stmt");
        one.add("Stmts");
        all.add(one);
        one = new ArrayList<>();
        one.add("null");
        all.add(one);
        grammar.put("Stmts", all);

        all = new ArrayList<>();
        one = new ArrayList<>();
        one.add("{");
        one.add("Stmts");
        one.add("}");
        all.add(one);
        grammar.put("StmtBlock", all);

        all = new ArrayList<>();
        one = new ArrayList<>();
        one.add("VarDecl");
        all.add(one);
        one = new ArrayList<>();
        one.add("IfStmt");
        all.add(one);
        one = new ArrayList<>();
        one.add("WhileStmt");
        all.add(one);
        one = new ArrayList<>();
        one.add("BreakStmt");
        all.add(one);
        one = new ArrayList<>();
        one.add("AssignStmt");
        all.add(one);
        one = new ArrayList<>();
        one.add("ReadStmt");
        all.add(one);
        one = new ArrayList<>();
        one.add("WriteStmt");
        all.add(one);
        one = new ArrayList<>();
        one.add("StmtBlock");
        all.add(one);
        grammar.put("Stmt", all);

        all = new ArrayList<>();
        one = new ArrayList<>();
        one.add("Type");
        one.add("VarList");
        one.add("AssignInDecl");
        one.add(";");
        all.add(one);
        grammar.put("VarDecl", all);

        all = new ArrayList<>();
        one = new ArrayList<>();
        one.add("=");
        one.add("Expr");
        all.add(one);
        one = new ArrayList<>();
        one.add("null");
        all.add(one);
        grammar.put("AssignInDecl", all);

        all = new ArrayList<>();
        one = new ArrayList<>();
        one.add("int");
        one.add("Array");
        all.add(one);
        one = new ArrayList<>();
        one.add("double");
        one.add("Array");
        all.add(one);
        grammar.put("Type", all);

        all = new ArrayList<>();
        one = new ArrayList<>();
        one.add("[");
        one.add("ArrayTemp");
        all.add(one);
        one = new ArrayList<>();
        one.add("null");
        all.add(one);
        grammar.put("Array", all);

        all = new ArrayList<>();
        one = new ArrayList<>();
        one.add("intconst");
        one.add("]");
        all.add(one);
        one = new ArrayList<>();
        one.add("Expr");
        one.add("]");
        all.add(one);
        grammar.put("ArrayTemp", all);

        all = new ArrayList<>();
        one = new ArrayList<>();
        one.add("ident");
        one.add("IdentList");
        all.add(one);
        grammar.put("VarList", all);

        all = new ArrayList<>();
        one = new ArrayList<>();
        one.add(",");
        one.add("ident");
        one.add("IdentList");
        all.add(one);
        one = new ArrayList<>();
        one.add("null");
        all.add(one);
        grammar.put("IdentList", all);

        all = new ArrayList<>();
        one = new ArrayList<>();
        one.add("if");
        one.add("Expr");
        one.add("Stmt");
        one.add("ElseStmt");
        all.add(one);
        grammar.put("IfStmt", all);

        all = new ArrayList<>();
        one = new ArrayList<>();
        one.add("else");
        one.add("Stmt");
        all.add(one);
        one = new ArrayList<>();
        one.add("null");
        all.add(one);
        grammar.put("ElseStmt", all);

        all = new ArrayList<>();
        one = new ArrayList<>();
        one.add("while");
        one.add("Expr");
        one.add("Stmt");
        all.add(one);
        grammar.put("WhileStmt", all);

        all = new ArrayList<>();
        one = new ArrayList<>();
        one.add("break");
        one.add(";");
        all.add(one);
        grammar.put("BreakStmt", all);

        all = new ArrayList<>();
        one = new ArrayList<>();
        one.add("read");
        one.add("(");
        one.add("ident");
        one.add("ReadTemp");
        all.add(one);
        grammar.put("ReadStmt", all);

        all = new ArrayList<>();
        one = new ArrayList<>();
        one.add(")");
        one.add(";");
        all.add(one);
        one = new ArrayList<>();
        one.add("[");
        one.add("intconst");
        one.add("]");
        one.add(")");
        one.add(";");
        all.add(one);
        grammar.put("ReadTemp", all);

        all = new ArrayList<>();
        one = new ArrayList<>();
        one.add("write");
        one.add("(");
        one.add("Expr");
        one.add(")");
        one.add(";");
        all.add(one);
        grammar.put("WriteStmt", all);

        all = new ArrayList<>();
        one = new ArrayList<>();
        one.add("Value");
        one.add("=");
        one.add("Expr");
        one.add(";");
        all.add(one);
        grammar.put("AssignStmt", all);

        all = new ArrayList<>();
        one = new ArrayList<>();
        one.add("ident");
        one.add("Array");
        all.add(one);
        grammar.put("Value", all);

        all = new ArrayList<>();
        one = new ArrayList<>();
        one.add("intconst");
        all.add(one);
        one = new ArrayList<>();
        one.add("doubleconst");
        all.add(one);
        one = new ArrayList<>();
        one.add("true");
        all.add(one);
        one = new ArrayList<>();
        one.add("false");
        all.add(one);
        grammar.put("Const", all);

        all = new ArrayList<>();
        one = new ArrayList<>();
        one.add("Value");
        one.add("ExprTemp");
        all.add(one);
        one = new ArrayList<>();
        one.add("Const");
        one.add("ExprTemp");
        all.add(one);
        one = new ArrayList<>();
        one.add("(");
        one.add("Expr");
        one.add(")");
        all.add(one);
        one = new ArrayList<>();
        one.add("-");
        one.add("Expr");
        all.add(one);
        grammar.put("Expr", all);

        all = new ArrayList<>();
        one = new ArrayList<>();
        one.add("*");
        one.add("Expr");
        one.add("ExprTemp");
        all.add(one);
        one = new ArrayList<>();
        one.add("/");
        one.add("Expr");
        one.add("ExprTemp");
        all.add(one);
        one = new ArrayList<>();
        one.add("%");
        one.add("Expr");
        one.add("ExprTemp");
        all.add(one);
        one = new ArrayList<>();
        one.add("+");
        one.add("Expr");
        one.add("ExprTemp");
        all.add(one);
        one = new ArrayList<>();
        one.add("-");
        one.add("Expr");
        one.add("ExprTemp");
        all.add(one);
        one = new ArrayList<>();
        one.add("<");
        one.add("Expr");
        one.add("ExprTemp");
        all.add(one);
        one = new ArrayList<>();
        one.add(">");
        one.add("Expr");
        one.add("ExprTemp");
        all.add(one);
        one = new ArrayList<>();
        one.add("<=");
        one.add("Expr");
        one.add("ExprTemp");
        all.add(one);
        one = new ArrayList<>();
        one.add(">=");
        one.add("Expr");
        one.add("ExprTemp");
        all.add(one);
        one = new ArrayList<>();
        one.add("==");
        one.add("Expr");
        one.add("ExprTemp");
        all.add(one);
        one = new ArrayList<>();
        one.add("!=");
        one.add("Expr");
        one.add("ExprTemp");
        all.add(one);
        one = new ArrayList<>();
        one.add("null");
        all.add(one);
        grammar.put("ExprTemp", all);

        return grammar;
    }


}
