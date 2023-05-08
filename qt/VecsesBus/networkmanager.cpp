
#include "networkmanager.h"
#include <QQuickItem>
#include <QNetworkReply>
#include <QUrl>
#include <QNetworkRequest>
#include <QJsonDocument>
#include <QJsonObject>


NetworkManager::NetworkManager(QObject* rootObject): QObject(nullptr), rootObject(rootObject)
{
    QObject::connect(rootObject, SIGNAL(getAllTickets()), this, SLOT(getAllFaultTicketsHandler()));
    QObject::connect(rootObject, SIGNAL(getTicketById(QString)), this, SLOT(getFaultTicketByIdHandler(QString)));
    mgr.setCookieJar(&cookieJar);
    cookies.append(QNetworkCookie("SESSION", "MDM0NmJjNDctNWFkNS00YjA4LTlkNGItYzYzNWQxZGIxYjYw"));

}

void NetworkManager::getAllFaultTicketsHandler()
{
    QUrl url = QUrl("http://localhost:8080/api/faultTickets");
    QNetworkRequest request(url);
    cookieJar.setCookiesFromUrl(cookies, url);
    reply = mgr.get(request);
    QObject::connect(reply, SIGNAL(finished()), this, SLOT(responseAllFaultTicketsHandler()));
}

void NetworkManager::responseAllFaultTicketsHandler()
{

    QJsonDocument jsonResponse = QJsonDocument::fromJson(reply->readAll());
    QJsonObject jsonObject = jsonResponse.object();
    QQuickItem* ticketList = rootObject->findChild<QQuickItem*>("faultticketlist");
    ticketList->setProperty("tickets", QVariant(jsonObject));
}

void NetworkManager::getFaultTicketByIdHandler(QString url)
{
    QUrl qurl = QUrl(url);
    QNetworkRequest request(qurl);
    cookieJar.setCookiesFromUrl(cookies, qurl);
    reply = mgr.get(request);
    QObject::connect(reply, SIGNAL(finished()), this, SLOT(responseFaultTicketByIdHandler()));
}

void NetworkManager::responseFaultTicketByIdHandler()
{
    QJsonDocument jsonResponse = QJsonDocument::fromJson(reply->readAll());
    QJsonObject jsonObject = jsonResponse.object();
    QQuickItem* ticketDetail = rootObject->findChild<QQuickItem*>("faultticketdetail");
    ticketDetail->setProperty("ticket", QVariant(jsonObject));
}

