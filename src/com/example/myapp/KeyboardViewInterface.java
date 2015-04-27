package com.example.myapp;

public interface KeyboardViewInterface {
	public void onKeyDown(String value, int location[], int width);
	public void onKeyUp(String string);
	public void setDraft(boolean isDraft);
}
