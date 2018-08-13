package com.github.bkhezry.indicatorview;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.support.annotation.ColorInt;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import comulez.github.droplibrary.R;


public class IndicatorView extends ViewGroup {
  private Paint mClickPaint;
  private int duration;
  private Paint mPaintCircle;
  private Paint mPaint;
  private Path mPath = new Path();
  private float ratio = 50;
  private final double c = 0.552284749831;
  private float startX;
  private int startY;
  private float distance;
  private float mCurrentTime;
  private int tabNum = 0;
  private XPoint p2, p4;
  private YPoint p1, p3;
  private float mc;
  private float radius;
  private int[] roundColors = new int[4];
  private float div;
  private float scale = 0.8f;
  private int count = 4;
  private boolean rtl = false;
  private boolean clickable = false;
  private int currentPos = 0;
  private int toPos = -1;
  private boolean animating;
  @ColorInt
  int[] colorArray = {
    Color.parseColor("#B04285F4"),
    Color.parseColor("#B0EA4335"),
    Color.parseColor("#B0FBBC05"),
    Color.parseColor("#B034A853")
  };

  public IndicatorView(Context context) {
    this(context, null);
  }

  public IndicatorView(Context context, AttributeSet attrs) {
    this(context, attrs, 0);
  }

  public IndicatorView(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.IndicatorView);

    TypedArray colorResValues = context.getResources().obtainTypedArray(typedArray.getResourceId(R.styleable.IndicatorView_colors, 0));
    count = typedArray.getInteger(R.styleable.IndicatorView_count, 4);
    for (int i = 0; i < count; i++) {
      roundColors[i] = colorResValues.getColor(i, colorArray[i]);
    }
    int clickColor = typedArray.getColor(R.styleable.IndicatorView_click_color, Color.WHITE);
    int circleColor = typedArray.getColor(R.styleable.IndicatorView_circle_color, Color.GRAY);
    radius = typedArray.getDimension(R.styleable.IndicatorView_radius, 50);
    duration = typedArray.getInteger(R.styleable.IndicatorView_duration, 1000);
    scale = typedArray.getFloat(R.styleable.IndicatorView_scale, 0.8f);
    rtl = typedArray.getBoolean(R.styleable.IndicatorView_rtl, false);
    clickable = typedArray.getBoolean(R.styleable.IndicatorView_clickable, false);
    typedArray.recycle();
    f = new float[roundColors.length][3];
    result = new float[3];
    ratio = radius;
    mc = (float) (c * ratio);
    mPaintCircle = new Paint();
    mPaintCircle.setColor(circleColor);
    mPaintCircle.setStyle(Paint.Style.STROKE);
    mPaintCircle.setAntiAlias(true);
    mPaintCircle.setStrokeWidth(3);

    mClickPaint = new Paint();
    mClickPaint.setColor(clickColor);
    mClickPaint.setStyle(Paint.Style.STROKE);
    mClickPaint.setAntiAlias(true);
    mClickPaint.setStrokeWidth(radius / 2);

    mPaint = new Paint();
    startColor = roundColors[0];
    mPaint.setColor(startColor);
    mPaint.setStyle(Paint.Style.FILL);
    mPaint.setStrokeWidth(1);
    mPaint.setAntiAlias(true);
    colorResValues.recycle();
  }


  @Override
  protected void onSizeChanged(int w, int h, int oldw, int oldh) {
    div = (w - 2 * tabNum * radius) / (tabNum + 1);
    startX = div + radius;
    startY = h / 2;
    if (currentPos == 0) {
      int r = 1;
      radius = r * ratio;
      mc = (float) (c * ratio);
      p1 = new YPoint(0, radius, mc);
      p3 = new YPoint(0, -radius, mc);
      p2 = new XPoint(radius, 0, mc);
      p4 = new XPoint(-radius, 0, mc);
      if (rtl) {
        int tempDuration = duration;
        duration = 0;
        startAniTo(currentPos, count - 1);
        duration = tempDuration;
      }
    }
    super.onSizeChanged(w, h, oldw, oldh);
  }

  @Override
  public boolean onTouchEvent(MotionEvent event) {
    if (clickable) {
      float x = event.getX();
      if (x > div + 2 * radius && x < (div + 2 * radius) * tabNum) {
        if (animator != null) {
          animator.cancel();
        }
        int toPos = (int) (x / (div + 2 * radius));
        if (toPos != currentPos && toPos <= tabNum) {
          startAniTo(currentPos, toPos);
        }
      } else if (x > div && x < div + 2 * radius) {
        if (animator != null) {
          animator.cancel();
        }
        if (currentPos != 0) {
          startAniTo(currentPos, 0);
        }
      }
    }
    return super.onTouchEvent(event);
  }


  ValueAnimator animator;

  private void startAniTo(int currentPos, int toPos) {
    if (toPos > count - 1 || toPos < 0) {
      return;
    }
    this.currentPos = currentPos;
    this.toPos = toPos;
    if (currentPos == toPos) {
      return;
    }
    startColor = roundColors[(this.currentPos) % 4];
    endColor = roundColors[(toPos) % 4];
    resetP();


    startX = div + radius + (this.currentPos) * (div + 2 * radius);
    distance = (toPos - this.currentPos) * (2 * radius + div) + (toPos > currentPos ? -radius : radius);

    if (animator == null) {
      animator = ValueAnimator.ofFloat(0, 1.0f);
      animator.setDuration(duration);
      animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
        @Override
        public void onAnimationUpdate(ValueAnimator animation) {
          mCurrentTime = (float) animation.getAnimatedValue();
          invalidate();
        }
      });
      animator.addListener(new Animator.AnimatorListener() {
        @Override
        public void onAnimationStart(Animator animation) {
          animating = true;
        }

        @Override
        public void onAnimationEnd(Animator animation) {
          goo();
          animating = false;
        }

        @Override
        public void onAnimationCancel(Animator animation) {
          goo();
          animating = false;
          mCurrentTime = 1;
          invalidate();
        }

        @Override
        public void onAnimationRepeat(Animator animation) {

        }
      });
    } else {
      animator.setDuration(duration);
    }
    animator.start();
  }

  private void resetP() {
      p1.setY(radius);
      p1.setX(0);
      p1.setMc(mc);

      p3.setY(-radius);
      p3.setX(0);
      p3.setMc(mc);

      p2.setY(0);
      p2.setX(radius);
      p2.setMc(mc);

      p4.setY(0);
      p4.setX(-radius);
      p4.setMc(mc);
  }

  private void goo() {
    currentPos = toPos;
  }

  private int startColor;
  private int endColor;
  boolean direction = true;

  @Override
  protected void dispatchDraw(Canvas canvas) {
    canvas.save();
    mPath.reset();
    tabNum = getChildCount();
    for (int i = 0; i < tabNum; i++) {
      canvas.drawCircle(div + radius + i * (div + 2 * radius), startY, radius, mPaintCircle);
    }
    if (mCurrentTime == 0) {
      resetP();
      canvas.drawCircle(div + radius + (currentPos) * (div + 2 * radius), startY, 0, mClickPaint);
      mPaint.setColor(startColor);
      canvas.translate(startX, startY);
      if (toPos > currentPos) {
        p2.setX(radius);
      } else {
        p4.setX(-radius);
      }
    }
    if (mCurrentTime > 0 && mCurrentTime <= 0.2) {
      direction = toPos > currentPos;
      if (animating) {
        canvas.drawCircle(div + radius + (toPos) * (div + 2 * radius), startY, radius * 1.0f * 5 * mCurrentTime, mClickPaint);
      }
      canvas.translate(startX, startY);
      if (toPos > currentPos) {
        p2.setX(radius + 2 * 5 * mCurrentTime * radius / 2);
      } else {
        p4.setX(-radius - 2 * 5 * mCurrentTime * radius / 2);
      }
    } else if (mCurrentTime > 0.2 && mCurrentTime <= 0.5) {
      canvas.translate(startX + (mCurrentTime - 0.2f) * distance / 0.7f, startY);
      if (toPos > currentPos) {
        p2.setX(2 * radius);
        p1.setX(0.5f * radius * (mCurrentTime - 0.2f) / 0.3f);
        p3.setX(0.5f * radius * (mCurrentTime - 0.2f) / 0.3f);
        p2.setMc(mc + (mCurrentTime - 0.2f) * mc / 4 / 0.3f);
        p4.setMc(mc + (mCurrentTime - 0.2f) * mc / 4 / 0.3f);
      } else {
        p4.setX(-2 * radius);
        p1.setX(-0.5f * radius * (mCurrentTime - 0.2f) / 0.3f);
        p3.setX(-0.5f * radius * (mCurrentTime - 0.2f) / 0.3f);
        p2.setMc(mc + (mCurrentTime - 0.2f) * mc / 4 / 0.3f);
        p4.setMc(mc + (mCurrentTime - 0.2f) * mc / 4 / 0.3f);
      }
    } else if (mCurrentTime > 0.5 && mCurrentTime <= 0.8) {
      canvas.translate(startX + (mCurrentTime - 0.2f) * distance / 0.7f, startY);
      if (toPos > currentPos) {
        p1.setX(0.5f * radius + 0.5f * radius * (mCurrentTime - 0.5f) / 0.3f);
        p3.setX(0.5f * radius + 0.5f * radius * (mCurrentTime - 0.5f) / 0.3f);
        p2.setMc(1.25f * mc - 0.25f * mc * (mCurrentTime - 0.5f) / 0.3f);
        p4.setMc(1.25f * mc - 0.25f * mc * (mCurrentTime - 0.5f) / 0.3f);
      } else {
        p1.setX(-0.5f * radius - 0.5f * radius * (mCurrentTime - 0.5f) / 0.3f);
        p3.setX(-0.5f * radius - 0.5f * radius * (mCurrentTime - 0.5f) / 0.3f);
        p2.setMc(1.25f * mc - 0.25f * mc * (mCurrentTime - 0.5f) / 0.3f);
        p4.setMc(1.25f * mc - 0.25f * mc * (mCurrentTime - 0.5f) / 0.3f);
      }
    } else if (mCurrentTime > 0.8 && mCurrentTime <= 0.9) {
      p2.setMc(mc);
      p4.setMc(mc);
      canvas.translate(startX + (mCurrentTime - 0.2f) * distance / 0.7f, startY);
      if (toPos > currentPos) {
        p4.setX(-radius + 1.6f * radius * (mCurrentTime - 0.8f) / 0.1f);
      } else {
        p2.setX(radius - 1.6f * radius * (mCurrentTime - 0.8f) / 0.1f);
      }
    } else if (mCurrentTime > 0.9 && mCurrentTime < 1) {
      if (toPos > currentPos) {
        p1.setX(radius);
        p3.setX(radius);
        canvas.translate(startX + distance, startY);
        p4.setX(0.6f * radius - 0.6f * radius * (mCurrentTime - 0.9f) / 0.1f);
      } else {
        p1.setX(-radius);
        p3.setX(-radius);
        canvas.translate(startX + distance, startY);
        p2.setX(-0.6f * radius + 0.6f * radius * (mCurrentTime - 0.9f) / 0.1f);
      }
    }
    if (mCurrentTime == 1) {
      mPaint.setColor(endColor);
      if (direction) {
        p1.setX(radius);
        p3.setX(radius);
        canvas.translate(startX + distance, startY);
        p4.setX(0);
      } else {
        p1.setX(-radius);
        p3.setX(-radius);
        canvas.translate(startX + distance, startY);
        p2.setX(0);
      }
      currentPos = toPos;
      resetP();
      if (direction) {
        canvas.translate(radius, 0);
      } else {
        canvas.translate(-radius, 0);
      }
    }
    mPath.moveTo(p1.x, p1.y);
    mPath.cubicTo(p1.right.x, p1.right.y, p2.bottom.x, p2.bottom.y, p2.x, p2.y);
    mPath.cubicTo(p2.top.x, p2.top.y, p3.right.x, p3.right.y, p3.x, p3.y);
    mPath.cubicTo(p3.left.x, p3.left.y, p4.top.x, p4.top.y, p4.x, p4.y);
    mPath.cubicTo(p4.bottom.x, p4.bottom.y, p1.left.x, p1.left.y, p1.x, p1.y);
    if (mCurrentTime > 0 && mCurrentTime < 1) {
      mPaint.setColor(getCurrentColor(mCurrentTime, startColor, endColor));
    }
    canvas.drawPath(mPath, mPaint);
    canvas.restore();
    super.dispatchDraw(canvas);
  }

  @Override
  protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    int sizeWidth = MeasureSpec.getSize(widthMeasureSpec);
    int sizeHeight = MeasureSpec.getSize(heightMeasureSpec);
    tabNum = getChildCount();
    for (int i = 0; i < tabNum; i++) {
      View child = getChildAt(i);
      measureChild(child, widthMeasureSpec, heightMeasureSpec);
    }
    setMeasuredDimension(sizeWidth, sizeHeight);
  }

  @Override
  protected void onLayout(boolean changed, int l, int t, int r, int b) {
    tabNum = getChildCount();
    for (int i = 0; i < tabNum; i++) {
      View child = getChildAt(i);
      double g2 = 1.41421;
      child.layout((int) (div + (1 - scale * 1 / g2) * radius + i * (div + 2 * radius)), (int) (startY - scale * radius / g2), (int) (div + (1 + scale * 1 / g2) * radius + i * (div + 2 * radius)), (int) (startY + scale * radius / g2));
    }
  }


  class XPoint {
    public float x;
    public float y;
    public float mc;
    public PointF bottom;
    public PointF top;

    public XPoint(float x, float y, float mc) {
      this.x = x;
      this.y = y;
      this.mc = mc;
      if (bottom == null) {
        bottom = new PointF();
      }
      if (top == null) {
        top = new PointF();
      }
      bottom.y = y + mc;
      top.y = y - mc;
      bottom.x = x;
      top.x = x;
    }

    public void setMc(float mc) {
      this.mc = mc;
      bottom.y = y + mc;
      top.y = y - mc;
    }

    public void setY(float y) {
      this.y = y;
      bottom.y = y + mc;
      top.y = y - mc;
    }

    public void setX(float x) {
      this.x = x;
      bottom.x = x;
      top.x = x;
    }

    @Override
    public String toString() {
      return "XPoint{" +
        "x=" + x +
        ", y=" + y +
        ", mc=" + mc +
        ", bottom=" + bottom +
        ", top=" + top +
        '}';
    }
  }

  class YPoint {
    public float x;
    public float y;
    public float mc;
    public PointF left;
    public PointF right;

    public YPoint(float x, float y, float mc) {
      this.x = x;
      this.y = y;
      this.mc = mc;
      if (left == null) {
        left = new PointF();
      }
      if (right == null) {
        right = new PointF();
      }
      right.x = x + mc;
      left.x = x - mc;
      left.y = y;
      right.y = y;
    }

    public void setMc(float mc) {
      this.mc = mc;
      right.x = x + mc;
      left.x = x - mc;
    }

    public void setX(float x) {
      this.x = x;
      right.x = x + mc;
      left.x = x - mc;
    }

    public void setY(float y) {
      this.y = y;
      left.y = y;
      right.y = y;
    }

    public void setLeftX(float leftX) {
      left.x = leftX;
      x = (left.x + right.x) / 2;
    }

    @Override
    public String toString() {
      return "YPoint{" +
        "x=" + x +
        ", y=" + y +
        ", mc=" + mc +
        ", left=" + left +
        ", right=" + right +
        '}';
    }
  }

  float[][] f;
  float[] result;
  int[] colors = new int[4];

  public int getCurrentColor(float percent, int startColor, int endColor) {
    colors[0] = startColor;
    colors[1] = Color.GRAY;
    colors[2] = Color.GRAY;
    colors[3] = endColor;
    for (int i = 0; i < colors.length; i++) {
      f[i][0] = (colors[i] & 0xff0000) >> 16;
      f[i][1] = (colors[i] & 0x00ff00) >> 8;
      f[i][2] = (colors[i] & 0x0000ff);
    }
    for (int i = 0; i < 3; i++) {
      for (int j = 0; j < f.length; j++) {
        if (f.length == 1 || percent == j / (f.length - 1f)) {
          result = f[j];
        } else {
          if (percent > j / (f.length - 1f) && percent < (j + 1f) / (f.length - 1)) {
            result[i] = f[j][i] - (f[j][i] - f[j + 1][i]) * (percent - j / (f.length - 1f)) * (f.length - 1f);
          }
        }
      }
    }
    return Color.rgb((int) result[0], (int) result[1], (int) result[2]);
  }

  public void setPositionWithAnim(int position) {
    startAniTo(currentPos, position);
  }

  public int getCurrentPos() {
    return this.currentPos;
  }
}
