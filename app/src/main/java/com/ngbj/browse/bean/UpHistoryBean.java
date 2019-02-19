package com.ngbj.browse.bean;

import java.io.Serializable;
import java.util.List;

/**
 * 上传历史记录 Bean
 *
 */
public class UpHistoryBean implements Serializable{

    private String device_id;
    private List<String> visit_link;
    private List<String> search_word;

    public String getDevice_id() {
        return device_id;
    }

    public void setDevice_id(String device_id) {
        this.device_id = device_id;
    }

    public List<String> getVisit_link() {
        return visit_link;
    }

    public void setVisit_link(List<String> visit_link) {
        this.visit_link = visit_link;
    }

    public List<String> getSearch_word() {
        return search_word;
    }

    public void setSearch_word(List<String> search_word) {
        this.search_word = search_word;
    }
}
