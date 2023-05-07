import QtQuick 2.15
import QtQuick.Controls 2.5

Rectangle {
    anchors.fill: parent
    color: "white"

    property var xhr: new XMLHttpRequest()

    property var fid
    onFidChanged: function() {
        xhr.onreadystatechange = function() {
            if (xhr.readyState === XMLHttpRequest.DONE) {
                if (xhr.status === 200) {
                    var responseText = JSON.parse(xhr.responseText)
                    titleLabel.text = "Title: " + responseText["title"]
                    descriptionLabel.text = "Description: " + responseText["description"]
                }
            }
        }
        xhr.open("GET", "https://dummyjson.com/products/" + fid);
        xhr.send();
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
            id: titleLabel
            leftPadding: 5
            width: parent.width
            text: "Title: -"
        }

        TextArea {
            id: descriptionLabel
            width: parent.width
            readOnly: true
            wrapMode: TextArea.Wrap
            text: "Description: -"
        }
        /*
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
        }*/
        Row {
            spacing : 10
            Button{
                width: 50
                text: "Back"
                font.pixelSize: 16
                onClicked: onBack()
            }

            Button{
                width: 75
                text: "Delete"
                font.pixelSize: 16
                onClicked: onBack()
            }
        }
    }
}
