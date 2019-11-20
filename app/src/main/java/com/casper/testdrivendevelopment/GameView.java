package com.casper.testdrivendevelopment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.SurfaceHolder;
import android.view.View;

import java.util.ArrayList;
import java.util.Random;

public class GameView extends SurfaceView implements SurfaceHolder.Callback {
    private final SurfaceHolder surfaceHolder;
    private DrawThread drawThread;
    private ArrayList<Sprite> sprites = new ArrayList<>();


    private float xTouch, yTouch;
    private int clickCount;

    @SuppressLint("ClickableViewAccessibility")
    public GameView(Context context) {
        super(context);
        surfaceHolder = this.getHolder();
        surfaceHolder.addCallback(this);

        sprites.add(new Sprite(R.drawable.book_icon));
        sprites.add(new Sprite(R.drawable.book_icon));
        sprites.add(new Sprite(R.drawable.book_icon));
        sprites.add(new Sprite(R.drawable.book_icon));




        this.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                xTouch = event.getX();
                yTouch = event.getY();
                return true;
            }
        });
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if (null == drawThread){
            drawThread = new DrawThread();
            drawThread.start();
        }

    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        if (null != drawThread){
            drawThread.stopThread();
            drawThread = null;
        }

    }

    private class DrawThread extends Thread{
        private boolean beAlive = false;     // 控制线程结束

        void stopThread(){
            beAlive = false;
            while (true){
                try {
                    this.join();    // 保证run 方法执行完毕
                    break;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        public void run(){
            beAlive = true;
            while (beAlive){
                Canvas canvas = null;
                try {
                    synchronized (surfaceHolder){
                        canvas = surfaceHolder.lockCanvas();    // 锁住画布
                        canvas.drawColor(Color.WHITE);      // 画布背景颜色

                        // 输出黑色的文本
                        Paint paint = new Paint();
                        paint.setTextSize(50);
                        paint.setColor(Color.BLACK);
                        canvas.drawText("击中" + clickCount + "个",50, 50, paint);


                        for (Sprite sprite:sprites) sprite.move();      // 让所有精灵移动
                        for (Sprite sprite:sprites) sprite.draw(canvas);   // 让所有精灵画图

                    }
                }catch (Exception e){
                    e.printStackTrace();
                }finally {
                    if (null != canvas){
                        // 把绘制好的内容提交上去
                        surfaceHolder.unlockCanvasAndPost(canvas);
                    }
                }
                // 休眠10毫秒开始刷新下一轮
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    private class Sprite {
        private int resourceId;
        private int x,y;            // 初始位置
        private double direction;   // 移动方向
        public Sprite(int book_icon) {
            this.resourceId = book_icon;
            x = (int) (Math.random() * getWidth());
            y = (int) (Math.random() * getHeight());
            direction = Math.random() * 2 * Math.PI;
        }

        public void move(){
            x += 15 * Math.cos(direction);    // 以每次15个单位移动
            y += 15 * Math.sin(direction);

            // 如果到达边界
            if (x < 0)  x = getWidth();
            else if(x > getWidth()) x = 0;

            if (y < 0) y = getHeight();
            else if(y > getHeight()) y = 0;

            // 方向以1/20的概率变化
            if (Math.random() < 0.05)
                direction = Math.random() * 2 * Math.PI;
        }

        // 在指定的位置画出图标
        public void draw(Canvas canvas){
            Drawable drawable = getContext().getResources().getDrawable(R.drawable.book_icon);
            Rect drawableRect = new Rect(x, y, x + drawable.getIntrinsicWidth(), y + drawable.getIntrinsicHeight());
            drawable.setBounds(drawableRect);
            drawable.draw(canvas);

            if ((x < xTouch && xTouch < x + drawable.getIntrinsicWidth()) && (y < yTouch && yTouch < y + drawable.getIntrinsicHeight())){
                x = (int) (Math.random() * getWidth());
                y = (int) (Math.random() * getHeight());
                xTouch = -1;
                yTouch = -1;
                clickCount++;
            }
        }
    }

}
