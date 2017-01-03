package io.github.hjxandhmr.game2048;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class BestScoreRecord {
	
	private SharedPreferences sp;

	public BestScoreRecord(Context context){
		sp = context.getSharedPreferences("BestScoreRecord", context.MODE_PRIVATE);
	}
	public int getBestScode(){
		int bestscode = sp.getInt("BestScoreRecord", 0);
		return bestscode;
	}
	public void setBestScode(int bestScore){
		Editor editor = sp.edit();
		editor.putInt("BestScoreRecord", bestScore);
		editor.commit();
	}
}
