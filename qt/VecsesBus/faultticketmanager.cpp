
#include "faultticketmanager.h"
#include <QJsonDocument>
#include <QJsonObject>
#include <QQuickItem>
#include <QNetworkReply>

FaultTicketManager::FaultTicketManager(QObject* rootObject) : NetworkManager(rootObject)
{
    QQuickItem* ticketListView = rootObject->findChild<QQuickItem*>("faultticketlist");
    QObject::connect(ticketListView, SIGNAL(getAllTickets()), this, SLOT(getAllFaultTicketsHandler()));
    QObject::connect(ticketListView, SIGNAL(getTicketById(QString)), this, SLOT(getFaultTicketByIdHandler(QString)));
}

void FaultTicketManager::getAllFaultTicketsHandler()
{
    QUrl url = QUrl("http://localhost:8080/api/faultTickets");
    QNetworkRequest request(url);
    setupCookie(url);
    reply = mgr.get(request);
    QObject::connect(reply, SIGNAL(finished()), this, SLOT(responseAllFaultTicketsHandler()));
}

void FaultTicketManager::responseAllFaultTicketsHandler()
{

    QJsonDocument jsonResponse = QJsonDocument::fromJson(reply->readAll());
    QJsonObject jsonObject = jsonResponse.object();
    QQuickItem* ticketList = rootObject->findChild<QQuickItem*>("faultticketlist");
    ticketList->setProperty("tickets", QVariant(jsonObject));
}

void FaultTicketManager::getFaultTicketByIdHandler(QString url)
{
    QUrl qurl = QUrl(url);
    QNetworkRequest request(qurl);
    setupCookie(qurl);
    reply = mgr.get(request);
    QObject::connect(reply, SIGNAL(finished()), this, SLOT(responseFaultTicketByIdHandler()));
}

void FaultTicketManager::responseFaultTicketByIdHandler()
{
    QJsonDocument jsonResponse = QJsonDocument::fromJson(reply->readAll());
    QJsonObject jsonObject = jsonResponse.object();
    QQuickItem* ticketDetail = rootObject->findChild<QQuickItem*>("faultticketdetail");
    ticketDetail->setProperty("ticket", QVariant(jsonObject));
}

