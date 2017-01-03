package io.github.hjxandhmr.game2048;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.view.View;
import android.widget.LinearLayout.LayoutParams;

public class ViewCell extends View {
	private RectF r = new RectF();
	private int number;
	private Canvas canvas =new Canvas();
	private Paint paint = new Paint();
	public ViewCell(Context context,int iwidth,int iheight){
		super(context);
		setLayoutParams(new LayoutParams(iwidth, iheight));
		r.set(0, 0, this.getWidth(), this.getHeight());
		r.inset(2, 2);
		
	}
	public void setNumber(int number){
		onDrawNum(number);
		this.number=number;
	}
	public int getNumber(){
		return number;
	}
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		this.canvas=canvas;
		onDrawNum(number);
	}
	//设置边框
	private void onDrawBorder(Canvas canvas) {
		paint.setShader(null);
		paint.setStrokeWidth(16);
		paint.setColor(Color.WHITE);
		canvas.drawLine(0, 0, 0, this.getHeight(), paint);
		canvas.drawLine(0, 0, this.getWidth(), 0, paint);
		canvas.drawLine(this.getWidth(), 0, this.getWidth(), this.getHeight(), paint);
		canvas.drawLine(0, this.getHeight(), this.getWidth(), this.getHeight(), paint);
	}
	//画背景和数字
	public void onDrawNum(int number){
//		switch(number){
//		case 0:
//			setBackgroundResource(R.drawable.zero);
//			break;
//		case 2:
//			setBackgroundResource(R.drawable._1x);
//			break;
//		case 4:
//			setBackgroundResource(R.drawable._2x);
//			break;
//		case 8:
//			setBackgroundResource(R.drawable._3x);
//			break;
//		case 16:
//			setBackgroundResource(R.drawable._4x);
//			break;
//		case 32:
//			setBackgroundResource(R.drawable._5x);
//			break;
//		case 64:
//			setBackgroundResource(R.drawable._1y);
//			break;
//		case 128:
//			setBackgroundResource(R.drawable._2y);
//			break;
//		case 256:
//			setBackgroundResource(R.drawable._3y);
//			break;
//		case 512:
//			setBackgroundResource(R.drawable._4y);
//			break;
//		case 1024:
//			setBackgroundResource(R.drawable._5y);
//			break;
//		case 2048:
//			setBackgroundResource(R.drawable.sun);
//			break;
//		default:
//			setBackgroundResource(R.drawable.sun);
//				break;
//		}
//		onDrawBorder(canvas);

		
		String bgcolor;
		switch(number){
		case 0:
			bgcolor="#CCC0B3";
			break;
		case 2:
			bgcolor="#EEE4DA";
			break;
		case 4:
			bgcolor="#EDE0C8";
			break;
		case 8:
			bgcolor="#F2B179";//#F2B179
			break;
		case 16:
			bgcolor="#F49563";
			break;
		case 32:
			bgcolor="#F5794D";
			break;
		case 64:
			bgcolor="#F55D37";
			break;
		case 128:
			bgcolor="#EEE863";
			break;
		case 256:
			bgcolor="#EDB04D";
			break;
		case 512:
			bgcolor="#ECB04D";
			break;
		case 1024:
			bgcolor="#EB9437";
			break;
		case 2048:
			bgcolor="#EA7821";
			break;
		default:
			bgcolor="#EA7821";
				break;
		}
		this.setBackgroundColor(Color.parseColor(bgcolor));
		paint.setShader(null);
		paint.setColor(Color.parseColor("#EA7821"));
		canvas.drawRect(r, paint);
		
		paint.setColor(Color.BLACK);
		paint.setTextSize(32);
		paint.setFakeBoldText(true);
		String snumber=number+"";
		if(number==0){
			snumber="";
		}
		final int x =  ((int) r.left + ((int) r.width() >> 1)
				+ (int)(paint.getTextSize()));

		final int y = (int) (this.getHeight()
				- (this.getHeight() - paint.getTextSize()) / 2 - paint
				.getFontMetrics().bottom);
		canvas.drawText(snumber, x, y, paint);
		onDrawBorder(canvas);
	}
}
