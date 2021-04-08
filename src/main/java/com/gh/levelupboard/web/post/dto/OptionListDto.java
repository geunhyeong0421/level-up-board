package com.gh.levelupboard.web.post.dto;

import lombok.Getter;

@Getter
public class OptionListDto {

    private String value;
    private String name;

    private boolean isSelected;

    public OptionListDto(String value, String name) {
        this.value = value;
        this.name = name;
    }

    public void setSelected() {
        isSelected = true;
    }

}
