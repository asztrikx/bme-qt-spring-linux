
#include "linemanager.h"
#include <QNetworkReply>
#include <QQuickItem>
#include <QJsonDocument>
#include <QJsonObject>

LineManager::LineManager(QObject* rootObject) : NetworkManager(rootObject)
{
    QQuickItem* lineListView = rootObject->findChild<QQuickItem*>("linelist");
    QObject::connect(lineListView, SIGNAL(getAllLines()), this, SLOT(getAllLinesHandler()));
    QObject::connect(lineListView, SIGNAL(getLineStops(QString)), this, SLOT(getLineStopsHandler(QString)));
}


void LineManager::getAllLinesHandler()
{
    QUrl url = QUrl("http://localhost:8080/api/lines");
    QNetworkRequest request(url);
    setAuthHeader(request);
    reply = mgr.get(request);
    QObject::connect(reply, SIGNAL(finished()), this, SLOT(responseGetAllLinesHandler()));
}

void LineManager::responseGetAllLinesHandler()
{
    QJsonDocument jsonResponse = QJsonDocument::fromJson(reply->readAll());
    QJsonObject jsonObject = jsonResponse.object();
    QQuickItem* lineList = rootObject->findChild<QQuickItem*>("linelist");
    lineList->setProperty("lines", QVariant(jsonObject));
}

void LineManager::getLineStopsHandler(QString url)
{
    QUrl qurl = QUrl(url);
    QNetworkRequest request(qurl);
    setAuthHeader(request);
    reply = mgr.get(request);
    QObject::connect(reply, SIGNAL(finished()), this, SLOT(responseGetLinesStopsHandler()));
}
void LineManager::responseGetLinesStopsHandler(){
    QJsonDocument jsonResponse = QJsonDocument::fromJson(reply->readAll());
    QJsonObject jsonObject = jsonResponse.object();
    QQuickItem* stopList = rootObject->findChild<QQuickItem*>("stoplist");
    stopList->setProperty("stops", QVariant(jsonObject));
}
