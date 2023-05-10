import QtQuick 2.15
import QtQuick.Controls 2.15
import QtQuick.Controls.Material 2.12

Item {
    anchors.fill: parent

    function setTimeTableLine(id, line){
        timetablemodel.get(id).name = line["name"]
        timetablemodel.get(id).line = line
    }
    property var timetables
    onTimetablesChanged: () => {
        if(!timetables["_embedded"]) return;
        timetablemodel.clear()
        for (const timetable of timetables["_embedded"]["timetables"]){
            timetablemodel.append({
                                   "startDate" : timetable["startDate"].split("T")[1],
                                   "lineUrl" : timetable["_links"]["line"]["href"],
                                   "url" : timetable["_links"]["self"]["href"],
                                   "name" : "Loading...",
                                   "line" : {}
                               })
            getTimeTableLine(timetablemodel.count - 1, timetable["_links"]["line"]["href"])
        }
    }

    onVisibleChanged: () => {
        if (!visible) return;
        getAllTimeTables()
    }

    ListModel {
        id: timetablemodel
    }

    ListView {
        id: timetablelist
        anchors.fill: parent
        model: timetablemodel
        delegate: Rectangle {
            width: parent.width
            height: 40
            color: "white"
            border.width: 1
            border.color: "gray"

            MouseArea {
                anchors.fill: parent
                onClicked: function(){
                    if (line["name"] !== undefined){
                        getAllSectionByTimeTable(line["_links"]["route"]["href"], startDate)
                    }
                }
            }

            Row {
                height: parent.height
                width: parent.width
                spacing: 10
                leftPadding: 10
                rightPadding: 10

                Row{
                    width: parent.width
                    height: parent.height

                    Text {
                        id: dateText
                        text: startDate + " - " + name
                        elide: Text.ElideRight
                        font.pixelSize: 16
                        anchors.verticalCenter: parent.verticalCenter
                    }
                }

            }

        }
    }
}
