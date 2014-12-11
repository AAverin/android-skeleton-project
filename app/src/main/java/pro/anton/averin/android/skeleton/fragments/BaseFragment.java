package pro.anton.averin.android.skeleton.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.ViewGroup;

import pro.anton.averin.android.skeleton.BaseContext;
import pro.anton.averin.android.skeleton.activities.BaseActivity;

/**
 * Created by AAverin on 29.06.2014.
 */
public abstract class BaseFragment extends Fragment {

    protected BaseContext baseContext = null;

    protected ViewGroup contentView = null;

    public BaseActivity getBaseActivity() {
        return (BaseActivity) getActivity();
    }

    public abstract String getFragmentName();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        Mint.leaveBreadcrumb(getFragmentName() + " onCreate();");
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        baseContext = (BaseContext) getActivity().getApplicationContext();
//        Mint.leaveBreadcrumb(getFragmentName() + " onActivityCreated();");
    }

    @Override
    public void onStop() {
        super.onStop();
//        Mint.leaveBreadcrumb(getFragmentName() + " onStop();");
    }

    @Override
    public void onStart() {
        super.onStart();
//        Mint.leaveBreadcrumb(getFragmentName() + " onStart();");
    }

    @Override
    public void onResume() {
        super.onResume();
//        Mint.leaveBreadcrumb(getFragmentName() + " onResume();");
    }

    @Override
    public void onPause() {
        super.onPause();
//        Mint.leaveBreadcrumb(getFragmentName() + " onPause();");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
//        Mint.leaveBreadcrumb(getFragmentName() + " onDestroy();");
    }

    public boolean handleBackPress() {
        return false;
    }
}
