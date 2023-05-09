
#ifndef LOGINMANAGER_H
#define LOGINMANAGER_H
#include "networkmanager.h"

#include <QObject>


class LoginManager : public NetworkManager
{
    Q_OBJECT
public:
    LoginManager(QObject* rootObject);
public slots:
    void loginHandler(QString username, QString password);
    void responseLoginHandler();
    void responseGetUserHandler();
private:
    QString username;
};

#endif // LOGINMANAGER_H
