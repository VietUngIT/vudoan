package vietung.it.dev.core.services;

import vietung.it.dev.apis.response.BaseResponse;
import vietung.it.dev.apis.response.TypeNewsResponse;

public interface TypeNewsService {
    TypeNewsResponse getTypeNews(String id);
    TypeNewsResponse getAllTypeNews(int typeCate);
    TypeNewsResponse addTypeNews(String nameType,int typeCate);
    TypeNewsResponse editTypeNews(String id, String nameType);
}
