
#include "loginmanager.h"
#include <QQuickItem>
#include <QNetworkReply>
#include <QMetaObject>
#include <QJsonDocument>
#include <QJsonObject>

LoginManager::LoginManager(QObject* rootObject) : NetworkManager(rootObject)
{
    QQuickItem* loginView = rootObject->findChild<QQuickItem*>("login");
    QObject::connect(loginView, SIGNAL(login(QString, QString)), this, SLOT(loginHandler(QString, QString)));
}


void LoginManager::loginHandler(QString username, QString password)
{
    this->username = username;
    NetworkManager::setAuth(username + ":" + password);
    QUrl url = QUrl(QString("http://localhost:8080/api"));
    QNetworkRequest request(url);
    setAuthHeader(request);
    reply = mgr.get(request);
    QObject::connect(reply, SIGNAL(finished()), this, SLOT(responseLoginHandler()));
}

void LoginManager::responseLoginHandler()
{
    if (reply->attribute(QNetworkRequest::HttpStatusCodeAttribute).toInt() == 200){
        QUrl url = QUrl(QString("http://localhost:8080/api/users/search/findUserByUsername?username=" + username));
        QNetworkRequest request(url);
        setAuthHeader(request);
        reply = mgr.get(request);
        QObject::connect(reply, SIGNAL(finished()), this, SLOT(responseGetUserHandler()));
    } else {
        QQuickItem* login = rootObject->findChild<QQuickItem*>("login");
        QMetaObject::invokeMethod(login, "handleError", Q_ARG(QVariant, "Bad username or password!"));
    }
}

void LoginManager::responseGetUserHandler()
{
    QJsonDocument jsonResponse = QJsonDocument::fromJson(reply->readAll());
    QJsonObject jsonObject = jsonResponse.object();
    rootObject->setProperty("userData", QVariant(jsonObject));
    qDebug() << jsonObject;
    QQuickItem* login = rootObject->findChild<QQuickItem*>("login");
    QMetaObject::invokeMethod(login, "onLoggedIn");
}
