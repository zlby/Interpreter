package cmm.model;

import java.util.ArrayList;

public class TreeNode {
    private String name;
    private ArrayList<TreeNode> childs;
    private TreeNode parent;
    private Token token;

    public TreeNode(String name, TreeNode parent){
        this.name = name;
        this.parent = parent;
        this.token = null;
        childs = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Token getToken() {
        return token;
    }

    public void setToken(Token token) {
        this.token = token;
    }

    public ArrayList<TreeNode> getChilds() {
        return childs;
    }

    public TreeNode getChild(int pos){
        if (pos == childs.size())
            return null;
        return childs.get(pos);
    }

    public void addChild(TreeNode node){
        childs.add(node);
    }

    public void addChild(int pos, TreeNode node){
        childs.add(pos, node);
    }

    public void removeChild(int pos){
        if (pos >= childs.size() || pos < 0){
            System.out.println("Remove Error!");
            return;
        }
        childs.remove(pos);
    }

    public void resetNode(){
        childs.clear();
    }

    public TreeNode getParent(){
        return parent;
    }

    @Override
    public String toString() {
        return "TreeNode{" +
                "name='" + name + '\'' +
                "token=" + token +
                '}';
    }
}
