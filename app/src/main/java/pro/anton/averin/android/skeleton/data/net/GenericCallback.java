package pro.anton.averin.android.skeleton.data.net;

import com.splunk.mint.Mint;

import java.util.ArrayList;
import java.util.HashMap;

import pro.anton.averin.android.skeleton.activities.BaseActivity;
import pro.anton.averin.android.skeleton.utils.Logger;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by AAverin on 14-7-2014.
 */
public abstract class GenericCallback<T> implements Callback<T> {

    private BaseActivity activity;
    public GenericCallback() {
        super();
    }

    public void setActivity(BaseActivity activity) {
        this.activity = activity;
    }

    public abstract void success(T t, Response response);

    @Override
    public void failure(RetrofitError error) {
        Logger.log_e(this, "failed with ", error.getMessage(), " for ", error.getUrl());
        HashMap<String, String> exceptionDetails = new HashMap<String, String>();

        ArrayList<String> errorParts = splitBy(error.getMessage(), 128);
        int i = 0;
        for (String errorPart : errorParts) {
            exceptionDetails.put("NetworkFailPart" + i, errorPart);
            i++;
        }


        ArrayList<String> urlParts = splitBy(error.getUrl(), 128);
        int j = 0;
        for (String urlPart : urlParts) {
            exceptionDetails.put("NetworkUrlPart" + j, urlPart);
            j++;
        }
        Mint.logExceptionMap(exceptionDetails, new Exception("GenericCallbackException"));
        if (activity != null && activity.isActive()) {
            activity.handleGenericErrorRestart(3000);
            _failure(true, error);
        } else {
            _failure(false, error);
        }
    }

    public abstract void _failure(boolean genericHandeled, RetrofitError error);

    private ArrayList<String> splitBy(String what, int length) {
        String msg = what;
        ArrayList<String> parts = new ArrayList<String>();
        int index = 0;
        while (index < msg.length()) {
            parts.add(msg.substring(index, Math.min(index + length, msg.length())));
            index += length;
        }
        return parts;
    }
}
