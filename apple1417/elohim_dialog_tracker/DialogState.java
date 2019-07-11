package apple1417.elohim_dialog_tracker;

public enum DialogState {
    UNCOLLECTED("Uncollected", "#F4C7C3"),
    NOT_SAVED("Not Saved", "#FDCB9C"),
    COLLECTED("Collected", "#B7E1CD");

    String name;
    String colour;
    private DialogState(String name, String colour) {
        this.name = name;
        this.colour = colour;
    }

    public String toString() {
        return name;
    }

    public String getColour() {
        return colour;
    }

    public DialogState next() {
        switch (this) {
            case UNCOLLECTED: return NOT_SAVED;
            case NOT_SAVED: return COLLECTED;
            case COLLECTED: return UNCOLLECTED;
            default: return UNCOLLECTED;
        }
    }
}
