import QtQuick 2.15
import QtQuick.Controls 2.15
import QtQuick.Layouts 1.12
import QtQuick.Window 2.15

ApplicationWindow {
    id: app
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
        width: app.width
        height: app.height

        Component {
            id: faultticketlist
            FaultTicketList {

                Component.onCompleted: function(){
                    app.getAllTickets()
                }

                onAdd: function() {
                    stackView.push(faultticketform);
                }

                onSelectedItem: function(item){
                    stackView.push(faultticketdetail, {"fid" : item});
                }
            }
        }


        Component {
            id: faultticketdetail
            FaultTicketDetail {
                id: detailView
                onBack: function(){ stackView.pop(); }
            }
        }


        Component {
            id: faultticketform
            FaultTicketForm {

                onSubmitClicked: function(){
                    stackView.pop()
                }
            }
        }




        initialItem: faultticketlist
    }
}
