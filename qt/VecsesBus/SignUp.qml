import QtQuick 2.15
import QtQuick.Layouts 1.12
import QtQuick.Controls 2.15
import QtQuick.Controls.Material 2.12

Item {
    anchors.fill: parent


    property var onSignedUp
    property var onBack

    Rectangle {
        width: parent.width
        height: parent.height

        Column {
            width: parent.width * 0.8
            spacing: 10
            anchors.centerIn: parent

            Label {
                text: "Sign Up"
                font.bold: true
                font.pixelSize: 24
            }

            TextField {
                id: usernameField
                leftPadding: 5
                width: parent.width
                placeholderText: "Username"
                font.pixelSize: 16
            }

            TextField {
                id: passwordField
                leftPadding: 5
                width: parent.width
                placeholderText: "Password"
                font.pixelSize: 16
                echoMode: TextInput.Password
            }

            TextField {
                id: passwordAgainField
                leftPadding: 5
                width: parent.width
                placeholderText: "Password again"
                font.pixelSize: 16
                echoMode: TextInput.Password
            }

            Row {
                spacing: 10
                Button {
                    text: "Submit"
                    font.pixelSize: 16
                    onClicked: {
                        if (onSignedUp !== undefined) {
                            onSignedUp();
                        }
                    }
                }

                Button {
                    text: "Back"
                    font.pixelSize: 16
                    onClicked: {
                        if (onBack !== undefined) {
                            onBack();
                        }
                    }
                }
            }

        }
    }
}
