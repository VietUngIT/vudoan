import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import vietung.it.dev.core.config.CloudinaryConfig;
import vietung.it.dev.core.services.UploadService;
import vietung.it.dev.core.services.imp.UploadServiceImp;

import java.io.IOException;
import java.util.Map;

public class UplaodTesst {
    public static void main(String[] args) throws IOException {
        CloudinaryConfig.loadConfig();
        UploadService service = new UploadServiceImp();
        System.out.println("start");
    String img = "";
//        service.uploadImage(img);
        System.out.println("finnish: "+service.uploadImage(img));
    }
}
