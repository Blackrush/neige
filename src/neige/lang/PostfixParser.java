package neige.lang;

import neige.lang.expr.Expression;
import neige.lang.expr.binary.BinaryExpression;
import neige.lang.expr.binary.bool.*;
import neige.lang.expr.binary.num.*;
import neige.lang.expr.literal.*;
import neige.lang.expr.unary.NotExpression;

import java.util.ArrayList;
import java.util.LinkedList;

public class PostfixParser implements Parser {



    @Override
    public Expression parse(String input) {
        ArrayList<Token> tokens = tokenize(input);
        LinkedList<Expression> remainExpression = new LinkedList<Expression>();
        for (Token token : tokens) {
            if (token instanceof Token.Dynamic) {
                remainExpression.add(Literals.parse((Token.Dynamic) token));
                continue;
            }


            //Num
            if (token == Token.Static.MUL) {
                remainExpression.addLast(new MulExpression(remainExpression.removeFirst(), remainExpression.removeFirst()));
            } else if (token == Token.Static.DIV) {
                remainExpression.addLast(new DivExpression(remainExpression.removeFirst(), remainExpression.removeFirst()));
            } else if (token == Token.Static.ADD) {
                remainExpression.addLast(new AddExpression(remainExpression.removeFirst(), remainExpression.removeFirst()));
            } else if (token == Token.Static.SUB) {
                remainExpression.addLast(new SubExpression(remainExpression.removeFirst(), remainExpression.removeFirst()));
            }

            //Boolean
            if ( token == Token.Static.NOT) {
                remainExpression.addLast( new NotExpression(remainExpression.removeFirst()));
            } else if ( token == Token.Static.MORE_EQ){
                remainExpression.addLast( new MoreEQExpression(remainExpression.removeFirst(), remainExpression.removeFirst()) );
            } else if ( token == Token.Static.LESS) {
                remainExpression.addLast( new LessExpression(remainExpression.removeFirst(), remainExpression.removeFirst()) );
            } else if ( token == Token.Static.LESS_EQ){
                remainExpression.addLast( new LessEQExpression(remainExpression.removeFirst(), remainExpression.removeFirst()) );
            } else if ( token == Token.Static.EQ){
                remainExpression.addLast( new EQExpression(remainExpression.removeFirst(), remainExpression.removeFirst()) );
            } else if ( token == Token.Static.NEQ) {
                remainExpression.addLast( new NotEQExpression(remainExpression.removeFirst(), remainExpression.removeFirst()) );
            } else if ( token == Token.Static.OR)  {
                remainExpression.addLast( new OrExpression(remainExpression.removeFirst(), remainExpression.removeFirst()) );
            } else if (token == Token.Static.MORE){
                remainExpression.addLast( new MoreExpression(remainExpression.removeFirst(), remainExpression.removeFirst()) );
            } else if ( token == Token.Static.XOR) {
                remainExpression.addLast( new XorExpression(remainExpression.removeFirst(), remainExpression.removeFirst()) );
            } else if ( token == Token.Static.AND) {
                remainExpression.addLast( new AndExpression(remainExpression.removeFirst(), remainExpression.removeFirst()) );
            }

            //Others
            if ( token == Token.Static.POW) {
                remainExpression.addLast( new PowExpression(remainExpression.removeFirst(),remainExpression.removeFirst()));
            } else if ( token == Token.Static.MOD){
                remainExpression.addLast( new ModExpression(remainExpression.removeFirst(),remainExpression.removeFirst()));
            }

        }
        if (remainExpression.size() == 1) { return remainExpression.getFirst(); }


        throw new RuntimeException();
    }

    @Override
    public String show(Expression exp) {
        if (exp instanceof LiteralExpression) {
            return ((LiteralExpression) exp).getValue().toString();
        } else if (exp instanceof BinaryExpression) {
            BinaryExpression bin = (BinaryExpression) exp;
            return show(bin.getLhs()) + " " +
                    show(bin.getRhs()) + " " +
                    bin.getToken().value();
        }

        // TODO PostfixParser#show
        throw new Error("TODO PostfixParser#show:88");
    }
    public ArrayList<Token> tokenize(String input){
        String[] tokens = input.split(" ");
        ArrayList<Token> res = new ArrayList<Token>();

        for(String s : tokens){
            Token token = Tokens.get(s);
            res.add(token);
        }
        return res;
    }
}