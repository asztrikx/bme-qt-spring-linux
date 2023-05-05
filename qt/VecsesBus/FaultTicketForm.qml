import QtQuick 2.15
import QtQuick.Layouts 1.12
import QtQuick.Controls 2.15

Item {
    anchors.fill: parent
    property var onSubmitClicked

    Rectangle {
        width: parent.width
        height: parent.height

        Column {
            width: parent.width * 0.8
            anchors.centerIn: parent

            TextField {
                id: descriptionField
                leftPadding: 5
                width: parent.width
                placeholderText: "Description"
                font.pixelSize: 16
            }

            Row {
                topPadding: 10
                spacing: 10
                width: parent.width
                TextField {
                    leftPadding: 5
                    id: latField
                    width: parent.width / 2 - parent.spacing / 2
                    placeholderText: "Latitude"
                    font.pixelSize: 16
                }
                TextField {
                    leftPadding: 5
                    id: longField
                    width: parent.width / 2 - parent.spacing / 2
                    placeholderText: "Longitude"
                    font.pixelSize: 16
                }
            }

            Row {
                topPadding: 10
                width: parent.width
                TextField {
                    id: startDateField
                    leftPadding: 5
                    width: parent.width
                    placeholderText: "Start Date"
                    font.pixelSize: 16
                }
            }

            Row {
                topPadding: 10
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
                            "state": stateComboBox.currentText,
                            "startDate": startDateField.text
                        }
                        if (onSubmitClicked !== undefined) {
                            onSubmitClicked(newTicket);
                        }
                    }
                }
            }

        }
    }



}
