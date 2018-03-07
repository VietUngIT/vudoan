package vietung.it.dev.core.services;

import vietung.it.dev.apis.response.NewsResponse;

public interface CommentNewsService {
    public NewsResponse commentNews(String idNews, String phone, String content) throws Exception;
    public NewsResponse deleteCommentNews(String idCmt,String phone);
    public NewsResponse getCommentbyNews(String idNews,int offset, long timeLast);
}
