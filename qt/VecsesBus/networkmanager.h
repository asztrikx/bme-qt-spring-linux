
#ifndef NETWORKMANAGER_H
#define NETWORKMANAGER_H


#include <QObject>
#include <QNetworkAccessManager>
#include <QList>
#include <QNetworkCookieJar>
#include <QNetworkCookie>


class NetworkManager : public QObject
{
    Q_OBJECT
public:
    NetworkManager(QObject* rootObject);
public slots:
    void getAllFaultTicketsHandler();
    void getFaultTicketByIdHandler(QString url);

    void responseAllFaultTicketsHandler();
    void responseFaultTicketByIdHandler();

private:
    QObject* rootObject;
    QNetworkAccessManager mgr;
    QNetworkReply* reply;
    QNetworkCookieJar cookieJar;
    QList<QNetworkCookie> cookies;
};

#endif // NETWORKMANAGER_H
