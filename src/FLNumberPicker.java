import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.psyklab.guitools.R;


public class FLNumberPicker extends LinearLayout {

    public static final String UP_BUTTON_TAG = "FL_PICKER_UP";
    public static final String DOWN_BUTTON_TAG = "FL_PICKER_DOWN";

    private TextView pickerTextView;
    private Button upBtn;
    private Button downBtn;

    private int maxNum;
    private int minNum;
    private int currentNum;

    public FLNumberPicker(Context context) {
        super(context);
        initializeView(context);
    }

    public FLNumberPicker(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initializeView(context);
    }

    public FLNumberPicker(Context context, AttributeSet attrs) {
        super(context, attrs);
        initializeView(context);
    }

    private void initializeView(final Context context) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.custom_number_picker_layout,
                this, true);
        this.pickerTextView = (TextView) view
                .findViewById(R.id.picker_text_view);
        this.pickerTextView.setText("" + this.currentNum);

        this.upBtn = (Button) view.findViewById(R.id.picker_up_button);
        this.upBtn.setTag(R.id.PICKER_BUTTON_TYPE, UP_BUTTON_TAG);
        this.upBtn.setOnClickListener(pickerButtonListener);
        this.upBtn.setOnTouchListener(pickerButtonListener);

        this.downBtn = (Button) view.findViewById(R.id.picker_down_button);
        this.downBtn.setTag(R.id.PICKER_BUTTON_TYPE, DOWN_BUTTON_TAG);
        this.downBtn.setOnTouchListener(pickerButtonListener);
        this.downBtn.setOnClickListener(pickerButtonListener);
    }

    public void setMaxNumber(final int maxNum) {
        this.maxNum = maxNum;
    }

    public void setMinNumber(final int minNum) {
        this.minNum = minNum;
    }

    public int getMaxNumber() {
        return this.maxNum;
    }

    public int getMinNumber() {
        return this.minNum;
    }

    public void setCurrentNumber(final int currentNum) {
        if (minNum <= currentNum && currentNum <= maxNum) {
            this.currentNum = currentNum;
        }
    }

    public int getCurrentNumber() {
        return this.currentNum;
    }

    private PickerButtonListener pickerButtonListener = new PickerButtonListener();

    private class PickerButtonListener implements OnClickListener,
            OnTouchListener {

        @Override
        public boolean onTouch(View v, MotionEvent event) {

            Button button = (Button) v;

            switch (event.getAction() & MotionEvent.ACTION_MASK) {

            case MotionEvent.ACTION_DOWN:
                v.setPressed(true);
                button.setTag(R.id.PICKER_BUTTON_PRESSED, true);
                new Handler().postDelayed(new LongPressRunnable(button), 300);
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_OUTSIDE:
                v.setPressed(false);
                button.setTag(R.id.PICKER_BUTTON_PRESSED, false);
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                break;
            case MotionEvent.ACTION_POINTER_UP:
                break;
            case MotionEvent.ACTION_MOVE:
                break;

            }
            return false;
        }

        @Override
        public void onClick(View v) {
            Button button = (Button) v;

            if (button.getTag(R.id.PICKER_BUTTON_TYPE) != null) {
                switch ((String) button.getTag(R.id.PICKER_BUTTON_TYPE)) {
                case FLNumberPicker.UP_BUTTON_TAG:
                    if (FLNumberPicker.this.currentNum < FLNumberPicker.this.maxNum) {
                        FLNumberPicker.this.currentNum += 1;
                        FLNumberPicker.this.pickerTextView.setText(""
                                + FLNumberPicker.this.currentNum);
                    }
                    break;
                case FLNumberPicker.DOWN_BUTTON_TAG:
                    if (FLNumberPicker.this.currentNum > FLNumberPicker.this.minNum) {
                        FLNumberPicker.this.currentNum -= 1;
                        FLNumberPicker.this.pickerTextView.setText(""
                                + FLNumberPicker.this.currentNum);
                    }
                    break;
                default:
                    break;
                }
            }
        }

    }

    private class LongPressRunnable implements Runnable {

        private Button button;

        public LongPressRunnable(final Button button) {
            this.button = button;
        }

        public void run() {
            final AsyncTask<Object, Void, Integer> task = new AsyncTask<Object, Void, Integer>() {

                @Override
                protected Integer doInBackground(Object... params) {
                    if (LongPressRunnable.this.button.isPressed()) {

                        if (FLNumberPicker.UP_BUTTON_TAG
                                .equals((String) LongPressRunnable.this.button
                                        .getTag(R.id.PICKER_BUTTON_TYPE))) {
                            if (FLNumberPicker.this.currentNum < FLNumberPicker.this.maxNum)
                                FLNumberPicker.this.currentNum += 1;
                        } else {
                            if (FLNumberPicker.this.currentNum > FLNumberPicker.this.minNum)
                                FLNumberPicker.this.currentNum -= 1;
                        }
                    }
                    return FLNumberPicker.this.currentNum;
                }

                @Override
                protected void onPostExecute(Integer result) {
                    if (LongPressRunnable.this.button.isPressed()) {
                        FLNumberPicker.this.pickerTextView.setText(""
                                + FLNumberPicker.this.currentNum);
                        new Handler().postDelayed(LongPressRunnable.this, 50);
                    }
                }

            };

            task.execute(new Object());
        }

    }

}
