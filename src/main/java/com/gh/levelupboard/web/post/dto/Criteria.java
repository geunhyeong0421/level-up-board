package com.gh.levelupboard.web.post.dto;

import com.gh.levelupboard.web.pagination.Pagination;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;


@Getter
@Setter
public class Criteria {

    private int page;
    private int size;

    private String type;
    private String keyword;

    private List<OptionListDto> typeOptions = new ArrayList<>();
    private List<OptionListDto> sizeOptions = new ArrayList<>();

    public Criteria() {
        page = 1;
        size = Pagination.POST.getSize();

        typeOptions.add(new OptionListDto("TC", "제목+내용"));
        typeOptions.add(new OptionListDto("T", "제목"));
        typeOptions.add(new OptionListDto("C", "내용"));
        typeOptions.add(new OptionListDto("W", "작성자"));
        
        sizeOptions.add(new OptionListDto("10", "10개씩"));
        sizeOptions.add(new OptionListDto("20", "20개씩"));
        sizeOptions.add(new OptionListDto("30", "30개씩"));
        sizeOptions.add(new OptionListDto("50", "50개씩"));
    }

    public Pageable getPageable() { // 파라미터 유효성 검사
        if (page < 1) { // 1보다 작을 때는
            page = 1; // 첫 페이지로 설정
        }

        if (size <= 10) {
            size = 10;
            sizeOptions.get(0).select();
        } else if (size <= 20) {
            size = 20;
            sizeOptions.get(1).select();
        } else if (size <= 30) {
            size = 30;
            sizeOptions.get(2).select();
        } else {
            size = 50;
            sizeOptions.get(3).select();
        }

        return PageRequest.of(page - 1, size); // (실제 페이지 - 1)의 값을 사용
    }

    public String getTypeAfterSet() {
        if (StringUtils.hasText(keyword)) {
            boolean selected = false;
            for (OptionListDto typeOption : typeOptions) {
                if (typeOption.getValue().equals(type)) {
                    typeOption.select();
                    selected = true;
                    break;
                }
            }
            if (!selected) {
                type = "TC"; // 기본값으로 설정
            }
        } else {
            type = null;
            keyword = null;
        }
        return type;
    }

}
