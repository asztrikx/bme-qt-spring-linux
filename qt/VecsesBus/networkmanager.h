
#ifndef NETWORKMANAGER_H
#define NETWORKMANAGER_H


#include <QObject>
#include <QList>
#include <QNetworkAccessManager>

class NetworkManager : public QObject
{
    Q_OBJECT
public:
    NetworkManager(QObject* rootObject);
    static void setAuth(QString value) { auth = value.toUtf8().toBase64(); }
    void setAuthHeader(QNetworkRequest& request);
protected:
    QString baseUrl;
    QObject* rootObject;
    QNetworkAccessManager mgr;
    QNetworkReply* reply;
    static QByteArray auth;
};

#endif // NETWORKMANAGER_H
