package vietung.it.dev.core.services;

import vietung.it.dev.apis.response.ExpertResponse;

public interface ExpertService {
    ExpertResponse addExpert(String name, String phone, String desc, String email, String address, String location, String field, String tags, String degree) throws Exception;

    ExpertResponse deleteExpert(String phone) throws Exception;

    ExpertResponse editExpert(String phone, String desc, String location, String degree,String tags,String field) throws Exception;

    ExpertResponse editnameExpert(String phone, String nName) throws Exception;

    ExpertResponse editPhoneExpert(String phone, String nPhone) throws Exception;

    ExpertResponse editEmailExpert(String phone, String email) throws Exception;

    ExpertResponse editAddressExpert(String phone, String address) throws Exception;
}