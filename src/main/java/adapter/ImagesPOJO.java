package adapter;

import android.os.Parcel;
import android.os.Parcelable;

public class ImagesPOJO implements Parcelable {

	private int id;
	private String name;
	private String imageUrl;

	public ImagesPOJO() {
		super();
	}

	private ImagesPOJO(Parcel in) {
		super();
		this.id = in.readInt();
		this.name = in.readString();
		this.imageUrl = in.readString();
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel parcel, int flags) {
		parcel.writeInt(getId());
		parcel.writeString(getName());
		parcel.writeString(getImageUrl());
	}

	public static final Parcelable.Creator<ImagesPOJO> CREATOR = new Parcelable.Creator<ImagesPOJO>() {
		public ImagesPOJO createFromParcel(Parcel in) {
			return new ImagesPOJO(in);
		}

		public ImagesPOJO[] newArray(int size) {
			return new ImagesPOJO[size];
		}
	};

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public static Parcelable.Creator<ImagesPOJO> getCreator() {
		return CREATOR;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ImagesPOJO other = (ImagesPOJO) obj;
		if (id != other.id)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Product [id=" + id + ", name=" + name + ", imageUrl="
				+ imageUrl + "]";
	}
}
