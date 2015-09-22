package gurbani.kosh;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class MainActivity extends Activity {
	static EditText original_text;
	static Button search;
	static ImageButton jankari, rate, android, facebook, hide_image;
	static String search_word;
	static char[] search_word_arr;
	int i;
	static int y = 0, hide_cnt = 0;
	static TextView link;

	View view;
	SharedPreferences sp;
	SharedPreferences.Editor spe;
//	static Button q, Q, e, E, r, t, T, p, P, a, A, s, S, d, D, f, F, g, G, h, j, J, k, K, l, L, 
//		aa, bb, z, Z, x, X, c, C, v, V, b, B, n, m, yy, Y, u, U, ii, I, o, O, H, cc, dd, ee, ff, R, w,
//		W, N, M, FF;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main2);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
		link = (TextView) findViewById(R.id.link);
		link.setClickable(true);
		link.setMovementMethod(LinkMovementMethod.getInstance());
		String text = "<a href='https://play.google.com/store/apps/details?id=com.inputmethod.gurmukhi'> Gurmukhi Keyboard</a>";
		link.setText(Html.fromHtml(text));

		original_text = (EditText) findViewById(R.id.word_to_search);

		jankari = (ImageButton) findViewById(R.id.jankari);
		rate = (ImageButton) findViewById(R.id.rate);
		android = (ImageButton) findViewById(R.id.apps);
		facebook = (ImageButton) findViewById(R.id.facebook);


		sp = PreferenceManager.getDefaultSharedPreferences(this);
		spe = sp.edit();
		view = findViewById(R.id.view);
		hide_image = (ImageButton) findViewById(R.id.hide_image);
		hide_cnt = sp.getInt("hide", 1);

		if (hide_cnt == 0) {
			view.setVisibility(View.GONE);
		}
		hide_image.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {


				if (view.getVisibility() == View.GONE) {
					view.setVisibility(View.VISIBLE);
					hide_image.setImageResource(R.drawable.minimize);
					spe.putInt("hide", 1);
					spe.commit();
				} else {
					view.setVisibility(View.GONE);
					hide_image.setImageResource(R.drawable.maximize);
					spe.putInt("hide", 0);
					spe.commit();
				}

			}
		});

		rate.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Uri uri = Uri
						.parse("https://play.google.com/store/apps/details?id=gurbani.kosh&hl=en");
				startActivity(new Intent(Intent.ACTION_VIEW, uri));

			}
		});

		android.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Uri uri = Uri
						.parse("https://play.google.com/store/apps/developer?id=IveSingh");
				startActivity(new Intent(Intent.ACTION_VIEW, uri));

			}
		});

		facebook.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Uri uri = Uri
						.parse("https://www.facebook.com/pages/IveSingh-Apps/1413125452234300");
				startActivity(new Intent(Intent.ACTION_VIEW, uri));

			}
		});



		jankari.setOnClickListener(new View.OnClickListener() {

			@SuppressLint("NewApi")
			@Override
			public void onClick(View v) {
				Dialog dialog = new Dialog(MainActivity.this);
				dialog.setContentView(R.layout.jankari);
				WebView wb = (WebView) dialog.findViewById(R.id.webview);
				wb.loadUrl("file:///android_asset/jankari/Jankari.html");
				wb.getSettings().setBuiltInZoomControls(true);
				if (Build.VERSION.SDK_INT > Build.VERSION_CODES.HONEYCOMB) {
					wb.getSettings().setDisplayZoomControls(false);

				}
				dialog.setCancelable(true);
				dialog.setTitle("  Help");
				dialog.show();	
			}
		});

		search = (Button) findViewById(R.id.search);
		search.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				if(original_text.getText().toString().length() == 0){
					Toast.makeText(getApplicationContext(), "Please, enter a word.",  Toast.LENGTH_LONG).show();
				}else{
					y++;
					if(y > 1){
						search_word = "";
					}
					search_word_arr	= original_text.getText().toString().toCharArray();
					for(i = 0; i < original_text.length(); i++){	
						check_words();
					}	
					check_i();

					if(search_word.contains("null")){
						search_word = search_word.substring(4, search_word.length());
					}
					if(search_word.length() > 1){
						try {
							Class ourClass = Class.forName("gurbani.kosh.Kosh");
							Intent ourIntent = new Intent(MainActivity.this, ourClass);
							startActivity(ourIntent);
						} catch (ClassNotFoundException e) {
							e.printStackTrace();
						}	
					}else{
						Toast.makeText(getApplicationContext(), "Sorry, but the word you searched for is not in Gurmukhi Dictionary. ", Toast.LENGTH_LONG).show();
					}
				}
			}
			private void check_words() {
				if(search_word_arr[i] == ('ਕ') || search_word_arr[i] == ('k')){
					search_word = search_word + "k";

				}else if(search_word_arr[i] == ('ਖ') || search_word_arr[i] == ('K')){
					search_word = search_word + "K";

				}else if(search_word_arr[i] == ('ਗ') || search_word_arr[i] == ('g')){
					search_word = search_word + "g";

				}else if(search_word_arr[i] == ('ਘ') || search_word_arr[i] == ('G')){
					search_word = search_word + "G";

				}else if(search_word_arr[i] == ('ਙ') || search_word_arr[i] == ('|')){
					search_word = search_word + "|";

				}else if(search_word_arr[i] == ('ਚ') || search_word_arr[i] == ('c')){
					search_word = search_word + "c";

				}else if(search_word_arr[i] == ('ਛ') || search_word_arr[i] == ('C')){
					search_word = search_word + "C";

				}else if(search_word_arr[i] == ('ਜ') || search_word_arr[i] == ('j')){
					search_word = search_word + "j";

				}else if(search_word_arr[i] == ('ਝ') || search_word_arr[i] == ('J')){
					search_word = search_word + "J";

				}else if(search_word_arr[i] == ('ਞ') || search_word_arr[i] == ('\\')){
					search_word = search_word + "\\";

				}else if(search_word_arr[i] == ('ਟ') || search_word_arr[i] == ('t')){
					search_word = search_word + "t";

				}else if(search_word_arr[i] == ('ਠ') || search_word_arr[i] == ('T')){
					search_word = search_word + "T";

				}else if(search_word_arr[i] == ('ਡ') || search_word_arr[i] == ('f')){
					search_word = search_word + "f";

				}else if(search_word_arr[i] == ('ਢ') || search_word_arr[i] == ('F')){
					search_word = search_word + "F";

				}else if(search_word_arr[i] == ('ਣ') || search_word_arr[i] == ('x')){
					search_word = search_word + "x";

				}else if(search_word_arr[i] == ('ਤ') || search_word_arr[i] == ('q')){
					search_word = search_word + "q";

				}else if(search_word_arr[i] == ('ਥ') || search_word_arr[i] == ('Q')){
					search_word = search_word + "Q";

				}else if(search_word_arr[i] == ('ਦ') || search_word_arr[i] == ('d')){
					search_word = search_word + "d";

				}else if(search_word_arr[i] == ('ਧ') || search_word_arr[i] == ('D')){
					search_word = search_word + "D";

				}else if(search_word_arr[i] == ('ਨ') || search_word_arr[i] == ('n')){
					search_word = search_word + "n";

				}else if(search_word_arr[i] == ('ਪ') || search_word_arr[i] == ('p')){
					search_word = search_word + "p";

				}else if(search_word_arr[i] == ('ਫ') || search_word_arr[i] == ('P')){
					search_word = search_word + "P";

				}else if(search_word_arr[i] == ('ਬ') || search_word_arr[i] == ('b')){
					search_word = search_word + "b";

				}else if(search_word_arr[i] == ('ਭ') || search_word_arr[i] == ('B')){
					search_word = search_word + "B";

				}else if(search_word_arr[i] == ('ਮ') || search_word_arr[i] == ('m')){
					search_word = search_word + "m";

				}else if(search_word_arr[i] == ('ਯ') || search_word_arr[i] == ('X')){
					search_word = search_word + "X";
				}
				else if(search_word_arr[i] == ('ਰ') || search_word_arr[i] == ('r')){
					search_word = search_word + "r";

				}else if(search_word_arr[i] == ('ਲ') || search_word_arr[i] == ('l')){
					search_word = search_word + "l";

				}else if(search_word_arr[i] == ('ਲ਼') || search_word_arr[i] == ('L')){
					search_word = search_word + "L";

				}else if(search_word_arr[i] == ('ਵ') || search_word_arr[i] == ('v')){
					search_word = search_word + "v";

				}else if(search_word_arr[i] == ('ਸ਼') || search_word_arr[i] == ('S')){
					search_word = search_word + "S";

				}
				else if(search_word_arr[i] == ('ਸ') || search_word_arr[i] == ('s')){
					search_word = search_word + "s";

				}else if(search_word_arr[i] == ('ਹ') || search_word_arr[i] == ('h')){
					search_word = search_word + "h";

				}else if(search_word_arr[i] == ('ਖ਼') || search_word_arr[i] == ('^')){
					search_word = search_word + "^";

				}else if(search_word_arr[i] == ('ਗ਼') || search_word_arr[i] == ('Z')){
					search_word = search_word + "Z";

				}else if(search_word_arr[i] == ('ਜ਼') || search_word_arr[i] == ('z')){
					search_word = search_word + "z";

				}else if(search_word_arr[i] == ('ੜ') || search_word_arr[i] == ('V')){
					search_word = search_word + "V";

				}else if(search_word_arr[i] == ('ਫ਼') || search_word_arr[i] == ('&')){
					search_word = search_word + "&";

				}else if(search_word_arr[i] == ('ਅ') || search_word_arr[i] == ('A')){
					search_word = search_word + "A";

				}else if(search_word_arr[i] == ('ਆ')){
					search_word = search_word + "Aw";

				}else if(search_word_arr[i] == ('ਇ')){
					search_word = search_word + "ie";
				}
				else if(search_word_arr[i] == ('ਈ')){
					search_word = search_word + "eI";

				}else if(search_word_arr[i] == ('ਉ')){	//done
					search_word = search_word + "au";

				}else if(search_word_arr[i] == ('ਊ')){	//done
					search_word = search_word + "aU";

				}else if(search_word_arr[i] == ('ਏ')){	//done
					search_word = search_word + "ey";

				}else if(search_word_arr[i] == ('ਐ')){	//done
					search_word = search_word + "AY";

				}else if(search_word_arr[i] == ('ਓ') || search_word_arr[i] == ('E')){
					search_word = search_word + "E";

				}else if(search_word_arr[i] == ('ਔ')){ //done
					search_word = search_word + "AO";

				}else if(search_word_arr[i] == ('ੲ') || search_word_arr[i] == ('e')){
					search_word = search_word + "e";

				}else if(search_word_arr[i] == ('ੳ') || search_word_arr[i] == ('a')){
					search_word = search_word + "a";

				}else if(search_word_arr[i] == ('ਾ') || search_word_arr[i] == ('w')){
					search_word = search_word + "w";

				}
				
				else if(search_word_arr[i] == ('ਿ') || search_word_arr[i] == ('i')){
					search_word = search_word + "i";

				}else if(search_word_arr[i] == ('ੀ') || search_word_arr[i] == ('I')){
					search_word = search_word + "I";

				}else if(search_word_arr[i] == ('ੁ') || search_word_arr[i] == ('u')){
					search_word = search_word + "u";

				}else if(search_word_arr[i] == ('ੂ') || search_word_arr[i] == ('U')){
					search_word = search_word + "U";

				}else if(search_word_arr[i] == ('ੇ') || search_word_arr[i] == ('y')){
					search_word = search_word + "y";
				}
				else if(search_word_arr[i] == ('ੈ') || search_word_arr[i] == ('Y')){
					search_word = search_word + "Y";

				}else if(search_word_arr[i] == ('ੋ') || search_word_arr[i] == ('o')){
					search_word = search_word + "o";

				}else if(search_word_arr[i] == ('ੌ') || search_word_arr[i] == ('O')){
					search_word = search_word + "O";

				}else if(search_word_arr[i] == ('ੰ') || search_word_arr[i] == ('M')){
					search_word = search_word + "M";

				}else if(search_word_arr[i] == ('ੱ') || search_word_arr[i] == ('`')){
					search_word = search_word + "`";

				}else if(search_word_arr[i] == ('ਂ') || search_word_arr[i] == ('N')){
					search_word = search_word + "N";

				}else if(search_word_arr[i] == ('ਁ')){

				}else if(search_word_arr[i] == ('੍') && search_word_arr[i+1] == ('ਰ')){
					search_word = search_word + "R";
					i = i+1;
				}else if(search_word_arr[i] == ('R')){
					search_word = search_word + "R";
					
				}else if(search_word_arr[i] == ('O')){
					search_word = search_word + "O";
					
				}else if(search_word_arr[i] == ('W')){
					search_word = search_word + "W";
				}
				
			}

			private void check_i(){
				if(search_word.contains("i")){
					char[] wordArr = search_word.toCharArray();
					char temp;
					for(int j = 0; j < wordArr.length; j++){
						if(wordArr[j] == 'i'){
							temp = wordArr[j-1];
							wordArr[j-1] = 'i';
							wordArr[j] = temp;
						}
					}
					String word = "";
					for(int k = 0; k < wordArr.length; k++){
						word =  word+ wordArr[k];
					}
					search_word = word;
				}

			}

		});

		original_text.setTextColor(Color.WHITE);

		original_text.setOnEditorActionListener(new TextView.OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				if (actionId == EditorInfo.IME_ACTION_SEARCH) {
					search.performClick();
					return true;
				}
				return false;
			}

		});
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
