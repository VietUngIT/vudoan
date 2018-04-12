package vietung.it.dev.core.services;

import vietung.it.dev.apis.response.QAQuestionResponse;

public interface QAQuestionService {
    QAQuestionResponse delQAQuestion(String id) throws Exception;

    QAQuestionResponse editQAQuestion(String id, String idfield, String content) throws Exception;

    QAQuestionResponse addQAQuestion(String idfield, String content) throws Exception;

    QAQuestionResponse getQAQueationByIdField(int page, int ofset, String idfield) throws Exception;
}
