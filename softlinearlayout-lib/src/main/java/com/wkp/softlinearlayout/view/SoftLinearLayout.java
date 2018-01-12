package com.wkp.softlinearlayout.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.transition.ChangeBounds;
import android.transition.TransitionManager;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;

import com.wkp.softlinearlayout.R;
import com.wkp.softlinearlayout.util.SPUtils;

/**
 * Created by user on 2017/11/10.
 */
@RequiresApi(api = Build.VERSION_CODES.KITKAT)
public class SoftLinearLayout extends LinearLayout {
    private static final int DEFAULT_CHILD_COUNT = 2;
    private static final int DEFAULT_SECOND_CHILD_HEIGHT = 740;
    private static final int DEFAULT_SECOND_CHILD_MIN_HEIGHT = 400;
    private static final int DEFAULT_SECOND_CHILD_MAX_HEIGHT = 800;
    private static final int DEFAULT_SECOND_CHILD_MID_HEIGHT = 600;
    private static final long DEFAULT_SHOW_SOFT_DURATION = 50;
    private static final long DEFAULT_TOGGLE_DURATION = 200;
    private static final String SP_KEY_SECOND_HEIGHT = "second_height";
    private int secondChildHeight = DEFAULT_SECOND_CHILD_HEIGHT;
    private int secondChildMinHeight = DEFAULT_SECOND_CHILD_MIN_HEIGHT;
    private int secondChildMaxHeight = DEFAULT_SECOND_CHILD_MAX_HEIGHT;
    private long showSoftDuration = DEFAULT_SHOW_SOFT_DURATION;
    private long toggleDuration = DEFAULT_TOGGLE_DURATION;
    private int firstChildHeight;
    private boolean secondChildState;
    private Context mContext;
    private OnToggleChangedListener mListener;
    private boolean mHasStatusBar = true;

    public SoftLinearLayout(Context context) {
        this(context, null);
    }

    public SoftLinearLayout(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SoftLinearLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        initAttr(attrs);
        init();
    }

    /**
     * 判断控件是否可见
     *
     * @param visibility
     */
    @Override
    protected void onWindowVisibilityChanged(int visibility) {
        super.onWindowVisibilityChanged(visibility);
        if (visibility == GONE) {
            SPUtils.put(mContext, SP_KEY_SECOND_HEIGHT, secondChildHeight);
        }
    }

    /**
     * 初始化属性
     *
     * @param attrs
     */
    private void initAttr(AttributeSet attrs) {
        TypedArray typedArray = mContext.obtainStyledAttributes(attrs, R.styleable.SoftLinearLayout);
        if (typedArray != null) {
            int minHeight = typedArray.getInteger(R.styleable.SoftLinearLayout_wkp_minHeight, DEFAULT_SECOND_CHILD_MIN_HEIGHT);
            int maxHeight = typedArray.getInteger(R.styleable.SoftLinearLayout_wkp_maxHeight, DEFAULT_SECOND_CHILD_MAX_HEIGHT);
            int showSoftDuration = typedArray.getInteger(R.styleable.SoftLinearLayout_wkp_showSoftDuration, (int) DEFAULT_SHOW_SOFT_DURATION);
            int toggleDuration = typedArray.getInteger(R.styleable.SoftLinearLayout_wkp_toggleDuration, (int) DEFAULT_TOGGLE_DURATION);
            boolean hasStatusBar = typedArray.getBoolean(R.styleable.SoftLinearLayout_wkp_hasStatusBar, true);
            setMinHeight(minHeight);
            setMaxHeight(maxHeight);
            setShowSoftAnimDuration(showSoftDuration);
            setToggleAnimDuration(toggleDuration);
            hasStatusBar(hasStatusBar);
            typedArray.recycle();
        }
    }

    /**
     * 初始化其他
     */
    private void init() {
        setOrientation(LinearLayout.VERTICAL);
        secondChildHeight = SPUtils.getInt(mContext, SP_KEY_SECOND_HEIGHT, DEFAULT_SECOND_CHILD_HEIGHT);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        firstChildHeight = getTotalHeight() - getTitleBarHeight();          //固定高度
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if (getChildCount() > DEFAULT_CHILD_COUNT) {
            throw new IllegalStateException("SoftLinearLayout can not has three or more children!");
        }
        int visibility = SoftLinearLayout.this.getVisibility();
        int divTopHeight = 0;
        int divBotHeight = 0;
        if (visibility == VISIBLE) {
            int topHeight = getVisibleHeight() - getTitleBarHeight();
            int bottomHeight = getTotalHeight() - getVisibleHeight();
            if (isSoftShowing()) {
                if (secondChildState && mListener != null) {
                    mListener.onToggleChanged(false);
                }
                secondChildState = false;
                divBotHeight = topHeight;
                divTopHeight = firstChildHeight - topHeight;
                setSecondChildHeight(bottomHeight);
                setTransition(showSoftDuration);
            } else {
                if (secondChildState) {
                    divBotHeight = topHeight - secondChildHeight;
                    divTopHeight = secondChildHeight;
                } else {
                    divBotHeight = topHeight;
                    divTopHeight = 0;
                }
                setTransition(toggleDuration);
            }
        }
        for (int i = 0; i < getChildCount(); i++) {
            View view = getChildAt(i);
            ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
            switch (i) {
                case 0:
                    view.layout(l, t - divTopHeight, r, t - divTopHeight + firstChildHeight);
                    if (layoutParams.height != firstChildHeight) {
                        layoutParams.height = firstChildHeight;
                        view.setLayoutParams(layoutParams);
                    }
                    break;
                case 1:
                    view.layout(l, t + divBotHeight, r, t + divBotHeight + secondChildHeight);
                    if (layoutParams.height != secondChildHeight) {
                        layoutParams.height = secondChildHeight;
                        view.setLayoutParams(layoutParams);
                    }
                    break;
            }
        }
    }

    /**
     * 设置第二个孩子高度
     *
     * @param height
     */
    private void setSecondChildHeight(int height) {
        secondChildHeight = height < secondChildMinHeight ? secondChildMinHeight : (height > secondChildMaxHeight ? secondChildMaxHeight : height);
    }

    /**
     * 获取状态栏高度
     *
     * @return
     */
    private int getStatusBarHeight() {
        Rect outRect = new Rect();
        getWindowVisibleDisplayFrame(outRect);
        return outRect.top;
    }

    /**
     * 获取ToolBar高度（包含状态栏高度）
     *
     * @return
     */
    private int getTitleBarHeight() {
        View rootView = getRootView();
        if (rootView != null) {
            View window = rootView.findViewById(Window.ID_ANDROID_CONTENT);
            if (window != null) {
                return window.getTop() + (mHasStatusBar ? getStatusBarHeight() : 0);
            }
        }
        return mHasStatusBar ? getStatusBarHeight() : 0;
    }

    /**
     * 获取窗口可见区域高度（包含状态栏和ToolBar高度）
     *
     * @return
     */
    private int getVisibleHeight() {
        Rect outRect = new Rect();
        getWindowVisibleDisplayFrame(outRect);
        return outRect.bottom;
    }

    /**
     * 获取屏幕高度（不包含虚拟键高度,包含状态栏和ToolBar高度）
     *
     * @return
     */
    private int getTotalHeight() {
        return getResources().getDisplayMetrics().heightPixels;
    }

    /**
     * 隐藏软键盘
     */
    private void hideSoft() {
        InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    /**
     * 设置动画时长
     *
     * @param duration
     */
    private void setTransition(long duration) {
        ChangeBounds changeBounds = new ChangeBounds();
        changeBounds.setDuration(duration);
        TransitionManager.beginDelayedTransition(SoftLinearLayout.this, changeBounds);
    }

    /**
     * 判断软键盘当前是否显示
     *
     * @return
     */
    public boolean isSoftShowing() {
        return getTotalHeight() - getVisibleHeight() != 0;
    }

    /**
     * 设置当前状态
     *
     * @param state
     */
    public void toggle(boolean state) {
        if (secondChildState != state) {
            if (mListener != null) {
                mListener.onToggleChanged(state);
            }
        }
        secondChildState = state;
        if (state) {
            if (isSoftShowing()) {
                hideSoft();
            } else {
                requestLayout();
            }
        } else {
            requestLayout();
        }
    }

    /**
     * 设置当前反状态
     */
    public void toggle() {
        toggle(!secondChildState);
    }

    /**
     * 获取当前状态
     *
     * @return
     */
    public boolean isToggle() {
        return secondChildState;
    }

    /**
     * 是否有沉浸式状态栏（默认true）
     * @param hasStatusBar
     * @return
     */
    public SoftLinearLayout hasStatusBar(boolean hasStatusBar) {
        mHasStatusBar = hasStatusBar;
        return this;
    }

    /**
     * 设置可变高度的极限小高度
     *
     * @param minHeight
     */
    public SoftLinearLayout setMinHeight(int minHeight) {
        secondChildMinHeight = minHeight < DEFAULT_SECOND_CHILD_MIN_HEIGHT ? DEFAULT_SECOND_CHILD_MIN_HEIGHT : (minHeight > DEFAULT_SECOND_CHILD_MID_HEIGHT ?
                DEFAULT_SECOND_CHILD_MID_HEIGHT : minHeight);
        return this;
    }

    /**
     * 设置可变高度的极限大高度
     *
     * @param maxHeight
     */
    public SoftLinearLayout setMaxHeight(int maxHeight) {
        secondChildMaxHeight = maxHeight < DEFAULT_SECOND_CHILD_MID_HEIGHT ? DEFAULT_SECOND_CHILD_MID_HEIGHT : (maxHeight > DEFAULT_SECOND_CHILD_MAX_HEIGHT ?
                DEFAULT_SECOND_CHILD_MAX_HEIGHT : maxHeight);
        return this;
    }

    /**
     * 设置软键盘显示阶段的控件动画时长（需自己调到与键盘动画同步，但大部分输入法键盘动画时长不一致，最小0，最大200）
     * @param duration
     * @return
     */
    public SoftLinearLayout setShowSoftAnimDuration(long duration) {
        showSoftDuration = duration < 0 ? 0 : (duration > 200 ? 200 : duration);
        return this;
    }

    /**
     * 设置开关阶段的控件动画时长（最小0，最大500）
     * @param duration
     * @return
     */
    public SoftLinearLayout setToggleAnimDuration(long duration) {
        toggleDuration = duration < 0 ? 0 : (duration > 500 ? 500 : duration);
        return this;
    }

    /**
     * 状态改变监听
     */
    public interface OnToggleChangedListener {
        void onToggleChanged(boolean isToggle);
    }

    /**
     * 设置状态改变监听
     *
     * @param listener
     */
    public void setOnToggleChangedListener(OnToggleChangedListener listener) {
        mListener = listener;
    }
}
