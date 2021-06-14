package com.hurix.kitaboo.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

/**
 * Created by pratibha.chandanavar on 03-08-2015.
 */
public class KitabooEditTextCopyPasteDiabled extends EditText{

    /** This is to keep track if the view is enabled/diable with respect to dispalying copy/paste/replace methods. */
    private boolean isDisabled = false;

    public KitabooEditTextCopyPasteDiabled(Context context) {
        super(context);
    }

    public KitabooEditTextCopyPasteDiabled(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public KitabooEditTextCopyPasteDiabled(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /** This is a replacement method for the base TextView class' method of the same name. This
     * method is used in hidden class android.widget.Editor to determine whether the PASTE/REPLACE popup
     * appears when triggered from the text insertion handle. Returning false forces this window
     * to never appear.
     * @return false
     */
    boolean canPaste()
    {
        return !isDisabled;
    }

    /** This is a replacement method for the base TextView class' method of the same name. This method
     * is used in hidden class android.widget.Editor to determine whether the PASTE/REPLACE popup
     * appears when triggered from the text insertion handle. Returning false forces this window
     * to never appear.
     * @return false
     */
    @Override
    public boolean isSuggestionsEnabled()
    {
        return isDisabled;
    }

    public void disableMenu() {
        this.setCustomSelectionActionModeCallback(new ActionModeCallbackInterceptor());
        this.setLongClickable(false);
        isDisabled = true;
    }

    public void enableMenu() {
        this.setCustomSelectionActionModeCallback(null);
        this.setLongClickable(true);
        isDisabled = false;
    }

    /**
     * Prevents the action bar (top horizontal bar with cut, copy, paste, etc.) from appearing
     * by intercepting the callback that would cause it to be created, and returning false.
     */
    private class ActionModeCallbackInterceptor implements ActionMode.Callback
    {
        private final String TAG = KitabooEditText.class.getSimpleName();

        public boolean onCreateActionMode(ActionMode mode, Menu menu) { return false; }
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) { return false; }
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) { return false; }
        public void onDestroyActionMode(ActionMode mode) {}
    }
}
