package com.ngbj.browse.bean;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

/**
 * Date:2018/7/19
 * author:zl
 * 备注：楼盘信息
 */
public class BuildingBean implements Parcelable {
    private long ProjectId;//楼盘Id
    private String ProjectName;//楼盘名
    private String ProjectShortName;//楼盘简称
    private int AvgPrice;
    private String PageViews;//浏览量
    private String ProjectCity;//楼盘所在城市
    private String ProjectDistrict;//楼盘所在区域
    private String CoverImg;//楼盘图片路径
    private double PointX;//楼盘X坐标
    private double PointY;//楼盘Y坐标
    private String Distance;//当前定位的位置与楼盘的距离
    private String ExtCommissionRuleCount;//佣金方案总数
    private String ExtCommissionRuleValue;//最新的佣金数据
    private String TagIds;//楼盘标签
    private String ShowObjects;
    public void setProjectId(long ProjectId) {
        this.ProjectId = ProjectId;
    }
    public long getProjectId() {
        return ProjectId;
    }

    public void setProjectName(String ProjectName) {
        this.ProjectName = ProjectName;
    }
    public String getProjectName() {
        return ProjectName;
    }

    public void setProjectShortName(String ProjectShortName) {
        this.ProjectShortName = ProjectShortName;
    }
    public String getProjectShortName() {
        return ProjectShortName;
    }

    public void setAvgPrice(int AvgPrice) {
        this.AvgPrice = AvgPrice;
    }
    public int getAvgPrice() {
        return AvgPrice;
    }

    public void setPageViews(String PageViews) {
        this.PageViews = PageViews;
    }
    public String getPageViews() {
        return PageViews;
    }

    public void setProjectCity(String ProjectCity) {
        this.ProjectCity = ProjectCity;
    }
    public String getProjectCity() {
        return ProjectCity;
    }

    public void setProjectDistrict(String ProjectDistrict) {
        this.ProjectDistrict = ProjectDistrict;
    }
    public String getProjectDistrict() {
        return ProjectDistrict;
    }

    public void setCoverImg(String CoverImg) {
        this.CoverImg = CoverImg;
    }
    public String getCoverImg() {
        return CoverImg;
    }

    public void setPointX(double PointX) {
        this.PointX = PointX;
    }
    public double getPointX() {
        return PointX;
    }

    public void setPointY(double PointY) {
        this.PointY = PointY;
    }
    public double getPointY() {
        return PointY;
    }

    public void setDistance(String Distance) {
        this.Distance = Distance;
    }
    public String getDistance() {
        return Distance;
    }

    public void setExtCommissionRuleCount(String ExtCommissionRuleCount) {
        this.ExtCommissionRuleCount = ExtCommissionRuleCount;
    }
    public String getExtCommissionRuleCount() {
        return ExtCommissionRuleCount;
    }

    public void setExtCommissionRuleValue(String ExtCommissionRuleValue) {
        this.ExtCommissionRuleValue = ExtCommissionRuleValue;
    }
    public String getExtCommissionRuleValue() {
        return ExtCommissionRuleValue;
    }

    public void setTagIds(String TagIds) {
        this.TagIds = TagIds;
    }
    public String getTagIds() {
        return TagIds;
    }

    public String getShowObjects() {
        return ShowObjects;
    }

    public void setShowObjects(String showObjects) {
        ShowObjects = showObjects;
    }

    /** 在类中转换一下 */
    public String[] getTagNames(){
        if(!TextUtils.isEmpty(TagIds)){
            return TagIds.split(",");
        }
        return null;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.ProjectId);
        dest.writeString(this.ProjectName);
        dest.writeString(this.ProjectShortName);
        dest.writeInt(this.AvgPrice);
        dest.writeString(this.PageViews);
        dest.writeString(this.ProjectCity);
        dest.writeString(this.ProjectDistrict);
        dest.writeString(this.CoverImg);
        dest.writeDouble(this.PointX);
        dest.writeDouble(this.PointY);
        dest.writeString(this.Distance);
        dest.writeString(this.ExtCommissionRuleCount);
        dest.writeString(this.ExtCommissionRuleValue);
        dest.writeString(this.TagIds);
        dest.writeString(this.ShowObjects);
    }

    public BuildingBean() {
    }

    protected BuildingBean(Parcel in) {
        this.ProjectId = in.readLong();
        this.ProjectName = in.readString();
        this.ProjectShortName = in.readString();
        this.AvgPrice = in.readInt();
        this.PageViews = in.readString();
        this.ProjectCity = in.readString();
        this.ProjectDistrict = in.readString();
        this.CoverImg = in.readString();
        this.PointX = in.readDouble();
        this.PointY = in.readDouble();
        this.Distance = in.readString();
        this.ExtCommissionRuleCount = in.readString();
        this.ExtCommissionRuleValue = in.readString();
        this.TagIds = in.readString();
        this.ShowObjects = in.readString();
    }

    public static final Creator<BuildingBean> CREATOR = new Creator<BuildingBean>() {
        @Override
        public BuildingBean createFromParcel(Parcel source) {
            return new BuildingBean(source);
        }

        @Override
        public BuildingBean[] newArray(int size) {
            return new BuildingBean[size];
        }
    };
}
