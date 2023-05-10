import QtQuick 2.15
import QtQuick.Controls 2.15
import QtQuick.Controls.Material 2.12

Rectangle {
    anchors.fill: parent
    color: "white"

    property var stops
    onStopsChanged: () => {
        if(!stops["_embedded"]) return;
        stopmodel.clear()
        for (const stop of stops["_embedded"]["stops"]){
            stopmodel.append({
                                   "name" : stop["name"],
                                   "coordinate" : stop["coordinate"],
                                   "url" : stop["_links"]["self"]["href"]
                               })
        }
    }

    ListModel {
        id: stopmodel
    }

    ListView {
        id: stopList
        anchors.fill: parent
        model: stopmodel
        delegate: Rectangle {
            width: parent.width
            height: 65
            color: "white"

            Row {
                height: parent.height
                width: parent.width
                spacing: 10
                leftPadding: 10
                rightPadding: 10

                Row{
                    width: parent.width
                    spacing: 10

                    Canvas {
                        id: canvas
                        width: 30
                        height: 65
                        onPaint: {
                            var ctx = getContext("2d")
                            ctx.clearRect(0, 0, width, height)
                            ctx.fillStyle = "purple";
                            let start_y = (index === 0) ? 32.5 : 0
                            let height_y = (index === 0 || index === stopmodel.count - 1) ? 32.5 : 65
                            ctx.fillRect(10, start_y, 10, height_y);
                            ctx.beginPath();
                            ctx.arc(15, 32.5, 10, 0, 2 * Math.PI);
                            ctx.fillStyle = "white";
                            ctx.fill();
                            ctx.strokeStyle = "purple";
                            ctx.lineWidth = 5;
                            ctx.stroke();
                        }
                    }

                    Text {
                        id: nameText
                        text: name
                        elide: Text.ElideRight
                        font.pixelSize: 16
                        anchors.verticalCenter: parent.verticalCenter
                    }
                }

            }

        }
    }
    Row {
        anchors.bottomMargin: 50
        anchors.horizontalCenter: parent.horizontalCenter
        anchors.bottom: parent.bottom

        Button {
            text: "Back"
            font.pixelSize: 20
            onClicked: onBack()
        }
    }
}
