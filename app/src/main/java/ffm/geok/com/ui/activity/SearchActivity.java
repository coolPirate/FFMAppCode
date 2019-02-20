package ffm.geok.com.ui.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import ffm.geok.com.R;

public class SearchActivity extends AppCompatActivity {

    @BindView(R.id.iv_arrow)
    ImageView mArrowImg;
    @BindView(R.id.tv_search_bg)
    TextView mSearchBGTxt;
    @BindView(R.id.tv_hint)
    EditText mHintTxt;
    @BindView(R.id.tv_search)
    TextView mSearchTxt;
    @BindView(R.id.frame_bg)
    FrameLayout frameBg;
    @BindView(R.id.frame_content_bg)
    FrameLayout mContentFrame;

    private boolean finishing;
    private float originX;
    float searchBgHeight = 0;
    float frameBgHeight = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);
        initView();
        execute();
    }

    private void initView() {
    }

    private void execute() {
        mSearchBGTxt.getViewTreeObserver()
                .addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override public void onGlobalLayout() {
                        mSearchBGTxt.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                        performEnterAnimation();
                    }
                });
        mArrowImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    private void performEnterAnimation() {
        initLocation();
        final float top = getResources().getDisplayMetrics().density * 20;
        frameBgHeight = frameBg.getHeight();
        searchBgHeight = mSearchBGTxt.getY();


        final ValueAnimator translateVa = translateVa(mSearchBGTxt.getY(), top);
        final ValueAnimator translateBg = translateBg(mSearchBGTxt.getY(), mSearchBGTxt.getY() - 100);
        final ValueAnimator scaleVa = scaleVa(1, 0.8f);
        final ValueAnimator alphaVa = alphaVa(0, 1f);
        originX = mHintTxt.getX();
        final float leftSpace = mArrowImg.getRight() * 2;
        final ValueAnimator translateVaX = translateVax(originX, leftSpace);

        setDuration(translateVa, scaleVa, translateVaX, alphaVa,translateBg);
        star(translateVa, scaleVa, translateVaX, alphaVa,translateBg);

        final ValueAnimator translateBg1 = ValueAnimator.ofFloat(mSearchBGTxt.getY(), mSearchBGTxt.getY() - 100);
        translateBg1.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                mSearchBGTxt.setY((Float) valueAnimator.getAnimatedValue());
                ViewGroup.LayoutParams linearParams = frameBg.getLayoutParams(); //取控件textView当前的布局参数
                linearParams.height = (int) (frameBgHeight-(searchBgHeight-(Float) valueAnimator.getAnimatedValue())*2);
                frameBg.setLayoutParams(linearParams);
                /*mArrowImg.setY(mSearchBGTxt.getY() + (mSearchBGTxt.getHeight() - mArrowImg.getHeight()) / 2);
                mHintTxt.setY(mSearchBGTxt.getY() + (mSearchBGTxt.getHeight() - mHintTxt.getHeight()) / 2);
                mSearchTxt.setY(mSearchBGTxt.getY() + (mSearchBGTxt.getHeight() - mSearchTxt.getHeight()) / 2);*/
            }
        });
        translateBg1.setDuration(350);
        translateBg1.start();

    }

    private void initLocation() {
        final float translateY = getTranslateY();
        //放到前一个页面的位置
        mSearchBGTxt.setY(mSearchBGTxt.getY() + translateY);
        mHintTxt.setY(mSearchBGTxt.getY() + (mSearchBGTxt.getHeight() - mHintTxt.getHeight()) / 2);
        mSearchTxt.setY(mSearchBGTxt.getY() + (mSearchBGTxt.getHeight() - mSearchTxt.getHeight()) / 2);
    }

    private float getTranslateY() {
        float originY = getIntent().getIntExtra("y", 0);
        int[] location = new int[2];
        mSearchBGTxt.getLocationOnScreen(location);
        return originY - (float) location[1];
    }

    @Override public void onBackPressed() {
        if (!finishing) {
            finishing = true;
            performExitAnimation();
        }
    }

    private void performExitAnimation() {
        final float translateY = getTranslateY();
        final ValueAnimator translateVa = translateVa(mSearchBGTxt.getY(), mSearchBGTxt.getY() + translateY);
        final ValueAnimator translateBg = translateBg(mSearchBGTxt.getY(), mSearchBGTxt.getY() - 100);
        final ValueAnimator scaleVa = scaleVa(0.8f, 1f);
        final ValueAnimator alphaVa = alphaVa(1f, 0f);
        exitListener(translateVa);
        final float currentX = mHintTxt.getX();
        ValueAnimator translateVaX = translateVax(currentX, originX);

        setDuration(translateVa, scaleVa, translateVaX, alphaVa,translateBg);
        star(translateVa, scaleVa, translateVaX, alphaVa,translateBg);


    }

    @NonNull private ValueAnimator translateBg(float from,float to){
        ValueAnimator translateVaX = ValueAnimator.ofFloat(from, to);
        translateVaX.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override public void onAnimationUpdate(ValueAnimator animation) {
                mSearchBGTxt.setY((Float) animation.getAnimatedValue());
                ViewGroup.LayoutParams linearParams = frameBg.getLayoutParams(); //取控件textView当前的布局参数
                linearParams.height = (int) (frameBgHeight-(searchBgHeight-(Float) animation.getAnimatedValue())*2);
                frameBg.setLayoutParams(linearParams);
            }
        });
        return translateVaX;

    }

    @NonNull private ValueAnimator translateVax(float from, float to) {
        ValueAnimator translateVaX = ValueAnimator.ofFloat(from, to);
        translateVaX.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override public void onAnimationUpdate(ValueAnimator animation) {
                Float value = (Float) animation.getAnimatedValue();
                mHintTxt.setX(value);
            }
        });
        return translateVaX;
    }

    @NonNull private ValueAnimator translateVa(float from, float to) {
        ValueAnimator translateVa = ValueAnimator.ofFloat(from, to);
        translateVa.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override public void onAnimationUpdate(ValueAnimator valueAnimator) {
                mSearchBGTxt.setY((Float) valueAnimator.getAnimatedValue());
                mArrowImg.setY(
                        mSearchBGTxt.getY() + (mSearchBGTxt.getHeight() - mArrowImg.getHeight()) / 2);
                mHintTxt.setY(mSearchBGTxt.getY() + (mSearchBGTxt.getHeight() - mHintTxt.getHeight()) / 2);
                mSearchTxt.setY(
                        mSearchBGTxt.getY() + (mSearchBGTxt.getHeight() - mSearchTxt.getHeight()) / 2);
            }
        });
        return translateVa;
    }

    @NonNull private ValueAnimator scaleVa(float from, float to) {
        ValueAnimator scaleVa = ValueAnimator.ofFloat(from, to);
        scaleVa.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override public void onAnimationUpdate(ValueAnimator valueAnimator) {
                mSearchBGTxt.setScaleX((Float) valueAnimator.getAnimatedValue());
            }
        });
        return scaleVa;
    }

    @NonNull
    private ValueAnimator alphaVa(float from, float to) {
        ValueAnimator alphaVa = ValueAnimator.ofFloat(from, to);
        alphaVa.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override public void onAnimationUpdate(ValueAnimator valueAnimator) {
                mContentFrame.setAlpha((Float) valueAnimator.getAnimatedValue());
                mSearchTxt.setAlpha((Float) valueAnimator.getAnimatedValue());
                mArrowImg.setAlpha((Float) valueAnimator.getAnimatedValue());
            }
        });
        return alphaVa;
    }

    private void exitListener(ValueAnimator translateVa) {
        translateVa.addListener(new AnimatorListenerAdapter() {
            @Override public void onAnimationEnd(Animator animation) {
                finish();
                overridePendingTransition(0, 0);
            }
        });
    }

    private void setDuration(ValueAnimator translateVa, ValueAnimator scaleVa,
                             ValueAnimator translateVaX, ValueAnimator alphaVa,ValueAnimator translateBg) {
        alphaVa.setDuration(350);
        translateVa.setDuration(350);
        scaleVa.setDuration(350);
        translateVaX.setDuration(350);
        translateBg.setDuration(350);
    }

    private void star(ValueAnimator translateVa, ValueAnimator scaleVa, ValueAnimator translateVaX,
                      ValueAnimator alphaVa,ValueAnimator translateBg) {
        alphaVa.start();
        translateVa.start();
        scaleVa.start();
        translateVaX.start();
        translateBg.start();
    }


}
