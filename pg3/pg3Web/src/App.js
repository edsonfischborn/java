import React from 'react';
import { ToastContainer } from 'react-toastify';
import { Router } from 'react-router-dom';

// Components / Pages
import Routes from './routes';

// Services
import history from './services/history';

// Styles
import { GlobalStyle } from './styles/global';

export default function App() {
  return (
    <Router history={history}>
      <Routes />
      <GlobalStyle />
      <ToastContainer position="top-center" autoClose={3000} />
    </Router>
  );
}
