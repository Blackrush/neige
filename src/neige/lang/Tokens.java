package neige.lang;

public final class Tokens {
    private Tokens() {}

    public static Token get(String value) {
        Token token = Token.Static.get(value);
        if (token == null) {
            return new Token.Dynamic(value);
        }
        return token;
    }
}
