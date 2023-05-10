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

    property var userData

    menuBar: MenuBar {
        visible: (app.userData !== undefined)
        Menu {
            title: "Fault ticket"
            MenuItem {
                text: "List all"
                onTriggered: { actualView = "faultticketlist" }
            }
            MenuItem {
                text: "Create new"
                onTriggered: { actualView = "faultticketform" }
                enabled: (app.userData !== undefined && app.userData["roles"].includes("Driver"))
            }
        }
        Menu {
            title: "Line"
            MenuItem {
                text: "List all"
                onTriggered: { actualView = "linelist" }
            }
        }

        Menu {
            title: "Timetable"
            MenuItem {
                text: "List all"
                onTriggered: { actualView = "timetablelist" }
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

        function onAdd() {
            actualView = "faultticketform"
        }

        function onStats(stats) {
            faultticketstats.created = stats["created"]
            faultticketstats.inProgress = stats["inProgress"]
            faultticketstats.resolved = stats["resolved"]
            faultticketstats.invalidate()
            actualView = "faultticketstats"
        }

        function onSelectedItem(item){
            actualView = "faultticketdetail"
            faultticketdetail.enableEdit = false;
            getTicketById(item)
        }

        function onEditItem(item) {
            actualView = "faultticketdetail"
            faultticketdetail.enableEdit = true;
            getTicketById(item)
        }
    }



    FaultTicketDetail {
        id: faultticketdetail
        visible: actualView === "faultticketdetail"
        objectName: "faultticketdetail"
        function onBack(){ actualView = "faultticketlist" }
    }




    FaultTicketForm {
        visible: actualView === "faultticketform"
        objectName: "faultticketform"
        function onSubmitClicked(){
            actualView = "faultticketlist"
        }
    }

    FaultTicketStats {
        id: faultticketstats
        visible: actualView === "faultticketstats"

        function onBack() {
            actualView = "faultticketlist"
        }
    }



    LineList {
        visible: actualView === "linelist"
        objectName: "linelist"
        signal getAllLines()
        signal getLineStops(string url)

        function onSelectedItem(item){
            getLineStops(item)
            actualView = "stoplist"
        }

    }

    StopList {
        visible: actualView === "stoplist"
        objectName: "stoplist"

        function onBack(){
            actualView = "linelist"
        }
    }

    TimeTableList {
        visible: actualView === "timetablelist"
        objectName: "timetablelist"

        signal getAllTimeTables()
        signal getTimeTableLine(var id, string url)

        function onSelectedItem() {
        }
    }

    Login {
        objectName: "login"
        visible: (actualView === "login")
        function onLoggedIn() {
            actualView = "faultticketlist"
        }
        function onSignUp() {
            actualView = "signup"
        }
    }


    SignUp {
        objectName: "signup"
        signal signUp(var user)
        visible: actualView === "signup"
        function onSignedUp() {
            actualView = "login"
        }
        function onBack() {
            actualView = "login"
        }
    }


    /*BusMap {
    }*/

}
