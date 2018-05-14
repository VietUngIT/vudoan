package vietung.it.dev.core.services;

import com.google.gson.JsonObject;
import org.jongo.Jongo;
import vietung.it.dev.apis.response.BaseResponse;
import vietung.it.dev.apis.response.QAQuestionResponse;
import vietung.it.dev.core.models.Report;

public interface QAQuestionService {
    QAQuestionResponse delQAQuestion(String id) throws Exception;

    QAQuestionResponse editQAQuestion(String id,String title, String content,String answer) throws Exception;

    QAQuestionResponse addQAQuestion(String idfield,String title, String content,String answer) throws Exception;

    QAQuestionResponse getQAQueationByIdField(int page, int ofset, String idfield) throws Exception;

    QAQuestionResponse searchQA(String content, String id) throws Exception;

    Report getQAForDashBoard(Jongo jongo) throws Exception;
}
