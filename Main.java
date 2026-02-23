import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;

public class Main {
    public static void main(String[] args) throws Exception {

        CharStream input = CharStreams.fromFileName(args[0]);
        delphiLexer lexer = new delphiLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        delphiParser parser = new delphiParser(tokens);

        ParseTree tree = parser.program();

        Interpreter interpreter = new Interpreter();
        interpreter.visit(tree);
    }
}