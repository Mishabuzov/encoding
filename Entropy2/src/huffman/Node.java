package huffman;

class Node {

    private Node leftNode;
    private Node rightNode;
    private double frequency;
    private String character;

    Node(double frequency, String character) {
        this.frequency = frequency;
        this.character = character;
        leftNode = null;
        rightNode = null;
    }

    Node(Node leftNode, Node rightNode) {
        this.frequency = leftNode.getFrequency() + rightNode.getFrequency();
        character = leftNode.getCharacter() + rightNode.getCharacter();
        if (leftNode.getFrequency() < rightNode.getFrequency()) {
            this.rightNode = rightNode;
            this.leftNode = leftNode;
        } else {
            this.rightNode = leftNode;
            this.leftNode = rightNode;
        }
    }

    public boolean isLeaf(final Node left, final Node right) {
        return left == null && right == null;
    }

    public Node getLeftNode() {
        return leftNode;
    }

    public void setLeftNode(Node leftNode) {
        this.leftNode = leftNode;
    }

    public Node getRightNode() {
        return rightNode;
    }

    public void setRightNode(Node rightNode) {
        this.rightNode = rightNode;
    }

    public double getFrequency() {
        return frequency;
    }

    public void setFrequency(double frequency) {
        this.frequency = frequency;
    }

    public String getCharacter() {
        return character;
    }

    public void setCharacter(String character) {
        this.character = character;
    }

}
