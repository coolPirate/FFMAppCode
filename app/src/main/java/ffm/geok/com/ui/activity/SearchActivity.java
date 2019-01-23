package ffm.geok.com.ui.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import ffm.geok.com.R;

public class SearchActivity extends AppCompatActivity {

    @BindView(R.id.iv_arrow)
    ImageView ivArrow;
    @BindView(R.id.tv_search_bg)
    TextView tvSearchBg;
    /*@BindView(R.id.tv_hint)
    EditText tvHint;*/
    @BindView(R.id.frame_bg)
    FrameLayout frameBg;
    @BindView(R.id.frame_content_bg)
    FrameLayout frameContentBg;
    @BindView(R.id.ll_search_bg)
    LinearLayout llSearchBg;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tv_search)
    TextView tvSearch;
    @BindView(R.id.abl_title)
    AppBarLayout ablTitle;
    @BindView(R.id.view_line)
    View viewLine;

    private boolean finishing;
    private float originX;

    float frameBgHeight = 0;
    float searchBgHeight = 0;

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
        tvSearchBg.getViewTreeObserver()
                .addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        tvSearchBg.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                        performEnterAnimation1();
                    }
                });
    }

    private void performEnterAnimation1() {
        float originY = getIntent().getIntExtra("y", 0);

        int location[] = new int[2];
        tvSearchBg.getLocationOnScreen(location);

        final float translateY = originY - (float) location[1];


        frameBgHeight = frameBg.getHeight();

        //放到前一个页面的位置
        tvSearchBg.setY(tvSearchBg.getY() + translateY);
        viewLine.setY(tvSearchBg.getY() + translateY);
        tvSearch.setY(tvSearchBg.getY() + (tvSearchBg.getHeight() - tvSearch.getHeight()) / 2);
        final ValueAnimator translateVa = ValueAnimator.ofFloat(tvSearchBg.getY(), tvSearchBg.getY() - 100);
        searchBgHeight = tvSearchBg.getY();
        translateVa.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                tvSearchBg.setY((Float) valueAnimator.getAnimatedValue());
                /*ViewGroup.LayoutParams linearParams = frameBg.getLayoutParams(); //取控件textView当前的布局参数
                linearParams.height = (int) (frameBgHeight - (searchBgHeight - (Float) valueAnimator.getAnimatedValue()) * 2);
                frameBg.setLayoutParams(linearParams);*/

                ivArrow.setY(tvSearchBg.getY() );
                tvSearch.setY(tvSearchBg.getY() );
            }
        });

        ValueAnimator scaleVa = ValueAnimator.ofFloat(1, 0.8f);
        scaleVa.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                tvSearchBg.setScaleX((Float) valueAnimator.getAnimatedValue());
            }
        });

        ValueAnimator alphaVa = ValueAnimator.ofFloat(0, 1f);
        alphaVa.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                frameContentBg.setAlpha((Float) valueAnimator.getAnimatedValue());
                tvSearch.setAlpha((Float) valueAnimator.getAnimatedValue());
                ivArrow.setAlpha((Float) valueAnimator.getAnimatedValue());
            }
        });
        ValueAnimator alphaVa2 = ValueAnimator.ofFloat(1f, 0);
        alphaVa2.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                toolbar.setAlpha((Float) valueAnimator.getAnimatedValue());
                ablTitle.setAlpha((Float) valueAnimator.getAnimatedValue());
            }
        });
        alphaVa.setDuration(500);
        alphaVa2.setDuration(300);
        translateVa.setDuration(500);
        scaleVa.setDuration(500);

        alphaVa.start();
        alphaVa2.start();
        translateVa.start();
        scaleVa.start();

    }

    private void performEnterAnimation() {
        initLocation();
        final float top = getResources().getDisplayMetrics().density * 20;
        final ValueAnimator translateVa = translateVa(tvSearchBg.getY(), top);
        final ValueAnimator scaleVa = scaleVa(1, 0.8f);
        final ValueAnimator alphaVa = alphaVa(0, 1f);
        originX = llSearchBg.getX();
        final float leftSpace = ivArrow.getRight() * 2;
        final ValueAnimator translateVaX = translateVax(originX, leftSpace);

        setDuration(translateVa, scaleVa, translateVaX, alphaVa);
        star(translateVa, scaleVa, translateVaX, alphaVa);
    }

    private void initLocation() {
        final float translateY = getTranslateY();
        //放到前一个页面的位置
        tvSearchBg.setY(tvSearchBg.getY() + translateY);
        //tvHint.setY(tvSearchBg.getY() + (tvSearchBg.getHeight() - tvHint.getHeight()) / 2);
        tvSearch.setY(tvSearchBg.getY() + (tvSearchBg.getHeight() - tvSearch.getHeight()) / 2);
    }

    private float getTranslateY() {
        float originY = getIntent().getIntExtra("y", 0);
        int[] location = new int[2];
        tvSearchBg.getLocationOnScreen(location);
        return originY - (float) location[1];
    }

    @Override
    public void onBackPressed() {
        if (!finishing) {
            finishing = true;
            performExitAnimation();
        }
    }

    private void performExitAnimation() {
        final float translateY = getTranslateY();
        final ValueAnimator translateVa = translateVa(tvSearchBg.getY(), tvSearchBg.getY() + translateY);
        final ValueAnimator scaleVa = scaleVa(0.8f, 1f);
        final ValueAnimator alphaVa = alphaVa(1f, 0f);
        exitListener(translateVa);
        final float currentX = tvSearchBg.getX();
        ValueAnimator translateVaX = translateVax(currentX, originX);

        setDuration(translateVa, scaleVa, translateVaX, alphaVa);
        star(translateVa, scaleVa, translateVaX, alphaVa);
    }

    @NonNull
    private ValueAnimator translateVax(float from, float to) {
        ValueAnimator translateVaX = ValueAnimator.ofFloat(from, to);
        translateVaX.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                Float value = (Float) animation.getAnimatedValue();
                //tvHint.setX(value);
            }
        });
        return translateVaX;
    }

    @NonNull
    private ValueAnimator translateVa(float from, float to) {
        ValueAnimator translateVa = ValueAnimator.ofFloat(from, to);
        translateVa.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                tvSearchBg.setY((Float) valueAnimator.getAnimatedValue());
                ivArrow.setY(
                        tvSearchBg.getY() + (tvSearchBg.getHeight() - ivArrow.getHeight()) / 2);
                //tvHint.setY(tvSearchBg.getY() + (tvSearchBg.getHeight() - tvHint.getHeight()) / 2);
                tvSearch.setY(
                        tvSearchBg.getY() + (tvSearchBg.getHeight() - tvSearch.getHeight()) / 2);
            }
        });
        return translateVa;
    }

    @NonNull
    private ValueAnimator scaleVa(float from, float to) {
        ValueAnimator scaleVa = ValueAnimator.ofFloat(from, to);
        scaleVa.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                tvSearchBg.setScaleX((Float) valueAnimator.getAnimatedValue());
            }
        });
        return scaleVa;
    }

    @NonNull
    private ValueAnimator alphaVa(float from, float to) {
        ValueAnimator alphaVa = ValueAnimator.ofFloat(from, to);
        alphaVa.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                frameContentBg.setAlpha((Float) valueAnimator.getAnimatedValue());
                tvSearch.setAlpha((Float) valueAnimator.getAnimatedValue());
                ivArrow.setAlpha((Float) valueAnimator.getAnimatedValue());
            }
        });
        return alphaVa;
    }

    private void exitListener(ValueAnimator translateVa) {
        translateVa.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                finish();
                overridePendingTransition(0, 0);
            }
        });
    }

    private void setDuration(ValueAnimator translateVa, ValueAnimator scaleVa,
                             ValueAnimator translateVaX, ValueAnimator alphaVa) {
        alphaVa.setDuration(350);
        translateVa.setDuration(350);
        scaleVa.setDuration(350);
        translateVaX.setDuration(350);
    }

    private void star(ValueAnimator translateVa, ValueAnimator scaleVa, ValueAnimator translateVaX,
                      ValueAnimator alphaVa) {
        alphaVa.start();
        translateVa.start();
        scaleVa.start();
        translateVaX.start();
    }

}
