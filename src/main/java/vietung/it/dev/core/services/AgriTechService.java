package vietung.it.dev.core.services;

import vietung.it.dev.apis.response.AgriTechResponse;
import vietung.it.dev.apis.response.BaseResponse;

public interface AgriTechService {
    AgriTechResponse deleteNewsAgriTech(String id) throws Exception;

    AgriTechResponse editTagsNewsAgriTech(String id, String tags) throws Exception;

    AgriTechResponse editImageNewsAgriTech(String id, String image) throws Exception;

    AgriTechResponse editNewsAgriTech(String id, String title, String author, String idSubCate, String content) throws Exception;

    AgriTechResponse createNewsAgriTech(String title, String author, String image, String tags, String idSubCate, String content) throws Exception;

    AgriTechResponse getNewsAgriTechById(String id) throws Exception;

    AgriTechResponse getAllNewsAgriTechBySubCate(int page, int ofs, String idsubcate) throws Exception;
}
