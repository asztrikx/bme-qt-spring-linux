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
                                   "startDate" : timetable["startDate"].split("T")[1].split(".")[0],
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
        if (app.isAnyRole("Driver")){
            let tzoffset = new Date().getTimezoneOffset() * 60000;
            let localISOTime = new Date(Date.now() - tzoffset).toISOString()
            // remove Z
            localISOTime = localISOTime.slice(0, -1);
            // remove ms
            localISOTime = localISOTime.split(".")[0]
            getAllAvailableTimeTables(localISOTime)
        } else {
            getAllTimeTables()
        }

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
                        timeTableDetailList.lineID = line["id"]
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
                    spacing: 10
                    Text {
                        id: dateText
                        text: startDate + " - " + name
                        elide: Text.ElideRight
                        font.pixelSize: 16
                        anchors.verticalCenter: parent.verticalCenter
                        width: parent.width * 0.8
                    }

                    Text {
                        text: "Take"
                        anchors.verticalCenter: parent.verticalCenter
                        visible: (app.isAnyRole("Driver"))
                        font.pixelSize: 16
                        font.family: "Roboto"
                        MouseArea {
                            anchors.fill: parent
                            cursorShape: Qt.PointingHandCursor
                            hoverEnabled: true

                            onEntered: {
                                textDecoration: Text.Underline
                            }

                            onExited: {
                                textDecoration: Text.NoUnderline
                            }

                            onClicked: {
                                const urlParts = url.split("/");
                                takeTimeTable(urlParts[urlParts.length - 1]);
                            }
                        }
                    }
                }

            }

        }
    }
}
