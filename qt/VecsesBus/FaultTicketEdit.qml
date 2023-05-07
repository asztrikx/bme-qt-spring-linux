import QtQuick 2.15
import QtQuick.Controls 2.5
import QtQuick.Controls.Material 2.12

Rectangle {
    anchors.fill: parent
    color: "white"

    property var xhr: new XMLHttpRequest()

    property var url
    onUrlChanged: function() {
        xhr.onreadystatechange = function() {
            if (xhr.readyState === XMLHttpRequest.DONE) {
                if (xhr.status === 200) {
                    var responseText = JSON.parse(xhr.responseText)
                    descriptionLabel.text = "Description: " + responseText["description"]
                    startDateLabel.text = "Description: " + responseText["startDate"]
                    resolveDateLabel.text = "Description: " + responseText["resolveDate"]
                    stateLabel.currentIndex = parseInt(responseText["state"])
                    coordinateLabel.text = "Description: " + responseText["coordinate"]["latitude"] + ", " + responseText["coordinate"]["longitude"]
                }
            }
        }
        xhr.open("GET", url, true);
        //xhr.setRequestHeader('Cookie', 'SESSION=Njc1ZjI0MDctYTQ2Ny00YjdlLThlNjYtOTU3ZjkwNTAwODEy');
        xhr.send();
    }
    property var onBack


    Column {
        spacing: 10
        anchors.centerIn: parent
        width: parent.width * 0.8

        Label {
            text: "Fault Ticket Edit"
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

        ComboBox {
            id: stateLabel
            model: ["Created", "In Progress", "Resolved"]
            currentIndex: 0
        }

        /*
        Label {
            text: "User: " + faultTicket.user_name
        }

        Label {
            text: "Bus: " + faultTicket.bus_name
        }*/
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
                onClicked: onBack()
            }

            Button{
                text: "Delete"
                font.pixelSize: 16
                onClicked: onBack()
            }
        }
    }
}
