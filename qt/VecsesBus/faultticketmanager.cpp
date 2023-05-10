
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
    QObject::connect(ticketListView, SIGNAL(getAllTicketsByUser(QVariant)), this, SLOT(getAllFaultTicketsByUser(QVariant)));
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
    setAuthHeader(request);
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
    setAuthHeader(request);
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
    QByteArray data = ticket.toByteArray();
    QUrl qurl = QUrl(url);
    QNetworkRequest request(qurl);
    request.setHeader(QNetworkRequest::ContentTypeHeader, "application/json");
    setAuthHeader(request);
    reply = mgr.put(request, data);
    QObject::connect(reply, SIGNAL(finished()), this, SLOT(responseSaveFaultTicketHandler()));
}
void FaultTicketManager::deleteFaultTicketHandler(QString url)
{
    QUrl qurl = QUrl(url);
    QNetworkRequest request(qurl);
    setAuthHeader(request);
    reply = mgr.deleteResource(request);
    QObject::connect(reply, SIGNAL(finished()), this, SLOT(responseDeleteFaultTicketHandler()));
}

void FaultTicketManager::responseSaveFaultTicketHandler()
{
    QQuickItem* ticketDetail = rootObject->findChild<QQuickItem*>("faultticketdetail");
    QMetaObject::invokeMethod(ticketDetail, "onBack");
}

void FaultTicketManager::responseDeleteFaultTicketHandler(){
    QQuickItem* ticketDetail = rootObject->findChild<QQuickItem*>("faultticketdetail");
    QMetaObject::invokeMethod(ticketDetail, "onBack");
}

void FaultTicketManager::createFaultTicketHandler(QVariant ticket){
    QByteArray data = ticket.toByteArray();
    QUrl url = QUrl("http://localhost:8080/api/faultTickets");
    QNetworkRequest request(url);
    request.setHeader(QNetworkRequest::ContentTypeHeader, "application/json");
    setAuthHeader(request);
    reply = mgr.post(request, data);
    QObject::connect(reply, SIGNAL(finished()), this, SLOT(responseCreateFaultTicketHandler()));
}

void FaultTicketManager::responseCreateFaultTicketHandler(){
    QQuickItem* ticketForm = rootObject->findChild<QQuickItem*>("faultticketform");
    QMetaObject::invokeMethod(ticketForm, "onSubmitClicked");
}

void FaultTicketManager::getAllFaultTicketsByUser(QVariant id)
{
    QString user = id.toString();
    QUrl url = QUrl("http://localhost:8080/api/faultTickets/search/findAllByUser?user=" + user);
    QNetworkRequest request(url);
    setAuthHeader(request);
    reply = mgr.get(request);
    QObject::connect(reply, SIGNAL(finished()), this, SLOT(responseAllFaultTicketsHandler()));
}
