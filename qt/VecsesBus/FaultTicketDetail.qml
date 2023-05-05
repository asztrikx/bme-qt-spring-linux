import QtQuick 2.15

import QtQuick 2.0
import QtQuick.Controls 2.5

Item {
    anchors.fill: parent

    property var faultTicket
    property var onBack

    Column {
        spacing: 10
        anchors.centerIn: parent

        Label {
            text: "Fault Ticket Details"
            font.bold: true
            font.pixelSize: 24
        }

        Label {
            text: "Start Date: " + faultTicket.startDate
        }

        Label {
            text: "Resolve Date: " + faultTicket.resolveDate
        }

        Label {
            text: "Description: " + faultTicket.description
        }

        Label {
            text: "Coordinates: " + faultTicket.coordinate
        }

        Label {
            text: "State: " + faultTicket.state
        }

        Label {
            text: "User: " + faultTicket.user_name
        }

        Label {
            text: "Bus: " + faultTicket.bus_name
        }

        Button{
            width: 50
            text: "Back"
            font.pixelSize: 16
            onClicked: onBack()
        }
    }
}
