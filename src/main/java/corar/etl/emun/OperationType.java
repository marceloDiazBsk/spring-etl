package corar.etl.emun;

public enum OperationType {

    INSERT("I","INSERT"),
    DELETE("D", "DELETE"),
    UPDATE("U", "UPDATE"),
    IGNORE("IG", "IGNORE");
    private final String code;
    private final String desc;

    private OperationType(String code, String desc){
        this.code = code;
        this.desc = desc;
    }

    public String getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }
}
