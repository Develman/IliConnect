package com.android.iliConnect.message;

public interface IliOnClickListener {
	
	public void onClickMessageBox();
	public void onClickCoursePassword(String refID,String password);
	public void onClickJoinCourse(String refID, String courseName);
	public void onClickLeftCourse(String refID, String courseName);
}
