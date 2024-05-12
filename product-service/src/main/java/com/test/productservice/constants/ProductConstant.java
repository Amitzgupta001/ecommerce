package com.test.productservice.constants;

import org.springframework.data.domain.Sort;

public interface ProductConstant {
    String DEFAULT_PAGE_NO = "0";
    String DEFAULT_PAGE_SIZE = "30";
    String DEFAULT_SORT_BY = "productId,asc";
    CharSequence DESC = "desc";
    Sort.Direction sortOrderAsc = Sort.Direction.ASC;
    Sort.Direction sortOrderDes = Sort.Direction.DESC;
}
