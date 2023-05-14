import QtQuick 2.15
import QtQuick.Controls 2.15
import QtQuick.Controls.Material 2.12

Item {
    anchors.fill: parent

    function setStop(id, stop){
        sectionmodel.get(id).name = stop.name
        stopUrl = stop["_links"]["self"]["href"]
        let timespan = 0
        for(let i = id; i >= 0; i--){
            timespan += sectionmodel.get(i).timespan;
        }
        let startTime = sectionmodel.get(id).time;
        sectionmodel.get(id).time = new Date(startTime.getTime() + timespan)
    }
    function parseDuration(durationString) {
      const regex = /^PT(?:(\d+)H)?(?:(\d+)M)?(?:(\d+)S)?$/;
      const matches = durationString.match(regex);

      if (!matches) {
        throw new Error("Invalid duration string format");
      }

      const hours = matches[1] ? parseInt(matches[1]) : 0;
      const minutes = matches[2] ? parseInt(matches[2]) : 0;
      const seconds = matches[3] ? parseInt(matches[3]) : 0;

      return (hours * 60 * 60 + minutes * 60 + seconds) * 1000;
    }

    function toDate(timeValue){
        const dateParts = timeValue.split(":");
        const date = new Date();
        date.setHours(dateParts[0]);
        date.setMinutes(dateParts[1]);
        date.setSeconds(dateParts[2]);
        return date;
    }

    property string startDate
    property var lineUrl
    property var stopUrl
    property var sections
    onSectionsChanged: () => {
        if(!sections["_embedded"]) return;
        sectionmodel.clear()
        onSectionsLoaded();
        let first = sections["_embedded"]["sections"][0];
        sectionmodel.append({
                              "timespan" : parseDuration("PT0S"),
                              "time" : toDate(startDate),
                              "stop" : first["_links"]["start"]["href"],
                              "url" : first["_links"]["self"]["href"],
                              "name" : "Loading..."
                          })
        getTimeTableSection(0, first["_links"]["start"]["href"])
        for (const section of sections["_embedded"]["sections"]){
            sectionmodel.append({
                                   "timespan" : parseDuration(section["timespan"]),
                                   "time" : toDate(startDate),
                                   "stop" : section["_links"]["stop"]["href"],
                                   "url" : section["_links"]["self"]["href"],
                                   "name" : "Loading..."
                               })
            getTimeTableSection(sectionmodel.count - 1, section["_links"]["stop"]["href"])
        }
    }

    ListModel {
        id: sectionmodel
    }

    ListView {
        id: sectionlist
        anchors.fill: parent
        model: sectionmodel
        delegate: Rectangle {
            width: parent.width
            height: 40
            color: "white"
            border.width: 1
            border.color: "gray"

            MouseArea {
                anchors.fill: parent
                onClicked: function(){
                    const lineUrlParts = lineUrl.split("/")
                    const lineId = lineUrlParts[lineUrlParts.length - 1]
                    const stopUrlParts = stopUrl.split("/")
                    const stopId = stopUrlParts[stopUrlParts.length - 1]
                    onSelectedItem(lineId, stopId);
                }
            }

            Row {
                height: parent.height
                width: parent.width
                spacing: 10
                leftPadding: 10
                rightPadding: 10

                Row{
                    width: parent.width
                    height: parent.height

                    Text {
                        id: dateText
                        text: time.toLocaleTimeString() + " > " + name
                        elide: Text.ElideRight
                        font.pixelSize: 16
                        anchors.verticalCenter: parent.verticalCenter
                        font.bold: (index === 0 || index === sectionmodel.count - 1)
                    }
                }

            }

        }
    }
    Row {
        anchors.bottomMargin: 50
        anchors.horizontalCenter: parent.horizontalCenter
        anchors.bottom: parent.bottom

        Button {
            text: "Back"
            font.pixelSize: 16
            onClicked: onBack()
        }
    }
}
