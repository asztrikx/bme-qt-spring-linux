
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
    QObject::connect(lineListView, SIGNAL(subscribeFor(QVariant)), this, SLOT(subscribeForHandler(QVariant)));
    QObject::connect(lineListView, SIGNAL(unsubscribeFrom(QVariant)), this, SLOT(unsubscribeFromHandler(QVariant)));
}


void LineManager::getAllLinesHandler()
{
    QUrl url = QUrl(baseUrl + "/lines");
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
void LineManager::responseGetLinesStopsHandler()
{
    QJsonDocument jsonResponse = QJsonDocument::fromJson(reply->readAll());
    QJsonObject jsonObject = jsonResponse.object();
    QQuickItem* stopList = rootObject->findChild<QQuickItem*>("stoplist");
    stopList->setProperty("stops", QVariant(jsonObject));
}


void LineManager::subscribeForHandler(QVariant id)
{
    QUrl url = QUrl(baseUrl + "/users/subscribe/" + id.toString());
    QNetworkRequest request(url);
    setAuthHeader(request);
    reply = mgr.post(request, QByteArray());
    QObject::connect(reply, &QNetworkReply::finished, [=](){
        QObject* lineDialog = rootObject->findChild<QObject*>("linedialog");
        if (reply->attribute(QNetworkRequest::HttpStatusCodeAttribute).toInt() == 200)
        {
            QMetaObject::invokeMethod(lineDialog, "show", Q_ARG(QVariant, "Success subscription"));
        }
        else
        {
            QMetaObject::invokeMethod(lineDialog, "show", Q_ARG(QVariant, "Failed to subscribe, you may already be subscribed."));
        }

    });
}

void LineManager::unsubscribeFromHandler(QVariant id)
{
    QUrl url = QUrl(baseUrl + "/users/unsubscribe/" + id.toString());
    QNetworkRequest request(url);
    setAuthHeader(request);
    reply = mgr.post(request, QByteArray());
    QObject::connect(reply, &QNetworkReply::finished, [=](){
        QObject* lineDialog = rootObject->findChild<QObject*>("linedialog");
        if (reply->attribute(QNetworkRequest::HttpStatusCodeAttribute).toInt() == 200)
        {
            QMetaObject::invokeMethod(lineDialog, "show", Q_ARG(QVariant, "Success unsubscription"));
        }
        else
        {
            QMetaObject::invokeMethod(lineDialog, "show", Q_ARG(QVariant, "Failed to unsubscribe, you may already be unsubscribed."));
        }
    });
}
