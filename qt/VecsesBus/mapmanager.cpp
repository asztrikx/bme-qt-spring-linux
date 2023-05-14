#include "mapmanager.h"
#include <QQuickItem>
#include <QNetworkReply>
#include <QMetaObject>
#include <QJsonDocument>
#include <QJsonObject>
#include <QDebug>

MapManager::MapManager(QObject* rootObject) : NetworkManager(rootObject) {
    QQuickItem* mapView = rootObject->findChild<QQuickItem*>("map");
    QObject::connect(mapView, SIGNAL(network(QVariant, QVariant)), this, SLOT(drawHandler(QVariant, QVariant)));
}

void MapManager::drawHandler(QVariant lineId, QVariant stopId) {
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
        mapView->setProperty("nextBus", QVariant(jsonObject));
    });

    QNetworkRequest request3(QUrl("http://localhost:8080/api/lines/" + lineId.toString() + "/brokenbuses/" + stopId.toString()));
    setAuthHeader(request3);
    QNetworkReply* reply3 = mgr.get(request3);
    QObject::connect(reply3, &QNetworkReply::finished, [=]() {
        QJsonDocument jsonResponse = QJsonDocument::fromJson(reply3->readAll());
        QJsonObject jsonObject = jsonResponse.object();
        QQuickItem* mapView = rootObject->findChild<QQuickItem*>("map");
        mapView->setProperty("brokenBuses", QVariant(jsonObject));
    });
}
