package vietung.it.dev.core.services;

import vietung.it.dev.apis.response.UserResponse;

import java.io.IOException;

public interface UploadService {
    String uploadImage(String imageBase64)throws IOException;
    UserResponse changeAvatar(String phone, String avatar);
}
