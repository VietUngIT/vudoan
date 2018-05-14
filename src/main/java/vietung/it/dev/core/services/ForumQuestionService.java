package vietung.it.dev.core.services;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import io.vertx.core.json.Json;
import org.jongo.Jongo;
import vietung.it.dev.apis.response.BaseResponse;
import vietung.it.dev.apis.response.ForumQuestionResponse;
import vietung.it.dev.core.models.Report;

import java.util.Calendar;

public interface ForumQuestionService {
    ForumQuestionResponse delQuestion(String phone, String id) throws Exception;

    ForumQuestionResponse editQuestion(String phone, String id, String content) throws Exception;

    ForumQuestionResponse likeQuestion(String id, Boolean isLike, String phone) throws Exception;

    int commentQuestion(String id, Boolean isCmt) throws Exception;

    ForumQuestionResponse getQuestionByField(int page, int ofset, String id, String phone) throws Exception;

    ForumQuestionResponse getQuestionByID(String id, String phone) throws Exception;

    ForumQuestionResponse addQuestion(String phone, String image, String idField, String content, int nExpert) throws Exception;

    ForumQuestionResponse getExpertByIDQuestion(String id,double lat,double lon,int status) throws Exception;

    ForumQuestionResponse getQuestionAll(int page,int ofset,String phone)throws Exception;

    ForumQuestionResponse getTagsForumQuestion(String idQuestion, String strTags) throws Exception;

    String getForumQuestionByDayForDashBoard(Jongo jongo) throws Exception;
    Report getReportQuestionByDayForDashBoard(Jongo jongo, Calendar calendarST, Calendar calendarED) throws Exception;

    ForumQuestionResponse getExpertByIDUserQuestion(String id, int ofs, int page, String phone) throws Exception;

    ForumQuestionResponse getExpertByIDExpertQuestion(String id, int ofs, int page) throws Exception;
}
