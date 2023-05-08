import QtQuick 2.15
import QtQuick.Layouts 1.12
import QtQuick.Controls 2.15
import QtQuick.Controls.Material 2.12

Item {
    anchors.fill: parent

    signal createTicket(var ticket)
    property var onSubmitClicked

    Rectangle {
        width: parent.width
        height: parent.height

        Column {
            width: parent.width * 0.8
            anchors.centerIn: parent
            spacing: 10

            Label {
                text: "Create fault ticket"
                font.bold: true
                font.pixelSize: 24
            }

            TextField {
                id: descriptionField
                width: parent.width
                placeholderText: "Description"
                font.pixelSize: 16
            }

            Row {
                spacing: 10
                width: parent.width
                TextField {
                    id: latField
                    width: parent.width / 2 - parent.spacing / 2
                    placeholderText: "Latitude"
                    font.pixelSize: 16
                }
                TextField {
                    id: longField
                    width: parent.width / 2 - parent.spacing / 2
                    placeholderText: "Longitude"
                    font.pixelSize: 16
                }
            }


            Row {
                width: parent.width
                Button {
                    text: "Submit"
                    font.pixelSize: 16
                    onClicked: {
                        var newTicket = {
                            "description": descriptionField.text,
                            "coordinate": {
                                "latitude": latField.text,
                                "longitude": longField.text
                            },
                            "state": 0,
                            "startDate": new Date(),
                            "resolvedDate": null
                        }
                        if (onSubmitClicked !== undefined) {
                            createTicket(JSON.stringify(newTicket));
                            onSubmitClicked();
                        }
                    }
                }
            }

        }
    }



}
