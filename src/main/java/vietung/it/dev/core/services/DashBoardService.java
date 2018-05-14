package vietung.it.dev.core.services;

import vietung.it.dev.apis.response.DashBoardResponse;

public interface DashBoardService {
    DashBoardResponse getDashBoardByCurrentDay() throws Exception;
}
