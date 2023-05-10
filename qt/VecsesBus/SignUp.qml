import QtQuick 2.15
import QtQuick.Layouts 1.12
import QtQuick.Controls 2.15
import QtQuick.Controls.Material 2.12

Item {
    id: signup
    anchors.fill: parent

    function setError(msg){
        errorText.text = msg;
        errorText.visible = true
    }

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
                width: parent.width
                placeholderText: "Username"
                font.pixelSize: 16
            }

            TextField {
                id: emailField
                width: parent.width
                placeholderText: "Email"
                font.pixelSize: 16
            }

            TextField {
                id: nameField
                width: parent.width
                placeholderText: "Name"
                font.pixelSize: 16
            }

            TextField {
                id: passwordField
                width: parent.width
                placeholderText: "Password"
                font.pixelSize: 16
                echoMode: TextInput.Password
            }

            TextField {
                id: passwordAgainField
                width: parent.width
                placeholderText: "Password again"
                font.pixelSize: 16
                echoMode: TextInput.Password
            }

            Text {
                id: errorText
                text: ""
                visible: false
                color: Material.accent
            }


            Row {
                spacing: 10
                Button {
                    text: "Sign up"
                    font.pixelSize: 16

                    onClicked: {
                        errorText.visible = false
                        const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
                        const userNameRegex = /^[a-zA-Z0-9]{6,}$/;
                        const nameRegex = /^[a-zA-Z]+([ '-][a-zA-Z]+)*$/;
                        const passwordRegex = /^[a-zA-Z0-9!@#$%&*_+-=:,.?]{8,}$/;
                        if (!userNameRegex.test(usernameField.text)){
                            signup.setError("Bad username format or too short")
                            return;
                        }
                        if (!emailRegex.test(emailField.text)){
                            signup.setError("Bad email format or too short")
                            return;
                        }
                        if (!nameRegex.test(nameField.text)){
                            signup.setError("Bad name format or too short")
                            return;
                        }
                        if (passwordField.text !== passwordAgainField.text || !passwordRegex.test(passwordField.text)){
                            signup.setError("Dismatch passwords or wrong format")
                            return;
                        }
                        signUp(JSON.stringify({
                                   "email" : emailField.text,
                                   "username" : usernameField.text,
                                   "password" : passwordField.text,
                                   "name" : nameField.text
                               }));
                    }
                }

                Button {
                    text: "Back"
                    font.pixelSize: 16
                    onClicked: {
                        onBack();
                    }
                }
            }

        }
    }
}
