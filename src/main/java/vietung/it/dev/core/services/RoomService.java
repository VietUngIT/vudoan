package vietung.it.dev.core.services;

import vietung.it.dev.apis.response.MessagesResponse;
import vietung.it.dev.apis.response.RoomResponse;

public interface RoomService {
    public RoomResponse createRoom(String name, String user, int type);
    public RoomResponse editRoom(String idroom,String name, String user, int type);
    public RoomResponse deleteRoom(String idroom);
    public RoomResponse getAllRoomByIdUserWithRoom(int page, int ofset, String iduser);
}
