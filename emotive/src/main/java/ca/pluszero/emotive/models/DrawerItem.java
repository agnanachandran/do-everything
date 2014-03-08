package ca.pluszero.emotive.models;

import android.graphics.drawable.Drawable;

public class DrawerItem {
	
	private String drawerText;
	private Drawable drawable;

	public DrawerItem(String drawerText, Drawable drawable) {
		this.setDrawerText(drawerText);
		this.setDrawable(drawable);
	}

	public String getDrawerText() {
		return drawerText;
	}

	public void setDrawerText(String drawerText) {
		this.drawerText = drawerText;
	}

	public Drawable getDrawable() {
		return drawable;
	}

	public void setDrawable(Drawable drawable) {
		this.drawable = drawable;
	}

}
