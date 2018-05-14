package vietung.it.dev.core.services;

import vietung.it.dev.apis.response.RoomsResponse;

public interface RoomService {
    public RoomsResponse createRoom(String name, String user, int type);
    public RoomsResponse editRoom(String idroom, String name, String user, int type);
    public RoomsResponse deleteRoom(String idroom);
    public RoomsResponse getAllRoomByIdUserWithRoom(int page, int ofset, String iduser);
    public RoomsResponse getRoomById(String id);
}
