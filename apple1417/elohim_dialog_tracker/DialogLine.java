package apple1417.elohim_dialog_tracker;

import javafx.beans.property.SimpleStringProperty;

public class DialogLine {
    /*
      By using a property javafx can observe the state, so only the state cell will get updated
      This means the other cells won't ignore their text wrapping and shrink when we update them
    */
    private DialogState rawState;
    private SimpleStringProperty state;

    private final String level;
    private final String dialog;
    private final String shortName;
    private final String trigger;

    public DialogLine(DialogState state, String level, String dialog, String shortName, String trigger) {
        this.rawState = state;
        this.level = level;
        this.dialog = dialog;
        this.shortName = shortName;
        this.trigger = trigger;

        this.state = new SimpleStringProperty(state.toString());
    }
    public DialogLine(String level, String dialog, String shortName, String trigger) {
        this(DialogState.UNCOLLECTED, level, dialog, shortName, trigger);
    }

    public void setRawState(DialogState state) {
        rawState = state;
        this.state.set(state.toString());
    }
    public DialogState getRawState() {
        return rawState;
    }
    public SimpleStringProperty StateProperty() {
        return state;
    }

    public String getLevel() {
        return level;
    }
    public String getDialog() {
        return dialog;
    }
    public String getShortName() {
        return shortName;
    }
    public String getTrigger() {
        return trigger;
    }
}
