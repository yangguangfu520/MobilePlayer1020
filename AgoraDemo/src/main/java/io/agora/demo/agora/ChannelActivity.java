package io.agora.demo.agora;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import io.agora.demo.agora.util.NetworkConnectivityUtils;
import io.agora.rtc.IRtcEngineEventHandler;
import io.agora.rtc.RtcEngine;
import io.agora.rtc.video.VideoCanvas;


/**
 * some refs:
 * <p/>
 * 1. http://stackoverflow.com/questions/6026625/layout-design-surfaceview-doesnt-display
 * 2. http://stackoverflow.com/questions/1096618/android-surfaceview-scrolling/2216788#2216788
 */

/**
 * Created by apple on 15/9/9.
 */
public class ChannelActivity extends BaseEngineEventHandlerActivity {

    public final static int CALLING_TYPE_VIDEO = 0x100;
    public final static int CALLING_TYPE_VOICE = 0x101;

    public final static String EXTRA_CALLING_TYPE = "EXTRA_CALLING_TYPE";
    public final static String EXTRA_VENDOR_KEY = "EXTRA_VENDOR_KEY";
    public final static String EXTRA_CHANNEL_ID = "EXTRA_CHANNEL_ID";


    private int mCallingType;
    private SurfaceView mLocalView;
    private String vendorKey = "";
    private String channelId = "";
    private TextView mDuration;
    private TextView mByteCounts;
    private View mCameraEnabler;
    private View mCameraSwitcher;
    private LinearLayout mRemoteUserContainer;
    private AlertDialog alertDialog;
    private int time = 0;

    private int mLastRxBytes = 0;
    private int mLastTxBytes = 0;
    private int mLastDuration = 0;

    private int mRemoteUserViewWidth = 0;

    RtcEngine rtcEngine;


    @Override
    public void onCreate(Bundle savedInstance) {

        super.onCreate(savedInstance);
        setContentView(R.layout.activity_room);

        // keep screen on - turned on
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        mRemoteUserViewWidth = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 80, getResources().getDisplayMetrics());
        mCallingType = getIntent().getIntExtra(EXTRA_CALLING_TYPE, CALLING_TYPE_VOICE /*default is voice call*/);

        setupRtcEngine();
        initViews();
        setupTime();

        if (CALLING_TYPE_VIDEO == mCallingType) {
            // video call

            View simulateClick = new View(getApplicationContext());
            simulateClick.setId(R.id.wrapper_action_video_calling);
            this.onUserInteraction(simulateClick);


        } else if (CALLING_TYPE_VOICE == mCallingType) {
            // voice call
            View simulateClick = new View(getApplicationContext());
            simulateClick.setId(R.id.wrapper_action_voice_calling);
            this.onUserInteraction(simulateClick);
        }


        // check network
        if (!NetworkConnectivityUtils.isConnectedToNetwork(getApplicationContext())) {
            onError(104);
        }
    }

    void setupChannel() {
        String channelId = getIntent().getStringExtra(EXTRA_CHANNEL_ID);
        this.channelId = channelId;

        this.rtcEngine.joinChannel(
                this.vendorKey,
                this.channelId,
                "" /*optionalInfo*/,
                new Random().nextInt(Math.abs((int) System.currentTimeMillis()))/*optionalUid*/);

        ((TextView) findViewById(R.id.channel_id)).setText(String.format(getString(R.string.title_channel), channelId));

    }

    void setupRtcEngine() {

        String vendorKey = getIntent().getStringExtra(EXTRA_VENDOR_KEY);
        this.vendorKey = vendorKey;

        // setup engine
        ((AgoraApplication) getApplication()).setRtcEngine(vendorKey);
        rtcEngine = ((AgoraApplication) getApplication()).getRtcEngine();
//        LogUtil.log.d(getApplicationContext().getExternalFilesDir(null).toString() + "/agorasdk.log");
        rtcEngine.setLogFile(getApplicationContext().getExternalFilesDir(null).toString() + "/agorasdk.log");


        // setup engine event activity
        ((AgoraApplication) getApplication()).setEngineEventHandlerActivity(this);

        rtcEngine.enableVideo();

    }


    void ensureLocalViewIsCreated() {


        if (this.mLocalView == null) {

            // local view has not been added before
            FrameLayout localViewContainer = (FrameLayout) findViewById(R.id.user_local_view);
            SurfaceView localView = rtcEngine.CreateRendererView(getApplicationContext());
            this.mLocalView = localView;
            localViewContainer.addView(localView, new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));

            rtcEngine.enableVideo();
            rtcEngine.setupLocalVideo(new VideoCanvas(this.mLocalView));
        }

    }

    /**
     * Initialize views and its listeners
     */
    void initViews() {

        // muter
        CheckBox muter = (CheckBox) findViewById(R.id.action_muter);
        muter.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean mutes) {

                rtcEngine.muteLocalAudioStream(mutes);
                compoundButton.setBackgroundResource(mutes ? R.drawable.ic_room_mute_pressed:R.drawable.ic_room_mute);

            }
        });

        // speaker
        CheckBox speaker = (CheckBox) findViewById(R.id.action_speaker);
        speaker.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean usesSpeaker) {

                rtcEngine.setEnableSpeakerphone(usesSpeaker);
                compoundButton.setBackgroundResource(usesSpeaker ? R.drawable.ic_room_loudspeaker : R.drawable.ic_room_loudspeaker_pressed);

            }
        });

        // camera enabler
        CheckBox cameraEnabler = (CheckBox) findViewById(R.id.action_camera_enabler);
        cameraEnabler.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean disablesCamera) {

                rtcEngine.muteLocalVideoStream(disablesCamera);

                if (disablesCamera) {
                    findViewById(R.id.user_local_voice_bg).setVisibility(View.VISIBLE);
                    rtcEngine.muteLocalVideoStream(true);

                } else {
                    findViewById(R.id.user_local_voice_bg).setVisibility(View.GONE);
                    rtcEngine.muteLocalVideoStream(false);
                }

                compoundButton.setBackgroundResource(disablesCamera ? R.drawable.ic_room_button_close_pressed : R.drawable.ic_room_button_close);

            }
        });


        // camera switcher
        CheckBox cameraSwitch = (CheckBox) findViewById(R.id.action_camera_switcher);
        cameraSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean switches) {

                rtcEngine.switchCamera();

                compoundButton.setBackgroundResource(switches ? R.drawable.ic_room_button_change_pressed : R.drawable.ic_room_button_change);

            }
        });


        // setup states of action buttons
        muter.setChecked(false);
        speaker.setChecked(true);
        cameraEnabler.setChecked(false);
        cameraSwitch.setChecked(false);


        findViewById(R.id.wrapper_action_video_calling).setOnClickListener(getViewClickListener());
        findViewById(R.id.wrapper_action_voice_calling).setOnClickListener(getViewClickListener());
        findViewById(R.id.action_hung_up).setOnClickListener(getViewClickListener());
        findViewById(R.id.action_back).setOnClickListener(getViewClickListener());


        mDuration = (TextView) findViewById(R.id.stat_time);
        mByteCounts = (TextView) findViewById(R.id.stat_bytes);


        mCameraEnabler = findViewById(R.id.wrapper_action_camera_enabler);
        mCameraSwitcher = findViewById(R.id.wrapper_action_camera_switcher);

        mRemoteUserContainer = (LinearLayout) findViewById(R.id.user_remote_views);


        setRemoteUserViewVisibility(false);
    }

    void setRemoteUserViewVisibility(boolean isVisible) {

        findViewById(R.id.user_remote_views).getLayoutParams().height =
                isVisible ? (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 80, getResources().getDisplayMetrics())
                        : 0;

    }

    void removeBackgroundOfCallingWrapper() {
        findViewById(R.id.wrapper_action_video_calling).setBackgroundResource(R.drawable.shape_transparent);
        findViewById(R.id.wrapper_action_voice_calling).setBackgroundResource(R.drawable.shape_transparent);
    }

    void setupTime() {

        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        time++;

                        if (time >= 3600) {
                            mDuration.setText(String.format("%d:%02d:%02d", time / 3600, (time % 3600) / 60, (time % 60)));
                        } else {
                            mDuration.setText(String.format("%02d:%02d", (time % 3600) / 60, (time % 60)));
                        }
                    }
                });
            }
        };

        Timer timer = new Timer();
        timer.schedule(task, 1000, 1000);
    }


    /**
     * 切换视频音频通话时，更新 view 的显示。只是更新重用的 view，并不新添加。
     *
     * @param callingType
     */
    void updateRemoteUserViews(int callingType) {

        int visibility = View.GONE;

        if (CALLING_TYPE_VIDEO == callingType) {
            visibility = View.GONE;

        } else if (CALLING_TYPE_VOICE == callingType) {
            visibility = View.VISIBLE;
        }

        for (int i = 0, size = mRemoteUserContainer.getChildCount(); i < size; i++) {

            View singleRemoteView = mRemoteUserContainer.getChildAt(i);
            singleRemoteView.findViewById(R.id.remote_user_voice_container).setVisibility(visibility);

            if (CALLING_TYPE_VIDEO == callingType) {
                // re-setup remote video

                FrameLayout remoteVideoUser = (FrameLayout) singleRemoteView.findViewById(R.id.viewlet_remote_video_user);
                // ensure remote video view setup
                if(remoteVideoUser.getChildCount()>0) {
                    final SurfaceView remoteView = (SurfaceView) remoteVideoUser.getChildAt(0);
                    if(remoteView!=null) {
                        remoteView.setZOrderOnTop(true);
                        remoteView.setZOrderMediaOverlay(true);
                        int savedUid = (Integer) remoteVideoUser.getTag();
                        log("saved uid: " + savedUid);
                        rtcEngine.setupRemoteVideo(new VideoCanvas(remoteView, VideoCanvas.RENDER_MODE_ADAPTIVE, savedUid));
                    }
                }

            }
        }
    }

    @Override
    public void onUserInteraction(View view) {
        switch (view.getId()) {
            default:
                super.onUserInteraction(view);
                break;
            case R.id.wrapper_action_video_calling: {

                mCallingType = CALLING_TYPE_VIDEO;

                mCameraEnabler.setVisibility(View.VISIBLE);
                mCameraSwitcher.setVisibility(View.VISIBLE);

                removeBackgroundOfCallingWrapper();
                findViewById(R.id.wrapper_action_video_calling).setBackgroundResource(R.drawable.ic_room_button_yellow_bg);
                findViewById(R.id.user_local_voice_bg).setVisibility(View.GONE);

                // enable video call
                ensureLocalViewIsCreated();

                rtcEngine.enableVideo();
                rtcEngine.muteLocalVideoStream(false);
                rtcEngine.muteLocalAudioStream(false);
                rtcEngine.muteAllRemoteVideoStreams(false);

                // join video call
                if (mRemoteUserContainer.getChildCount() == 0) {
                    this.setupChannel();
                }

                new android.os.Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        updateRemoteUserViews(CALLING_TYPE_VIDEO);
                    }
                },500);

                // ensure video camera enabler states
                CheckBox cameraEnabler = (CheckBox) findViewById(R.id.action_camera_enabler);
                cameraEnabler.setChecked(false);

            }
            break;
            case R.id.wrapper_action_voice_calling: {

                mCallingType = CALLING_TYPE_VOICE;

                mCameraEnabler.setVisibility(View.GONE);
                mCameraSwitcher.setVisibility(View.GONE);

                removeBackgroundOfCallingWrapper();
                findViewById(R.id.wrapper_action_voice_calling).setBackgroundResource(R.drawable.ic_room_button_yellow_bg);

                // show background for voice call
                findViewById(R.id.user_local_voice_bg).setVisibility(View.VISIBLE);


                ensureLocalViewIsCreated();

                // disable video call when necessary
                rtcEngine.disableVideo();
                rtcEngine.muteLocalVideoStream(true);
                rtcEngine.muteAllRemoteVideoStreams(true);

                // join voice call
                if (mRemoteUserContainer.getChildCount() == 0) {
                    this.setupChannel();
                }

                new android.os.Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        updateRemoteUserViews(CALLING_TYPE_VOICE);
                    }
                },500);

            }
            break;

            case R.id.action_hung_up:
            case R.id.action_back: {
                onBackPressed();
            }
            break;
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {

        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void onBackPressed() {

        new Thread(new Runnable() {
            @Override
            public void run() {
                rtcEngine.leaveChannel();
            }
        }).run();

        // keep screen on - turned off
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }


    public void onUpdateSessionStats(final IRtcEngineEventHandler.RtcStats stats) {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                // bytes
                mByteCounts.setText(((stats.txBytes + stats.rxBytes - mLastTxBytes - mLastRxBytes) / 1024 / (stats.totalDuration - mLastDuration + 1)) + "KB/s");

                // remember data from this call back
                mLastRxBytes = stats.rxBytes;
                mLastTxBytes = stats.txBytes;
                mLastDuration = stats.totalDuration;

            }
        });


    }

    public synchronized void onFirstRemoteVideoDecoded(final int uid, int width, int height, final int elapsed) {

        log("onFirstRemoteVideoDecoded: uid: " + uid + ", width: " + width + ", height: " + height);


        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                View remoteUserView = mRemoteUserContainer.findViewById(Math.abs(uid));

                // ensure container is added
                if (remoteUserView == null) {

                    LayoutInflater layoutInflater = getLayoutInflater();

                    View singleRemoteUser = layoutInflater.inflate(R.layout.viewlet_remote_user, null);
                    singleRemoteUser.setId(Math.abs(uid));

                    TextView username = (TextView) singleRemoteUser.findViewById(R.id.remote_user_name);
                    username.setText(String.valueOf(uid));

                    mRemoteUserContainer.addView(singleRemoteUser, new LinearLayout.LayoutParams(mRemoteUserViewWidth, mRemoteUserViewWidth));

                    remoteUserView = singleRemoteUser;
                }


                FrameLayout remoteVideoUser = (FrameLayout) remoteUserView.findViewById(R.id.viewlet_remote_video_user);
                remoteVideoUser.removeAllViews();
                remoteVideoUser.setTag(uid);

                // ensure remote video view setup
                final SurfaceView remoteView = RtcEngine.CreateRendererView(getApplicationContext());
                remoteVideoUser.addView(remoteView, new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
                remoteView.setZOrderOnTop(true);
                remoteView.setZOrderMediaOverlay(true);

                rtcEngine.enableVideo();
                int successCode = rtcEngine.setupRemoteVideo(new VideoCanvas(remoteView, VideoCanvas.RENDER_MODE_ADAPTIVE, uid));

                if (successCode < 0) {
                    new android.os.Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            rtcEngine.setupRemoteVideo(new VideoCanvas(remoteView, VideoCanvas.RENDER_MODE_ADAPTIVE, uid));
                            remoteView.invalidate();
                        }
                    }, 500);
                }


                if (remoteUserView != null && CALLING_TYPE_VIDEO == mCallingType) {
                    remoteUserView.findViewById(R.id.remote_user_voice_container).setVisibility(View.GONE);
                } else {
                    remoteUserView.findViewById(R.id.remote_user_voice_container).setVisibility(View.VISIBLE);
                }

                // app hints before you join
                TextView appNotification = (TextView) findViewById(R.id.app_notification);
                appNotification.setText("");
                setRemoteUserViewVisibility(true);
            }
        });

    }

    public synchronized void onUserJoined(final int uid, int elapsed) {

        log("onUserJoined: uid: " + uid);

        View existedUser = mRemoteUserContainer.findViewById(Math.abs(uid));
        if (existedUser != null) {
            // user view already added
            return;
        }

        runOnUiThread(new Runnable() {
            @Override
            public void run() {


                // Handle the case onFirstRemoteVideoDecoded() is called before onUserJoined()
                View singleRemoteUser = mRemoteUserContainer.findViewById(Math.abs(uid));
                if (singleRemoteUser != null) {
                    return;
                }

                LayoutInflater layoutInflater = getLayoutInflater();
                singleRemoteUser = layoutInflater.inflate(R.layout.viewlet_remote_user, null);
                singleRemoteUser.setId(Math.abs(uid));

                TextView username = (TextView) singleRemoteUser.findViewById(R.id.remote_user_name);
                username.setText(String.valueOf(uid));

                mRemoteUserContainer.addView(singleRemoteUser, new LinearLayout.LayoutParams(mRemoteUserViewWidth, mRemoteUserViewWidth));


                // app hints before you join
                TextView appNotification = (TextView) findViewById(R.id.app_notification);
                appNotification.setText("");
                setRemoteUserViewVisibility(true);

            }
        });


    }

    public void onUserOffline(final int uid) {

        log("onUserOffline: uid: " + uid);

        if(isFinishing()){
            return;
        }

        if(mRemoteUserContainer==null){
            return;
        }

        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                View userViewToRemove = mRemoteUserContainer.findViewById(Math.abs(uid));
                mRemoteUserContainer.removeView(userViewToRemove);

                // no joined users any more
                if (mRemoteUserContainer.getChildCount() == 0) {
                    setRemoteUserViewVisibility(false);
                    TextView appNotification = (TextView) findViewById(R.id.app_notification);
                    appNotification.setText(R.string.room_prepare);
                }
            }
        });


    }


    @Override
    public void finish() {

        if(alertDialog!=null){
            alertDialog.dismiss();
        }

        super.finish();
    }

    @Override
    public void onLeaveChannel(IRtcEngineEventHandler.RtcStats stats) {
        try {
            finish();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void onUserMuteVideo(final int uid, final boolean muted) {

        log("onUserMuteVideo uid: " + uid + ", muted: " + muted);

        if(isFinishing()){
            return;
        }

        if(mRemoteUserContainer==null){
            return;
        }

        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                View remoteView = mRemoteUserContainer.findViewById(Math.abs(uid));
                remoteView.findViewById(R.id.remote_user_voice_container).setVisibility(
                        (CALLING_TYPE_VOICE==mCallingType || (CALLING_TYPE_VIDEO==mCallingType && muted))
                                ? View.VISIBLE
                                : View.GONE);
                remoteView.invalidate();
            }
        });

    }

    @Override
    public synchronized void onError(int err) {


        if(isFinishing()){
            return;
        }


        // incorrect vendor key
        if(101==err){

            runOnUiThread(new Runnable() {
                @Override
                public void run() {

                   if(alertDialog!=null){
                       return;
                   }

                   alertDialog= new AlertDialog.Builder(ChannelActivity.this).setCancelable(false)
                            .setMessage(getString(R.string.error_101))
                            .setPositiveButton(getString(R.string.error_confirm), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    // Go to login
                                    Intent toLogin = new Intent(ChannelActivity.this, LoginActivity.class);
                                    toLogin.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                                    startActivity(toLogin);

                                    rtcEngine.leaveChannel();

                                }
                            }).setOnCancelListener(new DialogInterface.OnCancelListener() {
                               @Override
                               public void onCancel(DialogInterface dialogInterface) {
                                   dialogInterface.dismiss();
                               }
                           })
                           .create();

                    alertDialog.show();
                }
            });



        }

        // no network connection
        if (104 == err) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    TextView appNotification = (TextView) findViewById(R.id.app_notification);
                    appNotification.setText(R.string.network_error);
                }
            });
        }


    }

    public static String humanReadableByteCount(long bytes, boolean si) {
        int unit = si ? 1000 : 1024;
        if (bytes < unit) return bytes + " B";
        int exp = (int) (Math.log(bytes) / Math.log(unit));
        String pre = (si ? "kMGTPE" : "KMGTPE").charAt(exp - 1) + (si ? "" : "i");
        return String.format("%.1f %sB", bytes / Math.pow(unit, exp), pre);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ((AgoraApplication) getApplication()).setEngineEventHandlerActivity(null);
    }
}