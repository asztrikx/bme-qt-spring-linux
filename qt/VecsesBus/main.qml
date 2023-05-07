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

    property var cookie
    property var role

    menuBar: MenuBar {
        visible: (app.cookie !== undefined)
        Menu {
            title: "Fault ticket"
            MenuItem {
                text: "List all"
                onTriggered: stackView.push(faultticketlist)
            }
            MenuItem {
                text: "Create new"
                onTriggered: stackView.push(faultticketform)
                enabled: (app.role === "DRIVER")
            }
        }
        Menu {
            title: "Map"
            MenuItem {
                text: "Show"
                onTriggered: stackView.push(map)
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

                onAdd: function() {
                    stackView.push(faultticketform);
                }

                onSelectedItem: function(item){
                    stackView.push(faultticketdetail, {"url" : item});
                }

                onEditItem: function(item) {
                    stackView.push(faultticketedit, {"url" : item});
                }
            }
        }


        Component {
            id: faultticketdetail
            FaultTicketDetail {
                onBack: function(){ stackView.pop(); }
            }
        }

        Component {
            id: faultticketedit
            FaultTicketEdit {
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

        Component {
            id: login
            Login {
                onLoggedIn: function() {
                    stackView.push(faultticketlist)
                }
                onSignUp: function() {
                    stackView.push(signup)
                }
            }
        }

        Component {
            id: signup
            SignUp {
                onSignedUp: function() {
                    stackView.pop()
                }
                onBack: function() {
                    stackView.pop()
                }
            }

        }

        Component {
            id: map
            BusMap {
            }
        }

        initialItem: login
    }
}
