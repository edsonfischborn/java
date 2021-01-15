import React from 'react';
import { Link } from 'react-router-dom';

// Components / Pages
import NavBar from '../../../components/Navbar';
import Footer from '../../../components/Footer';
import { Container } from './styles';

export default function Auth({ bgImg, children }) {
  return (
    <>
      <NavBar>
        <Link to="/">Home</Link>
      </NavBar>
      <Container bgImg={bgImg}>{children}</Container>
      <Footer />
    </>
  );
}
