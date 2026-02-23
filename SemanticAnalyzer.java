import java.util.HashMap;
import java.util.Map;

public class SemanticAnalyzer extends delphiBaseVisitor<String> {

    // Store variable name -> type
    private Map<String, String> symbolTable = new HashMap<>();

    // Handle variable declarations
    @Override
    public String visitVariableDeclaration(delphiParser.VariableDeclarationContext ctx) {

        String typeName = ctx.type_().getText();
        delphiParser.IdentifierListContext idList = ctx.identifierList();

        for (delphiParser.IdentifierContext idCtx : idList.identifier()) {
            String varName = idCtx.getText();

            if (symbolTable.containsKey(varName)) {
                System.out.println("Semantic Error: Variable '" + varName + "' already declared.");
            } else {
                symbolTable.put(varName, typeName);
                System.out.println("Declared variable: " + varName + " : " + typeName);
            }
        }

        return null;
    }

    // Handle assignments
    @Override
    public String visitAssignmentStatement(delphiParser.AssignmentStatementContext ctx) {

        String varName = ctx.variable().identifier(0).getText();

        if (!symbolTable.containsKey(varName)) {
            System.out.println("Semantic Error: Variable '" + varName + "' used before declaration.");
            return null;
        }

        String variableType = symbolTable.get(varName);
        String expressionType = visit(ctx.expression());

        if (expressionType != null && !variableType.equalsIgnoreCase(expressionType)) {
            System.out.println("Type Error: Cannot assign " + expressionType +
                               " to " + variableType + " variable '" + varName + "'");
        }

        return null;
    }

    // Detect expression type
    @Override
    public String visitFactor(delphiParser.FactorContext ctx) {

        if (ctx.variable() != null) {
            String varName = ctx.variable().identifier(0).getText();

            if (!symbolTable.containsKey(varName)) {
                System.out.println("Semantic Error: Variable '" + varName + "' used before declaration.");
                return null;
            }

            return symbolTable.get(varName);
        }

        if (ctx.unsignedConstant() != null) {
            if (ctx.unsignedConstant().unsignedNumber() != null) {
                if (ctx.unsignedConstant().unsignedNumber().unsignedInteger() != null)
                    return "INTEGER";
                else
                    return "REAL";
            }
        }

        if (ctx.bool_() != null)
            return "BOOLEAN";

        return null;
    }
}