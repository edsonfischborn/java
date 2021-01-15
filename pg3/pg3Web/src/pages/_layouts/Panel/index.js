import React, { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';

// Components
import BasicSection from '../../../components/BasicSection';
import Navbar from '../../../components/Navbar';
import Footer from '../../../components/Footer';
import { Main } from './styles';

// Services
import TokenStorage from '../../../services/TokenStorage';
import UserStorage from '../../../services/UserStorage';

export default function Panel({ children }) {
  const [name, setName] = useState(' ');

  useEffect(() => {
    try {
      const user = UserStorage.get();
      const name = user.name.split(' ');

      setName(`${name[0]} ${name[name.length - 1]}`);
    } catch (ex) {}
  }, []);

  function handleSignOut() {
    UserStorage.clear();
    TokenStorage.clear();
  }

  return (
    <>
      <Navbar>
        <span>{name ? name + ' | ' : ''}</span>
        <Link onClick={handleSignOut} to="/">
          sair
        </Link>
      </Navbar>
      <Main>
        <BasicSection>{children}</BasicSection>
      </Main>
      <Footer />
    </>
  );
}
