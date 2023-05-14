
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
    void createFaultTicketHandler(QVariant ticket);
    void saveFaultTicketHandler(QString state, QString url);
    void deleteFaultTicketHandler(QString url);
    void getAllFaultTicketsByUser(QVariant id);

    void responseAllFaultTicketsHandler();
    void responseFaultTicketByIdHandler();
    void responseCreateFaultTicketHandler();
    void responseSaveFaultTicketHandler();
    void responseDeleteFaultTicketHandler();


};

#endif // FAULTTICKETMANAGER_H
