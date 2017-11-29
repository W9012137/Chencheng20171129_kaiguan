package chencheng.bwie.com.chencheng20171129_kaiguan;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by dell on 2017/11/29.
 */

public class MyView extends View implements View.OnClickListener{
    //背景图片
    private Bitmap bacgrout;
    //滑动的图片
    private Bitmap slide;
    private Paint paint;
    //开关当前状态
    private boolean courr=false;
    //刚开始的状态
    private float left;
    //拖拽判断
    private boolean isDrag=false;
    public MyView(Context context) {
        super(context);
    }

    public MyView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public MyView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //得到图片
        bacgrout= BitmapFactory.decodeResource(getResources(),R.drawable.bg_switchbutton);
        slide=BitmapFactory.decodeResource(getResources(),R.drawable.btn_switchbutton);
        //画笔
        paint=new Paint();
        //抗锯齿
        paint.setAntiAlias(true);
        //点击监听
        setOnClickListener(this);
       //设置当前view的大小
        setMeasuredDimension(bacgrout.getWidth(),bacgrout.getHeight());
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //背景绘制
        canvas.drawBitmap(bacgrout,0,0,paint);
        //可移动的按钮
        canvas.drawBitmap(slide,left,0,paint);
    }

    @Override
    public void onClick(View view) {
        if (!isDrag){
            courr=!courr;
            flushState();
        }
    }
    //非拖拽
    private void flushState(){
        if (courr){
            left=bacgrout.getWidth() - slide.getWidth();
        }else{
            left=0;
        }
        //刷新
        invalidate();
    }
    //点击事件的x值
    private int firstX;
    //点击事件之后的X值
    private int lastX;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                firstX=lastX= (int) event.getX();
                isDrag=false;
                break;
            case MotionEvent.ACTION_MOVE:
                //判断是否发生拖拽
                if (Math.abs(event.getX()-firstX)>5){
                    isDrag=true;
                }
                //计算手指在屏幕上的距离
                int dis= (int) (event.getX()-lastX);
                //将本次的位置设置给lastX
                lastX= (int) event.getX();
                left=left+dis;
                break;
            case MotionEvent.ACTION_UP:
                //当拖动时的状态
                if (isDrag){
                    if (left>(bacgrout.getWidth()-slide.getWidth())/2){
                        left=bacgrout.getWidth()-slide.getWidth();
                        courr=true;
                    }else{
                        left=0;
                        courr=false;
                    }
                }
                break;
        }
       flushView();
        return true;
    }
  //刷新view
    private void flushView(){
          //对left进行判断
        int maxLeft=bacgrout.getWidth()-slide.getWidth();
        //确保Left大于0
        left=(left>0)?left:0;
        //确保left小于maxLft
        left=(left<maxLeft)?left:maxLeft;
        invalidate();
    }
}
