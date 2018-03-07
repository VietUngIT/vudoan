package vietung.it.dev.core.services;

import vietung.it.dev.apis.response.NewsResponse;

public interface NewsService {
    public NewsResponse likeNews(String idNews,Boolean isLike) throws Exception;
    public NewsResponse viewNews(String idNews) throws Exception;

    public NewsResponse deleteNews(String idNews);
    public NewsResponse getNewsById(String idNews);
    public NewsResponse getAllNews(long tlast, int offer);
    public NewsResponse getAllNewsByType(long tlast, int offer, int idType);

}
