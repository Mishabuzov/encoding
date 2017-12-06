package huffman;

public enum Names {

    NORMAL("Карлос Кастанеда \"Путешествие в Икстлан\"", "res/Kastaneda.txt"),
    ENCODED("Encoded text", "res/encoded.txt");

    private String name;

    private String way;

    Names(String name, String way) {
        this.name = name;
        this.way = way;
    }

    public String getName() {
        return name;
    }

    public String getWay() {
        return way;
    }
}
