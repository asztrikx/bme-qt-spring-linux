
#include "networkmanager.h"

#include <QNetworkReply>
#include <QUrl>
#include <QNetworkRequest>


QByteArray NetworkManager::auth = QString("dev:123").toUtf8().toBase64();

NetworkManager::NetworkManager(QObject* rootObject): QObject(nullptr), rootObject(rootObject)
{
}


void NetworkManager::setAuthHeader(QNetworkRequest& request){
    QByteArray headerValue = "Basic " + auth;
    request.setRawHeader("Authorization", headerValue);
}

