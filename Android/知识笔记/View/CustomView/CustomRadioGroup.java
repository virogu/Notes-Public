package com.android.settings.util;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.android.internal.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Author iDste-Zhangzy
 * Date 2018/4/3
 * Description
 */

public class CustomRadioGroup extends RadioGroup {
    private final static String TAG = "CustomRadioGroup";

    private OnCheckedChangeListener mOnCheckedChangeListener;
    private int mCheckedId = View.NO_ID;

    private int radioIndex = 0;

    private List<RadioButton> radios = new ArrayList<>();

    public CustomRadioGroup(Context context) {
        super(context);
    }

    public CustomRadioGroup(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray attributes = context.obtainStyledAttributes(
                attrs, com.android.internal.R.styleable.RadioGroup, com.android.internal.R.attr.radioButtonStyle, 0);

        int value = attributes.getResourceId(R.styleable.RadioGroup_checkedButton, View.NO_ID);
        if (value != View.NO_ID) {
            mCheckedId = value;
        }
    }

    @Override
    public void addView(final View child, int index, ViewGroup.LayoutParams params) {
        findRadioButton(child);

        super.addView(child, index, params);
    }

    private void findRadioButton(View view) {
        if (view instanceof ViewGroup) {
            int childCount = ((ViewGroup) view).getChildCount();
            for (int i = 0; i < childCount; i++) {
                View child = ((ViewGroup) view).getChildAt(i);
                findRadioButton(child);
            }
        } else if (view instanceof RadioButton) {
            final RadioButton button = (RadioButton) view;
            final int index = radioIndex;
            radios.add(button);
            ((RadioButton) button).setOnTouchListener(new OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (event.getAction() == MotionEvent.ACTION_UP) {
                        check(button.getId());
                        // ((RadioButton) button).setChecked(true);
                        // checkRadioButton((RadioButton) button);
                        if (mOnCheckedChangeListener != null) {
                            mOnCheckedChangeListener.onCheckedChanged(CustomRadioGroup.this, index);
                        }
                    }
                    return true;
                }
            });
            radioIndex++;
        }
    }

    @Override
    public void check(int id) {
        if (id != -1 && (id == mCheckedId)) {
            return;
        }

        if (mCheckedId != -1) {
            setCheckedStateForView(mCheckedId, false);
        }

        if (id != -1) {
            setCheckedStateForView(id, true);
        }

        mCheckedId = id;
    }

    private void setCheckedStateForView(int viewId, boolean checked) {
        View checkedView = findViewById(viewId);
        Log.d(TAG, "checkedView is " + checkedView);
        if (checkedView != null && checkedView instanceof RadioButton) {
            ((RadioButton) checkedView).setChecked(checked);
        }
    }

    // public void checkRadioButton(RadioButton radioButton) {
    //     View child;
    //     int radioCount = getChildCount();
    //     for (int i = 0; i < radioCount; i++) {
    //         child = getChildAt(i);
    //         if (child instanceof RadioButton) {
    //             if (child == radioButton) {
    //                 // do nothing
    //             } else {
    //                 ((RadioButton) child).setChecked(false);
    //             }
    //         } else if (child instanceof LinearLayout) {
    //             int childCount = ((LinearLayout) child).getChildCount();
    //             for (int j = 0; j < childCount; j++) {
    //                 View view = ((LinearLayout) child).getChildAt(j);
    //                 if (view instanceof RadioButton) {
    //                     final RadioButton button = (RadioButton) view;
    //                     if (button == radioButton) {
    //                         // do nothing
    //                     } else {
    //                         ((RadioButton) button).setChecked(false);
    //                     }
    //                 }
    //             }
    //         }
    //     }
    // }

    public int getCheckedRadioPosition() {
        int checkedPosition = 0;
        if (radios.size() > 0) {
            for (int i = 0; i < radios.size(); i++) {
                if (radios.get(i).isChecked()) {
                    checkedPosition = i;
                    break;
                }
            }
        }

        return checkedPosition;
    }

    public void setOnCheckedChangeListener(OnCheckedChangeListener mOnCheckedChangeListener) {
        this.mOnCheckedChangeListener = mOnCheckedChangeListener;
    }

    public int getCheckedId() {
        return mCheckedId;
    }
}