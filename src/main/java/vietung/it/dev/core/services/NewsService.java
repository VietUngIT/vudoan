package vietung.it.dev.core.services;

import vietung.it.dev.apis.response.NewsResponse;

public interface NewsService {
    public NewsResponse likeNews(String idNews,Boolean isLike) throws Exception;
    public NewsResponse viewNews(String idNews) throws Exception;
    public NewsResponse commentNews(String idNews, String phone, String content) throws Exception;
    public NewsResponse deleteNews(String idNews, String phone);
}
