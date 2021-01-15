import React from "react";
import { Map as LeafletMap, TileLayer, Marker, Popup } from "react-leaflet";

class Map extends React.Component {
  render() {
    const pos = this.props.pos;
    return (
      <LeafletMap
        center={pos}
        zoom={13}
        maxZoom={17}
        attributionControl={true}
        zoomControl={true}
        doubleClickZoom={true}
        scrollWheelZoom={true}
        dragging={true}
        animate={true}
        easeLinearity={0.35}
      >
        <TileLayer url="http://{s}.tile.osm.org/{z}/{x}/{y}.png" />
        <Marker position={pos}>
          <Popup>Fast PG {pos[0] + " " + pos[1]}</Popup>
        </Marker>
      </LeafletMap>
    );
  }
}

export default Map;
