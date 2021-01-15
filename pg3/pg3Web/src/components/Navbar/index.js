import React from 'react';
import { Link } from 'react-router-dom';

// Copmponents
import { Nav } from './styles';

export default function Navbar({ children }) {
  return (
    <Nav>
      <Link to="/">
        <h2>PGIII</h2>
      </Link>

      <aside>{children}</aside>
    </Nav>
  );
}
