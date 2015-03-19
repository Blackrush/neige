package neige.tools;

import neige.Stdlib;
import neige.lang.expr.Expression;
import neige.lang.*;

import java.math.MathContext;
import java.util.Scanner;

public final class REPL {
    private REPL() {}

    private static Parser newParser(String not) {
        if (not.equalsIgnoreCase("postfix")) {
            return new PostfixParser();
        } else if (not.equalsIgnoreCase("infix")) {
            return new InfixParser();
        }

        throw new IllegalArgumentException(String.format(
                "la notation %s n'existe pas (postfix ou infix)",
                not));
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        Parser parser;
        if (args.length <= 0) {
            System.out.print("Entrez la notation souhaitée (postfix ou infix) : ");
            parser = newParser(scanner.nextLine());
        } else {
            parser = newParser(args[0]);
        }

        InterpContext ctx = new HashInterpContext(MathContext.DECIMAL128, System.in, System.out, System.err, true);
        Stdlib.load(ctx);

        while (ctx.isAlive()) {
            System.out.print("> ");
            String line = scanner.nextLine().trim();
            try {
                Expression exp = parser.parse(line);
                Expression result = exp.evaluate(ctx);
                System.out.println(parser.show(result));
            } catch (ParseException e) {
                System.out.println(Throwables.toString(e));
            } catch (NeigeRuntimeException e) {
                System.out.println(Throwables.toString(e));
            }
        }
    }

}
