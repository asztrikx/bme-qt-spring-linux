import QtQuick 2.15
import QtQuick.Controls 2.15

Item {
    anchors.fill: parent
    property var onAdd
    property var onSelectedItem

    ListModel {
        id: ticketModel
        ListElement {
            _id: "1"
            startDate: "2022.10.11."
            resolveDate: "2023.01.01."
            description: "Ticket 1"
            state: 2
        }
        ListElement {
            _id: "2"
            startDate: "2023.10.11."
            resolveDate: ""
            description: "Ticket 2"
            state: 0
        }
        ListElement {
            _id: "3"
            startDate: "2022.12.11."
            resolveDate: "2023.01.11."
            description: "Ticket 3"
            state: 2
        }
    }

    ListView {
        id: ticketList
        anchors.fill: parent
        model: ticketModel
        delegate: Rectangle {
            width: parent.width
            height: 40
            color: "white"
            border.width: 1
            border.color: "gray"

            Text {
                text: description
                anchors.verticalCenter: parent.verticalCenter
                anchors.left: parent.left
                anchors.leftMargin: 10
            }
            MouseArea {
                anchors.fill: parent
                onClicked: function(){
                    onSelectedItem(_id);
                }
            }
        }
    }

    Button {
        text: "+"
        width: 40
        height: 40
        anchors.bottom: parent.bottom
        anchors.horizontalCenter: parent.horizontalCenter
        onClicked: {
            if (onAdd !== undefined){
                onAdd();
            }
        }
    }
}
