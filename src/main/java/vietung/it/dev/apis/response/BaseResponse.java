package vietung.it.dev.apis.response;

import lombok.Data;
import vietung.it.dev.core.consts.ErrorCode;

@Data
public abstract class BaseResponse {
    private int error = ErrorCode.SUCCESS;
    private String msg = "Success.";
    public boolean isSuccess(){
        return error == ErrorCode.SUCCESS;
    }
    public abstract String toJonString();
}
