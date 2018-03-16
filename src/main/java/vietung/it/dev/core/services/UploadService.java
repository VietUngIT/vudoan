package vietung.it.dev.core.services;

import java.io.IOException;

public interface UploadService {
    String uploadImage(String imageBase64)throws IOException;
}
