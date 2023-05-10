
#include "timetablemanager.h"
#include <QNetworkReply>
#include <QQuickItem>
#include <QJsonDocument>
#include <QJsonObject>

TimeTableManager::TimeTableManager(QObject* rootObject): NetworkManager(rootObject)
{
    QQuickItem* timeTableListView = rootObject->findChild<QQuickItem*>("timetablelist");
    QObject::connect(timeTableListView, SIGNAL(getAllTimeTables()), this, SLOT(getAllTimeTablesHandler()));
    QObject::connect(timeTableListView, SIGNAL(getTimeTableLine(QVariant, QString)), this, SLOT(getTimeTableLineHandler(QVariant, QString)));
}


void TimeTableManager::getAllTimeTablesHandler()
{
    QUrl url = QUrl("http://localhost:8080/api/timetables");
    QNetworkRequest request(url);
    setAuthHeader(request);
    reply = mgr.get(request);
    QObject::connect(reply, SIGNAL(finished()), this, SLOT(responseGetAllTimeTablesHandler()));
}

void TimeTableManager::responseGetAllTimeTablesHandler()
{
    QJsonDocument jsonResponse = QJsonDocument::fromJson(reply->readAll());
    QJsonObject jsonObject = jsonResponse.object();
    QQuickItem* timeTableList = rootObject->findChild<QQuickItem*>("timetablelist");
    timeTableList->setProperty("timetables", QVariant(jsonObject));
}

void TimeTableManager::getTimeTableLineHandler(QVariant id, QString url)
{
    QUrl qurl = QUrl(url);
    QNetworkRequest request(qurl);
    setAuthHeader(request);
    QNetworkReply* reply = mgr.get(request);
    QQuickItem* timeTableList = rootObject->findChild<QQuickItem*>("timetablelist");
    QObject::connect(reply, &QNetworkReply::finished, [=]() {
        QJsonDocument jsonResponse = QJsonDocument::fromJson(reply->readAll());
        QJsonObject jsonObject = jsonResponse.object();
        QMetaObject::invokeMethod(timeTableList, "setTimeTableName", Q_ARG(QVariant, id), Q_ARG(QVariant, jsonObject.value("name")));
    });
}

