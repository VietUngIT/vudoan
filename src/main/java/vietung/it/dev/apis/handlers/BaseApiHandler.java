package vietung.it.dev.apis.handlers;

import com.google.gson.JsonArray;
import io.vertx.core.http.HttpServerRequest;
import lombok.Data;
import vietung.it.dev.apis.response.BaseResponse;
import vietung.it.dev.core.consts.Roles;
import vietung.it.dev.core.models.Users;
import vietung.it.dev.core.utils.Utils;

@Data
public abstract class BaseApiHandler {
    protected String path = "";
    protected boolean isPublic = false;
    protected String method;
    protected int[] roles = {Roles.SUPER_ADMIN};

    public abstract BaseResponse handle(HttpServerRequest request) throws Exception;

    public boolean checkSecurityCheck(String phone, String pass){
        Users users = Utils.getUserByPhone(phone);
        if(users != null) {
            if(users.getPassword().equals(Utils.sha256(pass))){
                int role = users.getRoles();
                return checkRole(role);
            }
            return false;
        }
        return false;
    }

    private boolean checkRole(int role){
        for(int i = 0; i < roles.length; i++){
            if(roles[i] == role){
                return true;
            }
        }
        return (roles.length == 0);
    }
    public void initRoles(JsonArray roleArray) {
        roles = new int[roleArray.size()];
        for(int i = 0; i < roles.length; i++){
            roles[i] = roleArray.get(i).getAsInt();
        }
    }
}
