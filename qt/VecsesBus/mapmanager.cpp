#include "mapmanager.h"
#include <QQuickItem>
#include <QNetworkReply>
#include <QMetaObject>
#include <QJsonDocument>
#include <QJsonObject>
#include <QJsonArray>
#include <QJsonValue>
#include <QDebug>

MapManager::MapManager(QObject* rootObject) : NetworkManager(rootObject) {
    QQuickItem* mapView = rootObject->findChild<QQuickItem*>("map");
    QObject::connect(mapView, SIGNAL(network(QVariant, QVariant)), this, SLOT(drawHandler(QVariant, QVariant)));
}

void MapManager::drawHandler(QVariant lineId, QVariant stopId) {
    QNetworkRequest request0(QUrl("http://localhost:8080/api/lines/" + lineId.toString() + "/route"));
    setAuthHeader(request0);
    QNetworkReply* reply0 = mgr.get(request0);
    QObject::connect(reply0, &QNetworkReply::finished, [=]() {
        QJsonDocument jsonResponse = QJsonDocument::fromJson(reply0->readAll());
        QJsonObject jsonObject = jsonResponse.object();
        QQuickItem* mapView = rootObject->findChild<QQuickItem*>("map");
        mapView->setProperty("sections", QVariant(jsonObject));
    });

    QNetworkRequest request1(QUrl("http://localhost:8080/api/lines/" + lineId.toString() + "/stops"));
    setAuthHeader(request1);
    QNetworkReply* reply1 = mgr.get(request1);
    QObject::connect(reply1, &QNetworkReply::finished, [=]() {
        QJsonDocument jsonResponse = QJsonDocument::fromJson(reply1->readAll());
        QJsonObject jsonObject = jsonResponse.object();
        QQuickItem* mapView = rootObject->findChild<QQuickItem*>("map");
        mapView->setProperty("stops", QVariant(jsonObject));
    });

    QNetworkRequest request2(QUrl("http://localhost:8080/api/lines/" + lineId.toString() + "/nextbus/" + stopId.toString()));
    setAuthHeader(request2);
    QNetworkReply* reply2 = mgr.get(request2);
    QObject::connect(reply2, &QNetworkReply::finished, [=]() {
        QJsonDocument jsonResponse = QJsonDocument::fromJson(reply2->readAll());
        QJsonObject jsonObject = jsonResponse.object();
        QQuickItem* mapView = rootObject->findChild<QQuickItem*>("map");
        if (reply2->attribute(QNetworkRequest::HttpStatusCodeAttribute).toInt() == 200) {
            mapView->setProperty("nextBus", QVariant(jsonObject));
        } else {
            mapView->setProperty("nextBus", QVariant("nodata"));
        }
    });

    QNetworkRequest request3(QUrl("http://localhost:8080/api/lines/" + lineId.toString() + "/brokenbuses/" + stopId.toString()));
    setAuthHeader(request3);
    QNetworkReply* reply3 = mgr.get(request3);
    QObject::connect(reply3, &QNetworkReply::finished, [=]() {
        QJsonDocument jsonResponse = QJsonDocument::fromJson(reply3->readAll());
        QQuickItem* mapView = rootObject->findChild<QQuickItem*>("map");
        if (reply3->attribute(QNetworkRequest::HttpStatusCodeAttribute).toInt() == 200) {
            mapView->setProperty("brokenBuses", QVariant(jsonResponse.array()));
        } else {
            mapView->setProperty("brokenBuses", QVariant("nodata"));
        }
    });
}
