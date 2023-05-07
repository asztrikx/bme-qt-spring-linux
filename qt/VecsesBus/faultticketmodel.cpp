
#include "faultticketmodel.h"

#include <QNetworkReply>
#include <QUrl>
#include <QNetworkRequest>

FaultTicketModel::FaultTicketModel() : QObject(nullptr){}

void FaultTicketModel::getAllTicketHandler(){
    QNetworkReply *reply = mgr.get(QNetworkRequest(QUrl("https://dummyjson.com/products/1")));
    //QObject::connect(reply, &QNetworkReply::finished, this, FaultTicketModel::onResponse());
}

void FaultTicketModel::onResponse(){

}
