package com.example.abo8a.jobconnector;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;

public class ClearableEditText extends android.support.v7.widget.AppCompatEditText {
   // @BindDrawable(R.drawable.ic_clear_dark_gray)
  //  private Drawable Drawable;


    Drawable mClearDrawable;

    public ClearableEditText(Context context) {
        super(context);
        init();
    }

    public ClearableEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        mClearDrawable=getResources().getDrawable(R.drawable.ic_clear_dark_gray);


        // Right Drawable onClick Listener
        setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_RIGHT = 2;

                if (event.getAction() == MotionEvent.ACTION_UP &&
                        getCompoundDrawables()[DRAWABLE_RIGHT] != null) {
                    if (event.getRawX() >= (getRight() -
                            getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        // Clear the Text when Right Drawable is Clicked
                        setText("");

                        return true;
                    }
                }
                return false;
            }
        });
    }

    @Override
    protected void onTextChanged(CharSequence text, int start, int lengthBefore, int lengthAfter) {
        super.onTextChanged(text, start, lengthBefore, lengthAfter);

        if (TextUtils.isEmpty(text)) {
            // Remove Clear Drawable when Text is Empty
            setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
        } else {
            // Add Clear Drawable when there is some Text
            setCompoundDrawablesWithIntrinsicBounds(null, null, mClearDrawable, null);
        }
    }
}