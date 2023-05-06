import QtQuick 2.15
import QtQuick.Controls 2.15
import QtQuick.Layouts 1.12
import QtQuick.Window 2.15

ApplicationWindow {
    width: 400
    height: 600
    visible: true
    title: "VecsesIoT"

    menuBar: MenuBar {
        Menu {
            title: "Fault ticket"
            MenuItem {
                text: "List all"
                onTriggered: stackView.push(faultticketlist)
            }
            MenuItem {
                text: "Create new"
                onTriggered: stackView.push(faultticketform)
            }
        }
    }

    StackView {
        id: stackView
        anchors.fill: parent

        Component {
            id: faultticketlist

            FaultTicketList {
                onAdd: function() {
                    stackView.push(faultticketform);
                }

                onSelectedItem: function(item){
                    console.log(item);
                    stackView.push(faultticketdetail);
                }
            }

        }

        Component {
            id: faultticketform

            FaultTicketForm {
                onSubmitClicked: function(ticket){
                    stackView.pop();
                    console.log(ticket);
                }
            }
        }

        Component {
            id: faultticketdetail
            FaultTicketDetail {
                onBack: function(){ stackView.pop(); }
                faultTicket: {
                    "description": "Engine is broken",
                    "startDate": "2022.10.22.",
                    "resolveDate": "-",
                    "coordinate": "Lon 43.1111 Lat 35.123",
                    "state": "In Progress",
                    "user_name": "Aladar Alajos",
                    "bus_name": "KGF123"
                }
            }
        }

        initialItem: faultticketlist
    }
}
