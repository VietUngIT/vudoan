package vietung.it.dev.core.services;

import vietung.it.dev.apis.response.HandBookResponse;

public interface HandbookService {
    HandBookResponse getAllHandbook(long timelast, int offer);
    HandBookResponse getAllHandbookByType(long timelast, int offer,String idtype);
    HandBookResponse getHandbookById(String idHB);
    HandBookResponse createHandbook(String title, String author, String image, String typeNews,String nameTypeNews, String content);
    HandBookResponse editHandbook(String idHB, String title, String author, String image, String typeNews,String nameTypeNews, String content);
    HandBookResponse deleteHandbook(String idHB);
}
