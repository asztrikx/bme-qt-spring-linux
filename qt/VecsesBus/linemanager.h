
#ifndef LINEMANAGER_H
#define LINEMANAGER_H


#include <QObject>
#include "networkmanager.h"

class LineManager : NetworkManager
{
    Q_OBJECT
public:
    LineManager(QObject* rootObject);
public slots:
    void getAllLinesHandler();
    void responseGetAllLinesHandler();

    void getLineStopsHandler(QString url);
    void responseGetLinesStopsHandler();

    void subscribeForHandler(QVariant id);
    void unsubscribeFromHandler(QVariant id);
};

#endif // LINEMANAGER_H
