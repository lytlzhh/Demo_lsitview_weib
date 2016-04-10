package com.example.llw.demo_lsitview_weib;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by llw on 2016/4/10.
 */
public class Mylistview extends ListView implements AbsListView.OnScrollListener {
    private View header;
    private LayoutInflater layoutInflater;

    private int height;//获取headerview布局的高度


    private final int NONE = 0;
    private final int PULL = 1;
    private final int REFRSH = 2;
    private final int REFESHING = 3;

    int firstVisibleItem;
    int scrollState;
    boolean is_state;
    private int state;


    IRefreshlistener iRfreshlistenner;
    int startY;

    public Mylistview(Context context) {
        super(context);
        myinitview(context);
    }

    public Mylistview(Context context, AttributeSet attrs) {
        super(context, attrs);
        myinitview(context);
    }

    public Mylistview(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        myinitview(context);
    }

    public void myinitview(Context context) {
        layoutInflater = LayoutInflater.from(context);
        header = layoutInflater.inflate(R.layout.headerview, null);

        setMeasureView(header);
        height = header.getMeasuredHeight();
        //取height的负值，作为头部布局的高度.即隐藏
        top_header_view(-height);

        this.addHeaderView(header);
        this.setOnScrollListener(this);
    }

    public void setMeasureView(View view) {
        ViewGroup.LayoutParams params = view.getLayoutParams();

        if (params == null) {
            params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }
        int width = ViewGroup.getChildMeasureSpec(0, 0, params.width);

        int headerview_height;
        int tempheight = params.height;

        if (tempheight > 0) {
            headerview_height = MeasureSpec.makeMeasureSpec(tempheight, MeasureSpec.EXACTLY);
        } else {
            headerview_height = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
        }
        view.measure(width, headerview_height);

    }

    public void top_header_view(int len) {
        header.setPadding(header.getPaddingLeft(), len, header.getPaddingRight(), header.getPaddingBottom());
        //对头部布局设置内边距,在这里的paddingTop设置为了负值，故会隐藏

        //设置隐藏
        header.invalidate();

    }


    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        this.scrollState = scrollState;
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        this.firstVisibleItem = firstVisibleItem;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (firstVisibleItem == 0) {
                    is_state = true;
                    startY = (int) ev.getY();

                }
                break;
            case MotionEvent.ACTION_UP:
                if (state == REFRSH) {
                    state = REFESHING;
                    //加载信息
                    RefreshViewByState();
                    iRfreshlistenner.onRefresh();
                } else if (state == PULL) {
                    state = NONE;
                    is_state = false;
                    RefreshViewByState();
                }
                break;
            case MotionEvent.ACTION_MOVE:
                onMove(ev);
                break;
        }
        return super.onTouchEvent(ev);
    }

    public void onMove(MotionEvent ev) {
        if (!is_state) {
            return;
        }
        int LocationY = (int) ev.getY();
        int temp = LocationY - startY;
        int toppadding = temp - height;

        switch (state) {
            case NONE:
                if (temp > 0) {
                    state = PULL;
                    //刷新
                    RefreshViewByState();

                }
                break;
            case PULL:
                top_header_view(toppadding);
                if (temp > height + 30 && scrollState == SCROLL_STATE_TOUCH_SCROLL) {
                    state = REFRSH;
                    RefreshViewByState();
                }
                break;
            case REFRSH:
                top_header_view(toppadding);
                if (temp < height + 30) {
                    state = PULL;
                    RefreshViewByState();
                } else if (temp <= 0) {
                    state = NONE;
                    is_state = false;
                    RefreshViewByState();
                }
                break;
            case REFESHING:

                break;

        }
    }

    public void RefreshViewByState() {
        final TextView textView = (TextView) header.findViewById(R.id.text_one);
        textView.setTextSize(20);
        final ImageView imageView = (ImageView) header.findViewById(R.id.imageview_id);
        final ProgressBar progressBar = (ProgressBar) header.findViewById(R.id.progress_id);

        //设置箭头动画
        RotateAnimation rotateAnimation = new RotateAnimation(0, 180, RotateAnimation.RELATIVE_TO_SELF, 0.5f,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f);//???????
        rotateAnimation.setDuration(500);
        rotateAnimation.setFillAfter(true);

        RotateAnimation rotateAnimation1 = new RotateAnimation(180, 0, RotateAnimation.RELATIVE_TO_SELF, 0.5f,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f);
        rotateAnimation1.setDuration(500);
        rotateAnimation1.setFillAfter(true);


        switch (state) {
            case NONE:
                imageView.clearAnimation();
                top_header_view(-height);//正常情况下隐藏header
                break;
            case PULL:
                //下拉时箭头显示，进度条隐藏,提示信息
                imageView.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
                textView.setText("下拉刷新了!!");

                imageView.clearAnimation();
                imageView.setAnimation(rotateAnimation1);
                break;
            case REFRSH:
                //达到释放状态的一瞬间时：箭头显示，进度条隐藏，提示信息
                imageView.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
                textView.setText("松开刷新!!");

                imageView.clearAnimation();
                imageView.setAnimation(rotateAnimation);
                break;
            case REFESHING:
                top_header_view(height);//正在刷新时有一个固定的高度

                //释放刷新时，箭头隐藏，进度条显示，提示信息
                imageView.setVisibility(View.GONE);
                progressBar.setVisibility(View.VISIBLE);
                textView.setText("正在刷新!!!!!!!!!!");
                imageView.clearAnimation();
                break;
        }
    }

    //???????????????????????????????????????
    public void setInterface(IRefreshlistener iRfreshlistenner) {
        this.iRfreshlistenner = iRfreshlistenner;
    }

    //数据刷新接口  ??????????????????????????????????????????????
    public interface IRefreshlistener {
        public void onRefresh();
    }

    //用于刷新
    public void RefreshComplete() {
        state = NONE;//刷新完成后变成正常状态
        is_state = false;
        RefreshViewByState();//刷新界面
        TextView lastUpdate_time = (TextView) header.findViewById(R.id.text_two);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyy年mm月dd日hh时 hh::mm::ss");
        Date date = new Date(System.currentTimeMillis());
        String time = simpleDateFormat.format(date);
        lastUpdate_time.setText(time);

    }


}
