
#include "networkmanager.h"

#include <QNetworkReply>
#include <QUrl>
#include <QNetworkRequest>


QString NetworkManager::cookie = "M2UzZjIyODYtYWJhZS00ZWY5LWFmNTctYjkyY2Q5NDM0ZTMx";

NetworkManager::NetworkManager(QObject* rootObject): QObject(nullptr), rootObject(rootObject)
{
    mgr.setCookieJar(&cookieJar);
}

void NetworkManager::setupCookie(QUrl url){
    cookies.clear();
    cookies.append(QNetworkCookie("SESSION", cookie.toUtf8()));
    cookieJar.setCookiesFromUrl(cookies, url);
}

