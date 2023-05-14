import QtQuick 2.15
import QtPositioning 5.15
import QtLocation 5.15
import QtGraphicalEffects 1.0

Item {
    anchors.fill: parent
    required property var lineId;
    required property var stopId;

    signal network2(var lineId2, var stopId2)
    Component.onCompleted: () => { console.log(lineId,stopId, "XDD"); network2(lineId, stopId) }

    property var stops;
    property var nextBus;
    property var brokenBuses;

    onStopsChanged: () => {
        var coords = [];
        for (const stop of stops["_embedded"]["stops"]) {
           coords.push(stop["coordinate"]);
        }

        routePoly.path = coords;

        for (const coord of coords) {
            routeStops.append(coord);
        }
    }

    Map {
        anchors.fill: parent
        plugin: mapPlugin
        center: QtPositioning.coordinate(47.41, 19.26)
        zoomLevel: 14
        maximumZoomLevel: 15
        minimumZoomLevel: 13

        Plugin {
            id: mapPlugin
            name: "osm"
        }

        MapItemGroup {
            MapPolyline {
                id: routePoly
                line.width: 5
                line.color: "#009ee3"
            }

            MapItemView {
                model: ListModel {
                    id: routeStops
                }

                delegate: MapQuickItem {
                    coordinate: QtPositioning.coordinate(latitude, longitude)
                    anchorPoint: Qt.point(station.width / 2, station.height / 2)

                    sourceItem: Image {
                        id: station
                        smooth: true
                        width: 25
                        height: 25
                        source: "image/station.png"
                    }
                }
            }
        }

        MapQuickItem {
            coordinate: QtPositioning.coordinate(47.39992883987876, 19.25913066972342)
            anchorPoint.x: bus.width / 2
            anchorPoint.y: bus.height / 2
            sourceItem: Image {
                id: bus
                smooth: true
                width:25
                height:25
                source: "image/bus.png"
            }
        }
        MapQuickItem {
            coordinate: QtPositioning.coordinate(47.40128681145784, 19.249980961251165)
            anchorPoint.x: location.width / 2
            anchorPoint.y: location.height / 2
            sourceItem: Image {
                id: location
                smooth: true
                width:25
                height:25
                source: "image/location.png"
            }
        }
        MapQuickItem {
            coordinate: QtPositioning.coordinate(47.41620956639614, 19.25185147253718)
            anchorPoint.x: broken1.width / 2
            anchorPoint.y: broken1.height / 2
            sourceItem: Image {
                id: broken1
                smooth: true
                width:25
                height:25
                source: "image/broken.png"
            }
        }
    }
}
