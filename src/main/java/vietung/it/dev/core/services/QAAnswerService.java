package vietung.it.dev.core.services;

import vietung.it.dev.apis.response.QAAnswerResponse;

public interface QAAnswerService {
    QAAnswerResponse delQAAnswer(String id) throws Exception;

    QAAnswerResponse editQAAnswer(String id, String idQuestion, String content) throws Exception;

    QAAnswerResponse addQAAnswer(String idQuestion, String content) throws Exception;

    QAAnswerResponse getQAAnswerByIDQuestion(String idQuestion, int page, int ofset) throws Exception;
}
