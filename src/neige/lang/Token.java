package neige.lang;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public interface Token {
    String value();
    int weight();
    boolean hasPrecedence(int prec);
    int nextPrecedence();
    Scoping getScoping();

    public enum Scoping { NONE, OPENING, CLOSING }

    public final class Dynamic implements Token {
        private final String value;

        public Dynamic(String value) {
            this.value = value;
        }

        @Override
        public String value() {
            return value;
        }

        @Override
        public int weight() {
            return value.length();
        }

        @Override
        public boolean hasPrecedence(int prec) {
            return false;
        }

        @Override
        public int nextPrecedence() {
            return 0;
        }

        @Override
        public Scoping getScoping() {
            return Scoping.NONE;
        }

        @Override
        public String toString() {
            return value;
        }
    }

    public static final int UTMOST = Integer.MAX_VALUE,
                            HIGHER = 5,
                            HIGH   = 4,
                            NORMAL = 3,
                            LOW    = 2,
                            LOWER  = 1,
                            ZERO   = 0;
    public static final Boolean LEFT_ASSOC = true,
                                RIGHT_ASSOC = false,
                                NO_ASSOC = null;

    public enum Static implements Token {
        NL         ("\n",       UTMOST,      NO_ASSOC),
        SEMICOLON  (";",        UTMOST,      NO_ASSOC),
        PAREN_START("(",        UTMOST,      NO_ASSOC, Scoping.OPENING),
        PAREN_END  (")",        UTMOST,      NO_ASSOC, Scoping.CLOSING),
        BLOCK_START("DEBUT",    UTMOST,      NO_ASSOC, Scoping.OPENING),
        BLOCK_END  ("FIN",      UTMOST,      NO_ASSOC, Scoping.CLOSING),
        LIST_START ("[",        UTMOST,      NO_ASSOC, Scoping.OPENING),
        LIST_END   ("]",        UTMOST,      NO_ASSOC, Scoping.CLOSING),
        COMMA      (",",        UTMOST,      NO_ASSOC),
        DECL_VAR   ("<-",       ZERO,        RIGHT_ASSOC),
        DECL_FUN   ("FONCTION", UTMOST,      NO_ASSOC),
        INC        ("++",       HIGHER,      NO_ASSOC),
        DEC        ("--",       HIGHER,      NO_ASSOC),
        MOD        ("%",        HIGH,        RIGHT_ASSOC),
        POW        ("**",       HIGH,        RIGHT_ASSOC),
        ADD        ("+",        LOW,         LEFT_ASSOC),
        SUB        ("-",        LOW,         LEFT_ASSOC),
        MUL        ("*",        NORMAL,      LEFT_ASSOC),
        DIV        ("/",        NORMAL,      LEFT_ASSOC),
        MORE_EQ    (">=",       LOWER,       LEFT_ASSOC),
        MORE       (">",        LOWER,       LEFT_ASSOC),
        LESS_EQ    ("<=",       LOWER,       LEFT_ASSOC),
        LESS       ("<",        LOWER,       LEFT_ASSOC),
        EQ         ("=",        LOWER,       LEFT_ASSOC),
        NEQ        ("!=",       LOWER,       LEFT_ASSOC),
        AND        ("ET",       LOWER,       LEFT_ASSOC),
        OR         ("OU",       LOWER,       LEFT_ASSOC),
        XOR        ("^",        LOWER,       LEFT_ASSOC),
        NOT        ("!",        LOWER,       NO_ASSOC),
        CAT        ("&",        LOWER,       LEFT_ASSOC),
        ELSE       ("SINON",    UTMOST,      NO_ASSOC),
        IF_START   ("SI",       UTMOST,      NO_ASSOC, Scoping.OPENING),
        IF_END     ("FSI",      UTMOST,      NO_ASSOC, Scoping.CLOSING),
        WHILE_START("TANTQUE",  UTMOST,      NO_ASSOC, Scoping.OPENING),
        WHILE_END  ("FTQ",      UTMOST,      NO_ASSOC, Scoping.CLOSING),
        EACH_START ("CHAQUE",   UTMOST,      NO_ASSOC, Scoping.OPENING),
        EACH_END   ("FCHAQUE",  UTMOST,      NO_ASSOC, Scoping.CLOSING),
        FOR_START  ("POUR",     UTMOST,      NO_ASSOC, Scoping.OPENING),
        FOR_END    ("FPOUR",    UTMOST,      NO_ASSOC, Scoping.CLOSING),

        VARIABLES("VARIABLES", LOW, RIGHT_ASSOC),
        ALGORITHME("ALGORITHME", UTMOST, NO_ASSOC),
        ;

        @Override
        public String value() {
            return value;
        }

        @Override
        public int weight() {
            return value.length();
        }

        @Override
        public boolean hasPrecedence(int prec) {
            return leftAssoc != null && precedence >= prec;
        }

        @Override
        public int nextPrecedence() {
            if (isLeftAssoc()) {
                return precedence + 1;
            }
            return precedence;
        }

        @Override
        public Scoping getScoping() {
            return scoping;
        }

        public boolean isLeftAssoc() {
            return leftAssoc != null && leftAssoc;
        }

        public boolean detect(String input, int start) {
            if (input.length() - start < value.length()) {
                return false;
            }
            String sliced = input.substring(start, start + value.length());
            return value.equals(sliced);
        }

        private final String value;
        private final int precedence;
        private final Boolean leftAssoc;
        private final Scoping scoping;
        Static(String value, int precedence, Boolean leftAssoc) {
            this.value = value;
            this.precedence = precedence;
            this.leftAssoc = leftAssoc;
            this.scoping = Scoping.NONE;
        }
        Static(String value, int precedence, Boolean leftAssoc, Scoping scoping) {
            this.value = value;
            this.precedence = precedence;
            this.leftAssoc = leftAssoc;
            this.scoping = scoping;
        }
        private static final Map<String, Token> tokens;
        static {
            Map<String, Token> tt = new HashMap<String, Token>();
            for (Token token : values()) {
                tt.put(token.value(), token);
            }
            tokens = Collections.unmodifiableMap(tt);
        }
        public static Token get(String value) {
            return tokens.get(value);
        }
    }
}
