package vietung.it.dev.core.services.imp;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.mongodb.DB;
import org.apache.commons.collections4.CollectionUtils;
import org.bson.types.ObjectId;
import org.jongo.Jongo;
import org.jongo.MongoCollection;
import org.jongo.MongoCursor;
import vietung.it.dev.apis.response.ForumQuestionResponse;
import vietung.it.dev.core.config.KafkaProduce;
import vietung.it.dev.core.config.MongoPool;
import vietung.it.dev.core.consts.ErrorCode;
import vietung.it.dev.core.models.*;
import vietung.it.dev.core.services.ExpertService;
import vietung.it.dev.core.services.FieldOfExpertService;
import vietung.it.dev.core.services.ForumQuestionService;
import vietung.it.dev.core.services.UploadService;
import vietung.it.dev.core.utils.Utils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

public class ForumQuestionServiceImp implements ForumQuestionService {
    @Override
    public ForumQuestionResponse delQuestion(String phone, String id) throws Exception {
        ForumQuestionResponse response = new ForumQuestionResponse();
        if (!ObjectId.isValid(id)) {
            response.setError(ErrorCode.NOT_A_OBJECT_ID);
            response.setMsg("Id không đúng.");
            return response;
        }

        DB db = MongoPool.getDBJongo();
        Jongo jongo = new Jongo(db);
        MongoCollection collection = jongo.getCollection(ForumQuestion.class.getSimpleName());
        MongoCursor<ForumQuestion> cursor = collection.find("{_id:#}", new ObjectId(id)).as(ForumQuestion.class);
        if(cursor.hasNext()){
            ForumQuestion forumQuestion = cursor.next();
            if(phone.trim().equals(forumQuestion.getPhone())){
                collection.update("{_id:#}", new ObjectId(id)).with("{$set:{status:#}}",ForumQuestion.INACTIVE);
                response.setData(forumQuestion.toJson());
            }else {
                response.setError(ErrorCode.LESS_ROLE);
                response.setMsg("Không có quyền xóa.");
            }
        }else {
            response.setError(ErrorCode.ID_NOT_EXIST);
            response.setMsg("Id không tồn tại.");
        }

        return response;
    }

    @Override
    public ForumQuestionResponse editQuestion(String phone,String id, String content) throws Exception {
        ForumQuestionResponse response = new ForumQuestionResponse();
        if (!ObjectId.isValid(id)) {
            response.setError(ErrorCode.NOT_A_OBJECT_ID);
            response.setMsg("Id không đúng.");
            return response;
        }

        DB db = MongoPool.getDBJongo();
        Jongo jongo = new Jongo(db);
        MongoCollection collection = jongo.getCollection(ForumQuestion.class.getSimpleName());
        MongoCursor<ForumQuestion> cursor = collection.find("{_id:#}", new ObjectId(id)).as(ForumQuestion.class);
        if(cursor.hasNext()){
            ForumQuestion forumQuestion = cursor.next();
            if(phone.trim().equals(forumQuestion.getPhone())){
                forumQuestion.setContent(content);
                collection.update("{_id:#}", new ObjectId(id)).with("{$set:{content:#}}",content);
                response.setData(forumQuestion.toJson());
            }else {
                response.setError(ErrorCode.LESS_ROLE);
                response.setMsg("Không có quyền sửa.");
            }
        }else {
            response.setError(ErrorCode.ID_NOT_EXIST);
            response.setMsg("Id không tồn tại.");
        }

        return response;
    }

    @Override
    public ForumQuestionResponse likeQuestion(String id, Boolean isLike, String phone) throws Exception {
        ForumQuestionResponse response = new ForumQuestionResponse();
        if (!ObjectId.isValid(id)) {
            response.setError(ErrorCode.NOT_A_OBJECT_ID);
            response.setMsg("Id không đúng.");
            return response;
        }
        DB db = MongoPool.getDBJongo();
        Jongo jongo = new Jongo(db);
        MongoCollection collection = jongo.getCollection(ForumQuestion.class.getSimpleName());
        StringBuilder builder = new StringBuilder();
        builder.append("{_id:#}");
        MongoCursor<ForumQuestion> cursor = collection.find(builder.toString(), new ObjectId(id)).as(ForumQuestion.class);
        if(cursor.hasNext()){
            ForumQuestion forumQuestion = cursor.next();
            int like = forumQuestion.getNumLike()>=0?forumQuestion.getNumLike():0;
            if(isLike){
                forumQuestion.setNumLike(like+1);
                collection.update("{_id:#}", new ObjectId(id)).with("{$set:{numLike:#}}",forumQuestion.getNumLike());
                collection.update("{_id:#}", new ObjectId(id)).with("{ $push: {userLike:#}}",phone);
            }else {
                forumQuestion.setNumLike(like-1);
                collection.update("{_id:#}", new ObjectId(id)).with("{$set:{numLike:#}}",forumQuestion.getNumLike());
                collection.update("{_id:#}", new ObjectId(id)).with("{ $pull: {userLike:#}}",phone);
            }
            JsonObject object = new JsonObject();
            object.addProperty("numLike",forumQuestion.getNumLike());
            response.setData(object);
        }else {
            response.setError(ErrorCode.ID_NOT_EXIST);
            response.setMsg("Id không tồn tại.");
        }
        return response;
    }

    @Override
    public int commentQuestion(String id, Boolean isCmt) throws Exception {
        DB db = MongoPool.getDBJongo();
        Jongo jongo = new Jongo(db);
        MongoCollection collection = jongo.getCollection(ForumQuestion.class.getSimpleName());
        StringBuilder builder = new StringBuilder();
        builder.append("{_id:#}");
        MongoCursor<ForumQuestion> cursor = collection.find(builder.toString(), new ObjectId(id)).as(ForumQuestion.class);
        if(cursor.hasNext()){
            ForumQuestion forumQuestion = cursor.next();
            int cmt = forumQuestion.getNumComment()>=0?forumQuestion.getNumComment():0;
            if(isCmt){
                forumQuestion.setNumComment(cmt+1);
            }else {
                forumQuestion.setNumComment(cmt-1);
            }

            collection.update("{_id:#}", new ObjectId(id)).with("{$set:{numComment:#}}",forumQuestion.getNumComment());
            return forumQuestion.getNumComment();
        }
        return 0;
    }

    @Override
    public ForumQuestionResponse getQuestionByField(int page, int ofset, String id, String phone) throws Exception {
        ForumQuestionResponse response = new ForumQuestionResponse();
        if (!ObjectId.isValid(id)) {
            response.setError(ErrorCode.NOT_A_OBJECT_ID);
            response.setMsg("Id không đúng.");
            return response;
        }
        DB db = MongoPool.getDBJongo();
        Jongo jongo = new Jongo(db);
        StringBuilder builder = new StringBuilder();
        MongoCursor<ForumQuestion> cursor = null;
        MongoCollection collection = jongo.getCollection(ForumQuestion.class.getSimpleName());
        builder.append("{$and: [{idField: #},{status: #}]}");
        cursor = collection.find(builder.toString(),id,ForumQuestion.ACTICE).sort("{timeCreate:-1}").skip(page*ofset).limit(ofset).as(ForumQuestion.class);
        JsonArray jsonArray = new JsonArray();
        response.setTotal(cursor.count());
        while(cursor.hasNext()){
            ForumQuestion forumQuestion = cursor.next();
            Users users = Utils.getUserByPhone(forumQuestion.getPhone());
            if(users!=null){
                forumQuestion.setAvatar(users.getAvatar());
                forumQuestion.setNameUser(users.getName());
            }
            List<String> userLike = forumQuestion.getUserLike();
            if(userLike!=null && userLike.contains(phone)){
                forumQuestion.setIsLiked(true);
            }else{
                forumQuestion.setIsLiked(false);
            }
            jsonArray.add(forumQuestion.toJson());
        }
        response.setArray(jsonArray);

        return response;
    }

    @Override
    public ForumQuestionResponse getQuestionByID(String id, String phone) throws Exception {
        ForumQuestionResponse response = new ForumQuestionResponse();
        if (!ObjectId.isValid(id)) {
            response.setError(ErrorCode.NOT_A_OBJECT_ID);
            response.setMsg("Id không đúng.");
            return response;
        }
        DB db = MongoPool.getDBJongo();
        Jongo jongo = new Jongo(db);
        StringBuilder builder = new StringBuilder();
        MongoCursor<ForumQuestion> cursor = null;
        MongoCollection collection = jongo.getCollection(ForumQuestion.class.getSimpleName());
        builder.append("{$and: [{_id: #}]}");
        cursor = collection.find(builder.toString(),new ObjectId(id)).limit(1).as(ForumQuestion.class);
        if(cursor.hasNext()){
            ForumQuestion forumQuestion = cursor.next();
            Users users = Utils.getUserByPhone(forumQuestion.getPhone());
            if(users!=null){
                forumQuestion.setAvatar(users.getAvatar());
                forumQuestion.setNameUser(users.getName());
            }
            List<String> userLike = forumQuestion.getUserLike();
            if(userLike!=null && userLike.contains(phone)){
                forumQuestion.setIsLiked(true);
            }else{
                forumQuestion.setIsLiked(false);
            }
            response.setData(forumQuestion.toJson());
        }else{
            response.setError(ErrorCode.ID_NOT_EXIST);
            response.setMsg("Id không tồn tại.");
        }

        return response;
    }

    @Override
    public ForumQuestionResponse addQuestion(String phone, String image,String idField, String content, int nExpert) throws Exception {

        ForumQuestionResponse response = new ForumQuestionResponse();
        UploadService service = new UploadServiceImp();
        if (!ObjectId.isValid(idField) && !idField.equals("")) {
            response.setError(ErrorCode.NOT_A_OBJECT_ID);
            response.setMsg("Id không đúng.");
            return response;
        }
        Users users = Utils.getUserByPhone(phone);
        ForumQuestion forumQuestion = new ForumQuestion();
        ObjectId objectId = new ObjectId();
        String id = objectId.toHexString();
        forumQuestion.set_id(id);
        forumQuestion.setIdField(idField);
        forumQuestion.setIdUser(users.get_id());
        forumQuestion.setPhone(phone);
        forumQuestion.setContent(content);
        forumQuestion.setStatus(ForumQuestion.ACTICE);
        if(image!=null && !image.equals("")){
            try {
                String urlImage = service.uploadImage(image);
                forumQuestion.setImage(urlImage);
            }catch (Exception e){
                response.setError(ErrorCode.UPLOAD_IMAGE_ERROR);
                response.setMsg("Có lỗi trong quá trình upload ảnh.");
                return response;
            }
        }
        forumQuestion.setTimeCreate(Calendar.getInstance().getTimeInMillis());
        forumQuestion.setNumLike(0);
        forumQuestion.setNumComment(0);

        //save data to db
        MongoPool.log(ForumQuestion.class.getSimpleName(),forumQuestion.toDocument());
        //return response to client
        response.setData(forumQuestion.toJson());

        //send content question to sv2 and get tags and field from sv2------------
        KafkaProduce.runProducer(forumQuestion.get_id(),forumQuestion.getContent());

        //save data to db tablde temp
        List<String> lstField = new ArrayList<>();
        List<Expert> lstExpert = new ArrayList<>();
        List<String> lstTag = new ArrayList<>();
        ExpertRorumQuestion expertRorumQuestion = new ExpertRorumQuestion();
        expertRorumQuestion.set_id(objectId.toHexString());
        expertRorumQuestion.setIdForumQuestion(id);
        expertRorumQuestion.setExperts(lstExpert);
        expertRorumQuestion.setIdParentField(idField);
        expertRorumQuestion.setIdField(lstField);
        expertRorumQuestion.setTags(lstTag);
        expertRorumQuestion.setNExpert(nExpert);
        expertRorumQuestion.setTimeCreate(Calendar.getInstance().getTimeInMillis());
        MongoPool.log(ExpertRorumQuestion.class.getSimpleName(),expertRorumQuestion.toDocument());

        return response;
    }

    @Override
    public ForumQuestionResponse getExpertByIDQuestion(String id,double lat,double lon,int status) throws Exception {
        ForumQuestionResponse response = new ForumQuestionResponse();
        ExpertService expertService = new ExpertServiceImp();
        if (!ObjectId.isValid(id)) {
            response.setError(ErrorCode.NOT_A_OBJECT_ID);
            response.setMsg("Id không đúng.");
            return response;
        }
        DB db = MongoPool.getDBJongo();
        Jongo jongo = new Jongo(db);
        StringBuilder builder = new StringBuilder();
        MongoCursor<ExpertRorumQuestion> cursor = null;
        MongoCollection collection = jongo.getCollection(ExpertRorumQuestion.class.getSimpleName());
        builder.append("{$and: [{idForumQuestion: #}]}");
        cursor = collection.find(builder.toString(),id).limit(1).as(ExpertRorumQuestion.class);
        if(cursor.hasNext()){
            ExpertRorumQuestion expertRorumQuestion = cursor.next();
            List<String> lstTags = expertRorumQuestion.getTags();
            if(lstTags.size()>0){
                List<Expert> lstExpert = null;
                if (checkConditionHourEnough24(Calendar.getInstance().getTimeInMillis(),expertRorumQuestion.getTimeCreate())){
                    lstExpert = expertService.getExpertByIds(expertRorumQuestion.getIdExpert());
                }else{
                    // re-search expert
                    lstExpert = ReSearchExpert(expertRorumQuestion.get_id(),expertRorumQuestion.getTags());
                }
                if(lstExpert!=null){
                    List<Expert> lstTemp = new ArrayList<>();
                    for (Expert e : lstExpert){
                        if (status==1){
                            if(e.getIsOnline()){
                                double dist = Utils.distance(lat,lon,e.getLat(),e.getLon(),"K");
                                e.setDistance(dist);
                                lstTemp.add(e);
                            }
                        } else if(status==0){
                            if (!e.getIsOnline()){
                                double dist = Utils.distance(lat,lon,e.getLat(),e.getLon(),"K");
                                e.setDistance(dist);
                                lstTemp.add(e);
                            }
                        }else if (status==-1){
                            double dist = Utils.distance(lat,lon,e.getLat(),e.getLon(),"K");
                            e.setDistance(dist);
                            lstTemp.add(e);
                        }
                    }
                    List<Expert> expertList = new ArrayList<>();
                    for (int i=0;i<lstTemp.size();i++){
                        if(i==expertRorumQuestion.getNExpert()) break;
                        expertList.add(lstTemp.get(i));
                    }
                    expertRorumQuestion.setExperts(expertList);
                    response.setData(expertRorumQuestion.toJsonExpert());
                    return response;
                }else{
                    response.setError(ErrorCode.SYSTEM_ERROR);
                    response.setMsg("Có lỗi xảy ra trong quá trình xử lý.");
                }
            }else{
                response.setError(ErrorCode.SEARCHING_EXPERT);
                response.setMsg("Đang tìm kiếm chuyến gia. Uqay lại sau.");
            }
        }else{
            response.setError(ErrorCode.ID_NOT_EXIST);
            response.setMsg("Id không tồn tại.");
        }
        return response;
    }

    @Override
    public ForumQuestionResponse getQuestionAll(int page, int ofset, String phone) throws Exception {
        ForumQuestionResponse response = new ForumQuestionResponse();
        DB db = MongoPool.getDBJongo();
        Jongo jongo = new Jongo(db);
        StringBuilder builder = new StringBuilder();
        MongoCursor<ForumQuestion> cursor = null;
        MongoCollection collection = jongo.getCollection(ForumQuestion.class.getSimpleName());
        builder.append("{$and: [{status: #}]}");
        cursor = collection.find(builder.toString(),ForumQuestion.ACTICE).sort("{timeCreate:-1}").skip(page*ofset).limit(ofset).as(ForumQuestion.class);
        JsonArray jsonArray = new JsonArray();
        response.setTotal(cursor.count());
        while(cursor.hasNext()){
            ForumQuestion forumQuestion = cursor.next();
            Users users = Utils.getUserByPhone(forumQuestion.getPhone());
            if(users!=null){
                forumQuestion.setAvatar(users.getAvatar());
                forumQuestion.setNameUser(users.getName());
            }
            List<String> userLike = forumQuestion.getUserLike();
            if(userLike!=null && userLike.contains(phone)){
                forumQuestion.setIsLiked(true);
            }else{
                forumQuestion.setIsLiked(false);
            }
            jsonArray.add(forumQuestion.toJson());
        }
        response.setArray(jsonArray);

        return response;
    }

    @Override
    public ForumQuestionResponse getTagsForumQuestion(String idQuestion, String strTags) throws Exception {

        ForumQuestionResponse response = new ForumQuestionResponse();
        FieldOfExpertService fieldOfExpertService = new FieldOfExpertServiceImp();
        ExpertService expertService = new ExpertServiceImp();
        if (!ObjectId.isValid(idQuestion) && !idQuestion.equals("")) {
            response.setError(ErrorCode.NOT_A_OBJECT_ID);
            response.setMsg("Id không đúng.");
            return response;
        }
        JsonArray array = Utils.toJsonArray(strTags);
        List<String> lstTag = new ArrayList<>();
        for (int i=0;i<array.size();i++){
            JsonObject object = array.get(i).getAsJsonObject();
            lstTag.add(object.get("name").getAsString());
        }
        //get by idquestion in table expertRorumQuestion

        DB db = MongoPool.getDBJongo();
        Jongo jongo = new Jongo(db);
        StringBuilder builder = new StringBuilder();
        MongoCursor<ExpertRorumQuestion> cursor = null;
        MongoCollection collection = jongo.getCollection(ExpertRorumQuestion.class.getSimpleName());
        builder.append("{$and: [{idForumQuestion: #}]}");
        cursor = collection.find(builder.toString(),idQuestion).limit(1).as(ExpertRorumQuestion.class);
        if(cursor.hasNext()){
            ExpertRorumQuestion expertRorumQuestion = cursor.next();
            //get 5 field
            List<String> lstIdField = fieldOfExpertService.getListFieldMatchTags(lstTag);
            //query expert
            List<Expert> lstExpert = expertService.getListExpertByParentField(expertRorumQuestion.getIdParentField());
            int nExpert = lstExpert.size();
            //-----------------------------
            // + sort expert by match tags and update weigth match
            for (Expert expert: lstExpert ){
                expert.setNumMatchTags(CollectionUtils.intersection(expert.getTags(),lstTag).size());
            }
            Collections.sort(lstExpert,Expert.NUM_MATCH_TAGS_DESC);
            for (int i=0;i<lstExpert.size();i++){
                if(i==0){
                    lstExpert.get(i).setWeigthMatch(nExpert*50);
                } else {
                    if(lstExpert.get(i).getNumMatchTags()==lstExpert.get(i-1).getNumMatchTags()){
                        lstExpert.get(i).setWeigthMatch(lstExpert.get(i-1).getWeigthMatch());
                    }else{
                        lstExpert.get(i).setWeigthMatch((nExpert-i)*50);
                    }
                }

            }
            // + sort expert by match idField and update weigth match
            for (Expert expert: lstExpert ){
                expert.setNumMatchField(CollectionUtils.intersection(expert.getIdFields(),lstIdField).size());
            }
            Collections.sort(lstExpert,Expert.NUM_MATCH_FIELD_DESC);
            int tempField = 0;
            for (int i=0;i<lstExpert.size();i++){
                int oldWeigth = lstExpert.get(i).getWeigthMatch();
                if(i==0){
                    lstExpert.get(i).setWeigthMatch(oldWeigth+ nExpert*35);
                } else {
                    if(lstExpert.get(i).getNumMatchField()==lstExpert.get(i-1).getNumMatchField()){
                        lstExpert.get(i).setWeigthMatch(oldWeigth + (nExpert-tempField)*35);
                    }else{
                        lstExpert.get(i).setWeigthMatch(oldWeigth + (nExpert-i)*35);
                        tempField = i;
                    }
                }

            }
            // + sort expert by rate and update weigth match
            Collections.sort(lstExpert,Expert.NUM_RATE_DESC);
            int tempRate = 0;
            for (int i=0;i<lstExpert.size();i++){
                int oldWeigth = lstExpert.get(i).getWeigthMatch();
                if(i==0){
                    lstExpert.get(i).setWeigthMatch(oldWeigth+ nExpert*25);
                } else {
                    if(lstExpert.get(i).getRate()==lstExpert.get(i-1).getRate()){
                        lstExpert.get(i).setWeigthMatch(oldWeigth + (nExpert-tempRate)*25);
                    }else{
                        lstExpert.get(i).setWeigthMatch(oldWeigth + (nExpert-i)*35);
                        tempRate = i;
                    }
                }

            }
            //sort expert by weight macth
            Collections.sort(lstExpert,Expert.WEIGHT_MATCH_DESC);
            List<String> lstIdExpert = new ArrayList<>();
            for (Expert expert: lstExpert){
                lstIdExpert.add(expert.get_id());
            }
            //update to tb temp
            collection.update("{idForumQuestion:#}",idQuestion).with("{$set:{tags:#,idField: #,idExpert: #}}",lstTag,lstIdField,lstIdExpert);
        }else {
            response.setError(ErrorCode.ID_NOT_EXIST);
            response.setMsg("id không tồn tại");
        }
        return response;
    }

    private List<Expert> ReSearchExpert(String id, List<String> lstTags){
        List<Expert> lstExpert = new ArrayList<>();
        FieldOfExpertService fieldOfExpertService = new FieldOfExpertServiceImp();
        ExpertService expertService = new ExpertServiceImp();
        DB db = MongoPool.getDBJongo();
        Jongo jongo = new Jongo(db);
        StringBuilder builder = new StringBuilder();
        MongoCursor<ExpertRorumQuestion> cursor = null;
        MongoCollection collection = jongo.getCollection(ExpertRorumQuestion.class.getSimpleName());
        builder.append("{$and: [{_id: #}]}");
        cursor = collection.find(builder.toString(),new ObjectId(id)).limit(1).as(ExpertRorumQuestion.class);
        try {
            if(cursor.hasNext()){
                ExpertRorumQuestion expertRorumQuestion = cursor.next();
                //get 5 field
                List<String> lstIdField = fieldOfExpertService.getListFieldMatchTags(lstTags);
                //query expert
                lstExpert = expertService.getListExpertByParentField(expertRorumQuestion.getIdParentField());
                int nExpert = lstExpert.size();
                //-----------------------------
                // + sort expert by match tags and update weigth match
                for (Expert expert: lstExpert ){
                    expert.setNumMatchTags(CollectionUtils.intersection(expert.getTags(),lstTags).size());
                }
                Collections.sort(lstExpert,Expert.NUM_MATCH_TAGS_DESC);
                for (int i=0;i<lstExpert.size();i++){
                    if(i==0){
                        lstExpert.get(i).setWeigthMatch(nExpert*50);
                    } else {
                        if(lstExpert.get(i).getNumMatchTags()==lstExpert.get(i-1).getNumMatchTags()){
                            lstExpert.get(i).setWeigthMatch(lstExpert.get(i-1).getWeigthMatch());
                        }else{
                            lstExpert.get(i).setWeigthMatch((nExpert-i)*50);
                        }
                    }

                }
                // + sort expert by match idField and update weigth match
                for (Expert expert: lstExpert ){
                    expert.setNumMatchField(CollectionUtils.intersection(expert.getIdFields(),lstIdField).size());
                }
                Collections.sort(lstExpert,Expert.NUM_MATCH_FIELD_DESC);
                int tempField = 0;
                for (int i=0;i<lstExpert.size();i++){
                    int oldWeigth = lstExpert.get(i).getWeigthMatch();
                    if(i==0){
                        lstExpert.get(i).setWeigthMatch(oldWeigth+ nExpert*35);
                    } else {
                        if(lstExpert.get(i).getNumMatchField()==lstExpert.get(i-1).getNumMatchField()){
                            lstExpert.get(i).setWeigthMatch(oldWeigth + (nExpert-tempField)*35);
                        }else{
                            lstExpert.get(i).setWeigthMatch(oldWeigth + (nExpert-i)*35);
                            tempField = i;
                        }
                    }

                }
                // + sort expert by rate and update weigth match
                Collections.sort(lstExpert,Expert.NUM_RATE_DESC);
                int tempRate = 0;
                for (int i=0;i<lstExpert.size();i++){
                    int oldWeigth = lstExpert.get(i).getWeigthMatch();
                    if(i==0){
                        lstExpert.get(i).setWeigthMatch(oldWeigth+ nExpert*25);
                    } else {
                        if(lstExpert.get(i).getRate()==lstExpert.get(i-1).getRate()){
                            lstExpert.get(i).setWeigthMatch(oldWeigth + (nExpert-tempRate)*25);
                        }else{
                            lstExpert.get(i).setWeigthMatch(oldWeigth + (nExpert-i)*35);
                            tempRate = i;
                        }
                    }

                }
                //sort expert by weight macth
                Collections.sort(lstExpert,Expert.WEIGHT_MATCH_DESC);
                List<String> lstIdExpert = new ArrayList<>();
                for (Expert expert: lstExpert){
                    lstIdExpert.add(expert.get_id());
                }
                //update to tb temp
                collection.update("{_id:#}",new ObjectId(id)).with("{$set:{idField: #,idExpert: #}}",lstIdField,lstIdExpert);
            }else {
                return null;
            }
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }

        return lstExpert;
    }

    private boolean checkConditionHourEnough24(long time1, long time2){
        long sub = time1 - time2;
        int timeMunite = (int)(sub/60000);
        if(timeMunite>=1440){
            return false;
        }
        return true;
    }
}
