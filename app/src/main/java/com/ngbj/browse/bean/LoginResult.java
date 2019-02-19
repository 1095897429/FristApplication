package com.ngbj.browse.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Date:2018/7/18
 * author:zl
 * 备注：Type 身份类型（1 职业经纪人  2 自由经纪人）
 *       AgentId 经纪人Id
 */
public class LoginResult implements Parcelable {

    private int AgentId;
    private int Type;

    protected LoginResult(Parcel in) {
        AgentId = in.readInt();
        Type = in.readInt();
    }

    public static final Creator<LoginResult> CREATOR = new Creator<LoginResult>() {
        @Override
        public LoginResult createFromParcel(Parcel in) {
            return new LoginResult(in);
        }

        @Override
        public LoginResult[] newArray(int size) {
            return new LoginResult[size];
        }
    };

    public int getAgentId() {
        return AgentId;
    }

    public void setAgentId(int agentId) {
        AgentId = agentId;
    }

    public int getType() {
        return Type;
    }

    public void setType(int type) {
            Type = type;
        }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(AgentId);
        parcel.writeInt(Type);
    }
}
