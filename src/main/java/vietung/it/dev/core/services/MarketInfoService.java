package vietung.it.dev.core.services;

import vietung.it.dev.apis.response.BaseResponse;
import vietung.it.dev.apis.response.MarketInfoResponse;

public interface MarketInfoService {
    MarketInfoResponse deleteNewsMarketInfo(String id) throws Exception;

    MarketInfoResponse editTagsNewsMarketInfo(String id, String tags) throws Exception;

    MarketInfoResponse editImageNewsMarketInfo(String id, String image) throws Exception;

    MarketInfoResponse editNewsmarketInfo(String id, String title, String author, String source, String idCateNews, String content) throws Exception;

    MarketInfoResponse createNewsMarketInfo(String title, String author, String image, String source, String tags, String idCateNews, String content) throws Exception;

    MarketInfoResponse getNewsMarketInfoById(String id);

    MarketInfoResponse getAllNewsMarketInfoByCate(int page, int ofs, String idcate);
}
