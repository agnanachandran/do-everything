package ca.pluszero.emotive;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import java.io.File;

public class MusicLauncher {
	
	private final Context context;
	private static MusicLauncher instance = null;

	private MusicLauncher(Context context) {
		this.context = context;
	}
	
	public static MusicLauncher getInstance(Context context) {
		if (instance == null) {
			instance = new MusicLauncher(context);
		}
		return instance;
	}
	
	public void startMusic(String songFileName) {
		File file = new File(songFileName);
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setDataAndType(Uri.fromFile(file), "audio/*");
		context.startActivity(intent);
	}

}
