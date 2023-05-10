
#ifndef DRIVERPROFILEMANAGER_H
#define DRIVERPROFILEMANAGER_H


#include <QObject>
#include "networkmanager.h"

class DriverProfileManager : public NetworkManager
{
    Q_OBJECT
public:
    DriverProfileManager(QObject* rootObject);
public slots:
    void getDriverActiveTimeTableHandler(QString id);
    void getDriverActiveTimeTableLineHandler(QString url);
};

#endif // DRIVERPROFILEMANAGER_H
