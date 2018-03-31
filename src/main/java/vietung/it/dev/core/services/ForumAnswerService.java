package vietung.it.dev.core.services;

import vietung.it.dev.apis.response.BaseResponse;
import vietung.it.dev.apis.response.ForumAnswerResponse;

public interface ForumAnswerService {
    ForumAnswerResponse getAnswerByQuestion(int page, int ofset, String id, String phone) throws Exception;

    ForumAnswerResponse likeAnswer(String id, Boolean isLike, String phone) throws Exception;

    ForumAnswerResponse delAnswer(String phone, String id) throws Exception;

    ForumAnswerResponse editAnswer(String phone, String id, String content) throws Exception;

    ForumAnswerResponse addAnswer(String id,String phone, String content) throws Exception;
}
