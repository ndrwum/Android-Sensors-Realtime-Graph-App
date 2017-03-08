package com.tikeon.ndrwum.assignment2;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PlotView extends View {


    List<Float> pointsList = new ArrayList<>();
    List<Float> avgsList = new ArrayList<>();
    List<Float> stdDevsList = new ArrayList<>();
    float last_val = 0;
    float max_val = 0; // Used to scale
    float last_avg = 0;
    int time_count = 0;
    boolean timer = true;

    public PlotView(Context context) {
        super(context);
    }

    public PlotView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PlotView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        Paint p = new Paint();
        p.setColor(Color.BLACK);
        p.setStrokeWidth(2);

        Paint p_text = new Paint();
        p_text.setColor(Color.BLACK);
        p_text.setStrokeWidth(10);
        p_text.setTextSize(40);

        Paint p_val = new Paint();
        p_val.setColor(Color.GREEN);
        p_val.setStrokeWidth(10);

        Paint p_avg = new Paint();
        p_avg.setColor(Color.BLUE);
        p_avg.setStrokeWidth(10);

        Paint p_stdDev = new Paint();
        p_stdDev.setColor(Color.YELLOW);
        p_stdDev.setStrokeWidth(10);

        // Set margin to allow space for labels
        int offset = 80;
        int h_offset = getHeight() - offset;
        // Divide grid to set equal spacing
        int w_div = (getWidth() - offset) / 6;
        int h_div = h_offset / 6;

        // Axis lines and labels.
        for (int i = 0; i < 7; i++) {
            canvas.drawLine(offset, i * h_div, getWidth(), i * h_div, p);
            canvas.drawLine(offset + i * w_div, 0, offset + i * w_div, h_offset, p);

            canvas.drawText(Integer.toString(Math.round(i * ((max_val + 10) / 6))), 0, (6 - i) * h_div + 20, p_text);
            if (time_count > 6) {
                canvas.drawText(Integer.toString(time_count - 6 + i), i * w_div + offset, getHeight() - 10, p_text);
            } else {
                canvas.drawText(Integer.toString(i), i * w_div + offset, getHeight() - 10, p_text);
            }
        }

        addPlotPoint(last_val);
        calcAvg();
        calcStdDev();

        // Plot point and then connect points
        for (int i = 0; i < pointsList.size(); i++) {
            canvas.drawCircle(offset + w_div * i, h_offset - avgsList.get(i) * ((h_offset) / (max_val + 10)), 10, p_avg);
            canvas.drawCircle(offset + w_div * i, h_offset - pointsList.get(i) * ((h_offset) / (max_val + 10)), 10, p_val);
            canvas.drawCircle(offset + w_div * i, h_offset - stdDevsList.get(i) * ((h_offset) / (max_val + 10)), 10, p_stdDev);
        }

        connectPoints(canvas, avgsList);
        connectPoints(canvas, pointsList);
        connectPoints(canvas, stdDevsList);

        // Timer to keep label and points in sync
        if (timer) {
            time_count++;
            timer = false;
        } else {
            timer = true;
            time_count++;
        }
    }


    public void addPoint(float val) {
        last_val = val;
    }

    public void addPlotPoint(float val) {
        if (pointsList.size() > 6) {
            pointsList.remove(0);
        }
        pointsList.add(val);

        max_val = Collections.max(pointsList);
    }

    public void calcAvg() {
        float total = 0;
        float avg;
        for (int i = 0; i < pointsList.size(); i++) {
            total += pointsList.get(i);
        }
        if (avgsList.size() > 6) {
            avgsList.remove(0);
        }
        avg = total / pointsList.size();
        avgsList.add(avg);
        last_avg = avg;

    }

    public void calcStdDev() {
        float temp = 0;
        for (int i = 0; i < pointsList.size(); i++) {
            temp += (last_avg - pointsList.get(i)) * (last_avg - pointsList.get(i));
        }
        if (stdDevsList.size() > 6) {
            stdDevsList.remove(0);
        }
        float stdDev = (float) Math.sqrt(temp / pointsList.size());

        stdDevsList.add(stdDev);
    }

    public void connectPoints(Canvas canvas, List<Float> list) {

        int offset = 80;
        int h_offset = getHeight() - offset;
        int w_part = (getWidth() - offset) / 6;

        Paint p = new Paint();
        p.setStrokeWidth(10);
        if (list == pointsList) {
            p.setColor(Color.GREEN);
        } else if (list == avgsList) {
            p.setColor(Color.BLUE);
        } else {
            p.setColor(Color.YELLOW);
        }

        for (int i = 0; i < list.size() - 1; i++) {
            float x1 = i * w_part + offset;
            float y1 = h_offset - list.get(i) * (h_offset / (max_val + 10));
            float x2 = (i + 1) * w_part + offset;
            float y2 = h_offset - list.get(i + 1) * (h_offset / (max_val + 10));

            canvas.drawLine(x1, y1, x2, y2, p);
        }
    }

    public double getLastAvg() {
        return (double) last_avg;
    }
}
