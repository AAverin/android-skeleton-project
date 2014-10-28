package pro.anton.averin.android.skeleton.views;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.widget.ProgressBar;
import android.widget.TextView;

import pro.anton.averin.android.skeleton.activities.BaseActivity;
import pro.anton.averin.android.skeleton.utils.Logger;
import pro.anton.averin.android.skeleton.utils.Utils;
import pro.averin.anton.android.skeleton.R;

/**
 * Created by AAverin on 7/5/2014.
 */
public class LoadingOverlay {

    private static class LoadingDialog extends DialogFragment {

        public interface DismissCallback {
            public void onDismiss(DialogInterface dialog);
        }
        private DismissCallback dismissCallback = null;
        public void setDismissCallback(DismissCallback callback) {
            dismissCallback = callback;
        }

        private ProgressBar loAnim;
        private TextView loText;
        private ViewGroup contentView = null;

        BaseActivity activity = null;

        int offscreen, carLeft, loadingLeft;

        public static LoadingDialog newInstance() {
            LoadingDialog dialogFragment = new LoadingDialog();
            return dialogFragment;
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setStyle(DialogFragment.STYLE_NO_TITLE, android.R.style.Theme_Holo_Light_Panel);
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            contentView = (ViewGroup) inflater.inflate(R.layout.d_loading, container, false);
            return contentView;
        }

        @Override
        public void onViewCreated(View view, Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);

            Window window = getDialog().getWindow();
            WindowManager.LayoutParams lp = window.getAttributes();
            lp.dimAmount = 0.75f;
            window.setAttributes(lp);
            window.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);

            getDialog().setCanceledOnTouchOutside(false);

        }

        @Override
        public void onActivityCreated(Bundle savedInstanceState) {
            super.onActivityCreated(savedInstanceState);

            activity = (BaseActivity) getActivity();

            loAnim = (ProgressBar) contentView.findViewById(R.id.lo_anim);
            loText = (TextView) contentView.findViewById(R.id.lo_text);

            final DisplayMetrics metrics = getActivity().getResources().getDisplayMetrics();

            ViewTreeObserver observer = contentView.getViewTreeObserver();
            observer.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    Utils.safeRemoveLayoutListener(contentView.getViewTreeObserver(), this);
                    int carWidth = loAnim.getMeasuredWidth();
                    int loadingWidth = loText.getMeasuredWidth();

                    offscreen = metrics.widthPixels + carWidth;
                    carLeft = metrics.widthPixels / 2 - (carWidth / 2);
                    loadingLeft = metrics.widthPixels / 2 - (loadingWidth / 2);
                }
            });

        }

        @Override
        public void onDismiss(DialogInterface dialog) {
            if (dismissCallback != null) {
                dismissCallback.onDismiss(dialog);
            }
            super.onDismiss(dialog);
        }

        @Override
        public void onResume() {
            super.onResume();
            Window window = getDialog().getWindow();
            WindowManager.LayoutParams lp = window.getAttributes();
            lp.width = WindowManager.LayoutParams.MATCH_PARENT;
            window.setAttributes(lp);
        }

        int SHOW_DURATION = 600;
        public void showLoading() {


            contentView.setVisibility(View.VISIBLE);
            ObjectAnimator carAnimator = ObjectAnimator.ofFloat(loAnim, "x", offscreen, carLeft);
            carAnimator.setDuration(SHOW_DURATION);
            carAnimator.setInterpolator(new AccelerateDecelerateInterpolator());

            ObjectAnimator textAnimator = ObjectAnimator.ofFloat(loText, "x", offscreen, loadingLeft);
            textAnimator.setDuration(SHOW_DURATION);
            textAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
            textAnimator.setStartDelay(100);

            AnimatorSet set = new AnimatorSet();
            set.playTogether(carAnimator, textAnimator);
            set.start();
        }

        public void hideLoading() {
            ObjectAnimator carAnimator = ObjectAnimator.ofFloat(loAnim, "x", carLeft, -offscreen);
            carAnimator.setDuration(SHOW_DURATION);
            carAnimator.setInterpolator(new AccelerateInterpolator());
            carAnimator.setStartDelay(100);

            ObjectAnimator textAnimator = ObjectAnimator.ofFloat(loText, "x", loadingLeft, -offscreen);
            textAnimator.setDuration(SHOW_DURATION);
            textAnimator.setInterpolator(new AccelerateInterpolator());


            AnimatorSet set = new AnimatorSet();
            set.playTogether(carAnimator, textAnimator);
            set.start();

            set.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {

                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    if (isAdded() && activity != null && activity.isActive())
                        dismiss();
                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });
        }
    }



    private static LoadingOverlay instance = null;

    private boolean isLoadingShowing = false;
    LoadingDialog loadingDialog = null;
    private final static String FRAGMENT_TAG = "LOADING_OVERLAY_DIALOG";
    private BaseActivity activity;

    public static LoadingOverlay getInstance() {
        if (instance == null) {
            instance = new LoadingOverlay();
        }
        return instance;
    }

    private LoadingOverlay() {

    }

    public void setActivity(BaseActivity activity) {
        this.activity = activity;
    }

    public void showLoading() {
        if (isLoadingShowing || activity == null || !activity.isActive()) {
            return;
        }
        Logger.log(this, "showLoading");
        loadingDialog = LoadingDialog.newInstance();
        loadingDialog.show(activity.getSupportFragmentManager(), FRAGMENT_TAG);
        activity.uiHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                loadingDialog.showLoading();
            }
        }, 50);

        isLoadingShowing = true;
    }

    public void hideLoading() {
        if (!isLoadingShowing || activity == null || !activity.isActive()) {
            return;
        }
        Logger.log(this, "hideLoading");
        if (loadingDialog != null) {
            loadingDialog.setDismissCallback(new LoadingDialog.DismissCallback() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    loadingDialog.setDismissCallback(null);
                    loadingDialog = null;
                }
            });
            loadingDialog.hideLoading();
        }

        isLoadingShowing = false;
    }
}
