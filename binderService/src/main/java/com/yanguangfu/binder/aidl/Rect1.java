package com.yanguangfu.binder.aidl;

import android.os.Parcel;
import android.os.Parcelable;

public class Rect1 implements Parcelable {
	public int left;
	public int top;
	public int right;
	public int bottom;
	public static final Parcelable.Creator<Rect1> CREATOR = new Parcelable.Creator<Rect1>() {
		public Rect1 createFromParcel(Parcel in) {
			return new Rect1(in);
		}

		public Rect1[] newArray(int size) {
			return new Rect1[size];
		}
	};

	public Rect1() {
	}

	private Rect1(Parcel in) {
		readFromParcel(in);
	}

	public void readFromParcel(Parcel in) {
		left = in.readInt();
		top = in.readInt();
		right = in.readInt();
		bottom = in.readInt();
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel out, int flags) {
		out.writeInt(left);
		out.writeInt(top);
		out.writeInt(right);
		out.writeInt(bottom);
	}

}
