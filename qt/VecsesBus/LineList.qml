import QtQuick 2.15
import QtQuick.Controls 2.15
import QtQuick.Controls.Material 2.12

Item {
    anchors.fill: parent

    property var lines
    onLinesChanged: () => {
        if(!lines["_embedded"]) return;
        linemodel.clear()
        for (const line of lines["_embedded"]["lines"]){
            linemodel.append({
                                   "number": line["name"].split(" - ")[0],
                                   "name" : line["name"].split(" - ")[1],
                                   "stops" : line["_links"]["stops"]["href"],
                                   "timetable" : line["_links"]["timetable"]["href"],
                                   "route" : line["_links"]["route"]["href"],
                                   "url" : line["_links"]["self"]["href"]
                               })
        }
    }

    onVisibleChanged: () => {
        if (!visible) return;
        getAllLines()
    }

    ListModel {
        id: linemodel
    }

    ListView {
        id: lineList
        anchors.fill: parent
        model: linemodel
        delegate: Rectangle {
            width: parent.width
            height: 75
            color: "white"
            border.width: 1
            border.color: "gray"

            MouseArea {
                anchors.fill: parent
                onClicked: function(){
                    onSelectedItem(stops);
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
                    anchors.verticalCenter: parent.verticalCenter
                    spacing: 10
                    Rectangle{
                        width: 25
                        height: 25
                        color: "blue"
                        Text {
                            anchors.centerIn: parent
                            id: numberText
                            text: number
                            color: "white"
                            font.bold: true
                        }
                    }

                    Text {
                        id: nameText
                        text: name
                        elide: Text.ElideRight
                        font.pixelSize: 16
                    }
                }

            }

        }
    }
}
