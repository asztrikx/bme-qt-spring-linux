
#include "networkmanager.h"

#include <QNetworkReply>
#include <QUrl>
#include <QNetworkRequest>


QByteArray NetworkManager::auth = QString("no_login").toUtf8().toBase64();

NetworkManager::NetworkManager(QObject* rootObject): QObject(nullptr), rootObject(rootObject)
{
}


void NetworkManager::setAuthHeader(QNetworkRequest& request){
    QByteArray headerValue = "Basic " + auth;
    request.setRawHeader("Authorization", headerValue);
}

