package cmm;

import java.util.ArrayList;

public class TreeNode {
    private String value;
    private ArrayList<TreeNode> childs;
    private TreeNode parent;

    public TreeNode(String value, TreeNode parent){
        this.value = value;
        this.parent = parent;
        childs = new ArrayList<>();
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
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
                "value='" + value + '\'' +
                '}';
    }
}
