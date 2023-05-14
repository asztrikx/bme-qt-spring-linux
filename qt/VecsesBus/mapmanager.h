#ifndef MAPMANAGER_H
#define MAPMANAGER_H

#include "networkmanager.h"
#include <QObject>

class MapManager: public NetworkManager {
    Q_OBJECT
  public:
    MapManager(QObject* rootObject);
  public slots:
    void drawHandler(QVariant lineId, QVariant stopId);
    void responseStops();
    void responseNextBus();
    void responseBrokenBuses();
};

#endif // MAPMANAGER_H
