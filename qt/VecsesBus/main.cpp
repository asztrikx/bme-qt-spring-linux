#include <QApplication>
#include <QQmlApplicationEngine>
#include "networkmanager.h"


int main(int argc, char *argv[])
{
    QApplication app(argc, argv);
    QQmlApplicationEngine engine;
    engine.load(QUrl(QStringLiteral("qrc:/main.qml")));
    NetworkManager mgr(engine.rootObjects()[0]);
    return app.exec();
}
