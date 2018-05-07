package vietung.it.dev.core.services;

import vietung.it.dev.apis.response.BaseResponse;
import vietung.it.dev.apis.response.QAQuestionResponse;

public interface QAQuestionService {
    QAQuestionResponse delQAQuestion(String id) throws Exception;

    QAQuestionResponse editQAQuestion(String id,String title, String content,String answer) throws Exception;

    QAQuestionResponse addQAQuestion(String idfield,String title, String content,String answer) throws Exception;

    QAQuestionResponse getQAQueationByIdField(int page, int ofset, String idfield) throws Exception;

    QAQuestionResponse searchQA(String content, String id) throws Exception;
}
