package cn.ac.ict.cana.modules.sound;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ac.ict.cana.R;

import static cn.ac.ict.cana.R.id.btn_start_recorder;
import static cn.ac.ict.cana.modules.sound.Constant.fileName;

/**
 * A simple {@link Fragment} subclass.
 */
public class FirstFragment extends Fragment {

    @BindView(btn_start_recorder)
    Button mBtnStartRecorder;
    SoundMainActivity mActivity;
    private MyCount myCount;

    public FirstFragment() {
        super();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_first, container, false);
        ButterKnife.bind(this, view);

        mActivity = (SoundMainActivity) getActivity();
        return view;
    }

    boolean isStartRecorder;
    @OnClick(btn_start_recorder)
    public void click() {
       // if (mBtnStartRecorder.isEnabled()) {
           // mActivity.prepareRecorder(fileName + "_", "first_");
        getFragmentManager().beginTransaction().replace(R.id.content, new SecondFragment()).commit();
       // }
        //setEnable(false);
       // setBtnText();
    }

    private void setBtnText() {
        isStartRecorder = !isStartRecorder;
        if (isStartRecorder) {
            myCount = new MyCount(30000, 1000);
            myCount.start();
            mBtnStartRecorder.setText("结束！");
            mBtnStartRecorder.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.freebie_2));
        } else {
            myCount.cancel();
            finishRecord();
            mBtnStartRecorder.setText("开始录音！");
            mBtnStartRecorder.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.freebie_4));
        }
    }


    private void setEnable(boolean enabled) {
        mBtnStartRecorder.setEnabled(enabled);
    }

    /**
     * 恢复状态并释放资源
     */
    private void release() {
        mBtnStartRecorder.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.freebie_4));
        mBtnStartRecorder.setText(getString(R.string.btn_start_record));
        setEnable(true);
        mActivity.releaseRecorder();
    }

    class MyCount extends CountDownTimer {

        public MyCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long l) {
            mBtnStartRecorder.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.freebie_2));
          //  mBtnStartRecorder.setText(String.format(Locale.CHINA, getString(R.string.sound_count_down), (l / 1000)));
        }

        @Override
        public void onFinish() {
            finishRecord();
        }
    }

    /**
     * 完成录音
     */
    private void finishRecord() {
        release();
        mActivity.showDialog(true);
    }


}