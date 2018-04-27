package vietung.it.dev.core.services;

import vietung.it.dev.apis.response.MessagesResponse;
import vietung.it.dev.apis.response.NewsResponse;
import vietung.it.dev.apis.response.UserResponse;

public interface MessagesService {
    public MessagesResponse getAllMessagesByRoomWithMessagesest(int page, int ofset, String idroom);
}
