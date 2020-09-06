package aarnav100.developer.attendancemanager.Generic;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by aarnavjindal on 27/01/18.
 */

public class TypeWriter extends android.support.v7.widget.AppCompatTextView {
    private CharSequence mText;
    private int mIndex;
    private long mDelay = 100; // in ms
    public TypeWriter(Context context) {
        super(context);
        animateText(getText());
    }
    public TypeWriter(Context context, AttributeSet attrs) {
        super(context, attrs);
        animateText(getText());
    }
    private Handler mHandler = new Handler();
    private Runnable characterAdder = new Runnable() {
        @Override
        public void run() {
            setText(mText.subSequence(0, mIndex++));
            if (mIndex <= mText.length()) {
                mHandler.postDelayed(characterAdder, mDelay);
            }
        }
    };
    public void animateText(CharSequence txt) {
        mText = txt;
        mIndex = 0;
        setText("");
        mHandler.removeCallbacks(characterAdder);
        mHandler.postDelayed(characterAdder, mDelay);
    }
    public void setCharacterDelay(long m) {
        mDelay = m;
    }
    public String getTypeText(){
        return String.valueOf(mText);
    }
}