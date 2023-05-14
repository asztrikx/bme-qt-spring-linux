
#include "driverprofilemanager.h"
#include <QNetworkReply>
#include <QQuickItem>
#include <QJsonDocument>
#include <QJsonObject>

DriverProfileManager::DriverProfileManager(QObject* rootObject): NetworkManager(rootObject)
{
    QQuickItem* driverProfile = rootObject->findChild<QQuickItem*>("driverprofile");
    QObject::connect(driverProfile, SIGNAL(getDriverActiveTimeTable(QString)), this, SLOT(getDriverActiveTimeTableHandler(QString)));
    QObject::connect(driverProfile, SIGNAL(getDriverActiveTimeTableLine(QString)), this, SLOT(getDriverActiveTimeTableLineHandler(QString)));
    QObject::connect(driverProfile, SIGNAL(finishTimetable()), this, SLOT(postFinishTimetable()));
}

void DriverProfileManager::getDriverActiveTimeTableHandler(QString id)
{
    QUrl url = QUrl("http://localhost:8080/api/timetables/search/findTimetableByBusIsNotNullAndBusUserId?user_id=" + id);
    QNetworkRequest request(url);
    setAuthHeader(request);
    reply = mgr.get(request);
    QObject::connect(reply, &QNetworkReply::finished, [=]() {
        QQuickItem* driverProfile = rootObject->findChild<QQuickItem*>("driverprofile");
        if (reply->attribute(QNetworkRequest::HttpStatusCodeAttribute).toInt() == 200){
            QJsonDocument jsonResponse = QJsonDocument::fromJson(reply->readAll());
            QJsonObject jsonObject = jsonResponse.object();
            driverProfile->setProperty("timetable", QVariant(jsonObject));
        } else  {
            driverProfile->setProperty("timetable", QVariant("No data"));
        }

    });
}

void DriverProfileManager::getDriverActiveTimeTableLineHandler(QString url)
{
    QNetworkRequest request(url);
    setAuthHeader(request);
    reply = mgr.get(request);
    QObject::connect(reply, &QNetworkReply::finished, [=]() {
        if (reply->attribute(QNetworkRequest::HttpStatusCodeAttribute).toInt() == 200){
            QJsonDocument jsonResponse = QJsonDocument::fromJson(reply->readAll());
            QJsonObject jsonObject = jsonResponse.object();
            QQuickItem* driverProfile = rootObject->findChild<QQuickItem*>("driverprofile");
            driverProfile->setProperty("lineName", QVariant(jsonObject.value("name")));
        }
    });
}

void DriverProfileManager::postFinishTimetable()
{
    QNetworkRequest request(QUrl("http://localhost:8080/api/buses/finish"));
    setAuthHeader(request);
    reply = mgr.post(request, QByteArray());
    QObject::connect(reply, &QNetworkReply::finished, [=]() {
        if (reply->attribute(QNetworkRequest::HttpStatusCodeAttribute).toInt() == 200){
            QQuickItem* driverProfile = rootObject->findChild<QQuickItem*>("driverprofile");
            driverProfile->setProperty("timetable", QVariant("No data"));
        }
    });
}
