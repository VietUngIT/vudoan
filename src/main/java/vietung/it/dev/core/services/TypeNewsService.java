package vietung.it.dev.core.services;

import vietung.it.dev.apis.response.BaseResponse;
import vietung.it.dev.apis.response.TypeNewsResponse;

public interface TypeNewsService {
    TypeNewsResponse addTypeNews(String nameType);

    TypeNewsResponse editTypeNews(String id, String nameType);

    TypeNewsResponse deleteTypeNews(String id);
}
