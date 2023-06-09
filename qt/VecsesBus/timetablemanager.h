
#ifndef TIMETABLEMANAGER_H
#define TIMETABLEMANAGER_H


#include <QObject>
#include "networkmanager.h"

class TimeTableManager : public NetworkManager
{
    Q_OBJECT
public:
    TimeTableManager(QObject* rootObject);
public slots:
    void getAllTimeTablesHandler();
    void responseGetAllTimeTablesHandler();
    void getAllAvailableTimeTablesHandler(QString date);
    void getTimeTableLineHandler(QVariant id, QString url);
    void getAllSectionByTimeTableHandler(QString url, QString startDate);
    void getTimeTableSectionHandler(QVariant id, QString url);
    void postTakeTimetableHandler(QVariant id);
};

#endif // TIMETABLEMANAGER_H
