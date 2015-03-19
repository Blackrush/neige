package neige.tools;

import neige.Stdlib;
import neige.lang.*;
import neige.lang.expr.Expression;
import neige.lang.expr.literal.ErrorExpression;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public final class Interpreter {
    private Interpreter() {}

    public static void main(String[] args) throws IOException {
        if (args.length <= 0) {
            System.err.println("You must provide only one parameter to the interpreter : the file to be executed.");
            return;
        }

        File file = new File(args[0]);
        if (!file.exists() || !file.isFile()) {
            System.err.printf("File \"%s\" not found.\n", args[0]);
            return;
        }

        Parser parser;

        if (hasExtension(file, "isnow")) {
            parser = new InfixParser();
        } else if (hasExtension(file, "psnow")) {
            parser = new PostfixParser();
        } else {
            System.err.printf("File \"%s\" has an unsupported extension.\n", args[0]);
            return;
        }

        BufferedReader reader = new BufferedReader(new FileReader(file));
        StringBuilder code = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            code.append(line).append('\n');
        }

        String theCode = code.toString();

        InterpContext ctx = HashInterpContext.empty();
        Stdlib.load(ctx);
        Expression parsed = parser.parse(theCode);
        Expression result = parsed.evaluate(ctx);

        if (result instanceof ErrorExpression) {
            ((ErrorExpression) result).getValue().printStackTrace();
        }
    }

    private static boolean hasExtension(File file, String ext) {
        return file.getName().endsWith(ext);
    }
}
