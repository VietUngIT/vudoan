package vietung.it.dev.core.services;

import vietung.it.dev.apis.response.CommentResponse;

public interface CommentService {
    CommentResponse commentNews(String idNews, String phone, String content, int collection) throws Exception;

    CommentResponse deleteCommentNews(String idCmnt, String phone, int collection) throws Exception;

    CommentResponse editCommentNews(String idCmnt, String phone, String content, int collection) throws Exception;

    CommentResponse getCommentbyNews(String idNews, int offset, int page, int collection) throws Exception;
}
