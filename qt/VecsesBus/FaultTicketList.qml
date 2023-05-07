import QtQuick 2.15
import QtQuick.Controls 2.15
import QtQuick.Controls.Material 2.12

Item {
    anchors.fill: parent

    property var xhr: new XMLHttpRequest()

    property var onAdd
    property var onSelectedItem
    property var onEditItem

    Component.onCompleted: function(){
        console.log(app.cookie)
        xhr.onreadystatechange = function() {
            if (xhr.readyState === XMLHttpRequest.DONE) {
                if (xhr.status === 200) {
                    console.log(xhr.responseText)
                    var responseText = JSON.parse(xhr.responseText)
                    for (const ticket of responseText["_embedded"]["faultTickets"]){
                        ticketModel.append({
                                               "startDate" : ticket["startDate"],
                                               "resolveDate" : ticket["resolveDate"],
                                               "description" : ticket["description"],
                                               "coordinate" : ticket["coordinate"],
                                               "state" : ticket["startDate"],
                                               "url" : ticket["_links"]["self"]["href"]
                                           })
                    }
                }
            }
        }
        xhr.open("GET", "http://localhost:8080/api/faultTickets/", true);
        //xhr.setRequestHeader('Cookie', 'SESSION=Njc1ZjI0MDctYTQ2Ny00YjdlLThlNjYtOTU3ZjkwNTAwODEy');
        xhr.send();
    }

    ListModel {
        id: ticketModel
        ListElement{
            startDate : "2022.10.12."
            resolveDate : "-"
            description : "Engine is broken"
            coordinate : '{"latitude" : 43.11, "longitude" : 34.45}'
            _state : 2
            url : "http://example.com"
        }
        ListElement{
            startDate : "2022.10.12."
            resolveDate : "-"
            description : "Engine is broken 3"
            coordinate : '{"latitude" : 43.11, "longitude" : 34.45}'
            _state : 1
            url : "http://example.com"
        }
        ListElement{
            startDate : "2022.10.12."
            resolveDate : "-"
            description : "Engine is broken 2"
            coordinate : '{"latitude" : 43.11, "longitude" : 34.45}'
            _state : 0
            url : "http://example.com"
        }
    }

    ListView {
        id: ticketList
        anchors.fill: parent
        model: ticketModel
        delegate: Rectangle {
            width: parent.width
            height: 75
            color: "white"
            border.width: 1
            border.color: "gray"

            MouseArea {
                anchors.fill: parent
                onClicked: function(){
                    onSelectedItem(url);
                }
            }

            Row {
                height: parent.height
                width: parent.width
                spacing: 10
                leftPadding: 10
                rightPadding: 10

                Text {
                    id: descriptionText
                    width: parent.width * 0.6
                    anchors.verticalCenter: parent.verticalCenter
                    text: description + " (" + startDate + ")"
                }

                Canvas {
                    id: canvas
                    width: 24
                    height: 24
                    anchors.verticalCenter: parent.verticalCenter
                    onPaint: {
                        var ctx = getContext("2d")
                        ctx.clearRect(0, 0, width, height)
                        ctx.beginPath()
                        ctx.arc(12, 12, 10, 0, 2 * Math.PI)
                        ctx.fillStyle = ((_state === 0) ? "red" : ((_state === 1) ? "yellow" : "green"))
                        ctx.fill()
                        ctx.stroke()
                    }
                }

                Button {
                    text: "Edit"
                    anchors.verticalCenter: parent.verticalCenter
                    visible: (app.role === "MAINTENANCE")
                    onClicked: {
                        onEditItem(url)
                    }
                }

            }



        }
    }

    Button {
        text: "+"
        anchors.bottom: parent.bottom
        anchors.bottomMargin: 50
        font.pixelSize: 20
        anchors.horizontalCenter: parent.horizontalCenter
        onClicked: {
            if (onAdd !== undefined){
                onAdd();
            }
        }
    }
}
