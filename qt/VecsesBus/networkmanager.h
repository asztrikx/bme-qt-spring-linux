
#ifndef NETWORKMANAGER_H
#define NETWORKMANAGER_H


#include <QObject>
#include <QList>
#include <QNetworkAccessManager>
#include <QNetworkCookie>
#include <QNetworkCookieJar>

class NetworkManager : public QObject
{
    Q_OBJECT
public:
    NetworkManager(QObject* rootObject);
    static void setCookie(QString value) { cookie = value; }
    void setupCookie(QUrl url);
protected:
    QObject* rootObject;
    QNetworkAccessManager mgr;
    QNetworkReply* reply;
    QNetworkCookieJar cookieJar;
    QList<QNetworkCookie> cookies;
    static QString cookie;
};

#endif // NETWORKMANAGER_H
