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
                                   "number": line["name"].split(" ")[0],
                                   "name" : line["name"].substring(line["name"].indexOf("(") + 1, line["name"].indexOf(")")),
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
                    Row{
                        width: parent.width * 0.5
                        spacing: 10
                        Rectangle{
                            width: 20 + number.length * 5
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


                    Button{
                        text: "+"
                        visible: app.isAnyRole("User")
                        onClicked: {
                            const urlParts = url.split("/")
                            subscribeFor(urlParts[urlParts.length - 1])
                        }
                    }
                    Button{
                        text: "-"
                        visible: app.isAnyRole("User")
                        onClicked: {
                            const urlParts = url.split("/")
                            unsubscribeFrom(urlParts[urlParts.length - 1])
                        }
                    }
                }

            }

        }
    }
}
