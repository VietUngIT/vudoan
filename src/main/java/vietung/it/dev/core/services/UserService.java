package vietung.it.dev.core.services;

import org.jongo.Jongo;
import vietung.it.dev.apis.response.UserResponse;
import vietung.it.dev.core.models.Users;

public interface UserService {
    public UserResponse register(String name, String phone, String pass, int role);
    public UserResponse login(String phone, String pass);
    public UserResponse loginAdmin(String phone, String pass);
    public UserResponse changePass(String phone, String oldPass, String newPass);
    public UserResponse changePhone(String oldPhone, String newPhone);
    public UserResponse changeName(String phone, String name);
    public UserResponse changeAddress(String phone, String address);
    public UserResponse changeRole(String phone, int role);
    public UserResponse getUserInfor(String phone);
    public UserResponse getUserInforbyid(String id);
    int getCountUser(Jongo jongo) throws Exception;
}
