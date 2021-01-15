import React from 'react';
import { Route as Router } from 'react-router-dom';

// Services
import history from '../services/history';
import TokenStorage from '../services/TokenStorage';
import UserStorage from '../services/UserStorage';

export default function Route({ auth, path, component, ...rest }) {
  const signed = TokenStorage.validate() && UserStorage.validate();
  const signedRedirectOn = ['/login', '/registrar'];

  if (
    signed &&
    !auth &&
    signedRedirectOn.findIndex((item) => item === path) > -1
  ) {
    history.push('/painel');
  }

  if (auth && !signed) {
    history.push('/login');
  }

  return <Router path={path} component={component} {...rest} />;
}
