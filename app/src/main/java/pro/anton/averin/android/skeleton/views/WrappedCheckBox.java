package pro.anton.averin.android.skeleton.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.CheckBox;
import android.widget.LinearLayout;

/**
 * Created by AAverin on 29-9-2014.
 */
public class WrappedCheckBox extends LinearLayout {

    private CheckBox checkBox = null;

    public WrappedCheckBox(Context context) {
        super(context);
        init();
    }

    public WrappedCheckBox(Context context, AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public WrappedCheckBox(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        checkBox = new CheckBox(getContext());
        addView(checkBox);
    }

    public void setChecked(boolean checked) {
        if (checkBox != null) {
            checkBox.setChecked(checked);
        }
    }

    public boolean isChecked() {
        if (checkBox != null) {
            return checkBox.isChecked();
        }
        return false;
    }
}
