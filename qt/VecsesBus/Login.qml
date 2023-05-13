import QtQuick 2.15
import QtQuick.Layouts 1.12
import QtQuick.Controls 2.15
import QtQuick.Controls.Material 2.12

Item {
    anchors.fill: parent

    signal login(string username, string password)
    function handleError(msg) {
        errorText.text = msg;
        errorText.visible = true;
    }

    Rectangle {
        width: parent.width
        height: parent.height

        Column {
            width: parent.width * 0.8
            spacing: 10
            anchors.centerIn: parent

            Label {
                text: "Login"
                font.bold: true
                font.pixelSize: 24
            }

            TextField {
                id: usernameField
                width: parent.width
                placeholderText: "Username"
                font.pixelSize: 16
            }

            TextField {
               id: passwordField
                width: parent.width
                placeholderText: "Password"
                font.pixelSize: 16
                echoMode: TextInput.Password
                onAccepted: () => { submit.onClicked(); }
            }

            Text {
                id: errorText
                width: parent.width
                text: "No error"
                font.pixelSize: 16
                color: Material.accent
                visible: false
            }

            Button {
                id: submit
                text: "Submit"
                font.pixelSize: 16
                onClicked: {
                    errorText.visible = false;
                    login(usernameField.text, passwordField.text);
                }
            }

            Text {
                text: "Don't have an account yet? Create one"
                font.pixelSize: 12
                font.family: "Roboto"
                color: "blue"

                MouseArea {
                    anchors.fill: parent
                    cursorShape: Qt.PointingHandCursor
                    hoverEnabled: true

                    onEntered: {
                        textDecoration: Text.Underline
                    }

                    onExited: {
                        textDecoration: Text.NoUnderline
                    }

                    onClicked: {
                        onSignUp()
                    }
                }
            }

        }
    }
}
