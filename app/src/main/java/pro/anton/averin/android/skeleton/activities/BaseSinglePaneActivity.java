package pro.anton.averin.android.skeleton.activities;


import android.os.Bundle;
import android.support.v4.app.Fragment;

import pro.averin.anton.android.skeleton.R;

/**
 * Created by AAverin on 29.06.2014.
 */
public abstract class BaseSinglePaneActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_singlepane_empty);

        if (savedInstanceState == null) {
            //init custom fragment content
            Fragment mFragment = onCreatePane();

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.root_container, mFragment, "MAIN")
                    .commit();
        }
    }

    protected abstract Fragment onCreatePane();
}
