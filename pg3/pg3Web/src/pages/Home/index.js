import React from 'react';
import { Link } from 'react-router-dom';

// Components / Pages
import { FaWhatsapp, FaPhone, FaMailBulk } from 'react-icons/fa';
import Footer from '../../components/Footer';
import Navbar from '../../components/Navbar';
import BasicSection from '../../components/BasicSection';
import Map from '../../components/Map';
import Carrousel from './Carousel';
import SiteMessageForm from './MessageForm';

// styles
import {
  Header,
  JumpButton,
  ContactContainer,
  ContactOptions,
  Main as MainCt,
} from './styles';

// config
import { homeConfig } from '../../config/appConfig';

// Util
import TokenStorage from '../../services/TokenStorage';

function Main() {
  const { textSections, contact } = homeConfig;

  return (
    <MainCt>
      {textSections.map((section) => (
        <BasicSection key={section.title} id="about">
          <h2>{section.title}</h2>
          <p>{section.text}</p>
        </BasicSection>
      ))}

      <BasicSection>
        <h2>Contato</h2>
        <ContactContainer>
          <ContactOptions>
            <h4>Formas de contato</h4>
            <li>
              <FaPhone />
              <span>Telefone:</span>
              <strong>{contact.phone}</strong>
            </li>
            <li>
              <FaWhatsapp />
              <span>Whatsapp:</span>
              <strong>{contact.whatsapp}</strong>
            </li>
            <li>
              <FaMailBulk />
              <span>E-mail:</span>
              <strong>{contact.email}</strong>
            </li>
          </ContactOptions>
          <div>
            <h4>Onde estamos</h4>
            <Map pos={[-29.6477638, -50.787595]} />
          </div>
          <SiteMessageForm />
        </ContactContainer>
      </BasicSection>
    </MainCt>
  );
}

export default function Home() {
  return (
    <>
      <Navbar>
        {TokenStorage.validate() ? (
          <Link to="/painel">Painel</Link>
        ) : (
          <Link to="/login">Login</Link>
        )}
      </Navbar>
      <Header id="home">
        <Carrousel />
        <JumpButton>
          <a href="#about">Descubra</a>
        </JumpButton>
      </Header>
      <Main />
      <Footer />
    </>
  );
}
