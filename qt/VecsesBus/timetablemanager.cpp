
#include "timetablemanager.h"
#include <QNetworkReply>
#include <QQuickItem>
#include <QJsonDocument>
#include <QJsonObject>

TimeTableManager::TimeTableManager(QObject* rootObject): NetworkManager(rootObject)
{
    QQuickItem* timeTableListView = rootObject->findChild<QQuickItem*>("timetablelist");
    QObject::connect(timeTableListView, SIGNAL(getAllTimeTables()), this, SLOT(getAllTimeTablesHandler()));
    QObject::connect(timeTableListView, SIGNAL(getAllAvailableTimeTables(QString)), this, SLOT(getAllAvailableTimeTablesHandler(QString)));
    QObject::connect(timeTableListView, SIGNAL(getTimeTableLine(QVariant, QString)), this, SLOT(getTimeTableLineHandler(QVariant, QString)));
    QObject::connect(timeTableListView, SIGNAL(getAllSectionByTimeTable(QString, QString)), this, SLOT(getAllSectionByTimeTableHandler(QString, QString)));
    QObject::connect(timeTableListView, SIGNAL(takeTimeTable(QVariant)), this, SLOT(postTakeTimetableHandler(QVariant)));
    QQuickItem* timeTableDetailView = rootObject->findChild<QQuickItem*>("timetabledetaillist");
    QObject::connect(timeTableDetailView, SIGNAL(getTimeTableSection(QVariant, QString)), this, SLOT(getTimeTableSectionHandler(QVariant, QString)));
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

    QObject::connect(reply, &QNetworkReply::finished, [=]() {
        QJsonDocument jsonResponse = QJsonDocument::fromJson(reply->readAll());
        QJsonObject jsonObject = jsonResponse.object();
        QQuickItem* timeTableList = rootObject->findChild<QQuickItem*>("timetablelist");
        QMetaObject::invokeMethod(timeTableList, "setTimeTableLine", Q_ARG(QVariant, id), Q_ARG(QVariant, jsonObject));
    });
}

void TimeTableManager::getAllSectionByTimeTableHandler(QString url, QString startDate)
{
    QUrl qurl = QUrl(url);
    QNetworkRequest request(qurl);
    setAuthHeader(request);
    QNetworkReply* reply = mgr.get(request);
    QQuickItem* timeTableDetailList = rootObject->findChild<QQuickItem*>("timetabledetaillist");
    timeTableDetailList->setProperty("startDate", startDate);
    QObject::connect(reply, &QNetworkReply::finished, [=]() {
        QJsonDocument jsonResponse = QJsonDocument::fromJson(reply->readAll());
        QJsonObject jsonObject = jsonResponse.object();
        timeTableDetailList->setProperty("sections", jsonObject);
    });
}

void TimeTableManager::getTimeTableSectionHandler(QVariant id, QString url)
{
    QUrl qurl = QUrl(url);
    QNetworkRequest request(qurl);
    setAuthHeader(request);
    QNetworkReply* reply = mgr.get(request);
    QObject::connect(reply, &QNetworkReply::finished, [=]() {
        QJsonDocument jsonResponse = QJsonDocument::fromJson(reply->readAll());
        QJsonObject jsonObject = jsonResponse.object();
        QQuickItem* timeTableDetailList = rootObject->findChild<QQuickItem*>("timetabledetaillist");
        QMetaObject::invokeMethod(timeTableDetailList, "setStop", Q_ARG(QVariant, id), Q_ARG(QVariant, jsonObject));
    });
}

void TimeTableManager::getAllAvailableTimeTablesHandler(QString date)
{
    QUrl url = QUrl("http://localhost:8080/api/timetables/search/findAllByBusIsNullAndStartDateIsAfterOrderByStartDateAsc?date=" + date);
    QNetworkRequest request(url);
    setAuthHeader(request);
    reply = mgr.get(request);
    QObject::connect(reply, &QNetworkReply::finished, [=]() {
        QJsonDocument jsonResponse = QJsonDocument::fromJson(reply->readAll());
        QJsonObject jsonObject = jsonResponse.object();
        QQuickItem* timeTableList = rootObject->findChild<QQuickItem*>("timetablelist");
        timeTableList->setProperty("timetables", QVariant(jsonObject));
    });
}

void TimeTableManager::postTakeTimetableHandler(QVariant id)
{
    QUrl url = QUrl("http://localhost:8080/api/buses/take/" + id.toString());
    QNetworkRequest request(url);
    setAuthHeader(request);
    reply = mgr.post(request, QByteArray());
    QObject::connect(reply, &QNetworkReply::finished, [=]() {
        QQuickItem* timeTableList = rootObject->findChild<QQuickItem*>("timetablelist");
        QMetaObject::invokeMethod(timeTableList, "showProfile");
    });
}
