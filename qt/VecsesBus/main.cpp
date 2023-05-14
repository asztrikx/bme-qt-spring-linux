#include <QApplication>
#include <QQmlApplicationEngine>
#include "faultticketmanager.h"
#include "loginmanager.h"
#include "linemanager.h"
#include "timetablemanager.h"
#include "driverprofilemanager.h"
#include "mapmanager.h"

int main(int argc, char *argv[])
{
    QApplication app(argc, argv);
    QQmlApplicationEngine engine;
    engine.load(QUrl(QStringLiteral("qrc:/main.qml")));
    QObject* root = engine.rootObjects()[0];
    FaultTicketManager ftmgr(root);
    LoginManager lmgr(root);
    LineManager linemgr(root);
    TimeTableManager tmgr(root);
    DriverProfileManager dpmgr(root);
    MapManager mmgr(root);
    return app.exec();
}
