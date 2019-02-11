package hellomeet.vdartvidhya.com.coremodule;

import android.os.Handler;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

import butterknife.BindView;
import butterknife.ButterKnife;
import hellomeet.vdartvidhya.com.R;
import hellomeet.vdartvidhya.com.Utils.CoreActivity;

/**
 * Created by Vidhya on 2/9/2019.
 * HelloMeet
 */
public class Splash extends CoreActivity {

    //***************************************************************************************
    //Declaration
    @BindView(R.id.txtvw_title)
    TextView txtvwTitle;
    @BindView(R.id.img_logo)
    ImageView imgLogo;
    @BindView(R.id.progressbar)
    ProgressBar progressbar;


    private int progress = 0;
    private int progressStatus = 0;
    private final Handler handler = new Handler();

    //***************************************************************************************
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.screen_splash);

        try {
            isStoragePermissionGranted();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    //***************************************************************************************
    @Override
    public void onPermissionsGranted(int requestCode) {

        try {

            GetInitialize();

            Controllisteners();

        } catch (Exception e) {

            e.printStackTrace();
        }


    }

    //***************************************************************************************

    private void GetInitialize() {

        ButterKnife.bind(this);


        Constants.changeStatusBarColour(this);

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                YoYo.with(Techniques.Landing)
                        .duration(1500)
                        .playOn(findViewById(R.id.img_logo));
            }
        });

    }

//***************************************************************************************

    private void Controllisteners() {

        CallNextIntent();

    }


    @Override
    protected void bindViews() {

    }

    @Override
    protected void setListeners() {

    }


    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


//***************************************************************************************


    @Override
    public void onBackPressed() {

    }



//***************************************************************************************

    private void CallNextIntent() {

        new Thread(new Runnable() {
            public void run() {

                while (progressStatus < 100) {
                    progressStatus = doSomeWork();

                    handler.post(() -> progressbar.setProgress(progressStatus));
                }

                handler.post(() -> {

                    progressbar.setVisibility(View.GONE);
                    Constants.globalStartIntent(Splash.this, Login.class, null,1);

                });
            }

            private int doSomeWork() {
                try {

                    Thread.sleep(40L);

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                ++progress;
                return
                        progress;
            }
        }).start();

    }



//***************************************************************************************


}//END
