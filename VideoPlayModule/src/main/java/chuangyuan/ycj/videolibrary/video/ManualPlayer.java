package chuangyuan.ycj.videolibrary.video;

import android.app.Activity;
import android.net.Uri;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;

import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.util.Util;
import java.util.List;
import chuangyuan.ycj.videolibrary.R;
import chuangyuan.ycj.videolibrary.widget.VideoPlayerView;
/**
 * Created by yangc on 2017/2/27.
 * E-Mail:1007181167@qq.com
 * Description： 手动控制播放播放器
 */
public class ManualPlayer extends GestureVideoPlayer {
    public static final String TAG = "ManualPlayer";
    private boolean isLoad = false;//已经加载
   private ImageButton exoBtn,exoPause;
    private View.OnTouchListener onTouchListener;
    public ManualPlayer(@NonNull Activity activity, @NonNull VideoPlayerView playerView) {
        super(activity, playerView);
        intiView();
    }
    public ManualPlayer(@NonNull Activity activity,@IdRes int reId) {
        super(activity, reId);
        exoBtn = (ImageButton) mPlayerView.findViewById(R.id.exo_play);
        intiView();
    }
    private  void intiView() {
        exoBtn = (ImageButton) mPlayerView.findViewById(R.id.exo_play);
        exoPause=(ImageButton) mPlayerView.findViewById(R.id.exo_pause);
        mPlayerView.getPreviewImage().setVisibility(View.VISIBLE);
        onTouchListener=new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction()==MotionEvent.ACTION_UP) {
                    isLoad = true;
                    VideoPlayerManager.getInstance().setCurrentVideoPlayer(ManualPlayer.this);
                    mPlayerView.getPreviewImage().setVisibility(View.GONE);
                    mPlayerView.getPlayerView().setControllerHideOnTouch(true);
                    createPlayers();
                    hslHideView();
                    registerReceiverNet();
                    exoBtn.setOnTouchListener(null);
                }
                return false;
            }
        };
        exoBtn.setOnTouchListener(onTouchListener);
    }
    @Override
    public void setPlayUri(@NonNull Uri uri) {
        exoBtn.setOnTouchListener(onTouchListener);
        MediaSourceBuilder.getInstance().setMediaSourceUri(activity.getApplicationContext(),uri);
        createPlayersNo();
    }
    @Override
    public void setPlaySwitchUri(@NonNull List<String> videoUri, @NonNull List<String> name, int index) {
        exoBtn.setOnTouchListener(onTouchListener);
        this.videoUri=videoUri;
        mPlayerViewListener.showSwitchName(name.get(index));
        MediaSourceBuilder.getInstance().setMediaSourceUri(activity.getApplicationContext(),Uri.parse(videoUri.get(index)));
        createPlayersNo();
    }
    @Override
    public void setPlayUri(@NonNull String firstVideoUri, @NonNull String secondVideoUri) {
        exoBtn.setOnTouchListener(onTouchListener);
        MediaSourceBuilder.getInstance().setMediaSourceUri(activity.getApplicationContext(), firstVideoUri, secondVideoUri);
        createPlayersNo();
    }
    @Override
    public void onResume() {
        if ((Util.SDK_INT <= 23 || player == null) && isLoad) {
            createPlayers();
        } else {
            createPlayersPlay();
        }
    }

    @Override
    public boolean onBackPressed() {
        return super.onBackPressed();
    }
    /**
     重置
     * **/
    public   void reset(){
        exoBtn.setVisibility(View.VISIBLE);
        exoPause.setVisibility(View.GONE);
        exoBtn.setOnTouchListener(onTouchListener);
        if (getPlayerView()!=null){
            getPlayerView().onDestroy();
        }
           releasePlayers();
    }
}
