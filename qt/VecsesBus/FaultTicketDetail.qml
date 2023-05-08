import QtQuick 2.15
import QtQuick.Controls 2.5
import QtQuick.Controls.Material 2.12

Rectangle {
    anchors.fill: parent
    color: "white"

    property bool enableEdit: false
    property var ticket
    onTicketChanged: function() {
        if (!ticket) return;
        descriptionLabel.text = "Description: " + ticket["description"]
        startDateLabel.text = "Description: " + ticket["startDate"]
        resolveDateLabel.text = "Description: " + (!ticket["resolveDate"]) ? ticket["resolveDate"] : "-"
        coordinateLabel.text = "Description: " + ticket["coordinate"]["latitude"] + ", " + ticket["coordinate"]["longitude"]
        if(!enableEdit) stateLabel.text = "Description: " + ticket["state"]
        else stateLabelBox.currentIndex = ((ticket["state"] === "Created") ? 0 : ((ticket["state"] === "Resolved") ? 2 : 1))

    }
    property var onBack


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
        /*
        Label {
            text: "User: " + faultTicket.user_name
        }

        Label {
            text: "Bus: " + faultTicket.bus_name
        }*/

        Button{
            text: "Back"
            font.pixelSize: 16
            onClicked: onBack()
        }
    }
}
