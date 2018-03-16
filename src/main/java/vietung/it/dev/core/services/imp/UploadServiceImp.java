package vietung.it.dev.core.services.imp;

import com.cloudinary.utils.ObjectUtils;
import com.google.gson.JsonObject;
import vietung.it.dev.core.config.CloudinaryConfig;
import vietung.it.dev.core.services.UploadService;
import vietung.it.dev.core.utils.Utils;

import java.io.IOException;
import java.util.Map;

public class UploadServiceImp implements UploadService {
    @Override
    public String uploadImage(String imageBase64) throws IOException {
        Map result = CloudinaryConfig.getInstance().uploader().upload(imageBase64, ObjectUtils.emptyMap());
        return result.get("url").toString();
    }
}
