package com.shoneworn.supermvp.common.bean;


import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by chenxiangxiang on 2018/12/17.
 */

public class Type implements Parcelable {
    protected String id;
    protected String title;
    protected String type;
    protected String desc;
    protected int position;
    protected String createTime;
    protected boolean isSelected;

    public Type(){
        super();
    }

    public Type(String type, String createTime){
        this.type = type;
        this.createTime = createTime;
    }

    protected Type(Parcel source) {
        id = source.readString();
        title = source.readString();
        type = source.readString();
        desc = source.readString();
        position = source.readInt();
        createTime = source.readString();
        isSelected = (Boolean) source.readValue(ClassLoader.getSystemClassLoader());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(title);
        dest.writeString(type);
        dest.writeString(desc);
        dest.writeInt(position);
        dest.writeString(createTime);
        dest.writeValue(isSelected);
    }

    public static Creator<Type> CREATOR = new Creator<Type>() {

        @Override
        public Type[] newArray(int size) {
            return new Type[size];
        }

        @Override
        public Type createFromParcel(Parcel source) {
            return new Type(source);
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Type{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", type='" + type + '\'' +
                ", desc='" + desc + '\'' +
                ", position=" + position +
                ", createTime='" + createTime + '\'' +
                ", isSelected=" + isSelected +
                '}';
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public static Creator<Type> getCREATOR() {
        return CREATOR;
    }

    public static void setCREATOR(Creator<Type> CREATOR) {
        Type.CREATOR = CREATOR;
    }
}
