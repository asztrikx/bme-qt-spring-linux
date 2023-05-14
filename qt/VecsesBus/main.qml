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

    function isAnyRole(...roles) {
        if (typeof app.userData === "undefined") return false;

        for (const role of roles) {
            if (app.userData["roles"].includes(role)) {
                return true;
            }
        }
        return false;
    }

    menuBar: MenuBar {
        visible: (app.userData !== undefined)

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

        Menu {
            title: "Profile"
            MenuItem {
                text: "Show"
                enabled: isAnyRole("Driver")
                onTriggered: { actualView = "driverprofile" }
            }
            MenuItem {
                text: "Logout"
                onTriggered: { actualView = "login"; userData = undefined }
            }
        }

        Menu {
            title: "Fault ticket"
            MenuItem {
                text: "List all"
                onTriggered: { actualView = "faultticketlist" }
                enabled: isAnyRole("Driver", "Maintenance")
            }
            MenuItem {
                text: "Create new"
                onTriggered: { actualView = "faultticketform" }
                enabled: isAnyRole("Driver")
            }
        }
    }

    FaultTicketList {
        visible: actualView === "faultticketlist"

        objectName: "faultticketlist"
        signal getAllTickets()
        signal getAllTicketsByUser(var id)
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

        signal getAllTimeTableByLineId(var id);
        function onBack(){
            actualView = "linelist"
        }
    }

    TimeTableList {
        visible: actualView === "timetablelist"
        objectName: "timetablelist"

        signal getAllTimeTables()
        signal getAllAvailableTimeTables(string date)
        signal getTimeTableLine(var id, string url)
        signal getAllSectionByTimeTable(string url, string startDate)
        signal takeTimeTable(var id)
        function showProfile() {
            actualView = "driverprofile"
        }

    }

    TimeTableDetailList {
        id: timeTableDetailList
        visible: actualView === "timetabledetaillist"
        objectName: "timetabledetaillist"

        signal getTimeTableSection(var id, string url)

        function onSectionsLoaded() {
            actualView = "timetabledetaillist"
        }

        function onBack() {
            actualView = "timetablelist"
        }

        function onSelectedItem(lineId, stopId){
            map.lineId = lineId
            map.stopId = stopId
            actualView = "map"
        }
    }

    DriverProfile {
        visible: actualView === "driverprofile"
        objectName: "driverprofile"
        signal finishTimetable()
        signal getDriverActiveTimeTable(string id)
        signal getDriverActiveTimeTableLine(string url)
    }


    Login {
        objectName: "login"
        visible: (actualView === "login")
        function onLoggedIn() {
            actualView = "linelist"
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

    // TODO remove timer from Busmap, replace it
    BusMap {
        id: map
        objectName: "map"
        visible: actualView === "map"
        lineId: 1
        stopId: 1

        function onBack(){
            actualView = "timetabledetaillist"
        }
    }
}
