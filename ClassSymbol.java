import java.util.HashMap;
import java.util.Map;

public class ClassSymbol {

    private String name;

    private Map<String, String> fields = new HashMap<>();

    private delphiParser.BlockContext constructorBlock = null;
    private delphiParser.BlockContext destructorBlock = null;

    public ClassSymbol(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public Map<String, String> getFields() {
        return fields;
    }

    public delphiParser.BlockContext getConstructorBlock() {
        return constructorBlock;
    }

    public delphiParser.BlockContext getDestructorBlock() {
        return destructorBlock;
    }

    public void addField(String fieldName, String type) {
        fields.put(fieldName, type);
        System.out.println("  Field added: " + fieldName + " : " + type);
    }

    public boolean hasField(String fieldName) {
        return fields.containsKey(fieldName);
    }

    public void setConstructor(delphiParser.BlockContext block) {
        this.constructorBlock = block;
        System.out.println("  Constructor registered for class " + name);
    }

    public void setDestructor(delphiParser.BlockContext block) {
        this.destructorBlock = block;
        System.out.println("  Destructor registered for class " + name);
    }
}