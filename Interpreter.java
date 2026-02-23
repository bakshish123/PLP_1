import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Interpreter extends delphiBaseVisitor<Object> {

    private Map<String, ClassSymbol> classes = new HashMap<>();
    private Map<String, Object> globals = new HashMap<>();

    private ClassSymbol currentClass = null;
    private boolean currentVisibilityPrivate = false;
    private ObjectInstance currentObject = null;

    private Scanner scanner = new Scanner(System.in);

    // =====================================================
    // TYPE DEFINITIONS (Classes)
    // =====================================================
    @Override
    public Object visitTypeDefinition(delphiParser.TypeDefinitionContext ctx) {

        if (ctx.classType() != null) {

            String className = ctx.identifier().getText();

            ClassSymbol classSymbol = new ClassSymbol(className);
            classes.put(className, classSymbol);

            currentClass = classSymbol;
            System.out.println("Class defined: " + className);

            visit(ctx.classType());

            currentClass = null;
        }

        return null;
    }

    // =====================================================
    // VISIBILITY SECTION
    // =====================================================
    @Override
    public Object visitVisibilitySection(delphiParser.VisibilitySectionContext ctx) {

        if (ctx.PUBLIC() != null) {
            currentVisibilityPrivate = false;
        } else if (ctx.PRIVATE() != null) {
            currentVisibilityPrivate = true;
        }

        for (delphiParser.ClassMemberContext member : ctx.classMember()) {
            visit(member);
        }

        return null;
    }

    // =====================================================
    // CONSTRUCTOR DECLARATION
    // =====================================================
    @Override
    public Object visitConstructorDeclaration(delphiParser.ConstructorDeclarationContext ctx) {

        if (currentClass != null) {
            currentClass.setConstructor(ctx.block());
        }

        return null;
    }

    // =====================================================
    // DESTRUCTOR DECLARATION
    // =====================================================
    @Override
    public Object visitDestructorDeclaration(delphiParser.DestructorDeclarationContext ctx) {

        if (currentClass != null) {
            currentClass.setDestructor(ctx.block());
        }

        return null;
    }

    // =====================================================
    // VARIABLE DECLARATION
    // =====================================================
    @Override
    public Object visitVariableDeclaration(delphiParser.VariableDeclarationContext ctx) {

        String typeName = ctx.type_().getText();

        for (delphiParser.IdentifierContext id : ctx.identifierList().identifier()) {

            String varName = id.getText();

            if (currentClass != null) {

                currentClass.addField(varName, typeName, currentVisibilityPrivate);

                System.out.println("  Field added: " + varName + " : " + typeName
                        + (currentVisibilityPrivate ? " (PRIVATE)" : " (PUBLIC)"));

            } else {

                if (classes.containsKey(typeName)) {
                    globals.put(varName, null);
                } else {
                    globals.put(varName, 0);
                }

                System.out.println("Variable declared: " + varName);
            }
        }

        return null;
    }

    // =====================================================
    // PROCEDURE STATEMENT (writeln / readln)
    // =====================================================
    @Override
    public Object visitProcedureStatement(delphiParser.ProcedureStatementContext ctx) {

        String name = ctx.identifier().getText();

        if (name.equalsIgnoreCase("writeln")) {

            if (ctx.parameterList() != null) {
                Object value = visit(ctx.parameterList().actualParameter(0).expression());
                System.out.println(value);
            } else {
                System.out.println();
            }

            return null;
        }

        if (name.equalsIgnoreCase("readln")) {

            if (ctx.parameterList() != null) {
                String varName =
                        ctx.parameterList().actualParameter(0).expression().getText();

                int value = scanner.nextInt();
                globals.put(varName, value);
            }

            return null;
        }

        return null;
    }

    // =====================================================
    // ASSIGNMENT
    // =====================================================
    @Override
    public Object visitAssignmentStatement(delphiParser.AssignmentStatementContext ctx) {

        delphiParser.VariableContext varCtx = ctx.variable();

        // -----------------------------------------
        // FIELD ASSIGNMENT (obj.field := value)
        // -----------------------------------------
        if (varCtx.identifier().size() == 2) {

            String objectName = varCtx.identifier(0).getText();
            String fieldName = varCtx.identifier(1).getText();

            ObjectInstance obj;

            if (objectName.equalsIgnoreCase("Self")) {
                obj = currentObject;
            } else {
                obj = (ObjectInstance) globals.get(objectName);
            }

            if (obj == null) {
                throw new RuntimeException("Object '" + objectName + "' is null");
            }

            // ENCAPSULATION CHECK
            if (!objectName.equalsIgnoreCase("Self")) {
                if (obj.getClassSymbol().isFieldPrivate(fieldName)) {
                    throw new RuntimeException(
                            "Cannot access PRIVATE field '" + fieldName + "' from outside class"
                    );
                }
            }

            Object value = visit(ctx.expression());
            obj.setField(fieldName, value);

            return null;
        }

// -----------------------------------------
// SIMPLE VARIABLE
// -----------------------------------------
String varName = varCtx.identifier(0).getText();
String exprText = ctx.expression().getText();

// -----------------------------------------
// CONSTRUCTOR CALL (p := Person.Create)
// -----------------------------------------
if (exprText.contains(".")) {

    String[] parts = exprText.split("\\.");

    if (parts.length == 2) {

        String className = parts[0];
        String methodName = parts[1];

        if (methodName.equalsIgnoreCase("Create")
                && classes.containsKey(className)) {

            ClassSymbol classSymbol = classes.get(className);

            // Create new object
            ObjectInstance obj = new ObjectInstance(classSymbol);
            globals.put(varName, obj);

            System.out.println("Object created: " + varName + " of class " + className);

            // Run constructor
            if (classSymbol.getConstructorBlock() != null) {

                ObjectInstance previousObject = currentObject;
                currentObject = obj;

                visit(classSymbol.getConstructorBlock());

                currentObject = previousObject;
            }

            return null;
        }
    }
}

// -----------------------------------------
// NORMAL ASSIGNMENT
// -----------------------------------------
Object value = visit(ctx.expression());
globals.put(varName, value);

return null;
    }

    // =====================================================
    // VARIABLE ACCESS
    // =====================================================
    @Override
    public Object visitVariable(delphiParser.VariableContext ctx) {

        if (ctx.identifier().size() == 2) {

            String objectName = ctx.identifier(0).getText();
            String fieldName = ctx.identifier(1).getText();

            ObjectInstance obj;

            if (objectName.equalsIgnoreCase("Self")) {
                obj = currentObject;
            } else {
                obj = (ObjectInstance) globals.get(objectName);
            }

            if (obj == null) {
                throw new RuntimeException("Object '" + objectName + "' is null");
            }

            // ENCAPSULATION CHECK
            if (!objectName.equalsIgnoreCase("Self")) {
                if (obj.getClassSymbol().isFieldPrivate(fieldName)) {
                    throw new RuntimeException(
                            "Cannot access PRIVATE field '" + fieldName + "' from outside class"
                    );
                }
            }

            return obj.getField(fieldName);
        }

        return globals.get(ctx.identifier(0).getText());
    }

    // =====================================================
    // LITERAL HANDLING
    // =====================================================
    @Override
    public Object visitUnsignedInteger(delphiParser.UnsignedIntegerContext ctx) {
        return Integer.parseInt(ctx.getText());
    }
}