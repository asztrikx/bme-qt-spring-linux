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

    property string actualView: "login"

    property var role

    menuBar: MenuBar {
        visible: (app.role !== undefined)
        Menu {
            title: "Fault ticket"
            MenuItem {
                text: "List all"
                onTriggered: { actualView = "faultticketlist" }
            }
            MenuItem {
                text: "Create new"
                onTriggered: { actualView = "faultticketform" }
                enabled: (app.role === "DRIVER")
            }
        }
        Menu {
            title: "Map"
            MenuItem {
                text: "Show"
                onTriggered: { actualView = "map" }
            }
        }
    }

    FaultTicketList {
        visible: actualView === "faultticketlist"

        objectName: "faultticketlist"
        signal getAllTickets()
        signal getTicketById(string url)

        onAdd: function() {
            actualView = "faultticketform"
        }

        onSelectedItem: function(item){
            actualView = "faultticketdetail"
            faultticketdetail.enableEdit = false;
            getTicketById(item)
        }

        onEditItem: function(item) {
            actualView = "faultticketdetail"
            faultticketdetail.enableEdit = true;
            getTicketById(item)
        }
    }



    FaultTicketDetail {
        id: faultticketdetail
        visible: actualView === "faultticketdetail"
        objectName: "faultticketdetail"
        onBack: function(){ actualView = "faultticketlist" }
    }




    FaultTicketForm {
        visible: actualView === "faultticketform"
        objectName: "faultticketform"
        onSubmitClicked: function(){
            actualView = "faultticketlist"
        }
    }



    Login {
        visible: (actualView === "login")
        onLoggedIn: function() {
            actualView = "faultticketlist"
        }
        onSignUp: function() {
            actualView = "signup"
        }
    }


    SignUp {
        visible: actualView === "signup"
        onSignedUp: function() {
            actualView = "login"
        }
        onBack: function() {
            actualView = "login"
        }
    }


    /*StackView {
        id: stackView
        width: app.width
        height: app.height



        Component {
            id: map
            BusMap {
            }
        }

        initialItem: login
    }*/
}
