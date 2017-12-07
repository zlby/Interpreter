package cmm;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;

public class Parser {
    private static HashMap<String, ArrayList<ArrayList<String>>> grammar = Grammar.getGrammar();

    private static TreeNode parsingTree;
    private static ArrayList<Token> tokens = new ArrayList<>();
    private static int pointer;

    public static int doParsing(String filename){
        pointer = 0;
        parsingTree = new TreeNode("Program", null);
        String strinput = "";
        try {
            strinput = Scanner.readInput(filename);
        }catch (FileNotFoundException e){
            System.err.println("Cannot find file");
            return -1;
        }
        ArrayList<Token> tokensTemp = new ArrayList<>(Scanner.inputAnalyse(strinput));
        boolean isRight = true;
        for (Token t : tokensTemp){
            if (t.getFlag().equals("wrong")){
                System.out.println("Error Token at line " + t.getLineNo() + ", position " + t.getPosition() + ": " + t.getValue());
                isRight = false;
            }
            else if ((!t.getFlag().equals("line comment")) && (!t.getFlag().equals("block comment"))){
                tokens.add(t);
            }
        }
        if (!isRight){
            System.out.println("Please check tokens.");
            return -1;
        }

        process(parsingTree);


        if (pointer != tokens.size()){
            System.out.println("Cannot parse! Please check grammar.");
            return -1;
        }

        DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode(parsingTree);
        copyTree(parsingTree, rootNode);

        final JTree tree = new JTree(rootNode);
        JScrollPane jScrollPane = new JScrollPane();
        jScrollPane.getViewport().add(tree, null);
        jScrollPane.setVisible(true);

        JFrame jFrame = new JFrame("ParsingTree");
        jFrame.add(jScrollPane);
        jFrame.setSize(300, 300);
        jFrame.setVisible(true);
        jFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        return 0;
    }

    public static void process(TreeNode target){
        if (pointer == tokens.size())
            return;
        if (grammar.containsKey(target.getValue())){
            boolean canmatch = false;
            ArrayList<ArrayList<String>> productionList = grammar.get(target.getValue());
            //choose X0 -> X1 X2 X3...Xk
            for (int i = 0; i < productionList.size(); i++){
                ArrayList<String> temp = new ArrayList<>(productionList.get(i));
                if (target.getChilds().size() != 0)
                    break;
                for (String val : temp){
                    target.addChild(new TreeNode(val, target));
                }
                //iterate X1 ~ Xk
                int pointerBg = pointer;
                ArrayList<TreeNode> childs = target.getChilds();
                for (int iter = 0; iter < childs.size(); iter++){
                    TreeNode child = childs.get(iter);
                    if (grammar.containsKey(child.getValue())){
                        process(child);
                    }
                    else {
                        if (judge(child.getValue())){
                            pointer++;
                        }
                        else if (child.getValue().equals("null")){
                            ;
                        }
                        else {
                            pointer = pointerBg;
                            target.resetNode();
                            break;
                        }
                    }
                    if (iter == (childs.size()-1)) {
                        canmatch = true;
                    }
                }
            }
            if (!canmatch){
                target.getParent().resetNode();
            }
        }
        else {
            System.err.println("Error!");
        }
    }

    public static boolean judge(String input) {
        try {
            Token current = tokens.get(pointer);
            if (current.getFlag().equals(input))
                return true;
            return false;
        }catch (IndexOutOfBoundsException e){
            return false;
        }
    }

    public static void copyTree(TreeNode node, DefaultMutableTreeNode treeNode){
        for (int i = 0; i < node.getChilds().size(); i++){
            TreeNode child = node.getChild(i);
            if (child != null){
                DefaultMutableTreeNode childNode = new DefaultMutableTreeNode(child);
                copyTree(child, childNode);
                treeNode.add(childNode);
                TreeNode cur = node.getChild(++i);
                while (cur!=null){
                    DefaultMutableTreeNode curNode = new DefaultMutableTreeNode(cur);
                    copyTree(cur, curNode);
                    treeNode.add(curNode);
                    cur = node.getChild(++i);
                }
            }


        }
    }
}
