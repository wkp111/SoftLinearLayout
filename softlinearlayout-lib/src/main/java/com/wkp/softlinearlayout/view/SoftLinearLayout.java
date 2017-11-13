package com.wkp.softlinearlayout.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.support.annotation.Nullable;
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

public class SoftLinearLayout extends LinearLayout {
    private static final int DEFAULT_CHILD_COUNT = 2;
    private static final int DEFAULT_SECOND_CHILD_HEIGHT = 740;
    private static final int DEFAULT_SECOND_CHILD_MIN_HEIGHT = 400;
    private static final int DEFAULT_SECOND_CHILD_MAX_HEIGHT = 800;
    private static final int DEFAULT_SECOND_CHILD_MID_HEIGHT = 600;
    private static final String SP_KEY_SECOND_HEIGHT = "second_height";
    private int secondChildHeight = DEFAULT_SECOND_CHILD_HEIGHT;
    private int secondChildMinHeight = DEFAULT_SECOND_CHILD_MIN_HEIGHT;
    private int secondChildMaxHeight = DEFAULT_SECOND_CHILD_MAX_HEIGHT;
    private int firstChildHeight;
    private boolean secondChildState;
    private Context mContext;
    private OnToggleChangedListener mListener;

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
     * @param visibility
     */
    @Override
    protected void onWindowVisibilityChanged(int visibility) {
        super.onWindowVisibilityChanged(visibility);
        if (visibility == GONE) {
            SPUtils.put(mContext,SP_KEY_SECOND_HEIGHT,secondChildHeight);
        }
    }

    /**
     * 初始化属性
     * @param attrs
     */
    private void initAttr(AttributeSet attrs) {
        TypedArray typedArray = mContext.obtainStyledAttributes(attrs, R.styleable.SoftLinearLayout);
        if (typedArray != null) {
            int minHeight = typedArray.getInteger(R.styleable.SoftLinearLayout_wkp_minHeight, DEFAULT_SECOND_CHILD_MIN_HEIGHT);
            int maxHeight = typedArray.getInteger(R.styleable.SoftLinearLayout_wkp_maxHeight, DEFAULT_SECOND_CHILD_MAX_HEIGHT);
            setMinHeight(minHeight);
            setMaxHeight(maxHeight);
            typedArray.recycle();
        }
    }

    /**
     * 初始化其他
     */
    private void init() {
        setOrientation(LinearLayout.VERTICAL);
        firstChildHeight = getTotalHeight() - getTitleBarHeight();
        secondChildHeight = SPUtils.getInt(mContext, SP_KEY_SECOND_HEIGHT, DEFAULT_SECOND_CHILD_HEIGHT);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if (getChildCount() > DEFAULT_CHILD_COUNT) {
            throw new IllegalStateException("SoftLinearLayout can not has three or more children!");
        }
        int visibility = SoftLinearLayout.this.getVisibility();
        if (visibility == VISIBLE) {
            int topHeight = getVisibleHeight() - getTitleBarHeight();
            int bottomHeight = getTotalHeight() - getVisibleHeight();
            if (isSoftShowing()) {
                if (secondChildState && mListener != null) {
                    mListener.onToggleChanged(false);
                }
                secondChildState = false;
                firstChildHeight = topHeight;
                setSecondChildHeight(bottomHeight);
            } else {
                if (secondChildState) {
                    firstChildHeight = topHeight - secondChildHeight;
                } else {
                    firstChildHeight = topHeight;
                }
            }
        }
        for (int i = 0; i < getChildCount(); i++) {
            View view = getChildAt(i);
            ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
            switch (i) {
                case 0:
                    view.layout(l, t, r, t + firstChildHeight);
                    if (layoutParams.height != firstChildHeight) {
                        layoutParams.height = firstChildHeight;
                        view.setLayoutParams(layoutParams);
                    }
                    break;
                case 1:
                    view.layout(l, t + firstChildHeight, r, t + firstChildHeight + secondChildHeight);
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
                return window.getTop() + getStatusBarHeight();
            }
        }
        return getStatusBarHeight();
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
        if (secondChildState != state && mListener != null) {
            mListener.onToggleChanged(state);
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
     * @return
     */
    public boolean isToggle() {
        return secondChildState;
    }

    /**
     * 设置可变高度的极限小高度
     * @param minHeight
     */
    public void setMinHeight(int minHeight) {
        secondChildMinHeight = minHeight < DEFAULT_SECOND_CHILD_MIN_HEIGHT ? DEFAULT_SECOND_CHILD_MIN_HEIGHT : (minHeight > DEFAULT_SECOND_CHILD_MID_HEIGHT ?
                DEFAULT_SECOND_CHILD_MID_HEIGHT : minHeight);
    }

    /**
     * 设置可变高度的极限大高度
     * @param maxHeight
     */
    public void setMaxHeight(int maxHeight) {
        secondChildMaxHeight = maxHeight < DEFAULT_SECOND_CHILD_MID_HEIGHT ? DEFAULT_SECOND_CHILD_MID_HEIGHT : (maxHeight > DEFAULT_SECOND_CHILD_MAX_HEIGHT ?
                DEFAULT_SECOND_CHILD_MAX_HEIGHT : maxHeight);
    }

    /**
     * 状态改变监听
     */
    public interface OnToggleChangedListener{
        void onToggleChanged(boolean isToggle);
    }

    /**
     * 设置状态改变监听
     * @param listener
     */
    public void setOnToggleChangedListener(OnToggleChangedListener listener) {
        mListener = listener;
    }
}
