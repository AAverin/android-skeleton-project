package pro.anton.averin.android.skeleton.views.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.TextView;

import pro.anton.averin.android.skeleton.BaseContext;
import pro.averin.anton.android.skeleton.R;
import pro.anton.averin.android.skeleton.activities.BaseActivity;

/**
 * Created by AAverin on 27.01.14.
 */
public abstract class BasePopupDialog extends DialogFragment implements View.OnClickListener {

    protected BaseContext baseContext;
    protected ViewGroup contentView;

    public final static String FRAGMENT_TAG = "PopupDialog";

    private String title;
    ViewGroup basePopupView;

    TextView titleView;
    ImageButton closeButton;

    protected boolean isDirty = false;

    public OnDialogDismissListener dialogDismissCallback = null;

    private boolean isOutsideDismissable = true;

    public interface OnDialogDismissListener {
        public void onDismiss(DialogInterface dialog, boolean isDirty);
    }

    public BasePopupDialog() {

    }

    public BaseActivity getBaseActivity() {
        return (BaseActivity) getActivity();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setStyle(DialogFragment.STYLE_NO_TITLE, android.R.style.Theme_Holo_Light_Panel);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        baseContext = (BaseContext) activity.getApplicationContext();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Window window = getDialog().getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.dimAmount = 0.75f;
        window.setAttributes(lp);
        window.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);

        getDialog().setCanceledOnTouchOutside(isOutsideDismissable);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (title != null) {
            titleView = (TextView) basePopupView.findViewById(R.id.dialog_title);
            titleView.setText(title);
        }

        closeButton = (ImageButton) basePopupView.findViewById(R.id.dialog_close);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        closeButtonVisible(true);
    }

    @Override
    public void onResume() {
        super.onResume();
        Window window = getDialog().getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 320, getResources().getDisplayMetrics());
//        lp.height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 500, getResources().getDisplayMetrics());
        window.setAttributes(lp);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        basePopupView = (ViewGroup) inflater.inflate(R.layout.popup_base, container, false);
        basePopupView.addView(getPopupContentView());
        return basePopupView;
    }

    abstract protected ViewGroup getPopupContentView();

    public BasePopupDialog(OnDialogDismissListener callback) {
        this.dialogDismissCallback = callback;
    }

    protected void setTitle(String title) {
        this.title = title;
        if (title != null) {
            if (titleView == null) {
                titleView = (TextView) basePopupView.findViewById(R.id.dialog_title);
            }
            titleView.setText(title);
        }
    }

    public void setOnDialogDismissListener(OnDialogDismissListener callback) {
        this.dialogDismissCallback = callback;
    }

    public BasePopupDialog(String title, Drawable titleDrawable) {
        init(title, titleDrawable);
    }

    protected void init(String title, Drawable titleDrawable) {
        this.title = title;
    }

    @Override
    public void onClick(View v) {
        dismiss();
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        if (dialogDismissCallback != null) {
            dialogDismissCallback.onDismiss(dialog, isDirty);
        }
        super.onDismiss(dialog);
    }

    public void setOutsideDismissable(boolean dismissable) {
        isOutsideDismissable = dismissable;
        Dialog d = getDialog();
        if (d != null) {
            d.setCanceledOnTouchOutside(dismissable);
        }
    }

    public void closeButtonVisible(boolean visible) {
        if (visible) {
            closeButton.setVisibility(View.VISIBLE);
        } else {
            closeButton.setVisibility(View.GONE);
        }
    }

    public void displayDialog(BaseActivity activity) {
        if (activity == null || !activity.isActive()) {
            return;
        }

        FragmentManager fragmentManager = activity.getSupportFragmentManager();

        if (fragmentManager == null || fragmentManager.isDestroyed()) {
            return;
        }

        FragmentTransaction transaction = fragmentManager.beginTransaction();
        Fragment oldDialog = fragmentManager.findFragmentByTag(BasePopupDialog.FRAGMENT_TAG);
        if (oldDialog != null) {
            transaction.remove(oldDialog);
        }
        transaction.addToBackStack(null);

        //fix for IllegalStateException: Can not perform this action after onSaveInstanceState
        //http://stackoverflow.com/questions/15729138/on-showing-dialog-i-get-can-not-perform-this-action-after-onsaveinstancestate
        if (!activity.isActive()) { //final safeguard in case activity was closed in the middle
            return;
        }
        FragmentTransaction ft = fragmentManager.beginTransaction();
        this.show(ft, BasePopupDialog.FRAGMENT_TAG);
    }
}

