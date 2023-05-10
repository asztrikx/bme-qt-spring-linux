import QtQuick 2.15

Rectangle {
    color: "white"
    anchors.fill: parent
    property string lineName
    property string startDate
    property var timetable
    onTimetableChanged: () => {
        lineName = "No data"
        startDate = "No data"
        if (timetable !== "No data"){
            startDate = timetable["startDate"]
            getDriverActiveTimeTableLine(timetable["_links"]["line"]["href"])
        }
    }

    onVisibleChanged: () => {
        if (app.userData !== undefined){
            getDriverActiveTimeTable(app.userData["id"])
        }

    }

    Column {
        spacing: 10
        anchors.centerIn: parent
        Text {
            anchors.horizontalCenter: parent.horizontalCenter
            text: "Latest job"
            font.pixelSize: 30
            font.bold: true
        }
        Row {

            spacing: 10
            Text {
                text: "Line name:"
                font.bold: true
                font.pixelSize: 16
            }
            Text {
                text: lineName
                font.pixelSize: 16
            }
        }
        Row {

            spacing: 10
            Text {
                text: "Starting time:"
                font.bold: true
                font.pixelSize: 16
            }
            Text {
                text: startDate
                font.pixelSize: 16
            }
        }

    }
}
