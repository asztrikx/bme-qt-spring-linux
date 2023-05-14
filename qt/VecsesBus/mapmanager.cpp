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
    QUrl url = QUrl("http://localhost:8080/api/lines/" + lineId.toString() + "/stops");
    QNetworkRequest request(url);
    setAuthHeader(request);
    reply = mgr.get(request);
    QObject::connect(reply, SIGNAL(finished()), this, SLOT(responseStops()));
    //std::cout << "################################x";
/* reply will be overwritten
    QNetworkRequest request(QUrl("http://localhost:8080/api/lines/" + lineId.toString() + "/nextbus/" + stopId.toString()));
    setAuthHeader(request);
    reply = mgr.get(request);
    QObject::connect(reply, SIGNAL(finished()), this, SLOT(responseNextBus()));

    QNetworkRequest request(QUrl("http://localhost:8080/api/lines/" + lineId.toString() + "/brokenbuses/" + stopId.toString()));
    setAuthHeader(request);
    reply = mgr.get(request);
    QObject::connect(reply, SIGNAL(finished()), this, SLOT(responseBrokenBuses()));*/
}

void MapManager::responseStops() {
    qDebug() << "################################x";
    QJsonDocument jsonResponse = QJsonDocument::fromJson(reply->readAll());
    QJsonObject jsonObject = jsonResponse.object();
    QQuickItem* mapView = rootObject->findChild<QQuickItem*>("map");
    mapView->setProperty("stops", QVariant(jsonObject));
}

void MapManager::responseNextBus() {
    QJsonDocument jsonResponse = QJsonDocument::fromJson(reply->readAll());
    QJsonObject jsonObject = jsonResponse.object();
    QQuickItem* mapView = rootObject->findChild<QQuickItem*>("map");
    mapView->setProperty("nextBus", QVariant(jsonObject));
}

void MapManager::responseBrokenBuses() {
    QJsonDocument jsonResponse = QJsonDocument::fromJson(reply->readAll());
    QJsonObject jsonObject = jsonResponse.object();
    QQuickItem* mapView = rootObject->findChild<QQuickItem*>("map");
    mapView->setProperty("brokenBuses", QVariant(jsonObject));
}
