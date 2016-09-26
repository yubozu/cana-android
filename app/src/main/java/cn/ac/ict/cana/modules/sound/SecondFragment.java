package cn.ac.ict.cana.modules.sound;


import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ac.ict.cana.R;

import static cn.ac.ict.cana.modules.sound.Constant.fileName;

/**
 * A simple {@link Fragment} subclass.
 */
public class SecondFragment extends Fragment {

    @BindView(R.id.btn_start_recorder)
    Button mBtnStartRecorder;

    private SoundMainActivity mActivity;

    public SecondFragment() {
        super();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_second, container, false);
        ButterKnife.bind(this, view);
        mActivity = (SoundMainActivity) getActivity();
        return view;
    }

    @OnClick(R.id.btn_start_recorder)
    public void click() {
        if (mBtnStartRecorder.isEnabled()) {
            mActivity.prepareRecorder(fileName + "_", "第二段录音_");
        }
        setEnable(false);
        new MyCount(10000, 1000).start();
    }

    private void setEnable(boolean enabled) {
        mBtnStartRecorder.setEnabled(enabled);
    }

    /**
     * 恢复状态并释放资源
     */
    private void release() {
        mBtnStartRecorder.setText("开始录音!");
        setEnable(true);
        mActivity.releaseRecorder();
    }

    class MyCount extends CountDownTimer {

        public MyCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long l) {
            mBtnStartRecorder.setText("录音倒计时:" + (l / 1000));
        }

        @Override
        public void onFinish() {
            release();
            mActivity.showDialog(true);
        }
    }
}
