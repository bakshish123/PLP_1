import java.util.HashMap;
import java.util.Map;

public class ObjectInstance {

    // Reference to class metadata
    private ClassSymbol classSymbol;

    // Runtime field values
    private Map<String, Object> fields = new HashMap<>();

    // -------------------------
    // Constructor
    // -------------------------
    public ObjectInstance(ClassSymbol classSymbol) {

        this.classSymbol = classSymbol;

        // Initialize all fields defined in the class
        for (String fieldName : classSymbol.getFields().keySet()) {
            fields.put(fieldName, null);
        }
    }

    // -------------------------
    // Set Field Value
    // -------------------------
    public void setField(String fieldName, Object value) {

        if (!fields.containsKey(fieldName)) {
            throw new RuntimeException(
                    "Field '" + fieldName + "' not found in class "
                            + classSymbol.getName());
        }

        fields.put(fieldName, value);
    }

    public ClassSymbol getClassSymbol() {
    return classSymbol;
}

    // -------------------------
    // Get Field Value
    // -------------------------
    public Object getField(String fieldName) {

        if (!fields.containsKey(fieldName)) {
            throw new RuntimeException(
                    "Field '" + fieldName + "' not found in class "
                            + classSymbol.getName());
        }

        return fields.get(fieldName);
    }
}