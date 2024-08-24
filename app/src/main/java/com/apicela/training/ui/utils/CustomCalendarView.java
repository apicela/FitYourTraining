package com.apicela.training.ui.utils;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.AttributeSet;
import android.widget.CalendarView;
import android.widget.TextView;

import java.lang.reflect.Field;

public class CustomCalendarView extends CalendarView {

    private int textColor = Color.BLUE; // You can set your desired color here

    public CustomCalendarView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // Get the TextView for the day using reflection (replace with your preferred method)
        try {
            Field field = CalendarView.class.getDeclaredField("mDayNumbers");
            field.setAccessible(true);
            TextView[] textViews = (TextView[]) field.get(this);

            for (TextView textView : textViews) {
                textView.setTextColor(textColor);
            }
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    // You can add a setter method to set the text color from outside the class
    public void setTextColor(int color) {
        textColor = color;
    }
}
