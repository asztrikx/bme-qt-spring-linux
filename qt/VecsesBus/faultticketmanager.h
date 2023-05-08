
#ifndef FAULTTICKETMANAGER_H
#define FAULTTICKETMANAGER_H
#include "networkmanager.h"

class FaultTicketManager : public NetworkManager
{
    Q_OBJECT
public:
    FaultTicketManager(QObject* rootObject);
public slots:
    void getAllFaultTicketsHandler();
    void getFaultTicketByIdHandler(QString url);

    void responseAllFaultTicketsHandler();
    void responseFaultTicketByIdHandler();
};

#endif // FAULTTICKETMANAGER_H
