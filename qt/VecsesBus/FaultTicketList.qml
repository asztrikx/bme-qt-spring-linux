import QtQuick 2.15
import QtQuick.Controls 2.15
import QtQuick.Controls.Material 2.12

Item {
    anchors.fill: parent

    property var tickets
    onTicketsChanged: () => {
        if(!tickets["_embedded"]) return;
        ticketModel.clear()
        for (const ticket of tickets["_embedded"]["faultTickets"]){
            ticketModel.append({
                                   "startDate" : ticket["startDate"],
                                   "description" : ticket["description"],
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
                    visible: (app.userData !== undefined && app.userData["roles"].includes("Maintenance"))
                    onClicked: {
                        onEditItem(url)
                    }
                }

            }



        }
    }

    Row {
        anchors.bottomMargin: 50
        anchors.horizontalCenter: parent.horizontalCenter
        anchors.bottom: parent.bottom
        spacing: 10
        Button {
            text: "Add"
            font.pixelSize: 20
            visible: (app.userData !== undefined && app.userData["roles"].includes("Maintenance"))
            onClicked: {
                onAdd();
            }
        }
        Button {
            text: "Stats"
            font.pixelSize: 20
            onClicked: {
                let created = 0;
                let inProgress = 0;
                let resolved = 0;
                for (const ticket of tickets["_embedded"]["faultTickets"]){
                    if (ticket["state"] === "Created") created++;
                    else if(ticket["state"] === "InProgress") inProgress++;
                    else if(ticket["state"] === "Resolved") resolved++;
                }
                onStats({ "created" : created, "inProgress" : inProgress, "resolved" : resolved });
            }
        }
    }

}
