package com.petgang.web.model.user;

import com.petgang.model.user.User;

public class RegisterUserView {

	private int uid;

	private String nn;

	private String furl;

	private int gender;

	public int getUid() {
		return uid;
	}

	public void setUid(int uid) {
		this.uid = uid;
	}

	public String getNn() {
		return nn;
	}

	public void setNn(String nn) {
		this.nn = nn;
	}

	public String getFurl() {
		return furl;
	}

	public void setFurl(String furl) {
		this.furl = furl;
	}

	public int getGender() {
		return gender;
	}

	public void setGender(int gender) {
		this.gender = gender;
	}

	public static RegisterUserView form(User user) {
		RegisterUserView view = new RegisterUserView();
		view.setUid(user.getId());
		view.setFurl(user.getFaceUrl());
		view.setNn(user.getNickName());
		view.setGender(user.getGender());
		return view;
	}
}
