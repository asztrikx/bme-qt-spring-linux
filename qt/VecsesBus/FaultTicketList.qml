import QtQuick 2.15
import QtQuick.Controls 2.15

Item {
    anchors.fill: parent

    property var xhr: new XMLHttpRequest()

    property var onAdd
    property var onSelectedItem

    Component.onCompleted: function(){
        xhr.onreadystatechange = function() {
            if (xhr.readyState === XMLHttpRequest.DONE) {
                if (xhr.status === 200) {
                    var responseText = JSON.parse(xhr.responseText)
                    for (const product of responseText["products"]){
                        ticketModel.append({
                                               "pid" : product["id"],
                                               "title" : product["title"],
                                               "description" : product["description"]
                                           })
                    }
                }
            }
        }
        xhr.open("GET", "https://dummyjson.com/products/");
        xhr.send();
    }

    ListModel {
        id: ticketModel
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
                text: title
                width: parent.width - 20
                anchors.verticalCenter: parent.verticalCenter
                anchors.left: parent.left
                anchors.leftMargin: 10
                anchors.rightMargin: 10
            }
            MouseArea {
                anchors.fill: parent
                onClicked: function(){
                    onSelectedItem(pid);
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
