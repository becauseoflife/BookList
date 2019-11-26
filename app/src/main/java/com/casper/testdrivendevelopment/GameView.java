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
    private ArrayList<Mouse> mice = new ArrayList<>();

    private Canvas canvas = null;
    private Mouse mouse1;

    private float xTouch, yTouch;
    private int clickCount;

    @SuppressLint("ClickableViewAccessibility")
    public GameView(final Context context) {
        super(context);
        surfaceHolder = this.getHolder();
        surfaceHolder.addCallback(this);

/*        sprites.add(new Sprite(R.drawable.book_icon));
        sprites.add(new Sprite(R.drawable.book_icon));
        sprites.add(new Sprite(R.drawable.book_icon));
        sprites.add(new Sprite(R.drawable.book_icon));*/


        mouse1 = new Mouse();

        this.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                xTouch = event.getX();
                yTouch = event.getY();
                if (mouse1.getX() < xTouch && xTouch < mouse1.getxOffset() && mouse1.getY() < yTouch && yTouch < mouse1.getyOffset()){
                    ClickDraw();
                    xTouch = -1;
                    yTouch = -1;
                }
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
                try {
                    synchronized (surfaceHolder){
                        canvas = surfaceHolder.lockCanvas();    // 锁住画布
                        canvas.drawColor(Color.WHITE);      // 画布背景颜色

                        // 输出黑色的文本
                        Paint paint = new Paint();
                        paint.setTextSize(50);
                        paint.setColor(Color.BLACK);
                        canvas.drawText("击中" + clickCount + "个",50, 50, paint);

                        int[] xHole = {getWidth() / 5, getWidth() / 5 * 2, getWidth() / 5 * 3};
                        int[] yHole = {getHeight() / 5, getHeight() / 5 * 2, getHeight() / 5 * 3};
                        mouse1.draw(canvas, xHole, yHole);

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
                    Thread.sleep(700);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void ClickDraw()
    {
        clickCount++;

        canvas = surfaceHolder.lockCanvas();    // 锁住画布
        canvas.drawColor(Color.WHITE);      // 画布背景颜色

        // 输出黑色的文本
        Paint paint = new Paint();
        paint.setTextSize(50);
        paint.setColor(Color.BLACK);
        canvas.drawText("击中" + clickCount + "个",50, 50, paint);


        Drawable drawable1 = getContext().getResources().getDrawable(R.drawable.mole);

        int holeOffsetX = drawable1.getIntrinsicWidth()/2;
        int holeOffsetY = drawable1.getIntrinsicHeight()/2;
        int[] xHole = {getWidth() / 5, getWidth() / 5 * 2, getWidth() / 5 * 3};
        int[] yHole = {getHeight() / 5, getHeight() / 5 * 2, getHeight() / 5 * 3};
        for (int x : xHole) {
            for (int y : yHole) {
                canvas.drawCircle(x + holeOffsetX, y + holeOffsetY, 130, new Paint());
            }
        }

        Drawable drawable = getContext().getResources().getDrawable(R.drawable.whack_a_mole);
        int xOffset = mouse1.getX() + drawable.getIntrinsicWidth();
        int yOffset = mouse1.getY() + drawable.getIntrinsicHeight();
        Rect drawableRect = new Rect(mouse1.getX(), mouse1.getY(), xOffset, yOffset);
        drawable.setBounds(drawableRect);
        drawable.draw(canvas);

        mouse1.setX(getWidth());
        mouse1.setY(getHeight());

        if (null != canvas){
        // 把绘制好的内容提交上去
        surfaceHolder.unlockCanvasAndPost(canvas);
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

    //
    private class Mouse {
        private int x,y;    // 位置
        private int xOffset, yOffset;
        private int[] xHole, yHole;

        public Mouse() {
            this.x = -1;
            this.y = -1;
            this.xOffset = -1;
            this.yOffset = -1;
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }

        public void setX(int x) {
            this.x = x;
        }

        public void setY(int y) {
            this.y = y;
        }

        public int getxOffset() {
            return xOffset;
        }

        public int getyOffset() {
            return yOffset;
        }

        public void appear()
        {
            // 随机生成位置
            Random random = new Random();
            int pX = random.nextInt(3);
            int pY = random.nextInt(3);
            x = xHole[pX];
            y = yHole[pY];

        }

        public void draw(Canvas canvas, int[] xHole, int[] yHole)
        {
            this.xHole = xHole;
            this.yHole = yHole;
            this.appear();
            Drawable drawable = getContext().getResources().getDrawable(R.drawable.mole);
            // 画洞
            int holeOffsetX = drawable.getIntrinsicWidth()/2;
            int holeOffsetY = drawable.getIntrinsicHeight()/2;
            for (int x : xHole) {
                for (int y : yHole) {
                    canvas.drawCircle(x + holeOffsetX, y + holeOffsetY, 130, new Paint());
                }
            }
            // 画出出现的地鼠
            xOffset = x + drawable.getIntrinsicWidth();
            yOffset = y + drawable.getIntrinsicHeight();
            Rect drawableRect = new Rect(x, y, xOffset, yOffset);
            drawable.setBounds(drawableRect);
            drawable.draw(canvas);
        }

    }
}
