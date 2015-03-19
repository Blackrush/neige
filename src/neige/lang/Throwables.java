package neige.lang;

import java.io.PrintWriter;
import java.io.StringWriter;

public class Throwables {
    public static String toString(Throwable err) {
        StringWriter w = new StringWriter();
        err.printStackTrace(new PrintWriter(w));
        return w.toString();
    }
}
