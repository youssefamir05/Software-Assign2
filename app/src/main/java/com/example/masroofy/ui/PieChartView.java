package com.example.masroofy.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class PieChartView extends View {

    private static final int[] SLICE_COLORS = {
            0xFFE57373,
            0xFF64B5F6,
            0xFFFFD54F,
            0xFF81C784,
            0xFFBA68C8,
            0xFF4DD0E1,
            0xFFFF8A65,
    };

    private static final int   LEGEND_TEXT_SIZE_SP = 12;
    private static final float LEGEND_SWATCH_SIZE  = 28f;
    private static final float LEGEND_PADDING      = 16f;

    private final List<String> labels  = new ArrayList<>();
    private final List<Float>  values  = new ArrayList<>();
    private float              total   = 0f;

    private final Paint slicePaint  = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint textPaint   = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint legendPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    private final RectF ovalRect = new RectF();

    public PieChartView(Context context) {
        super(context);
        init();
    }

    public PieChartView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public PieChartView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        float density = getResources().getDisplayMetrics().density;
        textPaint.setColor(Color.BLACK); // Changed to BLACK for better visibility
        textPaint.setTextSize(LEGEND_TEXT_SIZE_SP * density);
        textPaint.setTextAlign(Paint.Align.LEFT);
        legendPaint.setStyle(Paint.Style.FILL);
    }

    public void setData(Map<String, Float> categoryTotals) {
        labels.clear();
        values.clear();
        total = 0f;

        if (categoryTotals != null) {
            for (Map.Entry<String, Float> entry : categoryTotals.entrySet()) {
                if (entry.getValue() > 0) {
                    labels.add(entry.getKey());
                    values.add(entry.getValue());
                    total += entry.getValue();
                }
            }
        }
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (total == 0f || values.isEmpty()) {
            drawEmptyState(canvas);
            return;
        }

        int w = getWidth();
        int h = getHeight();

        float legendHeight  = (labels.size() * (LEGEND_SWATCH_SIZE + LEGEND_PADDING));
        float chartDiameter = Math.min(w, h - legendHeight) * 0.85f;
        float left   = (w - chartDiameter) / 2f;
        float top    = 0f;
        float right  = left + chartDiameter;
        float bottom = top  + chartDiameter;

        ovalRect.set(left, top, right, bottom);

        float startAngle = -90f;
        for (int i = 0; i < values.size(); i++) {
            float sweep = (values.get(i) / total) * 360f;
            slicePaint.setColor(SLICE_COLORS[i % SLICE_COLORS.length]);
            slicePaint.setStyle(Paint.Style.FILL);
            canvas.drawArc(ovalRect, startAngle, sweep, true, slicePaint);

            slicePaint.setColor(Color.WHITE);
            slicePaint.setStyle(Paint.Style.STROKE);
            slicePaint.setStrokeWidth(3f);
            canvas.drawArc(ovalRect, startAngle, sweep, true, slicePaint);

            startAngle += sweep;
        }

        drawLegend(canvas, bottom + LEGEND_PADDING);
    }

    private void drawLegend(Canvas canvas, float startY) {
        float density    = getResources().getDisplayMetrics().density;
        float swatchSize = LEGEND_SWATCH_SIZE * (density / 2f);
        float x = LEGEND_PADDING * density;
        float y = startY;

        for (int i = 0; i < labels.size(); i++) {
            legendPaint.setColor(SLICE_COLORS[i % SLICE_COLORS.length]);
            canvas.drawRect(x, y, x + swatchSize, y + swatchSize, legendPaint);

            float pct = (values.get(i) / total) * 100f;
            String label = String.format(Locale.getDefault(), "  %s  %.1f%%", labels.get(i), pct);
            canvas.drawText(label, x + swatchSize + 8f, y + swatchSize - 4f, textPaint);

            y += swatchSize + LEGEND_PADDING * density;
        }
    }

    private void drawEmptyState(Canvas canvas) {
        Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);
        p.setColor(Color.GRAY);
        p.setTextSize(36f);
        p.setTextAlign(Paint.Align.CENTER);
        canvas.drawText("No expenses yet", getWidth() / 2f, getHeight() / 2f, p);
    }
}
