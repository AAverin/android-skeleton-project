package pro.anton.averin.android.skeleton.utils;

import android.os.AsyncTask;

import pro.anton.averin.android.skeleton.BaseContext;
import pro.anton.averin.android.skeleton.activities.BaseActivity;

/**
 * Created by AAverin on 21.05.14.
 */
public class LoadingManager {

    public interface LoadingManagerListener {
        public void onLoadingFinished();
        public void onLoadingFailed();
    }
    private LoadingManagerListener listener = null;


    private class LoadingTask extends AsyncTask<Object, Void, Boolean> {

        private LoadingManagerListener taskListener = null;
        public LoadingTask(LoadingManagerListener taskListener) {
            this.taskListener = taskListener;
        }

        @Override
        protected Boolean doInBackground(Object... params) {

            //add loading here
            return false;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            if (result) {
                if (taskListener != null) {
                    taskListener.onLoadingFinished();
                }
            } else {
                if (taskListener != null) {
                    taskListener.onLoadingFailed();
                }
            }
        }
    }


    private BaseContext baseContext;
    private BaseActivity activity;

    private static LoadingManager instance = null;
    public static LoadingManager getInstance(BaseActivity activity, LoadingManagerListener listener) {
        if (instance == null) {
            instance = new LoadingManager(activity);
        }
        instance.setLoadingManagerListener(listener);
        return instance;
    }

    private LoadingManager(BaseActivity activity) {
        this.activity = activity;
        this.baseContext = (BaseContext) activity.getApplicationContext();
    }

    public void setLoadingManagerListener(LoadingManagerListener listener) {
        this.listener = listener;
    }

    public void start() {
        LoadingTask task = new LoadingTask(listener);
        Utils.executAsyncTask(task);
    }

   public void destroy() {
        listener = null;
        baseContext = null;
        activity = null;



        instance = null;
    }
}
