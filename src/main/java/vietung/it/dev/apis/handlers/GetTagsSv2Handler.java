package vietung.it.dev.apis.handlers;

import io.vertx.core.http.HttpServerRequest;
import vietung.it.dev.apis.response.BaseResponse;
import vietung.it.dev.core.consts.ErrorCode;
import vietung.it.dev.core.services.ForumQuestionService;
import vietung.it.dev.core.services.imp.ForumQuestionServiceImp;
import vietung.it.dev.core.utils.Utils;

public class GetTagsSv2Handler extends BaseApiHandler {
    @Override
    public BaseResponse handle(HttpServerRequest request) throws Exception {
        ForumQuestionService service = new ForumQuestionServiceImp();
        String type = request.getFormAttribute("t");
        if(type!=null){
            if(type.equals("tagsquestionforum")){
                String idQuestion = request.getFormAttribute("idquestion");
                String strTags = request.getFormAttribute("tags");
                return getTagsForumQuestionHandle(idQuestion,strTags,service);
            }else{
                return Utils.notifiError(ErrorCode.INVALID_PARAMS,"Invalid params.");
            }
        }else {
            return Utils.notifiError(ErrorCode.INVALID_PARAMS,"Invalid params.");
        }
    }

    private BaseResponse getTagsForumQuestionHandle(String idQuestion, String strTags, ForumQuestionService service) throws Exception {
        if(idQuestion!=null && strTags!=null){
            return service.getTagsForumQuestion(idQuestion,strTags);
        }else {
            return Utils.notifiError(ErrorCode.INVALID_PARAMS,"Invalid params.");
        }
    }
}
