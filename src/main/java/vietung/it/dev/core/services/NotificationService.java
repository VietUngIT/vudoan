package vietung.it.dev.core.services;

import vietung.it.dev.apis.response.MessagesResponse;
import vietung.it.dev.apis.response.NotificationResponse;
import vietung.it.dev.core.models.Notification;

public interface NotificationService {
    public NotificationResponse getAllNotificationByIdReceiver(int page, int ofset, String idreceiver);

    public NotificationResponse sendNoti(String idSend, String idreceiver,String message,String action,int type);

    public NotificationResponse sendNotification(String idSend, String idreceiver,String message,String action,int type);

    public NotificationResponse sendNotificationLogin(String idreceiver);

    public NotificationResponse updateStatus(String id,int status);
}
