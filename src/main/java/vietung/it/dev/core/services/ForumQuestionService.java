package vietung.it.dev.core.services;

import vietung.it.dev.apis.response.BaseResponse;
import vietung.it.dev.apis.response.ForumQuestionResponse;

public interface ForumQuestionService {
    ForumQuestionResponse delQuestion(String phone, String id) throws Exception;

    ForumQuestionResponse editQuestion(String phone, String id, String content) throws Exception;

    ForumQuestionResponse likeQuestion(String id, Boolean isLike, String phone) throws Exception;

    int commentQuestion(String id, Boolean isCmt) throws Exception;

    ForumQuestionResponse getQuestionByField(int page, int ofset, String id, String phone) throws Exception;

    ForumQuestionResponse getQuestionByID(String id, String phone) throws Exception;

    ForumQuestionResponse addQuestion(String phone, String image, String idField, String content) throws Exception;

    ForumQuestionResponse getExpertByIDQuestion(String id, int numExpert) throws Exception;
}
