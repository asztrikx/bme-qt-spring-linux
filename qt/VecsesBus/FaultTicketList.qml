import QtQuick 2.15
import QtQuick.Controls 2.15
import QtQuick.Controls.Material 2.12

Item {
    anchors.fill: parent

    property var onAdd
    property var onSelectedItem
    property var onEditItem

    property var tickets
    onTicketsChanged: () => {
        if(!tickets["_embedded"]) return;
        ticketModel.clear()
        for (const ticket of tickets["_embedded"]["faultTickets"]){
            ticketModel.append({
                                   "startDate" : ticket["startDate"],
                                   "resolveDate" : (!ticket["resolveDate"]) ? "" : ticket["resolveDate"],
                                   "description" : ticket["description"],
                                   "coordinate" : ticket["coordinate"],
                                   "_state" : ticket["state"],
                                   "url" : ticket["_links"]["self"]["href"]
                               })
        }
    }

    onVisibleChanged: () => {
        if (!visible) return;
        getAllTickets()
    }

    ListModel {
        id: ticketModel
    }

    ListView {
        id: ticketList
        anchors.fill: parent
        model: ticketModel
        delegate: Rectangle {
            width: parent.width
            height: 75
            color: "white"
            border.width: 1
            border.color: "gray"

            MouseArea {
                anchors.fill: parent
                onClicked: function(){
                    onSelectedItem(url);
                }
            }

            Row {
                height: parent.height
                width: parent.width
                spacing: 10
                leftPadding: 10
                rightPadding: 10

                Column{
                    width: parent.width * 0.6
                    anchors.verticalCenter: parent.verticalCenter
                    Text {
                        id: descriptionText
                        text: description
                        elide: Text.ElideRight
                        width: parent.width
                    }
                    Text {
                        id: dateText
                        text: "(" + startDate + ")"
                    }
                }

                Canvas {
                    id: canvas
                    width: 24
                    height: 24
                    anchors.verticalCenter: parent.verticalCenter
                    onPaint: {
                        var ctx = getContext("2d")
                        ctx.clearRect(0, 0, width, height)
                        ctx.beginPath()
                        ctx.arc(12, 12, 10, 0, 2 * Math.PI)
                        ctx.fillStyle = ((_state === "Created") ? "red" : ((_state === "Resolved") ? "green" : "yellow"))
                        ctx.fill()
                        ctx.stroke()
                    }
                }

                Button {
                    text: "Edit"
                    anchors.verticalCenter: parent.verticalCenter
                    visible: (app.role === "MAINTENANCE")
                    onClicked: {
                        onEditItem(url)
                    }
                }

            }



        }
    }

    Button {
        text: "+"
        anchors.bottom: parent.bottom
        anchors.bottomMargin: 50
        font.pixelSize: 20
        anchors.horizontalCenter: parent.horizontalCenter
        onClicked: {
            if (onAdd !== undefined){
                onAdd();
            }
        }
    }
}
