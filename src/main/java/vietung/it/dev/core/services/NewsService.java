package vietung.it.dev.core.services;

import vietung.it.dev.apis.response.NewsResponse;

public interface NewsService {
    NewsResponse likeNews(String idNews,Boolean isLike) throws Exception;
    NewsResponse viewNews(String idNews) throws Exception;
    int commentNews(String idNews, Boolean isCmt);
    NewsResponse deleteNews(String idNews);
    NewsResponse getNewsById(String idNews);
    NewsResponse getAllNews(long tlast, int offer);
    NewsResponse getAllNewsByType(long tlast, int offer, String idType);
    NewsResponse createNews(String title, String shortDescription, String author, String image, String source, String tags, String typeNews,String nameTypeNews, String content);
    NewsResponse editNews(String idNews,String title, String shortDescription, String author, String image, String source, String tags, String typeNews,String nameTypeNews, String content);

}
