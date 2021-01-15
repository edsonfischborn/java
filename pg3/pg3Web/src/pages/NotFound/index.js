import React, { useState, useEffect } from 'react';

// Components
import { Link } from 'react-router-dom';
import { Container } from './styles';
import Navbar from '../../components/Navbar';
import Footer from '../../components/Footer';

// Services
import history from '../../services/history';

function NotFound() {
  const [counter, setCounter] = useState(10);

  useEffect(() => {
    if (counter === 0) history.push('/');
    setTimeout(() => setCounter(counter - 1), 1000);
  }, [counter]);

  return (
    <>
      <Navbar>
        <Link to="/">Home</Link>
      </Navbar>
      <Container>
        <h1>404</h1>
        <strong>Página não encontrada</strong>

        <Link to="/">Voltar para página inicial {counter}</Link>
      </Container>
      <Footer />
    </>
  );
}

export default NotFound;
