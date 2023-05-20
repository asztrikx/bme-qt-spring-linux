import QtQuick 2.15
import QtQuick.Controls 2.5
import QtQuick.Controls.Material 2.12

Rectangle {
    anchors.fill: parent
    color: "white"

    signal saveTicket(string state, string url)
    signal deleteTicket(string url)


    property bool enableEdit: false
    property var ticket
    onTicketChanged: function() {
        if (!ticket) return;
        descriptionLabel.text = "Description: " + ticket["description"]
        startDateLabel.text = "Start Date: " + ticket["startDate"]
        resolveDateLabel.text = "Resolve Date: " + (!!ticket["resolveDate"] ? ticket["resolveDate"] : "-")
        coordinateLabel.text = "Coordinate: " + ticket["coordinate"]["latitude"] + ", " + ticket["coordinate"]["longitude"]
        if(!enableEdit) stateLabel.text = "State: " + ticket["state"]
        else stateLabelBox.currentIndex = ((ticket["state"] === "Created") ? 0 : ((ticket["state"] === "Resolved") ? 2 : 1))

    }


    Column {
        spacing: 10
        anchors.centerIn: parent
        width: parent.width * 0.8

        Label {
            text: "Fault Ticket Details"
            font.bold: true
            font.pixelSize: 24
        }

        Label {
            id: descriptionLabel
            width: parent.width
            wrapMode: Label.WordWrap
            text: "Description: -"
        }

        Label {
            id: startDateLabel
            text: "Start Date: -"
        }

        Label {
            id: resolveDateLabel
            text: "Resolve Date: -"
        }

        Label {
            id: coordinateLabel
            text: "Coordinates: -"
        }

        Label {
            id: stateLabel
            text: "State: -"
            visible: !enableEdit
        }

        ComboBox {
            id: stateLabelBox
            model: ["Created", "In Progress", "Resolved"]
            currentIndex: 0
            visible: enableEdit
        }

        Row {
            spacing: 10
            Button{
                text: "Back"
                font.pixelSize: 16
                onClicked: onBack()
            }

            Button{
                text: "Save"
                font.pixelSize: 16
                onClicked: {
                    saveTicket(((stateLabelBox.currentIndex === 0) ? "Created" : ((stateLabelBox.currentIndex === 2)) ? "Resolved" : "InProgress"), ticket["_links"]["self"]["href"])
                }
                visible: enableEdit
            }

            Button{
                text: "Delete"
                font.pixelSize: 16
                onClicked: {
                    deleteTicket(ticket["_links"]["self"]["href"])
                }
                visible: enableEdit
            }
        }

    }
}
