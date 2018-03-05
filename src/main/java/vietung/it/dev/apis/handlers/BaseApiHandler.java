package vietung.it.dev.apis.handlers;

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

    public boolean isUserExist(String phone){
        Users users = Utils.getUserByPhone(phone);
        if(users != null) {
            return true;
        }
        return false;
    }
}
