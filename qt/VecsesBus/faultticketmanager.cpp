
#include "faultticketmanager.h"
#include <QJsonDocument>
#include <QJsonObject>
#include <QQuickItem>
#include <QNetworkReply>
#include <QByteArray>

FaultTicketManager::FaultTicketManager(QObject* rootObject) : NetworkManager(rootObject)
{
    QQuickItem* ticketListView = rootObject->findChild<QQuickItem*>("faultticketlist");
    QObject::connect(ticketListView, SIGNAL(getAllTickets()), this, SLOT(getAllFaultTicketsHandler()));
    QObject::connect(ticketListView, SIGNAL(getTicketById(QString)), this, SLOT(getFaultTicketByIdHandler(QString)));
    QQuickItem* ticketDetailView = rootObject->findChild<QQuickItem*>("faultticketdetail");
    QObject::connect(ticketDetailView, SIGNAL(saveTicket(QVariant, QString)), this, SLOT(saveFaultTicketHandler(QVariant, QString)));
    QObject::connect(ticketDetailView, SIGNAL(deleteTicket(QString)), this, SLOT(deleteFaultTicketHandler(QString)));
    QQuickItem* ticketFormView = rootObject->findChild<QQuickItem*>("faultticketform");
    QObject::connect(ticketFormView, SIGNAL(createTicket(QVariant)), this, SLOT(createFaultTicketHandler(QVariant)));
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

void FaultTicketManager::saveFaultTicketHandler(QVariant ticket, QString url)
{
    qDebug() << url;
    QByteArray data = ticket.toByteArray();
    QUrl qurl = QUrl(url);
    QNetworkRequest request(qurl);
    request.setHeader(QNetworkRequest::ContentTypeHeader, "application/json");
    setupCookie(qurl);
    reply = mgr.put(request, data);
    QObject::connect(reply, SIGNAL(finished()), this, SLOT(responseSaveFaultTicketHandler()));
}
void FaultTicketManager::deleteFaultTicketHandler(QString url)
{
    QUrl qurl = QUrl(url);
    QNetworkRequest request(qurl);
    setupCookie(qurl);
    reply = mgr.deleteResource(request);
    QObject::connect(reply, SIGNAL(finished()), this, SLOT(responseDeleteFaultTicketHandler()));
}

void FaultTicketManager::responseSaveFaultTicketHandler()
{
    qDebug() << reply->readAll();
}

void FaultTicketManager::responseDeleteFaultTicketHandler(){
    qDebug() << reply->readAll();
}

void FaultTicketManager::createFaultTicketHandler(QVariant ticket){
    QByteArray data = ticket.toByteArray();
    QUrl url = QUrl("http://localhost:8080/api/faultTickets");
    QNetworkRequest request(url);
    request.setHeader(QNetworkRequest::ContentTypeHeader, "application/json");
    setupCookie(url);
    reply = mgr.post(request, data);
    QObject::connect(reply, SIGNAL(finished()), this, SLOT(responseCreateFaultTicketHandler()));
}

void FaultTicketManager::responseCreateFaultTicketHandler(){
    qDebug() << reply->readAll();
}


