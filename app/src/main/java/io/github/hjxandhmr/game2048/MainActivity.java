package io.github.hjxandhmr.game2048;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Display;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends Activity implements OnGestureListener {

    private int mWidth;
    private int mHeight;
    private int iwidth;
    private int iheight;
    private LinearLayout centerlayout;
    private List<ViewCell> lists = new ArrayList<ViewCell>();
    private int[][] arrays = new int[4][4];
    private Random random = new Random();
    private GestureDetector mGestureDetector;
    //	private int testnumber=1;//测试数字方格分布情况
    private boolean isFlagMove = false;
    private TextView mCurrenScoreTv;
    private TextView mBestScoreTv;
    private int mCurScore = 0;
    private BestScoreRecord mRecord;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowManager windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        mWidth = display.getWidth();
        mHeight = display.getHeight();
        iwidth = mWidth / 4;
        iheight = iwidth;
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(mWidth, mWidth);
        centerlayout = new LinearLayout(this);
        centerlayout.setLayoutParams(params);
        centerlayout.setOrientation(LinearLayout.VERTICAL);
        centerlayout.setBackgroundColor(Color.WHITE);

        centerlayout.addView(getMainLayout());
        View scode = View.inflate(this, R.layout.main, null);
        centerlayout.addView(scode);
        setContentView(centerlayout);
        mRecord = new BestScoreRecord(this);
        mCurrenScoreTv = (TextView) findViewById(R.id.tv_currenScore);
        mBestScoreTv = (TextView) findViewById(R.id.tv_bestScore);
        mBestScoreTv.setText(mRecord.getBestScode() + "");
        mGestureDetector = new GestureDetector(this);

        random2or4();
    }

    //随机产生2或4
    private void random2or4() {
        //没充满非0数字
        if (!getFillAll()) {
            int row = random.nextInt(4);
            int col = random.nextInt(4);
            ViewCell randomview = lists.get(row * 4 + col);
            if (randomview.getNumber() == 0) {
                int n2Or4 = (random.nextInt(2) + 1) * 2;
                randomview.setNumber(n2Or4);
                if (getFillAll() && notAdd()) {
                    Builder builder = new Builder(this);
                    builder.setTitle("游戏结束！");
                    builder.setMessage("是否继续游戏？");
                    DialogInterface.OnClickListener listener = new MyOnclickListener();
                    builder.setPositiveButton("继续游戏", listener);
                    builder.setNegativeButton("退出游戏", listener);
                    builder.setCancelable(false);//不能按退回键关闭
                    builder.create().show();
                }
            } else {
                random2or4();
            }
        }
    }

    /**
     * 对话框按钮事件监听器
     */
    public class MyOnclickListener implements
            DialogInterface.OnClickListener {
        public void onClick(DialogInterface dialog, int which) {

            switch (which) {
                case AlertDialog.BUTTON_POSITIVE:
                    //继续游戏
                    for (int x = 0; x < arrays.length; x++) {
                        for (int y = 0; y < arrays[x].length; y++) {
                            ViewCell view = lists.get(x * 4 + y);
                            view.setNumber(0);
                        }
                    }
                    //比较，写入最高分数
                    if (mCurScore > mRecord.getBestScode()) {
                        mRecord.setBestScode(mCurScore);
                        mBestScoreTv.setText(mCurScore + "");
                    }
                    mCurScore = 0;
                    mCurrenScoreTv.setText(0 + "");
                    random2or4();
                    break;
                //退出游戏
                case AlertDialog.BUTTON_NEGATIVE:
                    //比较，写入最高分数
                    if (mCurScore > mRecord.getBestScode()) {
                        mRecord.setBestScode(mCurScore);
                        mBestScoreTv.setText(mCurScore + "");
                    }
                    finish();
                    break;
            }
        }
    }

    //返回主布局
    private View getMainLayout() {
        LinearLayout colLayout = new LinearLayout(this);
        colLayout.setOrientation(LinearLayout.VERTICAL);
        for (int i = 0; i < 4; i++) {
            colLayout.addView(getRowLayout());
        }
        return colLayout;
    }

    //行
    private View getRowLayout() {
        LinearLayout rowLayout = new LinearLayout(this);
        rowLayout.setOrientation(LinearLayout.HORIZONTAL);
//		rowLayout.setBackgroundColor(Color.GRAY);
        for (int i = 0; i < 4; i++) {
            ViewCell viewcell = new ViewCell(this, iwidth, iheight);
            rowLayout.addView(viewcell);
            //测试数字方格排列情况
//			viewcell.setNumber((int) Math.pow(2, testnumber));
//			testnumber++;
            lists.add(viewcell);
        }
        return rowLayout;
    }

    /**
     * 判断是否充满非0数字
     *
     * @return
     */
    private boolean getFillAll() {
        markNumber();
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                if (arrays[i][j] == 0) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * 判断是否有相邻数字是否相等；
     *
     * @return true ：有（非0）相邻数字相等
     */
    private boolean notAdd() {
        markNumber();
        boolean notadd = true;
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 3; j++) {
                if (arrays[i][j] == arrays[i][j + 1] && arrays[i][j] != 0) {
                    notadd = false;
                } else if (arrays[j][i] == arrays[j + 1][i] && arrays[j][i] != 0) {
                    notadd = false;
                }
            }
        }
        return notadd;
    }

    @Override
    public boolean onDown(MotionEvent e) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {
    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
                            float distanceY) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
                           float velocityY) {
        float X = e2.getX() - e1.getX();
        float Y = e2.getY() - e1.getY();
        final int FLING_Min_DISTANCE = 50;

        if (X > FLING_Min_DISTANCE && Math.abs(velocityX) > Math.abs(velocityY)) {
            toRight();
//			Toast.makeText(this, "右", 0).show();
        } else if (X < -FLING_Min_DISTANCE && Math.abs(velocityX) > Math.abs(velocityY)) {
            toLeft();
//			Toast.makeText(this, "左", 0).show();
        } else if (Y > FLING_Min_DISTANCE && Math.abs(velocityX) < Math.abs(velocityY)) {
            toDown();
//			Toast.makeText(this, "下", 0).show();
        } else if (Y < -FLING_Min_DISTANCE && Math.abs(velocityX) < Math.abs(velocityY)) {
            toUp();
//			Toast.makeText(this, "上", 0).show();
        }
        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return mGestureDetector.onTouchEvent(event);
    }

    /**
     * 向上划动
     */
    private void toUp() {
        markNumber();
        orderUp();
        //合并相同数据
        int addscode = 0;
        for (int p = 0; p < 4; p++) {
            for (int q = 0; q < 3; q++) {
                if (arrays[q][p] == arrays[q + 1][p] && arrays[q][p] != 0) {
                    isFlagMove = true;
                    arrays[q][p] += arrays[q + 1][p];
                    addscode += arrays[q][p];
                    arrays[q + 1][p] = 0;
                    q++;
                }
            }
        }
        orderUp();
        move2draw();
        mCurScore += addscode;
        mCurrenScoreTv.setText(mCurScore + "");
    }

    /**
     * 向下划动
     */
    private void toDown() {
        markNumber();
        //逐一排列
        orderDown();
        //合并相同数据
        int addscode = 0;
        for (int p = 0; p < 4; p++) {
            for (int q = 3; q > 0; q--) {
                if (arrays[q][p] == arrays[q - 1][p] && arrays[q][p] != 0) {
                    isFlagMove = true;
                    arrays[q][p] += arrays[q - 1][p];
                    addscode += arrays[q][p];
                    arrays[q - 1][p] = 0;
                    q--;
                }
            }
        }
        orderDown();
        move2draw();
        mCurScore += addscode;
        mCurrenScoreTv.setText(mCurScore + "");
    }

    /**
     * 向左划动
     */
    private void toLeft() {
        markNumber();
        orderLeft();
        //合并相等数据
        int addscode = 0;
        for (int p = 0; p < 4; p++) {
            for (int q = 0; q < 3; q++) {
                if (arrays[p][q] == arrays[p][q + 1] && arrays[p][q] != 0) {
                    isFlagMove = true;
                    arrays[p][q] += arrays[p][q + 1];
                    addscode += arrays[p][q];
                    arrays[p][q + 1] = 0;
                    q++;
                }
            }
        }
        orderLeft();
        move2draw();
        mCurScore += addscode;
        mCurrenScoreTv.setText(mCurScore + "");
    }


    /**
     * 向右划动
     */
    private void toRight() {
        markNumber();
        orderRight();
        //合并相同数据
        int addscode = 0;
        for (int p = 0; p < 4; p++) {
            for (int q = 0; q < 3; q++) {
                if (arrays[p][q] == arrays[p][q + 1] && arrays[p][q] != 0) {
                    isFlagMove = true;
                    arrays[p][q] += arrays[p][q + 1];
                    addscode += arrays[p][q];
                    arrays[p][q + 1] = 0;
                    q++;
                }
            }
        }
        orderRight();
        move2draw();
        mCurScore += addscode;
        mCurrenScoreTv.setText(mCurScore + "");
    }

    /**
     * 向上划动，方格排列，非0靠前，0靠后，如0、2、0、4 ——>2、4、0、0
     */
    private void orderUp() {
        //逐一排列
        for (int n = 0; n < 4; n++) {
            //冒泡排序法
            for (int m = 0; m < 4; m++) {
                for (int i = m + 1; i < 4; i++) {
                    if (arrays[m][n] == 0 && arrays[i][n] != 0) {
                        isFlagMove = true;
                        arrays[m][n] = arrays[i][n];
                        arrays[i][n] = 0;
                    }
                }
            }
        }
    }

    /**
     * 向下划动，方格排列，非0靠前，0靠后，如0、2、0、4 ——>2、4、0、0
     */
    private void orderDown() {
        for (int n = 0; n < 4; n++) {
            //冒泡排序法
            for (int m = 3; m >= 0; m--) {
                for (int i = m - 1; i >= 0; i--) {
                    if (arrays[m][n] == 0 && arrays[i][n] != 0) {
                        isFlagMove = true;
                        arrays[m][n] = arrays[i][n];
                        arrays[i][n] = 0;
                    }
                }
            }
        }
    }

    /**
     * 向左划动，方格排列，非0靠前，0靠后，如0、2、0、4 ——>2、4、0、0
     */
    private void orderLeft() {
        //逐一排列
        for (int n = 0; n < 4; n++) {
            //冒泡排序法
            for (int m = 0; m < 4; m++) {
                for (int i = m + 1; i < 4; i++) {
                    if (arrays[n][m] == 0 && arrays[n][i] != 0) {
                        isFlagMove = true;
                        arrays[n][m] = arrays[n][i];
                        arrays[n][i] = 0;
                    }
                }
            }
        }
    }

    /**
     * 向右划动，方格排列，非0靠前，0靠后，如0、2、0、4 ——>2、4、0、0
     */
    private void orderRight() {
        //逐一排列
        for (int n = 0; n < 4; n++) {
            //冒泡排序法
            for (int m = 3; m >= 0; m--) {
                for (int i = m - 1; i >= 0; i--) {
                    if (arrays[n][m] == 0 && arrays[n][i] != 0) {
                        isFlagMove = true;
                        arrays[n][m] = arrays[n][i];
                        arrays[n][i] = 0;
                    }
                }
            }
        }
    }

    /**
     * 记录数字方格表中的数字。
     */
    private void markNumber() {
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                ViewCell view = lists.get(i * 4 + j);
                arrays[i][j] = view.getNumber();
            }
        }
    }

    /**
     * /有移动数字方格就重画数字方格表并随机产生2或4数字方格，否则，不重画和产生数字方格。
     */
    private void move2draw() {
        if (isFlagMove) {//判断是否有移动
            //重画方格
            for (int x = 0; x < arrays.length; x++) {
                for (int y = 0; y < arrays[x].length; y++) {
                    ViewCell view = lists.get(x * 4 + y);
                    view.setNumber(arrays[x][y]);
                }
            }
            random2or4();
            isFlagMove = false;
        }
    }
}
