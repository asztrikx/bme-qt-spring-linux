
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
    QQuickItem* signUpView = rootObject->findChild<QQuickItem*>("signup");
    QObject::connect(signUpView, SIGNAL(signUp(QVariant)), this, SLOT(signUpHandler(QVariant)));
}


void LoginManager::loginHandler(QString username, QString password)
{
    NetworkManager::setAuth(username + ":" + password);
    QUrl url = QUrl(baseUrl + "/");
    QNetworkRequest request(url);
    setAuthHeader(request);
    reply = mgr.get(request);
    QObject::connect(reply, SIGNAL(finished()), this, SLOT(responseLoginHandler()));
}

void LoginManager::responseLoginHandler()
{
    QQuickItem* login = rootObject->findChild<QQuickItem*>("login");
    if (reply->attribute(QNetworkRequest::HttpStatusCodeAttribute).toInt() == 200){
        QUrl url = QUrl(baseUrl + "/users/details");
        QNetworkRequest request(url);
        setAuthHeader(request);
        QNetworkReply* reply = mgr.get(request);
        QObject::connect(reply, &QNetworkReply::finished, [=](){
            QJsonDocument jsonResponse = QJsonDocument::fromJson(reply->readAll());
            QJsonObject jsonObject = jsonResponse.object();
            rootObject->setProperty("userData", QVariant(jsonObject));
            QMetaObject::invokeMethod(login, "onLoggedIn");
        });
    } else {
        QMetaObject::invokeMethod(login, "handleError", Q_ARG(QVariant, "Bad username or password!"));
    }
}

void LoginManager::signUpHandler(QVariant user)
{
    QByteArray data = user.toByteArray();
    QUrl url = QUrl(QString(baseUrl + "/users/register"));
    QNetworkRequest request(url);
    request.setHeader(QNetworkRequest::ContentTypeHeader, "application/json");
    reply = mgr.post(request, data);
    QObject::connect(reply, SIGNAL(finished()), this, SLOT(responseSignUpHandler()));
}

void LoginManager::responseSignUpHandler()
{
    QQuickItem* signUp = rootObject->findChild<QQuickItem*>("signup");
    if (reply->attribute(QNetworkRequest::HttpStatusCodeAttribute).toInt() == 200){
        QMetaObject::invokeMethod(signUp, "onSignedUp");
    } else {
        QMetaObject::invokeMethod(signUp, "setError", Q_ARG(QVariant, "Registration failed"));
    }
}

