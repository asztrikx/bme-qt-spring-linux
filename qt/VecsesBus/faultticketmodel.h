
#ifndef FAULTTICKETMODEL_H
#define FAULTTICKETMODEL_H
#include <QObject>
#include <QNetworkAccessManager>



class FaultTicketModel : public QObject{
    Q_OBJECT
public:
    FaultTicketModel();
    ~FaultTicketModel() = default;
public slots:
    void getAllTicketHandler();
    void onResponse();
private:
    QNetworkAccessManager mgr;
};

#endif // FAULTTICKETMODEL_H
