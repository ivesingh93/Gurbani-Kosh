package gurbani.kosh;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.method.ScrollingMovementMethod;
import android.text.style.CharacterStyle;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class Kosh extends Activity {



	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.kosh);
		Typeface font = Typeface.createFromAsset(getAssets(), "fonts/anmollipinumbers.ttf");

		String keywords;
		String[] arrKeywords;
		String result = "";
		TextView Result, actual_word;

		actual_word = (TextView)findViewById(R.id.actual_word);
		actual_word.setTypeface(font);

		Result = (TextView)findViewById(R.id.results);
		Result.setTypeface(font);
		Result.setTextSize(20);
		Result.setMovementMethod(new ScrollingMovementMethod());

		keywords = readTextFile(this, R.raw.kosh);
		arrKeywords = keywords.split("\n");


		String hor_roop = "hor rUp:";

		int n = 0, m = 0;


		for(int i = 0; i < arrKeywords.length; i++){			
			if(arrKeywords[i].startsWith(MainActivity.search_word + " ---- ")){
				result = "^^" +  arrKeywords[i].substring(MainActivity.search_word.length() + 5, arrKeywords[i].length()) + "^^";
				for(int j = i + 1; !arrKeywords[j].contains(" ---- "); j++){
					if(arrKeywords[j].startsWith("- ")){
						result = result + "\n\t\t\t\t\t\t" + "##" + arrKeywords[j] + "##";
					}else if(arrKeywords[j].startsWith("    @@\"") && arrKeywords[j].endsWith("\"@@")){
						result = result + "\n" + arrKeywords[j];
					}else if(arrKeywords[j].contains(hor_roop)){
						result = result + "\n\n" + "$$" +  hor_roop + "$$";
					}else{
						result = result + "\n" + "^^" + arrKeywords[j] + "^^";
					}
				}
			}
			if(result == ""){
				if(arrKeywords[i].startsWith(MainActivity.search_word + " -")){
					for(n = i-1;!arrKeywords[n].contains(" ---- "); n--){
						if(arrKeywords[n].startsWith("- ")){
							result = "\t\t\t\t\t\t" + "##" + arrKeywords[n] + "##" + "\n" + result;
						}else if(arrKeywords[n].contains(hor_roop)){
							result = "\n" + "$$" + hor_roop + "$$" +  "\n" + result;
						}else if(arrKeywords[n].startsWith("    @@\"") && arrKeywords[n].endsWith("\"@@")){
							result = arrKeywords[n] + "\n" + result;
						}else{
							result =  "^^" + arrKeywords[n] + "^^" +"\n" + result;
						}
					}
					result = result +   "^^" +arrKeywords[i] + "^^" ;
					for(m = i+1; !arrKeywords[m].contains(" ---- "); m++){
						if(arrKeywords[m].startsWith("- ")){
							result = result + "\n\t\t\t\t\t\t" + "##" + arrKeywords[m] + "##";
						}else if(arrKeywords[m].startsWith("    @@\"") && arrKeywords[m].endsWith("\"@@")){
							result = result + "\n" + arrKeywords[m];
						}else if(arrKeywords[m].contains(hor_roop)){
							result = result + "\n" + "$$" + hor_roop + "$$";
						}else{
							result = result + "\n" + "^^" + arrKeywords[m] + "^^";
						}
					}
					result = "^^" +  arrKeywords[n] + "^^"  + "\n" + result;
				}
			}
		}

		if(result == ""){
			try {
				Class ourClass = Class.forName("gurbani.kosh.MainActivity");
				Intent ourIntent = new Intent(Kosh.this, ourClass);
				startActivity(ourIntent);
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}	
			Toast.makeText(getApplicationContext(), "Sorry, either the word is not in Gurmukhi Dictionary " +
					"or is spelled incorrectly. ", Toast.LENGTH_LONG).show();
		}
		
		

		CharSequence text = result;		
		for(int i = 0; i < result.length(); i++){
			text = setSpanBetweenTokens(text, "@@", new RelativeSizeSpan(1.1f), new ForegroundColorSpan(Color.RED));
			text = setSpanBetweenTokens(text, "##", new ForegroundColorSpan(Color.rgb(18, 131, 141)));
			text = setSpanBetweenTokens(text, "$$", new RelativeSizeSpan(1.4f), new ForegroundColorSpan(Color.rgb(233, 177, 23)));
			text = setSpanBetweenTokens(text, "^^", new RelativeSizeSpan(1.2f), new ForegroundColorSpan(Color.BLUE));

		}



		actual_word.setText(MainActivity.search_word + "--");
		actual_word.setTextColor(Color.BLACK);
		actual_word.setTextSize(28);

		Result.setText(text);
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		finish();
	}
	public static CharSequence setSpanBetweenTokens(CharSequence text,
			String token, CharacterStyle... cs)
	{
		// Start and end refer to the points where the span will apply
		int tokenLen = token.length();
		int start = text.toString().indexOf(token) + tokenLen;
		int end = text.toString().indexOf(token, start);

		if (start > -1 && end > -1)
		{
			// Copy the spannable string to a mutable spannable string
			SpannableStringBuilder ssb = new SpannableStringBuilder(text);
			for (CharacterStyle c : cs)
				ssb.setSpan(c, start, end, 0);

			// Delete the tokens before and after the span
			ssb.delete(end, end + tokenLen);
			ssb.delete(start - tokenLen, start);

			text = ssb;
		}

		return text;
	}
	public static String readTextFile(Context ctx, int resId){
		InputStream inputStream = ctx.getResources().openRawResource(resId);

		InputStreamReader inputreader = new InputStreamReader(inputStream);
		BufferedReader bufferedreader = new BufferedReader(inputreader);
		String line;
		StringBuilder stringBuilder = new StringBuilder();
		try 
		{
			while (( line = bufferedreader.readLine()) != null) 
			{
				stringBuilder.append(line);
				stringBuilder.append('\n');
			}
		} 
		catch (IOException e) 
		{
			return null;
		}
		return stringBuilder.toString();
	}
}
