package vietung.it.dev.core.services;

import vietung.it.dev.apis.response.NewsResponse;

public interface NewsService {
    NewsResponse likeNews(String idNews,Boolean isLike, String phone) throws Exception;
    NewsResponse viewNews(String idNews) throws Exception;
    int commentNews(String idNews, Boolean isCmt);
    NewsResponse deleteNews(String idNews);
    NewsResponse getNewsById(String idNews,String phone);
    NewsResponse getAllNewsByCateWithNewest(int page, int ofset, String idcate);
    NewsResponse getAllNewsByCateWithFavorite(int page, int ofset, String idcate);
    NewsResponse getAllNewsByCateWithPopular(int page, int ofset, String idcate);
    NewsResponse createNews(String title, String shortDescription, String author, String image, String source, String tags, String idCateNews, String content) throws Exception;
    NewsResponse editNews(String idNews,String title, String shortDescription, String author,String source, String idCateNews,String content);
    NewsResponse editImageNews(String idNews,String image);
    NewsResponse editTagsNews(String idNews,String tags);

}
