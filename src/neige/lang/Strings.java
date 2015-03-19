package neige.lang;

public final class Strings {
    private Strings() {}

    public static String indent(int depth) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < depth; i++) {
            result.append("  ");
        }
        return result.toString();
    }
}
