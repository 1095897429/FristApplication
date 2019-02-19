package com.ngbj.browse.event;

import com.ngbj.browse.bean.LoginBean;

public class UpdateEvent {
    public LoginBean loginBean;
    public UpdateEvent(LoginBean loginBean){
        this.loginBean = loginBean;
    }

    public LoginBean getLoginBean() {
        return loginBean;
    }
}
