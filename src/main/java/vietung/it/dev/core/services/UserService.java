package vietung.it.dev.core.services;

import vietung.it.dev.apis.response.UserResponse;
import vietung.it.dev.core.models.Users;

public interface UserService {
    public UserResponse register(String name, String phone, String pass, int role);
    public UserResponse login(String phone, String pass);
    public UserResponse loginAdmin(String phone, String pass);
    public UserResponse changePass(String phone, String oldPass, String newPass);
    public UserResponse changePhone(String oldPhone, String newPhone, String pass);
    public UserResponse changeRole(String phone, String pass, int role);
    public UserResponse getUserInfor(String phone);
}