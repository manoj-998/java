package test;


public enum ModificationType {
    ADD("add"), UPDATE("update"), REMOVE("remove"), NONE("none");

    private final String action;

    ModificationType(String action) {
        this.action = action;
    }

    public String getAction() {
        return action;
    }
}
