
#include "networkmanager.h"

#include <QNetworkReply>
#include <QUrl>
#include <QNetworkRequest>


QByteArray NetworkManager::auth = QString("dev:123").toUtf8().toBase64();

NetworkManager::NetworkManager(QObject* rootObject): QObject(nullptr), baseUrl("http://localhost:8080/api"), rootObject(rootObject)
{
}


void NetworkManager::setAuthHeader(QNetworkRequest& request){
    QByteArray headerValue = "Basic " + auth;
    request.setRawHeader("Authorization", headerValue);
}

