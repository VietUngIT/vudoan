package vietung.it.dev.core.services.imp;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.mongodb.DB;
import org.bson.types.ObjectId;
import org.jongo.Jongo;
import org.jongo.MongoCollection;
import org.jongo.MongoCursor;
import vietung.it.dev.apis.response.DashBoardResponse;
import vietung.it.dev.core.config.MongoPool;
import vietung.it.dev.core.models.DashBoardObject;
import vietung.it.dev.core.models.ParentFieldExpert;
import vietung.it.dev.core.models.Report;
import vietung.it.dev.core.models.ReportObject;
import vietung.it.dev.core.services.*;
import vietung.it.dev.core.utils.Utils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

public class DashBoardServiceImp implements DashBoardService {
    @Override
    public DashBoardResponse getDashBoardByCurrentDay() throws Exception {
        DashBoardResponse response = new DashBoardResponse();
        DB db = MongoPool.getDBJongo();
        Jongo jongo = new Jongo(db);
        MongoCollection collection = jongo.getCollection(DashBoardObject.class.getSimpleName());

        Calendar calendar = Calendar.getInstance();
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("{startTime:{$lte:#},endTime:{$gte:#}}");
        MongoCursor<DashBoardObject> cursor = collection.find(stringBuilder.toString(),calendar.getTimeInMillis(),calendar.getTimeInMillis()).limit(1).as(DashBoardObject.class);
        if(cursor.hasNext()){
            DashBoardObject dashBoardObject = cursor.next();
            response.setData(dashBoardObject.toJson());
        }else{
            DashBoardObject dashBoardObject = getInfo(calendar,jongo);
            ObjectId id = new ObjectId();
            dashBoardObject.set_id(id.toHexString());
            MongoPool.log(DashBoardObject.class.getSimpleName(),dashBoardObject.toDocument());
            response.setData(dashBoardObject.toJson());
        }
        return response;
    }

    public DashBoardObject getInfo(Calendar calendar, Jongo jongo) throws Exception{
        DashBoardObject dashBoardObject = new DashBoardObject();
        ParentFieldExpertService parentFieldExpertService = new ParentFieldExpertServiceImp();
        ExpertService expertService = new ExpertServiceImp();
        QAQuestionService qaQuestionService = new QAQuestionServiceImp();
        UserService userService = new UserServiceImp();
        ForumQuestionService forumQuestionService = new ForumQuestionServiceImp();
        List<ReportObject> reportExpert = new ArrayList<>();
        List<ReportObject> reportQA = new ArrayList<>();
        List<ReportObject> reportFRQS = new ArrayList<>();
        List<ParentFieldExpert> lstParentField = parentFieldExpertService.getListParentField(jongo);
        initReport(lstParentField,reportExpert,reportQA,reportFRQS);
        Report expert = expertService.gtExpertForDashBoard(jongo);
        getExpertReport(dashBoardObject,expert,reportExpert);
        Report qa = qaQuestionService.getQAForDashBoard(jongo);
        getQAReport(dashBoardObject,qa,reportQA);
        Calendar calendarST = Calendar.getInstance();
        calendarST.add(Calendar.DATE,-16);
        Calendar calendarED = Calendar.getInstance();
        calendarED.add(Calendar.DATE,-1);
        Report frQS = forumQuestionService.getReportQuestionByDayForDashBoard(jongo,calendarST,calendarED);
        getQSReport(dashBoardObject,frQS,reportFRQS);
        dashBoardObject.setNumUser(userService.getCountUser(jongo));
        dashBoardObject.setForumByDay(forumQuestionService.getForumQuestionByDayForDashBoard(jongo));
        dashBoardObject.setStartTime(Utils.getStartDay(calendar.getTimeInMillis()));
        dashBoardObject.setEndTime(Utils.getEndDay(calendar.getTimeInMillis()));

        return dashBoardObject;
    }

    private void getExpertReport(DashBoardObject dashBoardObject,Report expert,List<ReportObject> reportExpert){
        dashBoardObject.setNumExpert(expert.getCount());
        List<JsonObject> lst = new ArrayList<>();
        for (ReportObject o: reportExpert){
            if(expert.getLst().containsKey(o.get_id())){
                ReportObject t = expert.getLst().get(o.get_id());
                o.setValue(t.getValue());
            }
            lst.add(o.toJson());
        }
        dashBoardObject.setExpertByField(lst.toString());
    }
    private void getQAReport(DashBoardObject dashBoardObject,Report qa,List<ReportObject> reportQA){
        dashBoardObject.setNumQA(qa.getCount());
        List<JsonObject> lst = new ArrayList<>();
        for (ReportObject o: reportQA){
            if(qa.getLst().containsKey(o.get_id())){
                ReportObject t = qa.getLst().get(o.get_id());
                o.setValue(t.getValue());
            }
            lst.add(o.toJson());
        }
        dashBoardObject.setQaByField(lst.toString());
    }
    private void getQSReport(DashBoardObject dashBoardObject,Report frQS,List<ReportObject> reportFRQS){
        dashBoardObject.setNumQuestionFR(frQS.getCount());
        List<JsonObject> lst = new ArrayList<>();
        for (ReportObject o: reportFRQS){
            if(frQS.getLst().containsKey(o.get_id())){
                ReportObject t = frQS.getLst().get(o.get_id());
                o.setValue(t.getValue());
                lst.add(o.toJson());
            }
        }
        dashBoardObject.setFrByField(lst.toString());
    }

    private void initReport(List<ParentFieldExpert> lstParentField,List<ReportObject> reportExpert,List<ReportObject> reportQA,List<ReportObject> reportFRQS){
        for (ParentFieldExpert item: lstParentField){
            ReportObject oExpert = new ReportObject();
            ReportObject oQA = new ReportObject();
            ReportObject oFRQS = new ReportObject();
            oExpert.set_id(item.get_id());
            oExpert.setValue(0);
            oExpert.setName(item.getName());
            oQA.set_id(item.get_id());
            oQA.setName(item.getName());
            oQA.setValue(0);
            oFRQS.set_id(item.get_id());
            oFRQS.setName(item.getName());
            oFRQS.setValue(0);
            reportExpert.add(oExpert);
            reportQA.add(oQA);
            reportFRQS.add(oFRQS);
        }
    }
}
